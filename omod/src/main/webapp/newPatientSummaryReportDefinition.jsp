<%@ include file="template/reportingInclude.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="Manage Patient Summaries"
	otherwise="/login.htm"
	redirect="/module/patientsummary/patientSummaryReportDefinitionEditor.form" />

<div id="page">
	<div id="container">

		<b class="boxHeader"><spring:message
				code="patientsummary.createNewPatientSummaryReportDefinitionEditor" /></b>
		<div class="box">
			<openmrs:portlet url="baseMetadata" id="baseMetadata"
				moduleId="reporting"
				parameters="type=org.openmrs.module.patientsummary.PatientSummaryReportDefinition|size=380|mode=edit|dialog=false|cancelUrl=patientSummaryReportDefinitions.list|successUrl=/module/patientsummary/patientSummaryReportDefinitionEditor.form?uuid=uuid" />
		</div>
	</div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>