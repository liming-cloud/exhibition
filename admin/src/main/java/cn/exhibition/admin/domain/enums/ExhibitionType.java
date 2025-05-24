package cn.exhibition.admin.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 展会类型枚举
 */
public enum ExhibitionType {
    ENERGY("ENERGY", "能源展"),
    ELEVATOR("ELEVATOR", "电梯展"),
    MEDICAL("MEDICAL", "医疗展");

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

    /**
     * 根据代码获取枚举
     */
    public static ExhibitionType fromCode(String code) {
        for (ExhibitionType type : ExhibitionType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown exhibition type code: " + code);
    }

    /**
     * 根据描述获取枚举
     */
    public static ExhibitionType fromDescription(String description) {
        for (ExhibitionType type : ExhibitionType.values()) {
            if (type.getDescription().equals(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown exhibition type description: " + description);
    }

    @Override
    public String toString() {
        return this.code;
    }
}