package tech.revocat.daidai.entity;

import lombok.Data;


@Data

public class UserOrder {
    private Long id;
    private User user;
    private Order order;

}
