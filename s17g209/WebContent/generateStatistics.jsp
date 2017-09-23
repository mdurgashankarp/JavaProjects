<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Generate Statistics - S17G209</title>
</head>
<body style="background-color: lightgrey">
	<f:view>
		<h2 align="center">IDS517 S17G209</h2>
		<p align="center">
			<u>Data Analysis Application</u>
		</p>
		<hr>
		<p align="center">Durgashankar M P
			&#160;&#160;&#160;&#160;&#160;Priya Harish
			&#160;&#160;&#160;&#160;&#160; Rajatha Priyadarshini</p>
		<hr />
		<div align="center">
			<h:form>
				<h:commandButton type="submit" value="Logout" action="#{dbAccessActionBean.logout}" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Main Menu" action="#{statisticsActionBean.resetMessage}" />
				<br />
				<hr />
			</h:form>
			<h4>Data Statistics</h4>
			<h:form enctype="multipart/form-data"><br/>

				<div align="center">
                    <h:panelGrid columns="6">
				<h:commandButton type="submit" value="DataSet List"
					action="#{statisticsActionBean.listTables}" />
				<h:commandButton type="submit" value="Field List"
					action="#{statisticsActionBean.listColumns}" />
				<h:commandButton id="descriptiveStatistics"
					action="#{statisticsActionBean.generateDescriptiveStatistics}"
					value="Descriptive Statistics" disabled="#{statisticsActionBean.descriptiveButton }"/>	
				<h:commandButton value = "Get Predictor and Response Variables for Regression Analysis" 
                            action = "#{statisticsActionBean.displayColumnsforRegression}" 
                            styleClass = "button" disabled="#{statisticsActionBean.prButton}"/>
				<h:commandButton value = "Generate Regression Report" 
                            action = "#{statisticsActionBean.generateRegressionReport}" 
                            styleClass = "button" 
                            disabled="#{statisticsActionBean.renderRegressionButton}"/>
		
				</h:panelGrid>
				<pre>
						<h:outputText value = "#{statisticsActionBean.message}"
							rendered="#{statisticsActionBean.renderMessage}"
							style="color:red" />
					</pre>
				<h:panelGrid columns="4">

				<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
						value="#{statisticsActionBean.tableNameSelected}"
						rendered="#{statisticsActionBean.tableListRendered}">
						<f:selectItems value="#{statisticsActionBean.tableViewList}" />
					</h:selectOneListbox>

				<h:selectManyListbox size="10" styleClass="selectManyListbox"
					value="#{statisticsActionBean.columnNamesSelected}"
					rendered="#{statisticsActionBean.columnListRendered}">
					<f:selectItems value="#{statisticsActionBean.columnNamesTable}" />
				</h:selectManyListbox>
				
				<h:selectOneListbox id = "predictor" value = "#{statisticsActionBean.predictorValue}"
							rendered = "#{statisticsActionBean.renderRegressionColumn}" size="5">
							<f:selectItem itemValue="0" itemLabel="Select Predictor Value" />
							<f:selectItems value = "#{statisticsActionBean.numericData}" />
						</h:selectOneListbox> 
						<h:selectOneListbox id = "response" value = "#{statisticsActionBean.responseValue}"
                      		rendered = "#{statisticsActionBean.renderRegressionColumn}" size="5">
                      		<f:selectItem itemValue="0" itemLabel="Select Response Value" />
                        	<f:selectItems value = "#{statisticsActionBean.numericData}"/>
                    	</h:selectOneListbox> 
					
					</h:panelGrid> 
			
			<t:dataTable value="#{statisticsActionBean.statisticsBeanList}"
				var="row" rendered="#{statisticsActionBean.renderStats}" border="1"
				cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
				headerClass="headerClass" footerClass="footerClass"
				rowClasses="rowClass2" styleClass="dataTableEx" width="900">
				<t:columns var="col" value="#{statisticsActionBean.columnNames}">
					<f:facet name="header">
						<t:outputText styleClass="outputHeader" value="#{col}" />
					</f:facet>
					<t:outputText styleClass="outputText" value="#{row[col]}" />
				</t:columns>
			</t:dataTable>
					</div><br/>
				<h:outputText value="Regression Equation: "
				    		rendered="#{statisticsActionBean.renderRegressionResult}">
						</h:outputText> &#160;
						<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.regressionEquation}"
							rendered="#{statisticsActionBean.renderRegressionResult}">
						</h:outputText> <br /><br />
						<h:outputText value="Regression Model" rendered="#{statisticsActionBean.renderRegressionResult}"></h:outputText>
						<h:panelGrid columns="5" rendered="#{statisticsActionBean.renderRegressionResult}" border="1" >
							<h:outputText value="Predictor"/>
							<h:outputText value="Co-efficient"/>
							<h:outputText value="Standard Error Co-efficient"/>
							<h:outputText value="T-Statistic"/>
							<h:outputText value="P-Value"/>
							<h:outputText value="Constant"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.intercept}"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.interceptStandardError}"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.tStatistic }"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.interceptPValue }"/>
							<h:outputText value="#{statisticsActionBean.predictorValue}"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.slope}"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.slopeStandardError}"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.tStatisticPredictor }"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.pValuePredictor }"/>
						</h:panelGrid> <br/> <br/>
						<h:panelGrid columns="2" rendered="#{statisticsActionBean.renderRegressionResult}" border="1">
							<h:outputText value="Model Standard Error:"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.standardErrorModel}"/>
							<h:outputText value="R Square(Co-efficient of Determination)"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.rSquare}"/>
							<h:outputText value="R Square Adjusted(Co-efficient of Determination)"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.rSquareAdjusted}"/>
						</h:panelGrid> <br/> <br/>
						<h:outputText value="Analysis of Variance" rendered="#{statisticsActionBean.renderRegressionResult}"/> <br/>
						<h:panelGrid columns="6" rendered="#{statisticsActionBean.renderRegressionResult}" border="1" >
							<h:outputText value="Source"/>
							<h:outputText value="Degrees of Freedom(DF)"/>
							<h:outputText value="Sum of Squares"/>
							<h:outputText value="Mean of Squares"/>
							<h:outputText value="F-Statistic"/>
							<h:outputText value="P-Value"/>
							<h:outputText value="Regression"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.predictorDF}"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.regressionSumSquares}"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.meanSquare }"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.fValue }"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.pValue}"/>
							<h:outputText value="Residual Error"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.residualErrorDF}"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.sumSquaredErrors }"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.meanSquareError }"/>
							<h:outputText value=""/>
							<h:outputText value=""/>
							<h:outputText value="Total"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.totalDF}"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.totalSumSquares}"/>
							<h:outputText value="#{statisticsActionBean.regressionAnalysisManagedBean.totalMeanSquares}"/>
						</h:panelGrid>
				
				
				</h:form>
		</div>
	</f:view>
</body>
</html>