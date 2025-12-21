package com.wangm.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangm.order.entity.Order;

/**
 * 订单Service接口
 */
public interface OrderService extends IService<Order> {
    /**
     * 创建订单
     * @param order 订单信息
     * @return 创建结果
     */
    Order createOrder(Order order);

    /**
     * 审批订单
     * @param orderId 订单ID
     * @param approved 是否通过审批
     * @return 审批结果
     */
    boolean approveOrder(Long orderId, boolean approved);
}