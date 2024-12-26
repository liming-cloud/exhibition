package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.entity.SubsidiesPo;
import cn.exhibition.admin.mapper.SubsidiesMapper;
import cn.exhibition.admin.service.SubsidiesService;
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
public class SubsidiesServiceImpl implements SubsidiesService {

    private final SubsidiesMapper subsidiesMapper;
    @Override
    public void save(SubsidiesPo subsidiesPo) {
        subsidiesMapper.insertOrUpdate(subsidiesPo);
    }

    @Override
    public void delete(Integer id) {
        subsidiesMapper.deleteById(id);
    }

    @Override
    public SubsidiesPo findById(Integer id) {
        return subsidiesMapper.selectById(id);
    }

    @Override
    public IPage<SubsidiesPo> page(PageQuery pageQuery, String title) {
        return subsidiesMapper.selectPage(pageQuery.build(), Wrappers.<SubsidiesPo>lambdaQuery()
                .like(ObjectUtil.isNotNull(title), SubsidiesPo::getTitle, title));
    }
}
