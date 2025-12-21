package com.wangm.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@TableName("t_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private String productName;
    private BigDecimal amount;
    private Integer quantity;
    private String customerName;
    private String customerPhone;
    private Integer status; // 0:待审批 1:已审批 2:已拒绝
    private String processInstanceId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}