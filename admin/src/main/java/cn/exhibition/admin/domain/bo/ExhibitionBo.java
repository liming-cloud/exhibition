package cn.exhibition.admin.domain.bo;

import lombok.Data;

/**
 * @author slm
 * @description
 */
@Data
public class ExhibitionBo {

    private String name;

    private String year;

    private String continent;

    private Integer country;

    public ExhibitionBo(String name, String year, String continent, Integer country) {
        this.name = name;
        this.year = year;
        this.continent = continent;
        this.country = country;
    }
}
