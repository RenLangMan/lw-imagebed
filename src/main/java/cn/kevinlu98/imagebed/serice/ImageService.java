package cn.kevinlu98.imagebed.serice;

import cn.kevinlu98.imagebed.domain.CosFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Author: 鲁恺文
 * Date: 2021/1/18 4:46 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
public interface ImageService {
    /**
     * 获取文件列表
     *
     * @param prefix 文件前辍
     * @return 文件列表
     */
    List<CosFile> list(String prefix);

    String upload(String prefix, MultipartFile file) throws IOException;

    void mkdir(String name);

    void delete(String name);
}
