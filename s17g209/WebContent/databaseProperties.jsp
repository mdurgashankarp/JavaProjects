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
				<h:commandButton type="submit" value="Logout" action="#{dbAccessActionBean.logout}" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Main Menu" action="#{dbAccessActionBean.resetMessage}" />
				<br />
				<hr />
			</h:form>
			<h4>Display Table Rows and Columns</h4>
		</div>

		<div align="center">
			<h:form>
				<h:outputText value="#{messagesBean.errorMessage}" style="color:red"
					rendered="#{messagesBean.renderErrorMessage}" />
				<h:panelGrid columns="10">
					<h:commandButton type="submit" value="Table List"
						action="#{dbAccessActionBean.listTables}" />
					<h:commandButton type="submit" value="Column List"
						action="#{dbAccessActionBean.listColumns}" />
					<h:commandButton type="submit" value="Display Table"
						action="#{dbAccessActionBean.selectAllColumn}" />
					<h:commandButton type="submit" value="Display Selected Columns"
						action="#{dbAccessActionBean.selectParticularColumn}" />
					<h:commandButton type="submit" value="Process SQL Query"
						action="#{dbAccessActionBean.processSQLQuery}" /> 
					<h:commandButton type="submit" value="Create Tables"
						action="#{dbAccessActionBean.processSQLQuery}" /> 
					<h:commandButton type="submit" value="Drop Tables"
						action="#{dbAccessActionBean.dropTables}" /> 
					<h:commandButton type="submit" value="Drop All Tables"
						action="#{dbAccessActionBean.dropAllTables}" /> 
					<h:commandButton type="submit" value="Create Access Log"
					action="#{dbAccessActionBean.createAccesslog}" /> 
					<h:commandButton type="submit" value="Create Transaction Log"
					action="#{dbAccessActionBean.createTransactionlog}" /> 
				</h:panelGrid>
				<h:panelGrid columns="80">

					<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
						value="#{dbAccessActionBean.tableName}"
						rendered="#{dbAccessActionBean.tableListRendered}">
						<f:selectItems value="#{dbAccessActionBean.tableViewList}" />
					</h:selectOneListbox>

					<h:selectManyListbox size="10" styleClass="selectManyListbox"
						value="#{dbAccessActionBean.columnNamesSelected}"
						rendered="#{dbAccessActionBean.columnListRendered}">
						<f:selectItems value="#{dbAccessActionBean.columnNames}" />
					</h:selectManyListbox>

					<h:inputTextarea cols="70" rows="10"
						value="#{dbAccessActionBean.sqlQuery}" />
				</h:panelGrid>
				<br />
				<hr />
				<br />

				<h:panelGrid columns="2"
					rendered="#{dbAccessActionBean.queryRendered}">
					<h:outputText value="SQL Query:" />
					<h:outputText id="sqlQuery" value="#{dbAccessActionBean.sqlQuery}" />
					<h:outputText value="Number of Columns:" />
					<h:outputText id="noOfCol" value="#{dbAccessActionBean.noOfCols}" />
					<h:outputText value="Number of Rows:" />
					<h:outputText id="noOfRows" value="#{dbAccessActionBean.noOfRows}" />
				</h:panelGrid>
				<hr />
				<div
					style="background-attachment: scroll; overflow: auto; height: 400px; background-repeat: repeat">
					<t:dataTable value="#{dbAccessActionBean.result}" var="row"
						rendered="#{dbAccessActionBean.queryRendered}" border="1"
						cellspacing="0" cellpadding="1"
						columnClasses="columnClass1 border" headerClass="headerClass"
						footerClass="footerClass" rowClasses="rowClass2"
						styleClass="dataTableEx" width="700">
						<t:columns var="col"
							value="#{dbAccessActionBean.columnNamesSelected}">
							<f:facet name="header">
								<t:outputText styleClass="outputHeader" value="#{col}" />
							</f:facet>
							<t:outputText styleClass="outputText" value="#{row[col]}" />
						</t:columns>
					</t:dataTable>
				</div>
			</h:form>
		</div>
	</f:view>
</body>
</html>