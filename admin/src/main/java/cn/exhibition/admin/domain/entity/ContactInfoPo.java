package cn.exhibition.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "contact_info", autoResultMap = true)
public class ContactInfoPo implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "address")
    private String address;

    @TableField(value = "phone", typeHandler = JacksonTypeHandler.class)
    private List<String> phone;

    @TableField(value = "contact_person")
    private String contactPerson;

    @TableField(value = "working_hours")
    private String workingHours;

    @TableField(value = "holiday_arrangement")
    private String holidayArrangement;

    @TableField(value = "email")
    private String email;

    @TableField(value = "company_profile", typeHandler = JacksonTypeHandler.class)
    private String companyProfile;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}