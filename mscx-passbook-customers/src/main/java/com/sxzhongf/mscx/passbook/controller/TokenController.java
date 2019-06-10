package com.sxzhongf.mscx.passbook.controller;

import com.sxzhongf.mscx.passbook.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TokenController for  实现PassTemplate Token Upload
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/6/3
 */
@Slf4j
@Controller
public class TokenController {

    /**
     * {@link StringRedisTemplate} & {@link RedisTemplate} 区别在于key会由自身字符串定义
     */
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public TokenController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping(path = "/upload")
    //d8f14e90ed5a8d2daf86dd3f05e7d17e
    public String upload() {
        return "upload";
    }

    /**
     * 文件上传Action
     *
     * @param merchantsId        Form表单数据：商户id
     * @param passTemplateId     Form表单数据：优惠券模版id
     * @param file               Form表单数据：要上传的文件
     * @param redirectAttributes 重定向属性
     * @return 重定向的View名称
     */
    @PostMapping(path = "/token")
    public String tokenFileUpload(@RequestParam String merchantsId,
                                  @RequestParam String passTemplateId,
                                  @RequestParam MultipartFile file,
                                  RedirectAttributes redirectAttributes) {
        if (null == passTemplateId || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message",
                    "passTemplateId is null or file is empty!");
            return "redirect:/upload/status";
        }
        try {
            String real_path = ResourceUtils.getURL("classpath:").getPath();
            File currentFile = new File(real_path + "templates/" + Constants.TOKEN_DIR + merchantsId);
            if (!currentFile.exists()) {
                log.info("Create file dir : {}", currentFile.mkdir());
            }

            Path path = Paths.get(real_path + "templates/" + Constants.TOKEN_DIR, merchantsId, passTemplateId);
            Files.write(path, file.getBytes());

            //如果写入Redis报错
            if (!writeTokenToRedis(path, passTemplateId)) {
                redirectAttributes.addFlashAttribute("message",
                        "token写入Redis报错！");
            } else {
                redirectAttributes.addFlashAttribute("message",
                        "token写入Redis成功！" + file.getOriginalFilename());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/upload/status";
    }

    @GetMapping(name = "/upload/status")
    public String uploadStatus() {
        return "upload-status";
    }


    /**
     * 将Token写入Redis
     *
     * @param path {@link Path}
     * @param key  redis key
     * @return true/false
     */
    private boolean writeTokenToRedis(Path path, String key) {
        Set<String> tokens;
        try (Stream<String> stream = Files.lines(path)) {
            tokens = stream.collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!CollectionUtils.isEmpty(tokens)) {
            //Redis单机可以executePipelined， 集群的话不支持
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (String token : tokens) {
                    connection.sAdd(key.getBytes(), token.getBytes());
                }
                return null;
            });

            return true;
        }

        return false;
    }
}
