CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单编号',
  `product_name` varchar(255) NOT NULL COMMENT '产品名称',
  `amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `quantity` int(11) NOT NULL COMMENT '产品数量',
  `customer_name` varchar(128) NOT NULL COMMENT '客户名称',
  `customer_phone` varchar(32) NOT NULL COMMENT '客户电话',
  `status` int(11) NOT NULL COMMENT '订单状态：0-待审批，1-已审批，2-已拒绝',
  `process_instance_id` varchar(64) DEFAULT NULL COMMENT '流程实例ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';