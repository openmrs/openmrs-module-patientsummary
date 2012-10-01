<%@ include file="/WEB-INF/template/include.jsp"%>

<c:set var="numSums" value="${fn:length(model.patientSummaries)}"/>
<c:set var="defaultId" value="${model.defaultSummary != null ? model.defaultSummary.reportDesign.id : 0}"/>

<script type="text/javascript">
	$j(document).ready(function() {
		// Hide the patient summary tab if no patient summaries are configured for display
		if (${numSums} == 0) {
			changeTab(document.getElementById("patientOverviewTab"));
			$j("#patientsummaryIdTab").parent().hide();
			$j("#patientsummaryId").hide();
		}
		// Display any default summary that is configured 
		else if (${defaultId > 0}) {
			loadPatientSummary('${defaultId}_${model.defaultSummary.contentType}');
		}
	});
	
	function loadPatientSummary(summaryAndContentType) {
		var split = summaryAndContentType.split("_");
		var summaryId = split[0];
		var contentType = split[1];
		$j(".summaryButtons").hide();
		$j("#${model.portletUUID}OutputDiv").html("");
		if (contentType.indexOf("text") != -1) {
			$j("#${model.portletUUID}LoadingDiv").show();
			$j("#${model.portletUUID}OutputDiv").load(
				"<openmrs:contextPath />/module/patientsummary/viewPatientSummary.form?patientId=${model.patientId}&summaryId="+summaryId,
				function() {
					$j("#${model.portletUUID}LoadingDiv").hide();
					$j("#summaryButton"+summaryId).show();
				}
			);
		}
		else {
			$j("#summaryButton"+summaryId).show();
		}
	}
</script>

<style type="text/css">
	div#patientSummarySelectDiv {	
		padding-top: 5px; padding-bottom: 5px;
	}
</style>

<div id="patientSummarySelectDiv">
	<c:if test="${numSums > 1}">
		<select id="summarySelect" onchange="loadPatientSummary(this.value);">
			<c:forEach items="${model.patientSummaries}" var="ps">		
				<option value="${ps.reportDesign.id}_${ps.contentType}"<c:if test="${defaultId == ps.reportDesign.id}"> selected</c:if>>${ps.reportDesign.name}</option>
			</c:forEach>
		</select>
	</c:if>
	<c:forEach items="${model.patientSummaries}" var="ps">
		<span id="summaryButton${ps.id}" class="summaryButtons">
			<button onclick="document.location.href='<openmrs:contextPath />/module/patientsummary/viewPatientSummary.form?download=true&patientId=${model.patientId}&summaryId=${ps.id}';">
				<spring:message code="patientsummary.download"/>
			</button>
		</span>
	</c:forEach>
</div>

<div id="${model.portletUUID}LoadingDiv" style="width:100%; text-align:center; padding-top:50px; display:none;">
	<b><spring:message code="patientsummary.loadingMessage"/></b>
	<br/>
	<img src="<c:url value="/images/loading.gif"/>" width="24" height="24" border="0" style="vertical-align:middle"/>
</div>
<div id="${model.portletUUID}OutputDiv" style="width:100%;">
</div>
