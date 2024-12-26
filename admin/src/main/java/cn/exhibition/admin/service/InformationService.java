package cn.exhibition.admin.service;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.entity.InformationPo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author slm
 * @description 市场资讯
 */
public interface InformationService{

    void save(InformationPo informationPo);

    void delete(Integer id);

    InformationPo findById(Integer id);

    IPage<InformationPo> findByPage(PageQuery pageQuery, String title);
}
