package cn.exhibition.admin.domain.dto;

import cn.exhibition.admin.domain.enums.ExhibitionType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

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

    @NotNull(message = "请选择开始日期")
    private LocalDate startDate;

    @NotNull(message = "请选择结束日期")
    private LocalDate endDate;

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

    @AssertTrue(message = "结束日期不能早于开始日期")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}
