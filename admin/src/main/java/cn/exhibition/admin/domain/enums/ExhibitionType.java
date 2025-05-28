package cn.exhibition.admin.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 展会类型枚举
 */
public enum ExhibitionType {
    ENERGY("ENERGY", "能源展"),
    ELEVATOR("ELEVATOR", "电梯展"),
    MEDICAL("MEDICAL", "医疗展");

    @EnumValue
    private final String code;
    @Getter
    private final String description;

    ExhibitionType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}