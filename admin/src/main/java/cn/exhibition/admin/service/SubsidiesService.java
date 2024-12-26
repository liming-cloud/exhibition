package cn.exhibition.admin.service;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.entity.SubsidiesPo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author slm
 * @description 补贴信息
 */

public interface SubsidiesService {

    void save(SubsidiesPo subsidiesPo);

    void delete(Integer id);

    SubsidiesPo findById(Integer id);

    IPage<SubsidiesPo> page(PageQuery pageQuery, String title);
}
