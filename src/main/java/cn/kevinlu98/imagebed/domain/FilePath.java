package cn.kevinlu98.imagebed.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Author: 鲁恺文
 * Date: 2021/1/19 4:16 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FilePath {
    private String href;
    private String name;
}
