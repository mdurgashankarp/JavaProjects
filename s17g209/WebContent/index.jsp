<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page - S17G209</title>
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
		<center>
			<h:form>
				<br />
				<br />
				<h:panelGrid columns="3">
					<h:outputText value="Username:*" />
					<h:inputText id="userName" value="#{dbInformationBean.userName}"
						style="width:75" required="true"
						requiredMessage="Username is required" />
					<h:message for="userName" style="color:red; font-size:90%" />
					<h:outputText value="Password:*" />
					<h:inputSecret id="password" value="#{dbInformationBean.password}"
						style="width:75" required="true"
						requiredMessage="Please enter your password" />
					<h:message for="password" style="color:red; font-size:90%" />
					<h:outputText value="DB Host:*" />
					<h:selectOneMenu id="host" value="#{dbInformationBean.dbmsHost}"
						style="width:100%" required="true"
						requiredMessage="Please select a host">
						<f:selectItem itemValue="127.0.0.1" itemLabel="localhost" />
						<f:selectItem itemValue="131.193.209.57" itemLabel="server57" />
						<f:selectItem itemValue="131.193.209.54" itemLabel="server54" />
					</h:selectOneMenu>
					<h:outputText value="" />
					<h:outputText value="DB Schema:" />
					<h:inputText id="schema" value="#{dbInformationBean.dbmsSchema}"
						style="width:75" />
					<h:outputText value="" />
					<h:outputText value="DBMS:*" />
					<h:selectOneListbox value="#{dbInformationBean.dbms}" size="3"
						style="width:100%">
						<f:selectItem itemValue="MySQL" itemLabel="MySQL" />
						<f:selectItem itemValue="DB2" itemLabel="DB2" />
						<f:selectItem itemValue="Oracle" itemLabel="Oracle" />
					</h:selectOneListbox>
					<h:outputText value="" />
					<h:outputText value="DB Port:" />
					<h:inputText id="port" value="#{dbInformationBean.dbmsPort}"
						style="width:75" required="true" />
					<h:outputText value="" />
					<h:outputText value="" />
					<h:outputText value="* required field"
						style="color:red; font-size:90%" />
					<br />
					<h:panelGroup></h:panelGroup>
				</h:panelGrid>
				<BR />
				<h:commandButton type="submit" value="Login"
					action="#{dbAccessBean.connectToDb}" />
				<br />
				<h:outputText value="#{dbAccessBean.message}" />
				<br />
			</h:form>
		</center>
	</f:view>
</body>
</html>