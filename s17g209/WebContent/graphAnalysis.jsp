<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home - S17G209</title>
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
		<h:form>
		<div align="center">
			
				<h:commandButton type="submit" value="Logout"
					action="#{dbAccessActionBean.logout}" />&nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Main Menu"
					action="#{actionBeanAnalysis.resetMessage }" />
				<br />
				<hr />
		</div>
		<center>
			<p>
				<b> Visualizations </b>
			</p>
		</center>
		<center>
			<h:commandButton value="Graphical Analysis" styleClass="button"
				action="#{actionBeanAnalysis.processGraphicQuery}" />
		</center>
		<div align="center">
			<h:panelGrid columns="3">
				<h:selectOneListbox id="displayCharts"
					value="#{actionBeanAnalysis.chartType}" size="6">
					<f:selectItem itemValue="0" itemLabel="Select Chart Type" />
					<f:selectItem itemValue="1" itemLabel="Pie Chart" />
					<f:selectItem itemValue="2" itemLabel="Bar Graph" />
					<f:selectItem itemValue="3" itemLabel="Scatterplot" />
					<f:selectItem itemValue="4"
						itemLabel="Scatterplot with regression Line" />
				</h:selectOneListbox>
				<h:commandButton type="submit" value="Table List"
					action="#{actionBeanAnalysis.listTables}" />
				<h:commandButton type="submit" value="Column List"
					action="#{actionBeanAnalysis.listColumns}" />
			</h:panelGrid>
			<h:panelGrid columns="100">
				<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
					value="#{actionBeanAnalysis.tableName}"
					rendered="#{actionBeanAnalysis.tableListRendered}">
					<f:selectItems value="#{actionBeanAnalysis.tableViewList}" />
				</h:selectOneListbox>
				<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
					value="#{actionBeanAnalysis.columnNameSelected}"
					rendered="#{actionBeanAnalysis.columnListRendered}">
					<f:selectItems value="#{actionBeanAnalysis.columnNames}" />
				</h:selectOneListbox>

				<h:selectOneListbox id="predictor"
					value="#{actionBeanAnalysis.predictorValue}"
					rendered="#{actionBeanAnalysis.renderRegressionColumns}">
					<f:selectItem itemValue="0" itemLabel="Select Predictor Value" />
					<f:selectItems value="#{actionBeanAnalysis.columnNames}" />
				</h:selectOneListbox>
				<h:selectOneListbox id="response"
					value="#{actionBeanAnalysis.responseValue}"
					rendered="#{actionBeanAnalysis.renderRegressionColumns}">
					<f:selectItem itemValue="0" itemLabel="Select Response Value" />
					<f:selectItems value="#{actionBeanAnalysis.columnNames}" />
				</h:selectOneListbox>
			</h:panelGrid>
			<hr />
			<div
				style="background-attachment: scroll; overflow: auto; height: 300px; background-repeat: repeat"
				align="center">
				<%-- <h:graphicImage value="#{actionBeanAnalysis.xySeriesChartFile}"
						height="450" width="600"
						rendered="#{actionBeanAnalysis.renderChart}" alt="xySeriesChart" />
					<br />
					<h:graphicImage value="#{actionBeanAnalysis.timeSeriesChartFile}"
						height="450" width="600"
						rendered="#{actionBeanAnalysis.renderChart}" alt="timeSeriesChart" /> --%>
				<h:graphicImage value="#{actionBeanAnalysis.pieChartPath}"
					width="600" height="600"
					rendered="#{actionBeanAnalysis.renderPieChart}" alt="PieChart" />

				<h:graphicImage value="#{actionBeanAnalysis.barChartPath}"
					width="600" height="600"
					rendered="#{actionBeanAnalysis.renderBarChart}" alt="BarChart" />

				<h:graphicImage value="#{actionBeanAnalysis.scatterChartPath}"
					width="600" height="600"
					rendered="#{actionBeanAnalysis.renderScatterPlot}"
					alt="ScatterChart" />

				<h:graphicImage value="#{actionBeanAnalysis.scatterChartPathLine}"
					width="600" height="600"
					rendered="#{actionBeanAnalysis.renderScatterPlotLine}"
					alt="ScatterChart" />

				<br />
			</div>
		</div>
		</h:form>
	</f:view>
</body>
</html>