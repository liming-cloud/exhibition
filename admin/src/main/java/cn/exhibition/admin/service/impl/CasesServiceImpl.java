package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.dto.CasesDto;
import cn.exhibition.admin.domain.entity.CasesPo;
import cn.exhibition.admin.exception.NotFoundException;
import cn.exhibition.admin.mapper.CasesMapper;
import cn.exhibition.admin.service.CasesService;
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
public class CasesServiceImpl implements CasesService {

    private static final String NOT_FOUND = "案例不存在";

    private final CasesMapper casesMapper;

    @Override
    public void add(CasesDto dto) {
        casesMapper.insert(BeanUtil.copyProperties(dto, CasesPo.class));
    }

    @Override
    public void update(Integer id, CasesDto dto) {
        CasesPo casesPo = BeanUtil.copyProperties(dto, CasesPo.class);
        casesPo.setId(id);
        if (casesMapper.updateById(casesPo) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    @Override
    public void delete(Integer id) {
        if (casesMapper.deleteById(id) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    @Override
    public CasesPo findById(Integer id) {
        CasesPo casesPo = casesMapper.selectById(id);
        if (casesPo == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        return casesPo;
    }

    @Override
    public IPage<CasesPo> findByPage(PageQuery pageQuery, String title) {
        return casesMapper.selectPage(pageQuery.build(), Wrappers.<CasesPo>lambdaQuery()
                .like(ObjectUtil.isNotNull(title), CasesPo::getTitle, title));
    }
}
