package cn.exhibition.admin.service;

import cn.exhibition.admin.domain.dto.ContactInfoDto;
import cn.exhibition.admin.domain.entity.ContactInfoPo;

/**
 * 联系信息服务接口
 */
public interface ContactInfoService {

    /**
     * 获取联系信息，没有数据时返回空对象（不写库）
     */
    ContactInfoPo getContactInfo();

    /**
     * 更新联系信息
     */
    void updateContactInfo(ContactInfoDto dto);
}