package com.ltx.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件存储属性
 *
 * @author tianxing
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {

    // 相对存储路径
    private String basePath = "./data/files";
    // 绝对存储路径
    private Path storagePath;

    @PostConstruct
    public void init() {
        storagePath = Paths.get(basePath).toAbsolutePath().normalize();
        try {
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
                log.info("已自动创建文件存储目录: {}", storagePath);
            } else {
                log.info("文件存储目录已存在: {}", storagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("初始化文件存储目录失败: " + storagePath, e);
        }
    }
}
