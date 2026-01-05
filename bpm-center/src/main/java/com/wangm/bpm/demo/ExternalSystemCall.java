package com.wangm.bpm.demo;

import com.alibaba.fastjson.JSON;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.Map;

/**
 * @author wangmeng
 */
public class ExternalSystemCall implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        Map<String, Object> variables = execution.getVariables();
        System.out.println(JSON.toJSONString(variables));
        System.out.println(JSON.toJSONString(execution));
    }
}
