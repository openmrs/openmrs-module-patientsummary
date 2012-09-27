<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

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

		<c:forEach items="${summaries}" var="summary" varStatus="status">
			$("#preview-report-${summary.uuid}").click(function(event){ 
				showReportingDialog({ 
					title: 'Execute Summary', 
					url: '<c:url value="/module/reporting/parameters/queryParameter.form"/>?uuid=${summary.uuid}&type=${summary.reportDesign.reportingDefinition['class'].name}',
					successCallback: function() { 
						window.location = window.location; //.reload(true);
					} 
				});
			});
	
			$("#render-report-${summary.uuid}").click(function(event){ 
				showReportingDialog({ 
					title: 'Execute Summary', 
					url: '<c:url value="/module/reporting/parameters/queryParameter.form"/>?uuid=${summary.uuid}&type=${summary.reportDesign.reportingDefinition['class'].name}&format=indicator&successView=redirect:/module/patientsummary/renderSummary.form',
					successCallback: function() { 
						window.location = window.location; //.reload(true);
					} 
				});
			});	
		</c:forEach>
	} );

	function confirmDelete(name, uuid) {
		if (confirm("Are you sure you want to delete the " + name + " summary?")) {
			document.location.href = '${pageContext.request.contextPath}/module/reporting/purgeReport.form?uuid=' + uuid;
		}
	}

</script>

<style>
	.small { font-size: x-small; }
</style>

<div id="page">
	<div id="container">
		<h1><spring:message code="patientsummary.listAll.title"/></h1>
		
		<table class="reporting-data-table display">
			<thead>
				<tr>
					<th><spring:message code="general.name"/></th>
					<th><spring:message code="general.description"/></th>
					<th><spring:message code="reporting.reportDefinition"/></th>
					<th><spring:message code="general.creator"/></th>
					<th><spring:message code="general.created"/></th>
					<th align="center" width="1%"><spring:message code="patientsummary.actions"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${summaries}" var="summary" varStatus="status">
					<tr>
						<td width="20%" nowrap>
							${summary.name}
						</td>
						<td width="20%">
							${summary.reportDesign.description}
						</td>
						<td width="10%" nowrap>
							<a href="${pageContext.request.contextPath}/module/patientsummary/reportDefinitionEditor.form?uuid=${summary.reportDesign.reportingDefinition.uuid}"></a>
								${summary.reportDesign.reportingDefinition.name}
							</a>
						</td>
						<td width="5%" nowrap>
							${summary.creator}
						</td>
						<td width="5%" nowrap>
							<rpt:timespan then="${summary.dateCreated}"/>
						</td>
						<td width="1%" align="center" nowrap>
							&nbsp;
							<a href="${pageContext.request.contextPath}/module/patientsummary/summaryEditor.form?uuid=${summary.uuid}"><img src="<c:url value='/images/edit.gif'/>" border="0"/></a>
							&nbsp;
							<a href="javascript:confirmDelete('${summary.name}','${summary.uuid}');"><img src="<c:url value='/images/trash.gif'/>" border="0"/></a>
							&nbsp;
							<a href="${pageContext.request.contextPath}/module/patientsummary/renderSummary.form?summaryId=${summary.uuid}">
								<img src='<c:url value="/images/play.gif"/>' align="absmiddle" border="0"/>
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