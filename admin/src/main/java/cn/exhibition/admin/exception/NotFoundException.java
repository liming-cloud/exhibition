package cn.exhibition.admin.exception;

import java.io.Serial;

/**
 * 要查询、修改或删除的记录不存在，返回 404
 */
public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
