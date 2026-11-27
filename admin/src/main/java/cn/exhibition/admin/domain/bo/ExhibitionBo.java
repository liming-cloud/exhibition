package cn.exhibition.admin.domain.bo;

import cn.exhibition.admin.domain.enums.ExhibitionType;
import lombok.Data;

/**
 * 展会列表查询条件，都可不填
 */
@Data
public class ExhibitionBo {

    /**
     * 名称关键词
     */
    private String name;

    /**
     * 展会类型
     */
    private ExhibitionType type;

    /**
     * 开始日期所在年份
     */
    private Integer year;

    /**
     * 大洲名称，如「亚洲」
     */
    private String continent;

    /**
     * 国家 id
     */
    private Integer country;

    /**
     * true 时只查未结束的展会（结束日期不早于今天）
     */
    private Boolean upcoming;
}
