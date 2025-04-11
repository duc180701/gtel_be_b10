package com.gtel.srpingtutorial.exception;

import com.gtel.srpingtutorial.utils.ERROR_CODE;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApplicationException extends RuntimeException {
    private String code;
    private Map<String, Object> data;
    private String title;

    public ApplicationException(ERROR_CODE errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getErrorCode();
        this.title = errorCode.getMessage();
    }

    public ApplicationException(ERROR_CODE errorCode, String message) {
        super(message);
        this.code = errorCode.getErrorCode();
        this.title = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}