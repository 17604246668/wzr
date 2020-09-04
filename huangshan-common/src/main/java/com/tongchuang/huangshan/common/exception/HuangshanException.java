package com.tongchuang.huangshan.common.exception;

import com.tongchuang.huangshan.common.enums.ResultEnum;
import lombok.Data;

@Data
public class HuangshanException extends RuntimeException {

    private String code;

    public HuangshanException(String code, String message) {
        super(message);
        this.code = code;
    }

    public HuangshanException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }
}
