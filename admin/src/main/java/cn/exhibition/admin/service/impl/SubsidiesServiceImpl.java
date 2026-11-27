package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.dto.SubsidiesDto;
import cn.exhibition.admin.domain.entity.SubsidiesPo;
import cn.exhibition.admin.domain.vo.SubsidiesListVo;
import cn.exhibition.admin.exception.NotFoundException;
import cn.exhibition.admin.mapper.SubsidiesMapper;
import cn.exhibition.admin.service.SubsidiesService;
import cn.exhibition.admin.util.StringUtils;
import cn.hutool.core.bean.BeanUtil;
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

    private static final String NOT_FOUND = "补贴政策不存在";

    private final SubsidiesMapper subsidiesMapper;

    @Override
    public void add(SubsidiesDto dto) {
        subsidiesMapper.insert(BeanUtil.copyProperties(dto, SubsidiesPo.class));
    }

    @Override
    public void update(Integer id, SubsidiesDto dto) {
        SubsidiesPo subsidiesPo = BeanUtil.copyProperties(dto, SubsidiesPo.class);
        subsidiesPo.setId(id);
        if (subsidiesMapper.updateById(subsidiesPo) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    @Override
    public void delete(Integer id) {
        if (subsidiesMapper.deleteById(id) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    @Override
    public SubsidiesPo findById(Integer id) {
        SubsidiesPo subsidiesPo = subsidiesMapper.selectById(id);
        if (subsidiesPo == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        return subsidiesPo;
    }

    @Override
    public IPage<SubsidiesListVo> page(PageQuery pageQuery, String title) {
        return subsidiesMapper.selectPage(pageQuery.build(), Wrappers.<SubsidiesPo>lambdaQuery()
                        .like(ObjectUtil.isNotNull(title), SubsidiesPo::getTitle, title)
                        .orderByDesc(SubsidiesPo::getCreateTime)
                        .orderByDesc(SubsidiesPo::getId))
                .convert(po -> {
                    SubsidiesListVo vo = BeanUtil.copyProperties(po, SubsidiesListVo.class);
                    vo.setSummary(StringUtils.summary(po.getContent()));
                    return vo;
                });
    }
}
