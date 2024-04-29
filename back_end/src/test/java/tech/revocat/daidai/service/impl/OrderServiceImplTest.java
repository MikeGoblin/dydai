package tech.revocat.daidai.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

@MapperScan("tech.revocat.daidai.mapper")
@ComponentScan("tech.revocat.daidai.entity")
@Slf4j
@Transactional
class OrderServiceImplTest {

    @Test
    void getOrderList() {
//        OrderServiceImpl service = new OrderServiceImpl();
//        service.getOrderList(10);
    }
}