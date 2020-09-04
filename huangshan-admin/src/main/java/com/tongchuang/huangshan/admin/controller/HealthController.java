package com.tongchuang.huangshan.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 */
@RestController
@RequestMapping("health")
public class HealthController {

	@RequestMapping(value = "check")
	public String check() {
		return "success";
	}
}