package cn.kevinlu98.imagebed.controller;

import cn.kevinlu98.imagebed.domain.CosFile;
import cn.kevinlu98.imagebed.domain.FilePath;
import cn.kevinlu98.imagebed.interceptor.LoginInterceptor;
import cn.kevinlu98.imagebed.serice.ImageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: 鲁恺文
 * Date: 2021/1/18 7:47 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Controller
public class IndexController {


    private final ImageService service;

    public IndexController(ImageService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String login(HttpServletRequest request, Model model) {
        request.getSession().removeAttribute(LoginInterceptor.USER_SIGN);
        model.addAttribute("error", "退出成功");
        return "login";
    }


    @GetMapping("/")
    public String index(Model model) {
        String prefix = "";
        List<CosFile> cosFiles = service.list(prefix);
        model.addAttribute("files", cosFiles);
        model.addAttribute("prefix", prefix);
        model.addAttribute("pathes", new ArrayList<FilePath>());
        return "index";
    }

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "") String prefix, Model model) {
        if (prefix.length() > 0 && !prefix.endsWith("/")) {
            prefix += "/";
        }
        List<CosFile> cosFiles = service.list(prefix);
        model.addAttribute("files", cosFiles);
        return "index::lw-files-list";
    }

    @GetMapping("/path")
    public String path(@RequestParam(defaultValue = "") String prefix, Model model) {
        if (prefix.length() > 0 && !prefix.endsWith("/")) {
            prefix += "/";
        }
        if (StringUtils.isEmpty(prefix)) {
            model.addAttribute("pathes", new ArrayList<FilePath>());
        } else {
            StringBuilder builder = new StringBuilder();
            List<FilePath> pathes = Arrays.stream(prefix.split("/")).map(s -> {
                builder.append(s).append("/");
                return FilePath.builder()
                        .href(builder.toString())
                        .name(s)
                        .build();
            }).collect(Collectors.toList());
            model.addAttribute("pathes", pathes);
        }
        model.addAttribute("prefix", prefix);
        return "index::lw-path";
    }


}
