package cn.exhibition.admin.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 轮播图新增、修改请求
 */
@Data
public class CarouselDto {

    @NotBlank(message = "标题不能为空")
    @Size(max = 255, message = "标题不能超过 255 个字")
    private String title;

    @NotBlank(message = "请上传图片")
    @Size(max = 255, message = "图片地址过长")
    private String imageUrl;

    @NotBlank(message = "链接不能为空")
    @Size(max = 255, message = "链接不能超过 255 个字")
    private String link;

    @NotNull(message = "排序不能为空")
    @Min(value = 0, message = "排序范围 0-999")
    @Max(value = 999, message = "排序范围 0-999")
    private Integer sort;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态只能是 0 或 1")
    @Max(value = 1, message = "状态只能是 0 或 1")
    private Integer status;
}
