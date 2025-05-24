package cn.exhibition.admin.service;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.entity.ExhibitionRegistrationPo;
import cn.exhibition.admin.domain.enums.ContactProgress;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 展会报名服务接口
 */
public interface ExhibitionRegistrationService {

    /**
     * 保存报名信息
     */
    void save(ExhibitionRegistrationPo registration);

    /**
     * 分页查询报名列表（包含展会信息）
     */
    IPage<ExhibitionRegistrationPo> pageWithExhibitionInfo(PageQuery pageQuery);

    /**
     * 更新联系进度
     */
    void updateProgress(Integer id, ContactProgress progress);

    /**
     * 根据ID获取报名信息
     */
    ExhibitionRegistrationPo getById(Integer id);

    /**
     * 删除报名记录
     */
    void removeById(Integer id);
}