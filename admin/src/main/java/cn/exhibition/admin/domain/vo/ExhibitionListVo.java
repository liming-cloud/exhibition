package cn.exhibition.admin.domain.vo;

import cn.exhibition.admin.domain.enums.ExhibitionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 展会列表项：不含正文，正文走详情接口
 */
@Data
public class ExhibitionListVo {

    private Integer id;

    private String name;

    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer sort;

    private Integer country;

    private String countryName;

    private ExhibitionType exhibitionType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
