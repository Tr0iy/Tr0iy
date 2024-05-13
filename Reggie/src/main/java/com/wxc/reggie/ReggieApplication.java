package com.wxc.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <a href="http://localhost:8080/backend/page/login/login.html">...</a>
 */
@Slf4j
@SpringBootApplication
// @ServletComponentScan: 使用@WebServlet、@WebFilter、@WebListener标记的 Servlet、Filter、Listener 就可以自动注册到Servlet容器中
@ServletComponentScan
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class);
        log.info("项目启动成功...");
    }
}
