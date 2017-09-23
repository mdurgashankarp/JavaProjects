<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Compute Return</title>
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
				<h:commandButton type="submit" value="Logout"
					action="#{dbAccessActionBean.logout}" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Main Menu" action="#{actionBeanComputeReturns.resetMessage}" />
				<br />
				<hr />
			</h:form>
			<h4>Compute Returns</h4>
		</div>
		<div align="left">
			<h:form enctype="multipart/form-data">
				
				<center>
				<h:outputText rendered="#{messagesBean.renderErrorMessage}"
					value="#{messagesBean.errorMessage}" />
					<h:panelGrid columns="20">
						<h:commandButton type="submit" value="Table List"
							action="#{actionBeanComputeReturns.listTables}" />
						<h:commandButton type="submit" value="Column List"
							action="#{actionBeanComputeReturns.displayColumns}" />
					</h:panelGrid>
					<h:panelGrid columns="100">
						<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
							value="#{actionBeanComputeReturns.tableNameSelected}"
							rendered="true">
							<f:selectItem itemValue="0" itemLabel="Select Table" />
							<f:selectItems value="#{actionBeanComputeReturns.tableViewList}"/>
						</h:selectOneListbox>
						<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
							id="source" value="#{actionBeanComputeReturns.source}">
							<f:selectItem itemValue="0" itemLabel="Select Source" />
							<f:selectItems value="#{actionBeanComputeReturns.sourceData}" />
						</h:selectOneListbox>
						<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
							id="destination" value="#{actionBeanComputeReturns.destination}">
							<f:selectItem itemValue="0" itemLabel="Select Destination" />
							<f:selectItems
								value="#{actionBeanComputeReturns.destinationData}" />
						</h:selectOneListbox>
					</h:panelGrid>
					<h:commandButton type="submit" value="Compute Returns"
						action="#{actionBeanComputeReturns.computeReturns}" />
				</center>
			</h:form>
		</div>
	</f:view>
</body>
</html>