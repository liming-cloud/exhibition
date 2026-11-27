package cn.exhibition.admin.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 案例新增、修改请求
 */
@Data
public class CasesDto {

    @NotBlank(message = "标题不能为空")
    @Size(max = 255, message = "标题不能超过 255 个字")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    @NotBlank(message = "请上传封面图")
    @Size(max = 255, message = "封面图地址过长")
    private String coverUrl;
}
