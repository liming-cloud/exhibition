package cn.exhibition.admin.domain.vo;

import lombok.Data;

/**
 * 有展会的国家及展会数量
 */
@Data
public class CountryStatVo {

    /**
     * 国家 id（global_area.id）
     */
    private Integer id;

    private String continentCode;

    private String continentName;

    private String countryCode;

    private String countryName;

    /**
     * 展会总数
     */
    private Integer exhibitionCount;

    /**
     * 未结束的展会数
     */
    private Integer upcomingCount;
}
