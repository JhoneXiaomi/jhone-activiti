package com.hebabr.base.util;

public class ReportNumPojo {
	
	private String reportNum1;
	private String reportNum2;
	private String reportNum3;
	private String reportNum4;
	private String reportNum5;
	private String reportNum6;
	private String reportNum7;
	
	public ReportNumPojo(){
		this.reportNum1=PropertiesUtils.getInstance().getConfigItem("reportNum1");
		this.reportNum2=PropertiesUtils.getInstance().getConfigItem("reportNum2");
		this.reportNum3=PropertiesUtils.getInstance().getConfigItem("reportNum3");
		this.reportNum4=PropertiesUtils.getInstance().getConfigItem("reportNum4");
		this.reportNum5=PropertiesUtils.getInstance().getConfigItem("reportNum5");
		this.reportNum6=PropertiesUtils.getInstance().getConfigItem("reportNum6");
		this.reportNum7=PropertiesUtils.getInstance().getConfigItem("reportNum7");
	}

	public String getReportNum1() {
		return reportNum1;
	}

	public void setReportNum1(String reportNum1) {
		this.reportNum1 = reportNum1;
	}

	public String getReportNum2() {
		return reportNum2;
	}

	public void setReportNum2(String reportNum2) {
		this.reportNum2 = reportNum2;
	}

	public String getReportNum3() {
		return reportNum3;
	}

	public void setReportNum3(String reportNum3) {
		this.reportNum3 = reportNum3;
	}

	public String getReportNum4() {
		return reportNum4;
	}

	public void setReportNum4(String reportNum4) {
		this.reportNum4 = reportNum4;
	}

	public String getReportNum5() {
		return reportNum5;
	}

	public void setReportNum5(String reportNum5) {
		this.reportNum5 = reportNum5;
	}

	public String getReportNum6() {
		return reportNum6;
	}

	public void setReportNum6(String reportNum6) {
		this.reportNum6 = reportNum6;
	}

	public String getReportNum7() {
		return reportNum7;
	}

	public void setReportNum7(String reportNum7) {
		this.reportNum7 = reportNum7;
	}
	
	
}
