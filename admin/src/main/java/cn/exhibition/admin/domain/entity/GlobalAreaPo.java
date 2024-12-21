package cn.exhibition.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 世界国家信息
 */
@Data
@TableName(value = "global_area")
public class GlobalAreaPo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 大洲编码
     */
    @TableField(value = "continent_code")
    private String continentCode;

    /**
     * 大洲名称
     */
    @TableField(value = "continent_name")
    private String continentName;

    /**
     * 城市编码
     */
    @TableField(value = "country_code")
    private String countryCode;

    /**
     * 城市名称
     */
    @TableField(value = "country_name")
    private String countryName;
}