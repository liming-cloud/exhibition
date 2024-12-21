package cn.exhibition.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
     * 时间
     */
    @TableField(value = "`time`")
    private String time;

    /**
     * 年份
     */
    @TableField(value = "`year`")
    private String year;

    /**
     * 月份
     */
    @TableField(value = "`month`")
    private String month;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

    @TableField(value = "file_url")
    private String fileUrl;

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
}