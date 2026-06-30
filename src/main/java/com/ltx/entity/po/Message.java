package com.ltx.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 消息实体类
 *
 * @author tianxing
 */
@Data
@NoArgsConstructor
@TableName("message")
public class Message {

    /* 由ShardingSphere雪花算法生成 */
    @TableId
    private Long id;

    private String title;

    private String content;

    private LocalDateTime createTime;

    public Message(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
