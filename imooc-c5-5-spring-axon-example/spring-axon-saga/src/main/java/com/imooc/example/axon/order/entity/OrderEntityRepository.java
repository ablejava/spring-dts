package com.imooc.example.axon.order.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mavlarn on 2018/5/28.
 */
public interface OrderEntityRepository extends JpaRepository<OrderEntity, String> {
}
