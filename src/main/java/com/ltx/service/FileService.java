package com.ltx.service;

import com.ltx.entity.vo.FileUploadVO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
     * @return 文件上传视图对象
     */
    FileUploadVO uploadFile(MultipartFile file);

    /**
     * 多个文件上传
     *
     * @param files 文件数组
     * @return 文件上传视图对象列表
     */
    List<FileUploadVO> uploadFiles(MultipartFile[] files);

    /**
     * 将文件作为资源加载
     *
     * @param fileKey 文件相对存储标识
     * @return 文件系统资源
     */
    FileSystemResource loadFileAsResource(String fileKey);
}
