package cn.exhibition.admin.domain.bo;

import lombok.Data;

/**
 * @author slm
 * @description
 */
@Data
public class CarouselBo {

    private String title;

    private Integer status;

    public CarouselBo(String title, Integer status) {
        this.title = title;
        this.status = status;
    }
}
