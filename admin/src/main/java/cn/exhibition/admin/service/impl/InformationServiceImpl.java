package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.dto.InformationDto;
import cn.exhibition.admin.domain.entity.InformationPo;
import cn.exhibition.admin.domain.vo.ArticleListVo;
import cn.exhibition.admin.exception.NotFoundException;
import cn.exhibition.admin.mapper.InformationMapper;
import cn.exhibition.admin.service.InformationService;
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
public class InformationServiceImpl implements InformationService {

    private static final String NOT_FOUND = "资讯不存在";

    private final InformationMapper informationMapper;

    @Override
    public void add(InformationDto dto) {
        informationMapper.insert(BeanUtil.copyProperties(dto, InformationPo.class));
    }

    @Override
    public void update(Integer id, InformationDto dto) {
        InformationPo informationPo = BeanUtil.copyProperties(dto, InformationPo.class);
        informationPo.setId(id);
        if (informationMapper.updateById(informationPo) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    @Override
    public void delete(Integer id) {
        if (informationMapper.deleteById(id) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    @Override
    public InformationPo findById(Integer id) {
        InformationPo informationPo = informationMapper.selectById(id);
        if (informationPo == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        return informationPo;
    }

    @Override
    public IPage<ArticleListVo> findByPage(PageQuery pageQuery, String title) {
        return informationMapper.selectPage(pageQuery.build(), Wrappers.<InformationPo>lambdaQuery()
                        .like(ObjectUtil.isNotNull(title), InformationPo::getTitle, title)
                        .orderByDesc(InformationPo::getCreateTime)
                        .orderByDesc(InformationPo::getId))
                .convert(po -> {
                    ArticleListVo vo = BeanUtil.copyProperties(po, ArticleListVo.class);
                    vo.setSummary(StringUtils.summary(po.getContent()));
                    return vo;
                });
    }
}
