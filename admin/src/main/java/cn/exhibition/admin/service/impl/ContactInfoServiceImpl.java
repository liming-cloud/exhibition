package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.domain.dto.ContactInfoDto;
import cn.exhibition.admin.domain.entity.ContactInfoPo;
import cn.exhibition.admin.mapper.ContactInfoMapper;
import cn.exhibition.admin.service.ContactInfoService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 联系信息服务实现类
 */
@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoMapper contactInfoMapper;

    @Override
    public ContactInfoPo getContactInfo() {
        // 只取最新一条；初始数据由 sql-scripts.sql 写入，这里不写库
        ContactInfoPo contactInfo = findLatest();
        return contactInfo != null ? contactInfo : new ContactInfoPo();
    }

    @Override
    public void updateContactInfo(ContactInfoDto dto) {
        ContactInfoPo contactInfo = BeanUtil.copyProperties(dto, ContactInfoPo.class);
        contactInfo.setUpdateTime(LocalDateTime.now());
        ContactInfoPo existing = findLatest();
        if (existing != null) {
            contactInfo.setId(existing.getId());
            contactInfoMapper.updateById(contactInfo);
        } else {
            contactInfo.setCreateTime(LocalDateTime.now());
            contactInfoMapper.insert(contactInfo);
        }
    }

    private ContactInfoPo findLatest() {
        return contactInfoMapper.selectOne(new LambdaQueryWrapper<ContactInfoPo>()
                .orderByDesc(ContactInfoPo::getId)
                .last("LIMIT 1"));
    }
}
