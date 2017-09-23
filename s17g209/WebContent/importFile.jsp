<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Database Properties - S17G209</title>
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
				<h:commandButton type="submit" value="Main Menu" action="#{actionBeanFile.resetMessage}" />
				<br />
				<hr />
			</h:form>
			<h4>Import File</h4>
			<h:form enctype="multipart/form-data">
				<h:outputText rendered="#{messagesBean.renderErrorMessage}"
					value="#{messagesBean.errorMessage}" />
				<h:panelGrid columns="2"
					style="background-color: lightgrey;
                                border-bottom-style: solid;
                                border-top-style: solid;
                                border-left-style: solid;
                                border-right-style: solid">

					<h:outputLabel value="File :" />
					<t:inputFileUpload id="fileUpload" label="File to upload"
						storage="default" value="#{actionBeanFile.uploadedFile}" size="60" />
					<h:outputLabel value="file label:" />
					<h:inputText id="fileLabel" value="#{actionBeanFile.fileLabel}"
						size="60" />
					<h:outputText value="Header: " />
					<h:selectOneListbox value="#{actionBeanFile.header}" size="2">
						<f:selectItem itemValue="y" itemLabel="Yes" />
						<f:selectItem itemValue="n" itemLabel="No" />
					</h:selectOneListbox>
					<h:outputText value="File type:" />
					<h:selectOneListbox value="#{actionBeanFile.fileType}" size="3">
						<f:selectItem itemValue="m" itemLabel="Metadata" />
						<f:selectItem itemValue="d" itemLabel="Data" />
					</h:selectOneListbox>
					<h:outputText value="File format:" />
					<h:selectOneListbox value="#{actionBeanFile.fileFormat}" size="5">
						<f:selectItem itemValue="csv" itemLabel="CSV" />
						<f:selectItem itemValue="text" itemLabel="Tab" />
					</h:selectOneListbox>
					<h:outputLabel value=" " />
					<h:commandButton id="upload"
						action="#{actionBeanFile.processFileUpload}" value="Submit" />

				</h:panelGrid>

				<h:panelGrid columns="2"
					style="background-color: lightgrey;
                        border-bottom-style: solid;
                        border-top-style: solid;
                        border-left-style: solid;
                        border-right-style: solid"
					width="800" rendered="#{actionBeanFile.fileImport}">
					<h:outputLabel value="Number of records:" />
					<h:outputText value="#{actionBeanFile.numberRows}" />
					<h:outputLabel value="Number of columns:" />
					<h:outputText value="#{actionBeanFile.numberColumns}" />
					<h:outputLabel value="fileLabel:" />
					<h:outputText value="#{actionBeanFile.fileLabel }" />
					<h:outputLabel value="fileName:" />
					<h:outputText value="#{actionBeanFile.fileName }" />
					<h:outputLabel value="fileSize:" />
					<h:outputText value="#{actionBeanFile.fileSize }" />
					<h:outputLabel value="fileContentType:" />
					<h:outputText value="#{actionBeanFile.fileContentType }" />
					<h:outputLabel value="tempFilePath:" />
					<h:outputText value="#{actionBeanFile.filePath }" />
					<h:outputLabel value="tempFileName:" />
					<h:outputText value="#{actionBeanFile.tempFileName }" />
					<h:outputLabel value="facesContext:" />
					<h:outputText value="#{actionBeanFile.facesContext }" />
				</h:panelGrid>
				<br />

			</h:form>
		</div>
	</f:view>
</body>
</html>