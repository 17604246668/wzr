package com.tongchuang.huangshan.common.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    UNKNOW_ERROR("-1","未知异常"),
    SUCCESS("200", "成功"),
    PARAM_ERROR("301", "参数错误"),
    PARAM_FILE_IS_NOT_NULL("301001","上传文件不能为空"),
    PARAM_FILE_NAME_IS_NOT_NULL("301002","上传文件名不能为空"),
    VEHICLE_INFO_IS_NULL("310001","车辆未录入环保台账，请联系销售部门或物流部门录入台账。如果车辆已到达现场可直接在门岗录入台账。"),
    VEHICLE_INFO_IS_LIMITED("310002","重污染限制,车辆不允许进入"),
    VEHICLE_CAR_NUMBER_IS_NOT_NUL("310003","车牌号不能为空"),
    VEHICLE_INFO_IS_EXIST("310004","车牌信息已存在"),
    OCR_IS_ERROR("310005","ocr识别失败"),
    DEPARTMENT_IS_STOP("401001","部门停用,不允许新增"),
    DICT_TYPE_IS_USE("401002","数据字典已分配,不能删除"),
    USER_ACCOUNT_IS_ERROR("402001","获取用户账户异常"),
    USER_INFO_IS_ERROR("402002","获取用户信息异常"),
    USER_IS_NOT_PERMITS("402003","用户没有权限"),
    USER_ROLE_IS_USE("402004","用户角色已分配,不能删除"),
    POST_IS_USE("403001","岗位已分配,不能删除"),
    IS_ERROR("500","系统异常")
    ;
    private String code;

    private String message;

    ResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
