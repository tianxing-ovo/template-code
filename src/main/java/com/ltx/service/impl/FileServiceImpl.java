package com.ltx.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.ltx.common.exception.CustomException;
import com.ltx.config.FileStorageProperties;
import com.ltx.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件服务实现类
 *
 * @author tianxing
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileStorageProperties fileStorageProperties;

    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CustomException(400, "文件为空");
        }
        String safeFileName = FileUtil.getName(file.getOriginalFilename());
        if (StrUtil.isEmpty(safeFileName)) {
            throw new CustomException(400, "文件名不合法");
        }
        try {
            Path storagePath = fileStorageProperties.getStoragePath();
            Path filePath = storagePath.resolve(safeFileName).normalize();
            if (!filePath.startsWith(storagePath)) {
                throw new CustomException(400, "非法文件名");
            }
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            log.error("File upload failed: {}", safeFileName, e);
            throw new CustomException(500, "文件上传失败");
        }
        return safeFileName;
    }

    @Override
    public String uploadFiles(MultipartFile[] files) {
        List<String> successMessageList = new ArrayList<>();
        List<String> errorMessageList = new ArrayList<>();
        Path storagePath = fileStorageProperties.getStoragePath();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                errorMessageList.add("文件为空");
                continue;
            }
            String safeFileName = FileUtil.getName(file.getOriginalFilename());
            if (StrUtil.isEmpty(safeFileName)) {
                errorMessageList.add("文件名不合法");
                continue;
            }
            try {
                Path filePath = storagePath.resolve(safeFileName).normalize();
                if (!filePath.startsWith(storagePath)) {
                    errorMessageList.add(safeFileName + " 非法文件名");
                    continue;
                }
                file.transferTo(filePath.toFile());
                successMessageList.add(safeFileName + " 上传成功");
            } catch (IOException e) {
                log.error("Batch file upload failed: {}", safeFileName, e);
                errorMessageList.add(safeFileName + " 上传失败");
            }
        }
        StringBuilder sb = new StringBuilder();
        successMessageList.forEach(s -> sb.append(s).append(" "));
        errorMessageList.forEach(s -> sb.append(s).append(" "));
        return sb.toString().trim();
    }

    @Override
    public FileSystemResource downloadFile(String fileName) {
        String safeFileName = FileUtil.getName(fileName);
        if (StrUtil.isEmpty(safeFileName)) {
            throw new CustomException(400, "文件名不合法");
        }
        Path storagePath = fileStorageProperties.getStoragePath();
        Path filePath = storagePath.resolve(safeFileName).normalize();
        if (!filePath.startsWith(storagePath)) {
            throw new CustomException(400, "非法文件访问路径");
        }
        if (!Files.exists(filePath)) {
            throw new CustomException(404, "文件不存在");
        }
        return new FileSystemResource(filePath);
    }
}
