package cn.exhibition.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 补贴
 */
@Data
@TableName(value = "subsidies")
public class SubsidiesPo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题简介
     */
    @TableField(value = "title")
    private String title;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 补贴金额
     */
    @TableField(value = "price")
    private Long price;

    /**
     * 开始日期
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /**
     * 结束日期
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;
}