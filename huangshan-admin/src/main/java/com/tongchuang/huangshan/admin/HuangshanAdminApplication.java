package com.tongchuang.huangshan.admin;

import com.tongchuang.huangshan.common.utils.SpringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.tongchuang.huangshan.admin.**.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@Import({SpringUtils.class})
public class HuangshanAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuangshanAdminApplication.class, args);
    }

}
