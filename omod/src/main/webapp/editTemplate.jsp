<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<%@ taglib prefix="rpt" uri="/WEB-INF/view/module/reporting/resources/reporting.tld" %>
<%@ taglib prefix="wgt" uri="/WEB-INF/view/module/htmlwidgets/resources/htmlwidgets.tld" %>

<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui.custom.min.js" />
<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" />
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/reporting/scripts/jquery/dataTables/css/page.css"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/reporting/scripts/jquery/dataTables/css/table.css"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/reporting/scripts/jquery/dataTables/css/custom.css"/>

<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/reporting/scripts/jquery/dataTables/jquery.dataTables.min.js"/>
<openmrs:htmlInclude file="/scripts/jquery-ui/css/redmond/jquery-ui-1.7.2.custom.css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/moduleResources/patientsummary/js/codemirror.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/moduleResources/patientsummary/js/mirrorframe.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/moduleResources/patientsummary/css/main.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/moduleResources/patientsummary/js/main.js"></script>

<style type="text/css">
.tab {
	background-color: #DEDEDE;
}
</style>

<script type="text/javascript">
	$j(document).ready(function() {

		$j("#data-schema-table").dataTable( {
			"bPaginate": true,
			"iDisplayLength": 10,
			"bLengthChange": false,
			"bFilter": false,
			"bSort": true,
			"bInfo": true,
			"bAutoWidth": false
		} );

		$j("#rendererType").change(function() {
			if ($j(this).find("option:selected").attr("value") == "org.openmrs.module.reporting.report.renderer.TextTemplateRenderer") {
				$j("#templateConfiguration").hide();
				$j("#textTemplate").show();
			} else {
				$j("#templateConfiguration").show();
				$j("#textTemplate").hide();
			}
		});
		
		$j("#rendererType").trigger("change");

		var editor = CodeMirror.fromTextArea('templateContents', {
			height: "375px",
			parserfile: ["tokenizejavascript.js", "parsejavascript.js"],
			stylesheet: "${pageContext.request.contextPath}/moduleResources/patientsummary/css/jscolors.css",
			path: "${pageContext.request.contextPath}/moduleResources/patientsummary/js/",
			continuousScanning: 500,
			lineNumbers: true,
			textWrapping: false,
			autoMatchParens: true,
			tabMode: "spaces",
			submitFunction: function() {
				$j("#previewLink").click();
			},
			saveFunction:function() {
				document.forms["scriptForm"].submit();
			}
		});
		
		$j("#previewLink").click(function() {
			$j("#templateContents").val(editor.getCode());
			var form = $j("#templateForm");
			form.attr("target", "previewFrame");
			form.attr("action", '<c:url value="/module/patientsummary/previewSummaries.form" />')
			form.submit();
			
			form.attr("target", "");
			form.attr("action", "");
		});
		
		$j("#editLink").trigger("click");

		$j("#textTemplate").tabs();
	});
</script>

<form id="templateForm" method="post" enctype="multipart/form-data">
<input type="hidden" name="templateUuid" value="${template.uuid}" />
<input type="hidden" name="summaryId" value="${template.id}" />
<input type="hidden" name="iframe" value="true" />

<div style="float: left; width: 30%">

	<div class="boxHeader">
		<spring:message code="patientsummary.template.details" />
	</div>
	<div class="box">
		<table>
			<tr><td><spring:message code="patientsummary.name" />:</td><td><input name="name" value="${template.reportDesign.name}"/></td></tr>
			<tr>
				<td><spring:message code="patientsummary.type" />:</td>
				<td>
					<select id="rendererType" name="rendererType">
						<c:forEach var="type" items="${rendererTypes}">
							<c:set var="isSelected" value="${type eq template.reportDesign.rendererType ? ' selected' : ''}"/>
							<option value="${type.name}"${isSelected}><rpt:displayLabel type="${type.name}"/></option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
		<br/><input type="checkbox" name="enableOnPatientDashboard" <c:if test="${enableOnPatientDashboard}">checked</c:if> ><spring:message code="patientsummary.enableOnPatientDashboard"/>
	</div>
	
	<br/>
	
	<div class="boxHeader">
		<spring:message code="patientsummary.dataSchema" />
	</div>
	<div class="box">
		<table id="data-schema-table" class="display">
			<thead>
				<tr style="text-align: left;"><th><spring:message code="patientsummary.name" /></th><th><spring:message code="patientsummary.type" /></th></tr>
			</thead>
			<tbody>
				<c:forEach items="${dataSchema}" var="item">
					<tr><td>$data.${item.key}</td><td>${item.value}</td></tr>
				</c:forEach>
			</tbody>
			<tfoot></tfoot>
		</table>
		<br/><br/>
	</div>
</div>

<div style="float: right; width: 69%">
	<div id="templateConfiguration">
		<div class="boxHeader">
			<spring:message code="patientsummary.template.configuration" />
		</div>
		<div class="box">
			<spring:message code="patientsummary.template.upload" />: <input name="resource" type="file" /><br/>
			<c:if test="${!empty template.reportDesign.resources}">
				<spring:message code="patientsummary.template.files" />:
				<ul>
				<c:forEach items="${template.reportDesign.resources}" var="resource">
					<li>${resource.name} <input type="button" value="<spring:message code="patientsummary.delete" />" onclick="window.location='editTemplate/deleteResource.form?templateUuid=${template.uuid}&resourceUuid=${resource.uuid}'"/></li>
				</c:forEach>
				</ul>
			</c:if>
			<br/>
			<spring:message code="patientsummary.template.configuration" />: <br/>
			<wgt:widget id="properties" name="properties" object="${template.reportDesign}" property="properties" attributes="rows=10|cols=100"/>
		</div>
	</div>

	<div id="textTemplate">
		<ul>
			<li><a id="editLink" href="#edit"><spring:message code="patientsummary.template.edit" /></a></li>
			<li><a id="previewLink" href="#preview"><spring:message code="patientsummary.template.preview" /></a></li>
		</ul>
			
		<div id="edit" style="font-size:small;">
			<spring:message code="patientsummary.template.scriptType" />:
			<select name="scriptType">
				<c:forEach var="type" items="${scriptTypes}">
					<c:set var="isSelected" value="${scriptType eq type ? ' selected' : ''}"/>
					<option value="${type}"${isSelected}>${type}</option>
				</c:forEach>
			</select>
			<br/><br/>
			<div id="textarea-container" class="border">
				<textarea id="templateContents" name="script" cols="140" rows="80">${script}</textarea>
			</div>
		</div>
		<div id="preview">
			<iframe id="previewFrame" name="previewFrame" style="width: 99%; height: 400px"></iframe>
		</div>
	</div>
	<br/><br/>
	<div style="text-align: right;">
		<input type="button" value="<spring:message code="patientsummary.template.backToSummary" />" onclick="window.location='editSummary.form?uuid=${template.reportDesign.reportDefinition.uuid}'"/> <input type="button" value="<spring:message code="patientsummary.cancel" />" onclick="window.location='editTemplate.form?templateUuid=${template.uuid}'"/> <input type="submit" value="<spring:message code="patientsummary.save" />"/>
	</div>
</div>

<div style="clear: both"></div>

</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>