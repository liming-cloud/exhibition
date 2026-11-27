package cn.exhibition.admin.domain.entity;

import cn.exhibition.admin.domain.enums.ExhibitionType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName(value = "exhibition_detail")
public class ExhibitionDetailPo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 位置
     */
    @TableField(value = "`location`")
    private String location;

    /**
     * 开始日期
     */
    @TableField(value = "start_date")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @TableField(value = "end_date")
    private LocalDate endDate;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

    @TableField(value = "content")
    private String content;

    /**
     * 展会国家
     */
    @TableField(value = "country")
    private Integer country;

    /**
     * 国家名称
     */
    @TableField(value = "country_name")
    private String countryName;

    /**
     * 展会类型
     */
    @TableField(value = "exhibition_type")
    private ExhibitionType exhibitionType;

    /**
     * 创建时间，新增时由后端填写
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间，新增、修改时由后端填写
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
