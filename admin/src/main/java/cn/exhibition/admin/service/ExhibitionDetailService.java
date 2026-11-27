package cn.exhibition.admin.service;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.dto.ExhibitionDto;
import cn.exhibition.admin.domain.bo.ExhibitionBo;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import com.baomidou.mybatisplus.core.metadata.IPage;

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

    IPage<ExhibitionDetailPo> page(PageQuery pageQuery, ExhibitionBo exhibitionBo);
}
