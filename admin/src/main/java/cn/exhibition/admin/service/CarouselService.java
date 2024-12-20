package cn.exhibition.admin.service;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.bo.CarouselBo;
import cn.exhibition.admin.domain.entity.CarouselPo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author slm
 * @description
 */

public interface CarouselService {

    void save(CarouselPo carousel);

    void delete(int id);

    CarouselPo findById(int id);

    IPage<CarouselPo> findByPage(PageQuery query, CarouselBo carouselBo);
}
