package cn.exhibition.admin.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 官网展会报名请求；手机号规则与官网 utils/validate.ts 一致
 */
@Data
public class RegistrationDto {

    @NotNull(message = "请选择展会")
    private Integer exhibitionId;

    @NotBlank(message = "请输入公司或个人名称")
    @Size(max = 100, message = "公司或个人名称不能超过 100 个字")
    private String companyOrPersonalName;

    @NotBlank(message = "请输入联系人姓名")
    @Size(max = 50, message = "联系人姓名不能超过 50 个字")
    private String contactName;

    @NotNull(message = "请输入手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入 11 位手机号")
    private String contactPhone;
}
