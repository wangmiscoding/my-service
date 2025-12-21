package com.wangm.order.controller;

import com.wangm.order.entity.Order;
import com.wangm.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 订单Controller
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    /**
     * 审批订单
     */
    @PostMapping("/approve/{orderId}")
    public ResponseEntity<Boolean> approveOrder(@PathVariable Long orderId, 
                                               @RequestParam Boolean approved) {
        boolean result = orderService.approveOrder(orderId, approved);
        return ResponseEntity.ok(result);
    }
}