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
		<div align="center">
			<h:form>
				<h:commandButton type="submit" value="Logout" action="#{dbAccessActionBean.logout} }"  />
				<h:commandButton type="submit" value="Main Menu" action="home.jsp" />
				<br />
				<hr />
			</h:form>
		</div>
		<p>
			<b> <center><h:commandButton value = "Get Access Log"
						action = "#{iPAccessLog.getLogData}" styleClass = "button"/> </center> </b>
		</p>
			<div align="center">
					<h:panelGrid columns="2" border ="2">
						<h:outputText value="UserName: " rendered="#{iPAccessLog.renderLogs}"/>
						<h:outputText value="#{iPAccessLog.currentUserName}" rendered="#{iPAccessLog.renderLogs}"/>
						<h:outputText value="schema: " rendered="#{iPAccessLog.renderLogs}"/>
						<h:outputText value="#{iPAccessLog.dbms}" rendered="#{iPAccessLog.renderLogs}"/>
						<h:outputText value="Last Login Time: " rendered="#{iPAccessLog.renderLogs}" />
						<h:outputText value="#{iPAccessLog.loginTimeString}" rendered="#{iPAccessLog.renderLogs}"/>
						<h:outputText value="Last Logout Time: " rendered="#{iPAccessLog.renderLogs}"/>
						<h:outputText value="#{iPAccessLog.logoutTimeString}" rendered="#{iPAccessLog.renderLogs}"/>
						<h:outputText value="IP Address: " rendered="#{iPAccessLog.renderLogs}"/>
						<h:outputText value="#{iPAccessLog.getIpAddress}" rendered="#{iPAccessLog.renderLogs}"/>
					</h:panelGrid>
					</div>
			
	</f:view>
</body>
</html>