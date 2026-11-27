package cn.exhibition.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 补贴新增、修改请求
 */
@Data
public class SubsidiesDto {

    @NotBlank(message = "标题不能为空")
    @Size(max = 255, message = "标题不能超过 255 个字")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    @NotNull(message = "补贴金额不能为空")
    @DecimalMin(value = "0", message = "补贴金额不能为负数")
    private BigDecimal price;

    @NotNull(message = "请选择开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "请选择结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @JsonIgnore
    @AssertTrue(message = "结束日期不能早于开始日期")
    public boolean isTimeRangeValid() {
        return startTime == null || endTime == null || !endTime.isBefore(startTime);
    }
}
