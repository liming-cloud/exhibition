package cn.exhibition.admin.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 联系信息修改请求
 */
@Data
public class ContactInfoDto {

    @NotBlank(message = "请输入公司地址")
    @Size(max = 500, message = "地址不能超过 500 个字")
    private String address;

    @NotEmpty(message = "请输入联系电话")
    private List<@NotBlank(message = "联系电话不能为空") @Size(max = 50, message = "联系电话不能超过 50 个字") String> phone;

    @NotBlank(message = "请输入联系人姓名")
    @Size(max = 100, message = "联系人姓名不能超过 100 个字")
    private String contactPerson;

    @NotBlank(message = "请输入工作时间")
    @Size(max = 100, message = "工作时间不能超过 100 个字")
    private String workingHours;

    @NotBlank(message = "请输入节假日安排")
    @Size(max = 100, message = "节假日安排不能超过 100 个字")
    private String holidayArrangement;

    @NotBlank(message = "请输入邮箱地址")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱不能超过 100 个字")
    private String email;

    @NotBlank(message = "请输入公司简介")
    @Size(max = 2000, message = "公司简介不能超过 2000 个字")
    private String companyProfile;
}
