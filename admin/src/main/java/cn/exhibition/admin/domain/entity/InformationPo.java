package cn.exhibition.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 资讯
 */
@Data
@TableName(value = "information")
public class InformationPo {
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
     * 封面图
     */
    @TableField(value = "cover_url")
    private String coverUrl;
}