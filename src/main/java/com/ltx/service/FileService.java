package com.ltx.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 *
 * @author tianxing
 */
public interface FileService {

    /**
     * 单个文件上传
     *
     * @param file 文件
     * @return 保存后的文件名
     */
    String uploadFile(MultipartFile file);

    /**
     * 多个文件上传
     *
     * @param files 文件数组
     * @return 上传结果的消息
     */
    String uploadFiles(MultipartFile[] files);

    /**
     * 获取下载文件资源
     *
     * @param fileName 文件名称
     * @return 文件系统资源
     */
    FileSystemResource downloadFile(String fileName);
}
