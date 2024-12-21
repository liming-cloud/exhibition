package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.bo.ExhibitionBo;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import cn.exhibition.admin.domain.entity.GlobalAreaPo;
import cn.exhibition.admin.mapper.ExhibitionDetailMapper;
import cn.exhibition.admin.mapper.GlobalAreaMapper;
import cn.exhibition.admin.service.ExhibitionDetailService;
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
public class ExhibitionDetailServiceImpl implements ExhibitionDetailService {

    private final ExhibitionDetailMapper exhibitionDetailMapper;
    private final GlobalAreaMapper globalAreaMapper;

    /**
     * @param exhibitionDetailPo
     * @author sunliming
     * @description 保存
     */
    @Override
    public void save(ExhibitionDetailPo exhibitionDetailPo) {

        GlobalAreaPo globalAreaPo = globalAreaMapper.selectById(exhibitionDetailPo.getCountry());
        exhibitionDetailPo.setCountryName(globalAreaPo.getCountryName());

        exhibitionDetailMapper.insertOrUpdate(exhibitionDetailPo);
    }

    @Override
    public void delete(Integer id) {
        exhibitionDetailMapper.deleteById(id);
    }

    @Override
    public ExhibitionDetailPo findById(Integer id) {
        return exhibitionDetailMapper.selectById(id);
    }

    @Override
    public IPage<ExhibitionDetailPo> page(PageQuery pageQuery, ExhibitionBo exhibitionBo) {

        return exhibitionDetailMapper.selectPage(pageQuery.build(), Wrappers.<ExhibitionDetailPo>lambdaQuery()
                .like(ObjectUtil.isNotNull(exhibitionBo.getName()), ExhibitionDetailPo::getName, exhibitionBo.getName())
                .eq(ObjectUtil.isNotNull(exhibitionBo.getCountry()), ExhibitionDetailPo::getCountry, exhibitionBo.getCountry()));
    }
}
