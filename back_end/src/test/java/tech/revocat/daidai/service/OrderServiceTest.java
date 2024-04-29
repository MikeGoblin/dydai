package tech.revocat.daidai.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tech.revocat.daidai.DaidaiApplication;

import javax.annotation.Resource;

@SpringBootTest(classes = DaidaiApplication.class)
class OrderServiceTest {
    @Resource
    OrderService service;

    @Test
    void getUserOrder() {
        System.out.println(service.getUserOrder("1145141919810", 1));
    }


}