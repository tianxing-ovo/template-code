package com.ltx.entity.vo;

/**
 * 文件上传视图对象
 *
 * @param fileKey      文件相对存储标识
 * @param originalName 原始文件名
 * @author tianxing
 */
public record FileUploadVO(String fileKey, String originalName) {
}
