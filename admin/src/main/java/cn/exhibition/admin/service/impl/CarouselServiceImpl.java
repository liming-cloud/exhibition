package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.bo.CarouselBo;
import cn.exhibition.admin.domain.dto.CarouselDto;
import cn.exhibition.admin.domain.entity.CarouselPo;
import cn.exhibition.admin.exception.NotFoundException;
import cn.exhibition.admin.mapper.CarouselMapper;
import cn.exhibition.admin.service.CarouselService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author slm
 * @description
 */
@Service
@RequiredArgsConstructor
public class CarouselServiceImpl implements CarouselService {

    private static final String NOT_FOUND = "轮播图不存在";

    private final CarouselMapper carouselMapper;

    @Override
    public void add(CarouselDto dto) {
        CarouselPo carousel = BeanUtil.copyProperties(dto, CarouselPo.class);
        carousel.setCreateTime(LocalDateTime.now());
        carousel.setUpdateTime(LocalDateTime.now());
        carouselMapper.insert(carousel);
    }

    @Override
    public void update(int id, CarouselDto dto) {
        CarouselPo carousel = BeanUtil.copyProperties(dto, CarouselPo.class);
        carousel.setId(id);
        carousel.setUpdateTime(LocalDateTime.now());
        if (carouselMapper.updateById(carousel) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    @Override
    public void delete(int id) {
        if (carouselMapper.deleteById(id) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    @Override
    public CarouselPo findById(int id) {
        CarouselPo carousel = carouselMapper.selectById(id);
        if (carousel == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        return carousel;
    }

    @Override
    public IPage<CarouselPo> findByPage(PageQuery query, CarouselBo carouselBo) {
        return carouselMapper.selectPage(query.build(), Wrappers.<CarouselPo>lambdaQuery()
                .like(ObjectUtil.isNotNull(carouselBo.getTitle()), CarouselPo::getTitle, carouselBo.getTitle())
                .eq(ObjectUtil.isNotNull(carouselBo.getStatus()), CarouselPo::getStatus, carouselBo.getStatus())
                .orderByAsc(CarouselPo::getSort)
                .orderByAsc(CarouselPo::getId));
    }
}
