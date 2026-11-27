package cn.exhibition.admin.service;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.dto.CasesDto;
import cn.exhibition.admin.domain.entity.CasesPo;
import cn.exhibition.admin.domain.vo.ArticleListVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author slm
 * @description 项目案例服务
 */

public interface CasesService {

    void add(CasesDto dto);

    /**
     * 记录不存在时抛 NotFoundException
     */
    void update(Integer id, CasesDto dto);

    void delete(Integer id);

    CasesPo findById(Integer id);

    IPage<ArticleListVo> findByPage(PageQuery pageQuery, String title);
}
