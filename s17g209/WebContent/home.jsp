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
				<h:commandButton type="submit" value="Logout" 
				action="#{dbAccessActionBean.logout}" />
				<br />
				<hr />
			</h:form>
		</div>
		<p>
			<b> Please select what you want to do : </b>
		</p>
		<ul>
			<li><a href="databaseProperties.jsp">Database Access</a></li>
			<li><a href="importFile.jsp">Import Data</a></li>
			<li><a href="exportFile.jsp">Export Data</a></li>
			<li><a href="generateStatistics.jsp">Generate Statistics</a></li>
			<li><a href="graphAnalysis.jsp">Generate
					Visualization</a></li>
			<li><a href="computereturn.jsp">Compute Return</a></li>
		</ul>
		
		<p>About Project</p>
		<ul>
			
			<li><a href="documents/userGuide.pdf" target="_blank">User Guide</a></li>
			<li><a href="documents/progGuide.pdf" target="_blank">Programmers Guide</a></li>
		</ul>
	</f:view>
</body>
</html>