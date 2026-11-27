package cn.exhibition.admin.exception;

import cn.exhibition.admin.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 全局异常处理：所有错误都返回 {@link R}，code 与 HTTP 状态码一致
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常，未指定错误码时按请求有误处理（400）
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<R<Void>> handleService(ServiceException e) {
        int code = e.getCode() != null ? e.getCode() : HttpStatus.BAD_REQUEST.value();
        return fail(code, e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<R<Void>> handleNotFound(NotFoundException e) {
        return fail(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    /**
     * 请求体校验失败，返回第一条字段错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<R<Void>> handleInvalid(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        String msg = error != null ? error.getDefaultMessage() : e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return fail(HttpStatus.BAD_REQUEST.value(), msg);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<R<Void>> handleBadFormat(Exception e) {
        return fail(HttpStatus.BAD_REQUEST.value(), "参数格式错误");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<R<Void>> handleMissingParam(MissingServletRequestParameterException e) {
        return fail(HttpStatus.BAD_REQUEST.value(), "缺少参数 " + e.getParameterName());
    }

    /**
     * 其他异常：Spring 自带的请求类异常（如请求方法不支持、路径不存在）沿用它们的状态码，其余按 500 处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<Void>> handleOther(Exception e) {
        if (e instanceof ErrorResponse errorResponse && !errorResponse.getStatusCode().is5xxServerError()) {
            return fail(errorResponse.getStatusCode().value(), errorResponse.getBody().getDetail());
        }
        log.error("请求处理失败", e);
        return fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统出错，请稍后重试");
    }

    private ResponseEntity<R<Void>> fail(int code, String msg) {
        return ResponseEntity.status(code).body(R.fail(code, msg));
    }
}
