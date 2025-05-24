package cn.exhibition.admin.service;

import cn.exhibition.admin.domain.entity.ContactInfoPo;

/**
 * 联系信息服务接口
 */
public interface ContactInfoService {

    /**
     * 获取联系信息
     */
    ContactInfoPo getContactInfo();

    /**
     * 更新联系信息
     */
    void updateContactInfo(ContactInfoPo contactInfo);
}