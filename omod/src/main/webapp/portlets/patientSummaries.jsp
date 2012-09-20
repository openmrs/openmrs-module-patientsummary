<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
var reportCount = ${fn:length(model.reportDesigns)};
var pageUrlPrefix = "<openmrs:contextPath />/module/patientsummary/";
var loadedSummaryResults = null;
$j(document).ready(function(){
	//no configured reports, switch to the overview tab and hide the patient summary tab
	if(${fn:length(model.reportDesigns)} == 0){
		changeTab(document.getElementById("patientOverviewTab"));
		$j("#patientsummaryIdTab").parent().hide();
		$j("#patientsummaryId").hide();
	}else if(${model.getSummaryOnPageLoad}){
		//we have one configure report design, fetch the data but dont rendere it
		jQuery.get(
				pageUrlPrefix+"processAjaxRequest.htm", 
				{patientId: "${model.patient.patientId}", reportDesignUuid: "${model.reportDesigns[0].uuid}"}, 
				function(data) {
					loadedSummaryResults = data;
				}
			);
	}
	
	//set the dialog to display results
	$j("#patientsummary_reportDesignDialog").dialog({
		autoOpen: false,
		width: '90%',
		height: '80%',
		modal: true,
		beforeClose: function(event, ui){
			//clear
			$j("#patientsummary_reportDesignDialog > iframe").attr("src", "about:blank");
			$j("#reportDesignFrame").hide();
			$j("#preloadedResults").html('');
			$j("#preloadedResults").hide();
			$j("#resultsError").html('');
			$j("#resultsError").hide();
		}
	});
});

function patientsummary_view(){
	var rdUuid = $j.trim($j("#reportDesignSelect").val());
	if(rdUuid == ''){
		alert("<spring:message code="patientsummary.error.selectSummary" />");
		return;
	}
	
	//Dislay the preloaded ajax results if any
	if(loadedSummaryResults != undefined){
		$j("#preloadedResults").html(loadedSummaryResults['results']);
		$j("#preloadedResults").show();
		if(loadedSummaryResults['errorDetails'] != undefined){
			$j("#resultsError").html(loadedSummaryResults['errorDetails']);
			$j("#resultsError").show();
		}
		//clear so that we dont get back here until next page load
		loadedSummaryResults = null;
	}else{
		$j("#reportDesignFrame").show();
		$j("#patientsummary_reportDesignDialog > iframe").attr("src", pageUrlPrefix+"viewPatientSummary.htm?patientId=${model.patient.patientId}&reportDesignUuid="+rdUuid+"&showParametersFormIfNecessary=true");
	}
	
	$j("#patientsummary_reportDesignDialog").dialog('open');
}
</script>

<style type="text/css">
div#reportDesignSelectDiv {	
	padding-top: 5px; padding-bottom: 5px;
}
div#patientsummary_reportDesignDialog {
	min-height: 600px !important;/* Firefox can't recognise the 90% perhaps one of the parent elements in core has no height' */
	width: 90% !important; 
	height: 80% !important; 
}
#reportDesignFrame {
	display: none;
}
</style>

<div id="reportDesignSelectDiv">
	<b><spring:message code="patientsummary.selectReportDesign" /></b>:
	<select id="reportDesignSelect">
		<option></option>
		<c:forEach items="${model.reportDesigns}" var="reportDesign">		
		<option value="${reportDesign.uuid}">${reportDesign.name}</option>
		</c:forEach>
	</select>
	<input type="button" value="<spring:message code="patientsummary.view" />" onclick="patientsummary_view()" />
</div>
	
<div id="patientsummary_reportDesignDialog" title="<spring:message code="patientsummary.patientSummary" />">
	<iframe id="reportDesignFrame" width="100%" height="100%" marginWidth="0" marginHeight="0" frameBorder="0" scrolling="auto"></iframe>
	
	<div id="resultsError" class="error" style="display: none"></div>	
	<div id="preloadedResults" style="display: none; width: 100%; height: 100%">
	</div>
</div>
