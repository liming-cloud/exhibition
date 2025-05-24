package cn.exhibition.admin.domain.entity;

import cn.exhibition.admin.domain.enums.ContactProgress;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "exhibition_registration")
public class ExhibitionRegistrationPo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "exhibition_id")
    private Integer exhibitionId;

    @TableField(value = "company_or_personal_name")
    private String companyOrPersonalName;

    @TableField(value = "contact_name")
    private String contactName;

    @TableField(value = "contact_phone")
    private String contactPhone;

    @TableField(value = "contact_progress")
    private ContactProgress contactProgress;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    // 关联展会信息
    @TableField(exist = false)
    private String exhibitionName;
}