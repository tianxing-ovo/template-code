package com.ltx.controller;

import com.ltx.common.Result;
import com.ltx.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

/**
 * 文件控制器
 *
 * @author tianxing
 */
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 单个文件上传
     *
     * @param file 文件
     * @return 通用响应对象
     */
    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.uploadFile(file);
        return Result.success().put("fileName", fileName);
    }

    /**
     * 多个文件上传
     *
     * @param files 文件数组
     * @return 通用响应对象
     */
    @PostMapping("/batch-upload")
    public Result uploadFiles(@RequestParam("files") MultipartFile[] files) {
        String msg = fileService.uploadFiles(files);
        return Result.success(msg);
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @return 响应实体
     */
    @SneakyThrows
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<?> download(@PathVariable String fileName) {
        FileSystemResource resource = fileService.downloadFile(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(resource.getFilename(), StandardCharsets.UTF_8)
                .build();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
