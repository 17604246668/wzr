package com.tongchuang.huangshan.model.dtos.admin.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ExcelDataVO {

    private String vehiclePlate;

    private String drivePermitUrl;

    private Date registerTime;

    private String vin;

    private String engineCode;

    private Integer emissionStageId;

    private String invoiceUrl;

}
