package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.database.PageQuery;
import cn.exhibition.admin.domain.dto.RegistrationDto;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import cn.exhibition.admin.domain.entity.ExhibitionRegistrationPo;
import cn.exhibition.admin.domain.enums.ContactProgress;
import cn.exhibition.admin.exception.NotFoundException;
import cn.exhibition.admin.exception.ServiceException;
import cn.exhibition.admin.mapper.ExhibitionDetailMapper;
import cn.exhibition.admin.mapper.ExhibitionRegistrationMapper;
import cn.exhibition.admin.service.ExhibitionRegistrationService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 展会报名服务实现类
 */
@Service
@RequiredArgsConstructor
public class ExhibitionRegistrationServiceImpl implements ExhibitionRegistrationService {

    private static final String NOT_FOUND = "报名记录不存在";

    private final ExhibitionRegistrationMapper registrationMapper;
    private final ExhibitionDetailMapper exhibitionDetailMapper;

    @Override
    public void save(RegistrationDto dto) {
        ExhibitionDetailPo exhibitionDetailPo = exhibitionDetailMapper.selectById(dto.getExhibitionId());
        if (exhibitionDetailPo == null) {
            throw new ServiceException("报名的展会不存在");
        }
        ExhibitionRegistrationPo registration = BeanUtil.copyProperties(dto, ExhibitionRegistrationPo.class);
        registration.setExhibitionName(exhibitionDetailPo.getName());
        registration.setContactProgress(ContactProgress.UNCONTACTED);
        registration.setCreateTime(LocalDateTime.now());
        registration.setUpdateTime(LocalDateTime.now());
        registrationMapper.insert(registration);
    }

    @Override
    public IPage<ExhibitionRegistrationPo> pageWithExhibitionInfo(PageQuery pageQuery) {
        LambdaQueryWrapper<ExhibitionRegistrationPo> queryWrapper = new LambdaQueryWrapper<ExhibitionRegistrationPo>()
                .orderByDesc(ExhibitionRegistrationPo::getCreateTime);
        return registrationMapper.selectPage(pageQuery.build(), queryWrapper);
    }

    @Override
    public void updateProgress(Integer id, ContactProgress progress) {
        ExhibitionRegistrationPo registration = new ExhibitionRegistrationPo();
        registration.setId(id);
        registration.setContactProgress(progress);
        registration.setUpdateTime(LocalDateTime.now());
        if (registrationMapper.updateById(registration) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    @Override
    public ExhibitionRegistrationPo getById(Integer id) {
        ExhibitionRegistrationPo registration = registrationMapper.selectById(id);
        if (registration == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        return registration;
    }

    @Override
    public void removeById(Integer id) {
        if (registrationMapper.deleteById(id) == 0) {
            throw new NotFoundException(NOT_FOUND);
        }
    }
}
