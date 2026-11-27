package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.bo.ExhibitionBo;
import cn.exhibition.admin.domain.dto.ExhibitionDto;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import cn.exhibition.admin.domain.entity.GlobalAreaPo;
import cn.exhibition.admin.domain.vo.CountryStatVo;
import cn.exhibition.admin.domain.vo.ExhibitionListVo;
import cn.exhibition.admin.exception.NotFoundException;
import cn.exhibition.admin.exception.ServiceException;
import cn.exhibition.admin.mapper.ExhibitionDetailMapper;
import cn.exhibition.admin.mapper.GlobalAreaMapper;
import cn.exhibition.admin.service.ExhibitionDetailService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author slm
 * @description
 */
@Service
@RequiredArgsConstructor
public class ExhibitionDetailServiceImpl implements ExhibitionDetailService {

    private static final String NOT_FOUND = "展会不存在";

    private final ExhibitionDetailMapper exhibitionDetailMapper;
    private final GlobalAreaMapper globalAreaMapper;

    @Override
    public void add(ExhibitionDto dto) {
        exhibitionDetailMapper.insert(toPo(dto));
    }

    @Override
    public void update(Integer id, ExhibitionDto dto) {
        ExhibitionDetailPo exhibitionDetailPo = toPo(dto);
        exhibitionDetailPo.setId(id);
        if (exhibitionDetailMapper.updateById(exhibitionDetailPo) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    /**
     * 转成实体并按国家 id 填写国家名称，国家不存在时报错
     */
    private ExhibitionDetailPo toPo(ExhibitionDto dto) {
        GlobalAreaPo globalAreaPo = globalAreaMapper.selectById(dto.getCountry());
        if (globalAreaPo == null) {
            throw new ServiceException("所选国家不存在");
        }
        ExhibitionDetailPo exhibitionDetailPo = BeanUtil.copyProperties(dto, ExhibitionDetailPo.class);
        exhibitionDetailPo.setCountryName(globalAreaPo.getCountryName());
        return exhibitionDetailPo;
    }

    @Override
    public void delete(Integer id) {
        if (exhibitionDetailMapper.deleteById(id) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    @Override
    public ExhibitionDetailPo findById(Integer id) {
        ExhibitionDetailPo exhibitionDetailPo = exhibitionDetailMapper.selectById(id);
        if (exhibitionDetailPo == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        return exhibitionDetailPo;
    }

    @Override
    public IPage<ExhibitionListVo> page(PageQuery pageQuery, ExhibitionBo exhibitionBo) {
        return exhibitionDetailMapper.selectListPage(pageQuery.build(), exhibitionBo, LocalDate.now());
    }

    @Override
    public List<CountryStatVo> countryStats() {
        return exhibitionDetailMapper.selectCountryStats(LocalDate.now());
    }

    @Override
    public List<Integer> years() {
        return exhibitionDetailMapper.selectYears();
    }
}
