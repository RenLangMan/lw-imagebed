package cn.kevinlu98.imagebed.domain.enums;

import lombok.Getter;

/**
 * Author: 鲁恺文
 * Date: 2021/1/15 4:41 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Getter
public enum ResponseCode {
    SUCCESS(2000, "SUCCESS"),
    FILE_EMPTY(5001, "文件不能为空"),
    FILE_UPLOAD_ERROR(5002, "文件上传出错"),
    FILE_MKDIR_ERROR(5003, "创建文件夹失败"),
    FILE_DELETE_ERROR(5004, "删除文件失败"),
    USER_ERROR(10001, "用户名或密码错误"),
    ;
    private final int code;
    private final String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
