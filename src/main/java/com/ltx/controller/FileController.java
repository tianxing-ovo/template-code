package com.ltx.controller;

import com.ltx.common.Result;
import com.ltx.entity.vo.FileUploadVO;
import com.ltx.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @PostMapping
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        FileUploadVO vo = fileService.uploadFile(file);
        return Result.success()
                .put("fileKey", vo.fileKey())
                .put("originalName", vo.originalName());
    }

    /**
     * 多个文件上传
     *
     * @param files 文件数组
     * @return 通用响应对象
     */
    @PostMapping("/batch")
    public Result uploadFiles(@RequestParam("files") MultipartFile[] files) {
        List<FileUploadVO> fileList = fileService.uploadFiles(files);
        return Result.success()
                .put("fileList", fileList)
                .put("total", fileList.size());
    }
}
