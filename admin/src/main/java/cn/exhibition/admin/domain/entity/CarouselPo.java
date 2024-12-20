package cn.exhibition.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 轮播图
 */
@Data
@TableName(value = "carousel")
public class CarouselPo {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "title")
    private String title;

    @TableField(value = "image_url")
    private String imageUrl;

    @TableField(value = "link")
    private String link;

    @TableField(value = "sort")
    private Integer sort;

    @TableField(value = "`status`")
    private Integer status;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}