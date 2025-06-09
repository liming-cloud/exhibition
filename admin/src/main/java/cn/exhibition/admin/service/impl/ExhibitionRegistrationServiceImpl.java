package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import cn.exhibition.admin.domain.entity.ExhibitionRegistrationPo;
import cn.exhibition.admin.domain.enums.ContactProgress;
import cn.exhibition.admin.mapper.ExhibitionDetailMapper;
import cn.exhibition.admin.mapper.ExhibitionRegistrationMapper;
import cn.exhibition.admin.service.ExhibitionRegistrationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 展会报名服务实现类
 */
@Service
@RequiredArgsConstructor
public class ExhibitionRegistrationServiceImpl implements ExhibitionRegistrationService {

    private final ExhibitionRegistrationMapper registrationMapper;
    private final ExhibitionDetailMapper exhibitionDetailMapper;

    @Override
    public void save(ExhibitionRegistrationPo registration) {
        registration.setContactProgress(ContactProgress.UNCONTACTED);
        registration.setCreateTime(LocalDateTime.now());
        registration.setUpdateTime(LocalDateTime.now());

        ExhibitionDetailPo exhibitionDetailPo = exhibitionDetailMapper.selectById(registration.getExhibitionId());
        registration.setExhibitionName(exhibitionDetailPo.getName());
        registrationMapper.insert(registration);
    }

    @Override
    public IPage<ExhibitionRegistrationPo> pageWithExhibitionInfo(PageQuery pageQuery) {
        Page<ExhibitionRegistrationPo> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        LambdaQueryWrapper<ExhibitionRegistrationPo> queryWrapper = new LambdaQueryWrapper<ExhibitionRegistrationPo>()
                .orderByDesc(ExhibitionRegistrationPo::getCreateTime);

        return registrationMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void updateProgress(Integer id, ContactProgress progress) {
        registrationMapper.update(null, new LambdaUpdateWrapper<ExhibitionRegistrationPo>()
                .eq(ExhibitionRegistrationPo::getId, id)
                .set(ExhibitionRegistrationPo::getContactProgress, progress)
                .set(ExhibitionRegistrationPo::getUpdateTime, LocalDateTime.now()));
    }

    @Override
    public ExhibitionRegistrationPo getById(Integer id) {
        return registrationMapper.selectById(id);
    }

    @Override
    public void removeById(Integer id) {
        registrationMapper.deleteById(id);
    }
}