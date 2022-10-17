package cn.kevinlu98.imagebed.domain;

import cn.kevinlu98.imagebed.client.TencentClient;
import com.qcloud.cos.model.COSObjectSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;

/**
 * Author: 鲁恺文
 * Date: 2021/1/15 5:35 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CosFile {
    public static final String FILE_TYPE_DIR = "dir";
    public static final String FILE_TYPE_IMAGE = "image";
    public static final String FILE_TYPE_FILE = "files";
    private String fileName;
    private String name;
    private String prefix;
    private long fileSize;
    private String url;
    private Date created;
    private String type;

    public CosFile(COSObjectSummary summary, String prefix) {
        this.fileName = summary.getKey();
        this.fileSize = summary.getSize() / 1024;
        this.created = summary.getLastModified();
        this.prefix = prefix;
        int index = Math.max(fileName.indexOf(prefix), 0) + prefix.length();
        this.name = this.fileName.substring(index);

        this.url = TencentClient.URL_PREFIX.endsWith("/") ?
                TencentClient.URL_PREFIX + fileName :
                TencentClient.URL_PREFIX + "/" + fileName;
        if (summary.getKey().endsWith("/")) {
            this.type = CosFile.FILE_TYPE_DIR;
        } else {
            int extStart = Math.max(this.fileName.lastIndexOf('.'), 0);
            String extName = this.fileName.substring(extStart);
            if (".jpg".equalsIgnoreCase(extName)
                    || ".png".equalsIgnoreCase(extName)
                    || ".tif".equalsIgnoreCase(extName)
                    || ".GIF".equalsIgnoreCase(extName)
                    || ".JPEG".equalsIgnoreCase(extName)
                    || ".bmp".equalsIgnoreCase(extName)) {
                this.type = CosFile.FILE_TYPE_IMAGE;
            } else {
                this.type = CosFile.FILE_TYPE_FILE;
            }
        }
    }

    public static CosFile buildDir(String fileName, String prefix) {
        int index = Math.max(fileName.indexOf(prefix), 0) + prefix.length();
        String name = fileName.substring(index);
        return CosFile.builder()
                .type(CosFile.FILE_TYPE_DIR)
                .fileName(fileName)
                .name(name)
                .prefix(prefix)
                .build();
    }

}
