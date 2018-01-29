package com.hebabr.base.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.approval.service.IFlowReportApprovalService;
import com.hebabr.base.dao.FlowContractAndProxyApprovalDtoMapper;
import com.hebabr.base.dao.FlowContractOfferTimeDtoMapper;
import com.hebabr.base.dao.FlowContractPaymentDtoMapper;
import com.hebabr.base.dao.FlowReportApprovalDtoMapper;
import com.hebabr.base.dao.FlowTenderDocAndAuthApprovalDtoMapper;
import com.hebabr.base.dao.TbDictionaryDtoMapper;
import com.hebabr.base.service.CoownService;
import com.hebabr.base.service.ITbDictionaryService;
import com.hebabr.base.util.DateUtils;
import com.hebabr.base.util.ReportNumPojo;
import com.hebabr.model.FlowContractAndProxyApprovalDto;
import com.hebabr.model.FlowQualificationBorrowDto;
import com.hebabr.model.FlowReportApprovalDto;
import com.hebabr.model.FlowTechPlanApprovalDto;
import com.hebabr.model.FlowTenderDocAndAuthApprovalDto;
import com.hebabr.model.TbDictionaryDto;
@Service
public class CoownServiceImpl implements CoownService{
	@Autowired 
	FlowTenderDocAndAuthApprovalDtoMapper flowTenderDocAndAuthApprovalDtoMapper;
	@Autowired 
	TbDictionaryDtoMapper tbDictionaryDtoMapper;
	@Autowired
	private FlowContractAndProxyApprovalDtoMapper flowContractAndProxyApprovalDtoMapper;
	@Autowired
	private FlowContractPaymentDtoMapper flowContractPaymentDtoMapper;
	@Autowired
	private FlowContractOfferTimeDtoMapper flowContractOfferTimeDtoMapper;
	@Autowired
	private FlowReportApprovalDtoMapper flowReportApprovalDtoMapper;
	@Autowired
	private ITbDictionaryService iTbDictionaryService;
	@Autowired
	private IFlowReportApprovalService iFlowReportApprovalService;
	
	@Override
	public TbDictionaryDto findDictionaryByPrimaryKey(String id) {
		return tbDictionaryDtoMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public synchronized FlowTenderDocAndAuthApprovalDto createBidNum(
					FlowTenderDocAndAuthApprovalDto flowTenderDocAndAuthApprovalDto) {
		//查询当前时间
		Calendar cal = Calendar.getInstance();
		TbDictionaryDto tbDictionaryDto = tbDictionaryDtoMapper.selectByPrimaryKey("12345678901234567890123456789036");
		String comment = tbDictionaryDto.getDictionaryComment();
		int serialNumber = Integer.valueOf(comment);
		int year = serialNumber/10000;
		int num = serialNumber%10000;
		int nowYear = cal.get(Calendar.YEAR);
		if(year!=nowYear){
			year = nowYear;
			num = 0;
		}
		//将流水号转换为4位形式
		String numStr = String.format("%04d",++num);
		tbDictionaryDto.setDictionaryComment(year+numStr);
		tbDictionaryDtoMapper.updateByPrimaryKey(tbDictionaryDto);
		//查询承接单位
		TbDictionaryDto undertakingUnit = tbDictionaryDtoMapper.selectByPrimaryKey(flowTenderDocAndAuthApprovalDto.getUndertakingUnitId()); 
		//组合投标编号
		flowTenderDocAndAuthApprovalDto.setBidNum(undertakingUnit.getDictionaryComment()+year+"-"+numStr);
		
		return flowTenderDocAndAuthApprovalDto;
	}

	@Override
	public FlowContractAndProxyApprovalDto createContractNum(
			FlowContractAndProxyApprovalDto flowContractAndProxyApprovalDto) {
		String serialNumber = "";
		do{
			flowContractAndProxyApprovalDto = createConractNumInit(flowContractAndProxyApprovalDto);
			char[] charArr= flowContractAndProxyApprovalDto.getContractNum().toCharArray();
			for(int i=0;i<charArr.length-1;i++){
				if(charArr[i] > 122){
					serialNumber = flowContractAndProxyApprovalDto.getContractNum().substring(i);
					break;
				}
			}
		}while(isHaveSameNum(serialNumber));
		return flowContractAndProxyApprovalDto;
	}
	
	
	
	public boolean isHaveSameNum(String contractNum) {
		List<FlowContractAndProxyApprovalDto> contractAndProxyApproval = flowContractAndProxyApprovalDtoMapper.ListContractAndProxyLikeContractNum(contractNum);
		if(contractAndProxyApproval != null && contractAndProxyApproval.size()>0){
			return true;
		}
		return false;
	}

	public FlowContractAndProxyApprovalDto createConractNumInit(
			FlowContractAndProxyApprovalDto flowContractAndProxyApprovalDto) {
		Calendar calendar = Calendar.getInstance();
		
		//查询承接单位信息
		TbDictionaryDto undertakingUnit = findDictionaryByPrimaryKey(flowContractAndProxyApprovalDto.getUndertakingUnitId());
		//查询合同类型信息
		TbDictionaryDto contractType = findDictionaryByPrimaryKey(flowContractAndProxyApprovalDto.getContractTypeId());
		//查询工程类型信息
		TbDictionaryDto projectType = findDictionaryByPrimaryKey(flowContractAndProxyApprovalDto.getProjectTypeId());
		
		//查询合同表单中的各个工程类型的流水号
		TbDictionaryDto tbDictionaryDto = tbDictionaryDtoMapper.selectByPrimaryKey(projectType.getDictionaryComment());
		String comment = tbDictionaryDto.getDictionaryComment();
		int serialNumber = Integer.valueOf(comment);
		int year = serialNumber/10000;
		int num = serialNumber%10000;
		int nowYear = calendar.get(Calendar.YEAR);
		if(year!=nowYear){
			year = nowYear;
			num = 0;
		}
		//将流水号转换为4位形式
		String numStr = String.format("%04d",++num);
		tbDictionaryDto.setDictionaryComment(year+numStr);
		//更新合同编号
		tbDictionaryDtoMapper.updateByPrimaryKey(tbDictionaryDto);
		
		//组合合同编号
		flowContractAndProxyApprovalDto.setContractNum(undertakingUnit.getDictionaryComment()+"-"+contractType.getDictionaryComment()+projectType.getDictionaryName()+year+"-"+numStr);
		return flowContractAndProxyApprovalDto;
	}

	@Override
	public FlowReportApprovalDto createReportNum(FlowReportApprovalDto flowReportApprovalDto) {
		ReportNumPojo reportNum=new ReportNumPojo();
		//tb_dictionary_dto中id为12345678901234567890123456789100的数据存储的是报告编号后缀最大值，
		//先查询出此编号，判断其中年份是否跟当前一致，若不一致，编号归零，若一致，在编号的基础上递增作为本次编号的一部分
		//将最新的编号后缀更新到字典表，供下一次编号生成时使用
				
		TbDictionaryDto tbDictionaryDto = tbDictionaryDtoMapper.selectByPrimaryKey("12345678901234567890123456789100");
				String comment=tbDictionaryDto.getDictionaryComment();
				String[] msg=comment.split("-");
				String year=msg[0];
				int num=Integer.valueOf(msg[1]);
				String nowYear = DateUtils.dateToString("yyyy");

				if(!year.equals(nowYear)){
					year=nowYear;
					num=0;
				}
				String numStr;
				
				//检查该编号是否已被占用
				do{
					numStr = String.format("%04d",++num);
				}while(iFlowReportApprovalService.isReportNumUsed(year+"-"+numStr));
				
				tbDictionaryDto.setDictionaryComment(year+"-"+numStr);
				tbDictionaryDtoMapper.updateByPrimaryKey(tbDictionaryDto);
				String underName=tbDictionaryDtoMapper.selectByPrimaryKey(flowReportApprovalDto.getUndertakingUnitId()).getDictionaryName();
				if(underName.equals("河北省建筑工程质量检测中心")){
					if(flowReportApprovalDto.getReportType2().equals("12345678901234567890123456789065")){//检测
						if(flowReportApprovalDto.getReportType3().equals("12345678901234567890123456789071")){//结构
							flowReportApprovalDto.setReportNum(reportNum.getReportNum1()+year+"-"+numStr);
						}else if(flowReportApprovalDto.getReportType3().equals("12345678901234567890123456789072")){//地基
							flowReportApprovalDto.setReportNum(reportNum.getReportNum2()+year+"-"+numStr);
						}else if(flowReportApprovalDto.getReportType3().equals("12345678901234567890123456789073")){//节能
							flowReportApprovalDto.setReportNum(reportNum.getReportNum3()+year+"-"+numStr);
						}else if(flowReportApprovalDto.getReportType3().equals("12345678901234567890123456789074")){//机械
							flowReportApprovalDto.setReportNum(reportNum.getReportNum4()+year+"-"+numStr);
						}else if(flowReportApprovalDto.getReportType3().equals("12345678901234567890123456789075")){//材料
							flowReportApprovalDto.setReportNum(reportNum.getReportNum5()+year+"-"+numStr);
						}else if(flowReportApprovalDto.getReportType3().equals("12345678901234567890123456789099")){//环境
							flowReportApprovalDto.setReportNum(reportNum.getReportNum6()+year+"-"+numStr);
						}
					}else if(flowReportApprovalDto.getReportType2().equals("12345678901234567890123456789067")){//评估
						flowReportApprovalDto.setReportNum(reportNum.getReportNum7()+year+"-"+numStr);
					}
				}else{//承接单位非“河北省建筑工程质量检测中心”
					String code=tbDictionaryDtoMapper.selectByPrimaryKey(flowReportApprovalDto.getUndertakingUnitId()).getDictionaryComment();
					if(flowReportApprovalDto.getReportType2().equals("12345678901234567890123456789065")){//检测
						flowReportApprovalDto.setReportNum(code+"（JC）"+year+"-"+numStr);
					}else if(flowReportApprovalDto.getReportType2().equals("12345678901234567890123456789066")){//施工
						flowReportApprovalDto.setReportNum(code+"（SG）"+year+"-"+numStr);
					}else if(flowReportApprovalDto.getReportType2().equals("12345678901234567890123456789067")){//评估
						flowReportApprovalDto.setReportNum(code+"（PG）"+year+"-"+numStr);
					}else if(flowReportApprovalDto.getReportType2().equals("12345678901234567890123456789068")){//勘察
						flowReportApprovalDto.setReportNum(code+"（KC）"+year+"-"+numStr);
					}else if(flowReportApprovalDto.getReportType2().equals("12345678901234567890123456789069")){//设计
						flowReportApprovalDto.setReportNum(code+"（SJ）"+year+"-"+numStr);
					}else if(flowReportApprovalDto.getReportType2().equals("12345678901234567890123456789070")){//分包
						flowReportApprovalDto.setReportNum(code+"（FB）"+year+"-"+numStr);
					}
				}
				flowReportApprovalDtoMapper.updateByPrimaryKey(flowReportApprovalDto);
				return flowReportApprovalDto;
	}

	@Override
	public FlowTechPlanApprovalDto createTeachPlanNum(FlowTechPlanApprovalDto flowTechPlanApprovalDto) {
		String year=DateUtils.dateToString("yyyy");
		String maxNum=iTbDictionaryService.getMaxTeachPlanNum(year);
		String text=iTbDictionaryService.selectDictionaryByPrimaryKey(flowTechPlanApprovalDto.getPlanTypeId()).getDictionaryName();
		flowTechPlanApprovalDto.setPlanNum(text+maxNum);
		return flowTechPlanApprovalDto;
	}

	@Override
	public synchronized FlowQualificationBorrowDto createStub(FlowQualificationBorrowDto flowQualificationBorrowDto) {
		
		//查询当前时间
		Calendar cal = Calendar.getInstance();
		TbDictionaryDto tbDictionaryDto = tbDictionaryDtoMapper.selectByPrimaryKey("12345678901234567890123456789134");
		String comment = tbDictionaryDto.getDictionaryComment();
		int serialNumber = Integer.valueOf(comment);
		int year = serialNumber/10000;
		int num = serialNumber%10000;
		int nowYear = cal.get(Calendar.YEAR);
		if(year!=nowYear){
			year = nowYear;
			num = 0;
		}
		//将流水号转换为4位形式
		String numStr = String.format("%04d",++num);
		tbDictionaryDto.setDictionaryComment(year+numStr);
		tbDictionaryDtoMapper.updateByPrimaryKey(tbDictionaryDto);
		//组合存根
		if(flowQualificationBorrowDto.getBorrowType() == 1){
			flowQualificationBorrowDto.setStub("Y"+year+numStr);
		}else if(flowQualificationBorrowDto.getBorrowType() == 2){
			flowQualificationBorrowDto.setStub("F"+year+numStr);
		}
		return flowQualificationBorrowDto;
		
	}

	

}
