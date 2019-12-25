package org.caison.springboot.sentinel.demo;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChenCaihua
 * @date 2019年10月10日
 */
@RestController
public class TestController {

    @GetMapping(value = "/hello")
    // 定义需要限流的资源名称为hello
    @SentinelResource("hello")
    public String hello() {
        System.out.println("hello....");
        return "Hello Sentinel";
    }
}
