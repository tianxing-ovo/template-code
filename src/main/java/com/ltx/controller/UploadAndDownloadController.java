package com.ltx.controller;

import com.ltx.constant.Constant;
import com.ltx.entity.Result;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 上传和下载控制器
 *
 * @author tianxing
 */
@RestController
@Slf4j
public class UploadAndDownloadController {

    /**
     * 提取安全的文件名(防止路径遍历攻击)
     *
     * @param originalFileName 原始文件名
     * @return 安全的文件名
     */
    private String getSecureFileName(String originalFileName) {
        // 通过Path.getFileName()去除路径分隔符和".."等路径穿越字符
        return Paths.get(Objects.requireNonNull(originalFileName)).getFileName().toString();
    }

    /**
     * 单个文件上传
     *
     * @param file 文件
     * @return 通用响应对象
     */
    @PostMapping("/single-upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            String fileName = getSecureFileName(file.getOriginalFilename());
            try {
                file.transferTo(Constant.DESKTOP_PATH.resolve(fileName).toFile());
            } catch (IOException e) {
                return Result.fail(201, "文件上传失败");
            }
            return Result.success();
        }
        return Result.fail(201, "文件为空,请上传文件");
    }

    /**
     * 多个文件上传
     *
     * @param files 文件数组
     * @return 通用响应对象
     */
    @PostMapping("/multiple-upload")
    public Result uploadFiles(@RequestParam("files") MultipartFile[] files) {
        // 成功的消息
        List<String> successMessageList = new ArrayList<>();
        // 失败的消息
        List<String> errorMessageList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = getSecureFileName(file.getOriginalFilename());
                try {
                    file.transferTo(Constant.DESKTOP_PATH.resolve(fileName).toFile());
                    successMessageList.add(fileName + " 上传成功");
                } catch (IOException e) {
                    errorMessageList.add(fileName + " 上传失败");
                }
            } else {
                errorMessageList.add("文件为空,请上传文件");
            }
        }
        StringBuilder sb = new StringBuilder();
        successMessageList.forEach(s -> sb.append(s).append(" "));
        errorMessageList.forEach(s -> sb.append(s).append(" "));
        return Result.success(sb.toString());
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @return 响应实体
     */
    @SneakyThrows
    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> download(@PathVariable String fileName) {
        fileName = getSecureFileName(fileName);
        FileSystemResource resource = new FileSystemResource(Constant.DESKTOP_PATH.resolve(fileName));
        HttpHeaders headers = new HttpHeaders();
        // "application/octet-stream"
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        // 附件
        headers.setContentDispositionFormData("attachment", fileName);
        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
