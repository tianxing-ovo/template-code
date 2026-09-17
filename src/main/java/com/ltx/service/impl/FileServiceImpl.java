package com.ltx.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ltx.common.exception.CustomException;
import com.ltx.config.FileStorageProperties;
import com.ltx.entity.vo.FileUploadVO;
import com.ltx.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    // 允许的文件扩展名
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "webp", "bmp", "svg",
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
            "zip", "rar", "7z", "tar", "gz",
            "txt", "csv", "json", "md"
    );

    @Override
    public FileUploadVO uploadFile(MultipartFile file) {
        // 校验文件是否为空
        if (file == null || file.isEmpty()) {
            throw new CustomException(400, "文件为空");
        }
        // 校验文件名是否为空
        String originalFilename = FileUtil.getName(file.getOriginalFilename());
        if (StrUtil.isBlank(originalFilename)) {
            throw new CustomException(400, "文件名不合法");
        }
        // 校验文件扩展名是否在允许列表中
        String extName = FileUtil.extName(originalFilename).toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extName)) {
            throw new CustomException(400, "不支持的文件类型: " + extName);
        }
        // 生成按日期划分的上传子目录
        String dateSubDir = "upload/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // 使用UUID生成唯一文件名
        String newFileName = IdUtil.fastSimpleUUID() + "." + extName;
        try {
            // 构建目标目录路径
            Path storagePath = fileStorageProperties.getStoragePath();
            Path targetDir = storagePath.resolve(dateSubDir).normalize();
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            // 构建目标文件路径
            Path filePath = targetDir.resolve(newFileName).normalize();
            // 校验文件路径是否在存储路径下
            if (!filePath.startsWith(storagePath)) {
                throw new CustomException(400, "非法文件存储路径");
            }
            file.transferTo(filePath.toFile());
            // 返回相对路径标识与原始文件名
            String fileKey = dateSubDir + "/" + newFileName;
            return new FileUploadVO(fileKey, originalFilename);
        } catch (IOException e) {
            log.error("File upload failed: {}", originalFilename, e);
            throw new CustomException(500, "文件上传失败");
        }
    }

    @Override
    public List<FileUploadVO> uploadFiles(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new CustomException(400, "文件列表为空");
        }
        List<FileUploadVO> fileList = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            fileList.add(uploadFile(file));
        }
        return fileList;
    }

    @Override
    public FileSystemResource loadFileAsResource(String fileKey) {
        if (StrUtil.isBlank(fileKey)) {
            throw new CustomException(400, "文件标识不合法");
        }
        Path storagePath = fileStorageProperties.getStoragePath();
        Path filePath = storagePath.resolve(fileKey).normalize();
        if (!filePath.startsWith(storagePath)) {
            throw new CustomException(400, "非法文件访问路径");
        }
        if (!Files.exists(filePath)) {
            throw new CustomException(404, "文件不存在");
        }
        return new FileSystemResource(filePath);
    }
}
