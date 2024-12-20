package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.bo.CarouselBo;
import cn.exhibition.admin.domain.entity.CarouselPo;
import cn.exhibition.admin.mapper.CarouselMapper;
import cn.exhibition.admin.service.CarouselService;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author slm
 * @description
 */
@Service
@RequiredArgsConstructor
public class CarouselServiceImpl implements CarouselService {

    private final CarouselMapper carouselMapper;

    @Override
    public void save(CarouselPo carousel) {

        carouselMapper.insertOrUpdate(carousel);
    }


    @Override
    public void delete(int id) {
        carouselMapper.deleteById(id);
    }

    @Override
    public CarouselPo findById(int id) {
        return carouselMapper.selectById(id);
    }

    @Override
    public IPage<CarouselPo> findByPage(PageQuery query, CarouselBo carouselBo) {

        return carouselMapper.selectPage(query.build(), Wrappers.<CarouselPo>lambdaQuery()
                .like(ObjectUtil.isNotNull(carouselBo.getTitle()), CarouselPo::getTitle, carouselBo.getTitle())
                .eq(ObjectUtil.isNotNull(carouselBo.getStatus()), CarouselPo::getStatus, carouselBo.getStatus()));
    }
}
