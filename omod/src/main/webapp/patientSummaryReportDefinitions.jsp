<%@ include file="template/reportingInclude.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="View Patient Summaries"
	otherwise="/login.htm"
	redirect="/module/patientsummary/patientSummaryReportDefinitions.list" />

<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		
		$(".reporting-data-table").dataTable( {
			"bPaginate": true,
			"iDisplayLength": 25,
			"bLengthChange": false,
			"bFilter": true,
			"bSort": true,
			"bInfo": true,
			"bAutoWidth": false
		} );
	} );

	function confirmDelete(name, uuid) {
		if (confirm("Are you sure you want to delete " + name + "?")) {
			document.location.href = '${pageContext.request.contextPath}/module/patientsummary/purgePatientSummaryReportDefinition.form?uuid=' + uuid;
		}
	}

</script>

<style>
.small {
	font-size: x-small;
}
</style>

<div id="page">
	<div id="container">
		<h1>
			<spring:message code="patientsummary.managePatientSummaryReportDefinitions" />
		</h1>
	
		<openmrs:hasPrivilege privilege="Manage Patient Summaries">
			<a
				href="<openmrs:contextPath />/module/patientsummary/newPatientSummaryReportDefinition.form">
				<spring:message
					code="patientsummary.addNewPatientSummaryReportDefinition" />
			</a>
		</openmrs:hasPrivilege>

		<table class="reporting-data-table display">
			<thead>
				<tr>
					<th>Name</th>
					<th>Description</th>
					<th>Creator</th>
					<th>Created</th>
					<th align="center" width="1%">Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${reportDefinitions}" var="reportDefinition"
					varStatus="status">
					<c:set var="editUrl">
						<c:choose>
							<c:when
								test="${reportDefinition['class'].simpleName == 'PeriodIndicatorReportDefinition'}">
								${pageContext.request.contextPath}/module/reporting/reports/periodIndicatorReport.form?uuid=${reportDefinition.uuid}
							</c:when>
							<c:otherwise>
								${pageContext.request.contextPath}/module/reporting/reports/reportEditor.form?uuid=${reportDefinition.uuid}
							</c:otherwise>
						</c:choose>
					</c:set>

					<tr>
						<td width="20%" nowrap>${reportDefinition.name}</td>
						<td width="20%">${reportDefinition.description}</td>
						<td width="5%" nowrap>${reportDefinition.creator}</td>
						<td width="5%" nowrap><rpt:timespan
								then="${reportDefinition.dateCreated}" /></td>
						<td width="1%" align="center" nowrap>&nbsp; <a
							href="patientSummaryReportDefinitionEditor.form?uuid=${reportDefinition.uuid}"><img
								src="<c:url value='/images/edit.gif'/>" border="0" /></a> &nbsp; <a
							href="javascript:confirmDelete('${reportDefinition.name}','${reportDefinition.uuid}');"><img
								src="<c:url value='/images/trash.gif'/>" border="0" /></a>
						</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
			</tfoot>
		</table>

	</div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>