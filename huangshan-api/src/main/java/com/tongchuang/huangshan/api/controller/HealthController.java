package com.tongchuang.huangshan.api.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 */

@Log4j2
@RestController
@RequestMapping("health")
public class HealthController {


    @GetMapping(value = "check")
    public String check() {

        return "success";
    }


}