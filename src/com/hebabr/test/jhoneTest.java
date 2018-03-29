package com.hebabr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hebabr.base.service.TaskFlowService;
import com.hebabr.base.util.ProcessUtil;

@RunWith(SpringJUnit4ClassRunner.class) // 整合
@ContextConfiguration(locations = "classpath:conf/spring.xml")
public class jhoneTest {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskFlowService taskFlowService;  
	
	@Test
	public void deploy() {

		String filePath = "D:\\Desktop.zip";
		File file = new File(filePath);
		InputStream in;
		try {
			in = new FileInputStream(file);
			Deployment deployment = repositoryService.createDeployment().name("报告审批")
					.addZipInputStream(new ZipInputStream(in)).deploy();
			System.out.println(deployment.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void startProcessInstance() {

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("enter", "jhone");
		runtimeService.startProcessInstanceById("contract_jiancezhongxin:1:4", variables);// 按照流程定义的Id
	}

	@Test
	public void getActivity() {

		ActivityImpl activityImpl = ProcessUtil.getActivity(repositoryService, "contract_jiancezhongxin:1:4",
				"usertask1");
		System.out.println(activityImpl);
	}
	
	@Test 
	public void testTaskFLow() {
		//taskFlowService.insertTaskAfter(targetTaskKey, taskId, doUserId, names, assignee);
	}

}
