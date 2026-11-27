package cn.exhibition.admin.domain.dto;

import cn.exhibition.admin.domain.enums.ExhibitionType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 展会新增、修改请求；国家名称由后端按国家 id 填写
 */
@Data
public class ExhibitionDto {

    @NotBlank(message = "展会名称不能为空")
    @Size(max = 255, message = "展会名称不能超过 255 个字")
    private String name;

    @NotBlank(message = "举办地址不能为空")
    @Size(max = 255, message = "举办地址不能超过 255 个字")
    private String location;

    @NotBlank(message = "请选择时间")
    @Size(max = 50, message = "时间格式错误")
    private String time;

    @Size(max = 10, message = "年份格式错误")
    private String year;

    @Size(max = 10, message = "月份格式错误")
    private String month;

    @NotNull(message = "排序不能为空")
    @Min(value = 0, message = "排序范围 0-999")
    @Max(value = 999, message = "排序范围 0-999")
    private Integer sort;

    @NotBlank(message = "展会内容不能为空")
    private String content;

    @NotNull(message = "请选择国家")
    private Integer country;

    @NotNull(message = "请选择展会类型")
    private ExhibitionType exhibitionType;
}
