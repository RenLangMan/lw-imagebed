package cn.kevinlu98.imagebed.serice;

import cn.kevinlu98.imagebed.client.TencentClient;
import cn.kevinlu98.imagebed.domain.CosFile;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import com.qcloud.cos.utils.Jackson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Author: 鲁恺文
 * Date: 2021/1/18 4:47 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Slf4j
@Service
public class TencentIamgeService implements ImageService {

    @Value("${tencent.cos.bucketName}")
    private String bucketName;

    private final COSClient client;

    public TencentIamgeService(TencentClient tencentClient) {
        this.client = tencentClient.client();
    }


    @Override
    public List<CosFile> list(String prefix) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucketName);
        listObjectsRequest.setPrefix(prefix);
        listObjectsRequest.setDelimiter("/");
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing;
        List<CosFile> cosFiles = new ArrayList<>();
        do {
            objectListing = client.listObjects(listObjectsRequest);
            objectListing.getCommonPrefixes().forEach(dirName -> cosFiles.add(CosFile.buildDir(dirName, prefix)));
            objectListing.getObjectSummaries().
                    stream()
                    .filter(summary -> !summary.getKey().equals(prefix))
                    .forEach(summary -> cosFiles.add(new CosFile(summary, prefix)));
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
        return cosFiles;
    }

    @Override
    public String upload(String prefix, MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        int extStart = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
        String extName = file.getOriginalFilename().substring(extStart);
        String key = prefix + UUID.randomUUID().toString().replace("-", "") + extName;
        log.info(key);
        ObjectMetadata metadata = new ObjectMetadata();
        PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream, metadata);
        PutObjectResult result = client.putObject(request);
        log.info(Jackson.toJsonString(result));
        String url = TencentClient.URL_PREFIX.endsWith("/") ?
                TencentClient.URL_PREFIX + key :
                TencentClient.URL_PREFIX + "/" + key;
        log.info(url);
        return url;
    }

    @Override
    public void mkdir(String name) {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, name, input, objectMetadata);
        PutObjectResult result = client.putObject(putObjectRequest);
        log.info(Jackson.toJsonString(result));
    }

    @Override
    public void delete(String name) {
        client.deleteObject(bucketName, name);
        ListObjectsRequest request = new ListObjectsRequest();
        request.setBucketName(bucketName);
        request.setPrefix(name);
        request.setDelimiter("");
        request.setMaxKeys(1000);
        ObjectListing objectListing;
        do {
            objectListing = client.listObjects(request);
            objectListing.getObjectSummaries().forEach(item -> client.deleteObject(bucketName, item.getKey()));
            String nextMarker = objectListing.getNextMarker();
            request.setMarker(nextMarker);
        } while (objectListing.isTruncated());

    }

}
