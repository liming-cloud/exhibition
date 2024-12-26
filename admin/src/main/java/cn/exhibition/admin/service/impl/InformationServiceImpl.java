package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.entity.InformationPo;
import cn.exhibition.admin.mapper.InformationMapper;
import cn.exhibition.admin.service.InformationService;
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
public class InformationServiceImpl implements InformationService {

    private final InformationMapper informationMapper;
    @Override
    public void save(InformationPo informationPo) {
        informationMapper.insertOrUpdate(informationPo);
    }

    @Override
    public void delete(Integer id) {
        informationMapper.deleteById(id);
    }

    @Override
    public InformationPo findById(Integer id) {
        return informationMapper.selectById(id);
    }

    @Override
    public IPage<InformationPo> findByPage(PageQuery pageQuery, String title) {
        return informationMapper.selectPage(pageQuery.build(), Wrappers.<InformationPo>lambdaQuery()
                .like(ObjectUtil.isNotNull(title), InformationPo::getTitle, title));
    }
}
