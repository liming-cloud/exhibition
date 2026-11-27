package cn.exhibition.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 资讯、案例列表项：正文换成纯文本摘要
 */
@Data
public class ArticleListVo {

    private Integer id;

    private String title;

    private String coverUrl;

    /**
     * 正文纯文本摘要
     */
    private String summary;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
