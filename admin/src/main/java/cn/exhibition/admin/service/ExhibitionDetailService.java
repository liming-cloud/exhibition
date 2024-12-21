package cn.exhibition.admin.service;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.bo.ExhibitionBo;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author slm
 * @description
 */

public interface ExhibitionDetailService {

    /**
    * @author sunliming
    * @description 保存
    */
    void save(ExhibitionDetailPo exhibitionDetailPo);

    void delete(Integer id);

    ExhibitionDetailPo findById(Integer id);

    IPage<ExhibitionDetailPo> page(PageQuery pageQuery, ExhibitionBo exhibitionBo);
}
