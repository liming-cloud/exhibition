package cn.exhibition.admin.service;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.dto.ExhibitionDto;
import cn.exhibition.admin.domain.bo.ExhibitionBo;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import cn.exhibition.admin.domain.vo.CountryStatVo;
import cn.exhibition.admin.domain.vo.ExhibitionListVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author slm
 * @description
 */

public interface ExhibitionDetailService {

    void add(ExhibitionDto dto);

    /**
     * 记录不存在时抛 NotFoundException
     */
    void update(Integer id, ExhibitionDto dto);

    void delete(Integer id);

    ExhibitionDetailPo findById(Integer id);

    IPage<ExhibitionListVo> page(PageQuery pageQuery, ExhibitionBo exhibitionBo);

    List<CountryStatVo> countryStats();

    List<Integer> years();
}
