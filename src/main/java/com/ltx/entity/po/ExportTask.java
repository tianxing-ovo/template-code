package com.ltx.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 数据导出任务实体类
 *
 * @author tianxing
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("export_task")
public class ExportTask extends BasePO {

    @TableId
    private Long id;

    private Integer userId;

    private String fileKey;

    private Long fileSize;

    private Integer totalRecords;

    private Integer exportStatus;

    private String failReason;
}
