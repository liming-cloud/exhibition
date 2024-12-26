package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.entity.CasesPo;
import cn.exhibition.admin.mapper.CasesMapper;
import cn.exhibition.admin.service.CasesService;
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

    private final CasesMapper casesMapper;
    /**
     * @param casesPo
     * @author sunliming
     * @description 保存
     */
    @Override
    public void save(CasesPo casesPo) {

        casesMapper.insertOrUpdate(casesPo);
    }

    @Override
    public void delete(Integer id) {
        casesMapper.deleteById(id);
    }

    @Override
    public CasesPo findById(Integer id) {
        return casesMapper.selectById(id);
    }

    @Override
    public IPage<CasesPo> findByPage(PageQuery pageQuery, String title) {
        return casesMapper.selectPage(pageQuery.build(), Wrappers.<CasesPo>lambdaQuery()
                .like(ObjectUtil.isNotNull(title), CasesPo::getTitle, title));
    }
}
