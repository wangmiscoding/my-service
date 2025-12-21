package com.wangm.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangm.order.entity.Order;
import com.wangm.order.enums.OrderStatusEnum;
import com.wangm.order.mapper.OrderMapper;
import com.wangm.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 订单Service实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        // 生成订单号
        order.setOrderNo(generateOrderNo());
        // 设置默认状态为待审批
        order.setStatus(OrderStatusEnum.PENDING_APPROVAL.getCode());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        // 保存订单到数据库
        this.save(order);
        
        // 调用bpm-center启动流程
        try {
            String bpmCenterUrl = "http://localhost:8889/flowable/process-instances";
            
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("processDefinitionKey", "orderApprovalProcess");
            requestBody.put("businessKey", String.valueOf(order.getId()));
            
            // 设置流程变量
            Map<String, Object> variables = new HashMap<>();
            variables.put("orderId", order.getId());
            variables.put("orderNo", order.getOrderNo());
            variables.put("amount", order.getAmount());
            variables.put("customerName", order.getCustomerName());
            requestBody.put("variables", variables);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // 调用bpm-center API启动流程
            Map<String, Object> response = restTemplate.postForObject(bpmCenterUrl, entity, Map.class);
            
            // 获取流程实例ID并保存到订单
            if (response != null && response.containsKey("id")) {
                String processInstanceId = (String) response.get("id");
                order.setProcessInstanceId(processInstanceId);
                this.updateById(order);
            }
            
        } catch (Exception e) {
            // 如果调用失败，记录日志但不回滚订单创建
            log.error("调用bpm-center启动流程失败: {}", e.getMessage(), e);
        }
        
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveOrder(Long orderId, boolean approved) {
        // 查询订单
        Order order = this.getById(orderId);
        if (order == null) {
            return false;
        }
        
        // 更新订单状态
        order.setStatus(approved ? OrderStatusEnum.APPROVED.getCode() : OrderStatusEnum.REJECTED.getCode());
        order.setUpdateTime(LocalDateTime.now());
        this.updateById(order);
        
        // 调用bpm-center完成审批任务
        try {
            // 这里需要先获取任务ID，为了简化先假设任务ID已知或通过API查询
            String bpmCenterUrl = "http://localhost:8889/flowable/tasks/{taskId}/complete";
            
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> variables = new HashMap<>();
            variables.put("approved", approved);
            requestBody.put("variables", variables);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // 调用bpm-center API完成审批任务
            // 注意：这里需要先通过业务键查询到对应的任务ID
            // 为了简化演示，这里假设已经获取到了任务ID
            String taskId = getTaskIdByOrderId(orderId);
            restTemplate.postForObject(bpmCenterUrl, entity, Void.class, taskId);
            
        } catch (Exception e) {
            // 如果调用失败，记录日志但不回滚订单状态更新
            log.error("调用bpm-center完成审批任务失败: {}", e.getMessage(), e);
        }
        
        return true;
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }
    
    /**
     * 根据订单ID获取任务ID
     */
    private String getTaskIdByOrderId(Long orderId) {
        // 这里应该调用bpm-center的API查询任务ID
        // 为了简化演示，这里返回一个模拟的任务ID
        return "task_" + orderId;
    }
}