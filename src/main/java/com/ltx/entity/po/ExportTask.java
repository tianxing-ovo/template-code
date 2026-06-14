package com.ltx.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 数据导出任务实体类
 *
 * @author tianxing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("export_task")
public class ExportTask {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer userId;

    private String fileName;

    private String filePath;

    private Long fileSize;

    private Integer totalRecords;

    private Integer exportStatus;

    private String failReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
