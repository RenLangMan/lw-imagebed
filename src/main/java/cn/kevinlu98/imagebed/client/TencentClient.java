package cn.kevinlu98.imagebed.client;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Author: 鲁恺文
 * Date: 2021/1/18 4:49 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Component
public class TencentClient {
    @Value("${tencent.cos.secretId}")
    private String secretId;
    @Value("${tencent.cos.secretKey}")
    private String secretKey;
    @Value("${tencent.cos.region}")
    private String region;

    public static String URL_PREFIX;

    @Value("${tencent.cos.urlPrefix}")
    public void setUrlPrefix(String urlPrefix) {
        TencentClient.URL_PREFIX = urlPrefix;
    }

    public COSClient client() {
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        return new COSClient(cred, clientConfig);
    }


}
