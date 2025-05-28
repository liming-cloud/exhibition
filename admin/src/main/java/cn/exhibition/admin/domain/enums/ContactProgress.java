package cn.exhibition.admin.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 联系进度枚举
 */
public enum ContactProgress {
    UNCONTACTED("UNCONTACTED", "未联系"),
    CONTACTED("CONTACTED", "已联系"),
    REPLIED("REPLIED", "已回复"),
    CLOSED("CLOSED", "已成交"),
    CANCELLED("CANCELLED", "已取消");

    @EnumValue
    private final String code;
    @Getter
    private final String description;

    ContactProgress(String code, String description) {
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
    public static ContactProgress fromCode(String code) {
        for (ContactProgress progress : ContactProgress.values()) {
            if (progress.getCode().equals(code)) {
                return progress;
            }
        }
        throw new IllegalArgumentException("Unknown contact progress code: " + code);
    }

    /**
     * 根据描述获取枚举
     */
    public static ContactProgress fromDescription(String description) {
        for (ContactProgress progress : ContactProgress.values()) {
            if (progress.getDescription().equals(description)) {
                return progress;
            }
        }
        throw new IllegalArgumentException("Unknown contact progress description: " + description);
    }

    @Override
    public String toString() {
        return this.code;
    }
}