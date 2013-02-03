<%@ include file="template/reportingInclude.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		
		$(".summary-data-table").dataTable( {
			"bPaginate": true,
			"iDisplayLength": 25,
			"bLengthChange": false,
			"bFilter": true,
			"bSort": true,
			"bInfo": true,
			"bAutoWidth": false
		} );
	} );

	function confirmDelete(schemaName, summaryName, uuid) {
		if (confirm("Are you sure you want to delete the patient summary named " + summaryName + " for schema " + schemaName + "?")) {
			document.location.href = '${pageContext.request.contextPath}/module/patientsummary/purgeSummary.form?uuid=' + uuid;
		}
	}

</script>

<style>
	.small { font-size: x-small; }
</style>

<div id="page">
	<div id="container">
		<h1><spring:message code="patientsummary.listAll.title"/></h1>
		
		<table class="summary-data-table display">
			<thead>
				<tr>
					<th><spring:message code="patientsummary.summarySchemaName"/></th>
					<th><spring:message code="patientsummary.summaryViewName"/></th>
					<th><spring:message code="general.description"/></th>
				</tr>
			</thead>
			<tbody>
					<c:forEach items="${summaries}" var="summary" varStatus="summaryStatus">
							<tr>
								<td width="20%" nowrap>
									<a href="${pageContext.request.contextPath}/module/patientsummary/patientSummaryReportDefinitionEditor.form?uuid=${summary.uuid}">${summary.name}</a>
								</td>
								<td width="20%">
									<c:forEach items="${summary.templates}" var="template" varStatus="templateStatus">
										<a href="${pageContext.request.contextPath}/module/reporting/viewPortlet.htm?id=reportDesignPortlet&url=reportDesignForm&parameters=reportDesignUuid=${template.uuid}">${template.name}</a> <br/>
									</c:forEach>
								</td>
								<td width="40%" nowrap>
									${summary.description}
								</td>
							</tr>
					</c:forEach>	
			</tbody>
			<tfoot></tfoot>
		</table>
	
	</div>
	
	<input type="button" value="Define a new Patient Summary" onclick="window.location='patientSummaryReportDefinitionEditor.form'"/>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>