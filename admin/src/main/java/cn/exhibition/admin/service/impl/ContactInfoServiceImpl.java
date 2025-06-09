package cn.exhibition.admin.service.impl;

import cn.exhibition.admin.domain.entity.ContactInfoPo;
import cn.exhibition.admin.mapper.ContactInfoMapper;
import cn.exhibition.admin.service.ContactInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 联系信息服务实现类
 */
@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoMapper contactInfoMapper;

    @Override
    public ContactInfoPo getContactInfo() {
        // 获取第一条记录，如果没有则返回默认值
        ContactInfoPo contactInfo = contactInfoMapper.selectOne(new LambdaQueryWrapper<ContactInfoPo>()
                .orderByDesc(ContactInfoPo::getId)
                .last("LIMIT 1"));

        if (contactInfo == null) {
            // 创建默认联系信息
            contactInfo = new ContactInfoPo();
            contactInfo.setAddress("北京市海淀区三里河路9号住建部大院");
            contactInfo.setPhone(List.of("010-53630591/2/3"));
            contactInfo.setContactPerson("林琳");
            contactInfo.setCreateTime(LocalDateTime.now());
            contactInfo.setUpdateTime(LocalDateTime.now());
            contactInfoMapper.insert(contactInfo);
        }

        return contactInfo;
    }

    @Override
    public void updateContactInfo(ContactInfoPo contactInfo) {
        ContactInfoPo existing = getContactInfo();
        if (existing != null) {
            contactInfo.setId(existing.getId());
            contactInfo.setUpdateTime(LocalDateTime.now());
            contactInfoMapper.updateById(contactInfo);
        } else {
            contactInfo.setCreateTime(LocalDateTime.now());
            contactInfo.setUpdateTime(LocalDateTime.now());
            contactInfoMapper.insert(contactInfo);
        }
    }
}