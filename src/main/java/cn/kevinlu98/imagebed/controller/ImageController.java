package cn.kevinlu98.imagebed.controller;

import cn.kevinlu98.imagebed.annotation.SwaggerMethodShow;
import cn.kevinlu98.imagebed.annotation.SwaggerShow;
import cn.kevinlu98.imagebed.domain.CosFile;
import cn.kevinlu98.imagebed.domain.Result;
import cn.kevinlu98.imagebed.domain.enums.ResponseCode;
import cn.kevinlu98.imagebed.interceptor.LoginInterceptor;
import cn.kevinlu98.imagebed.serice.ImageService;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Author: 鲁恺文
 * Date: 2021/1/15 4:29 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Slf4j
@SwaggerShow
@RestController
@RequestMapping("/image")
@Api(tags = "冷文图床测试")
public class ImageController {

    @Value("${user.username}")
    private String username;

    @Value("${user.password}")
    private String password;

    private final ImageService service;

    public ImageController(ImageService service) {
        this.service = service;
    }

    @SwaggerMethodShow
    @ApiOperation(value = "上传文件")
    @ApiOperationSupport(order = 1)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "prefix", value = "父级路径", paramType = "query", defaultValue = ""),
    })
    @PostMapping("/upload")
    public Result<String> upload(String prefix, @ApiParam(value = "文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            if (prefix.length() > 0 && !prefix.endsWith("/")) {
                prefix += "/";
            }
            if (file == null || file.isEmpty() || StringUtils.isBlank(file.getOriginalFilename()))
                return Result.fail(ResponseCode.FILE_EMPTY);
            String url = service.upload(prefix, file);
            return Result.success(url);
        } catch (IOException e) {
            log.error("上传出错:", e);
            return Result.fail(ResponseCode.FILE_UPLOAD_ERROR);
        }
    }

    @SwaggerMethodShow
    @ApiOperation(value = "创建文件夹")
    @ApiOperationSupport(order = 2)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "prefix", value = "父级路径", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "dirName", value = "目录名称", paramType = "query", defaultValue = ""),
    })
    @PostMapping("/mkdir")
    public Result<String> mkdir(String prefix, String dirName) {
        try {
            if (prefix.length() > 0 && !prefix.endsWith("/")) {
                prefix += "/";
            }
            if (!dirName.endsWith("/")) {
                dirName += "/";
            }
            service.mkdir(prefix + dirName);
            return Result.success();
        } catch (Exception e) {
            log.error("创建文件夹出错:", e);
            return Result.fail(ResponseCode.FILE_MKDIR_ERROR);
        }
    }


    @SwaggerMethodShow
    @ApiOperation(value = "文件列表")
    @ApiOperationSupport(order = 3)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "prefix", value = "父级路径", paramType = "query", defaultValue = "/"),
    })
    @GetMapping("/list")
    public Result<List<CosFile>> list(String prefix) {
        if (StringUtils.isEmpty(prefix))
            prefix = "/";
        if (!prefix.endsWith("/"))
            prefix += "/";
        List<CosFile> cosFiles = service.list(prefix);
        return Result.success(cosFiles);
    }

    @SwaggerMethodShow
    @ApiOperation(value = "删除文件")
    @ApiOperationSupport(order = 2)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "路径", defaultValue = ""),
    })
    @PostMapping("/delete")
    public Result<String> delete(String path) {
        try {
            service.delete(path);
            return Result.success();
        } catch (Exception e) {
            log.error("删除文件出错:", e);
            return Result.fail(ResponseCode.FILE_DELETE_ERROR);
        }
    }


    @PostMapping("/login")
    public Result<String> login(String username, String password, HttpServletRequest request) {
        if (this.username.equals(username) && this.password.equals(password)) {
            request.getSession().setAttribute(LoginInterceptor.USER_SIGN, username);
            return Result.success("登录成功");
        }
        return Result.fail(ResponseCode.USER_ERROR);
    }

}
