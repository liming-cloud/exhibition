package cn.exhibition.admin.service;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.entity.CasesPo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author slm
 * @description 项目案例服务
 */

public interface CasesService {

    /**
    * @author sunliming
    * @description 保存
    */
    void save(CasesPo casesPo);

    void delete(Integer id);

    CasesPo findById(Integer id);

    IPage<CasesPo> findByPage(PageQuery pageQuery, CasesPo casesPo);
}
