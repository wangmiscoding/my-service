package com.wangm.bpm.demo;

import com.alibaba.fastjson.JSON;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangmeng
 */
public class FlowableDemo {

    public static void main(String[] args) {
        // 启动流程引擎
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&useAffectedRows=true")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = cfg.buildProcessEngine();

        Task task = queryTask(processEngine);
        completeTask(processEngine, task);
    }

    /**
     * 查询任务
     */
    public static Task queryTask(ProcessEngine processEngine) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ") " + tasks.get(i).getName());
        }
        taskDetail(processEngine, tasks.get(0));
        return tasks.get(0);
    }

    public static void taskDetail(ProcessEngine processEngine, Task task) {
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> processVariables = taskService.getVariables(task.getId());
        System.out.println(JSON.toJSONString(processVariables));
    }

    public static void completeTask(ProcessEngine processEngine, Task task) {
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> processVariables = taskService.getVariables(task.getId());
        processVariables.put("approved", true);
        taskService.complete(task.getId(), processVariables);
    }


    public static void createTask(ProcessEngine processEngine) {
        // 部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/holiday-request.bpmn20.xml")
                .deploy();
        // 查询流程定义以验证是否成功部署
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        System.out.println("Found process definition : " + processDefinition.getName());

        String employee = "tom";
        Integer nrOfHolidays = 3;
        String description = "请假三天";
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", variables);
    }


}
