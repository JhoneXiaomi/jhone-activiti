package com.hebabr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hebabr.base.dao.ActReDeploymentMapper;
import com.hebabr.model.ActReDeployment;

@RunWith(SpringJUnit4ClassRunner.class) // 整合 
@ContextConfiguration(locations="classpath:conf/spring.xml") 
public class jhoneTest {
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private ActReDeploymentMapper actReDeploymentMapper;
	
	
	@Test
	public void deploy() {
		
		String filePath = "D:\\jhoneWorkspace\\htsp.zip";
		File file=new File(filePath);
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
		
		ProcessEngines.getDefaultProcessEngine().getRuntimeService()// 获取正在执行的Service
				.startProcessInstanceById("contract_jiancezhongxin:1:4");// 按照流程定义的Id
	}
	
	@Test
	public void getProDefine() {
		ProcessDefinition pde = repositoryService.getProcessDefinition("contract_jiancezhongxin:1:4");
		actReDeploymentMapper.selectByPrimaryKey("123");
	}
	
	
}
