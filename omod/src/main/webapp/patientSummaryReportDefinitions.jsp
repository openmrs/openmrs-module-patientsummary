<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Patient Summaries" otherwise="/login.htm" 
	redirect="/module/patientsummary/patientSummaryReportDefinitions.list" />

<%@ include file="template/localHeader.jsp"%>

<openmrs:hasPrivilege privilege="Manage Patient Summaries">
	<a href="<openmrs:contextPath />/module/patientsummary/patientSummaryReportDefinitionEditor.form">
		<spring:message code="patientsummary.addNewPatientSummaryReportDefinition" />
	</a>
	<br/><br/>
</openmrs:hasPrivilege>

<!-- TODO List all current patient summaryReport definitions See https://tickets.openmrs.org/browse/PS-11 -->

<%@ include file="/WEB-INF/template/footer.jsp"%>