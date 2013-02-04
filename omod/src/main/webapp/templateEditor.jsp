<%@ include file="template/reportingInclude.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		$("#rendererType").change(function() {
			if ($(this).find("option:selected").attr("value") == "org.openmrs.module.reporting.report.renderer.TextTemplateRenderer") {
				$("#templateConfiguration").hide();
				$("#textTemplate").show();
			} else {
				$("#templateConfiguration").show();
				$("#textTemplate").hide();
			}
		});
		
		$("#rendererType").trigger("change");
		
		$("#textTemplate").tabs();
	});
</script>

<form method="post" enctype="multipart/form-data">
<input type="hidden" name="templateUuid" value="${template.uuid}" />

<div style="float: left; width: 30%">

	<div class="boxHeader">
		Template Details
	</div>
	<div class="box">
		<span class="metadataField">Name:</span>
		<input name="name" value="${template.reportDesign.name}"/><br />
		<span class="metadataField">Type:</span>
		<wgt:widget id="rendererType" name="rendererType" object="${template.reportDesign}" property="rendererType" attributes="type=org.openmrs.module.reporting.report.renderer.ReportRenderer|simple=true"/>
		<br/><input type="checkbox" name="enableOnPatientDashboard" <c:if test="${enableOnPatientDashboard}">checked</c:if> ><spring:message code="patientsummary.enableOnPatientDashboard"/>
	</div>
	
	<br/>
	
	<div class="boxHeader">
		Schema Data
	</div>
	<div class="box">
		
	</div>
</div>

<div style="float: right; width: 69%">
	<div id="templateConfiguration">
		<div class="boxHeader">
			Template Configuration
		</div>
		<div class="box">
			Upload Template File: <input name="resource" type="file" /><br/>
			<c:if test="${!empty template.reportDesign.resources}">
				Template Files:
				<ul>
				<c:forEach items="${template.reportDesign.resources}" var="resource">
					<li>${resource.name} <input type="button" value="Delete" onclick="window.location='templateEditor/deleteResource.form?templateUuid=${template.uuid}&resourceUuid=${resource.uuid}'"/></li>
				</c:forEach>
				</ul>
			</c:if>
			<br/>
			Configuration: <br/>
			<wgt:widget id="properties" name="properties" object="${template.reportDesign}" property="properties" attributes="rows=20|cols=50"/>
		</div>
	</div>
	
	<div id="textTemplate">
		<ul>
			<li><a href="#edit">Edit</a></li>
			<li><a href="#preview">Preview</a></li>
		</ul>
		<div id="edit">
			Script Type:
			<select name="scriptType">
			<c:forEach var="type" items="${scriptTypes}">
				<c:choose>
				<c:when test="${scriptType eq type}">
					<option selected="selected">${type}</option>
				</c:when>
				<c:otherwise>
					<option>${type}</option>
				</c:otherwise>
				</c:choose>
			</c:forEach>
			</select>
			<br/><br/>
			<textarea name="script" rows="20" cols="2" style="width:99%">${script}</textarea>
		</div>
		<div id="preview">
			Patient to preview: <!-- Need to figure out how to make it work
			<openmrs_tag:patientField formFieldName="patientId" formFieldId="patientId" />  -->
			<input type="text" name="patientId" value="${patientId}" />
			<input type="button" value="Change" /><br /><br />
			<iframe style="width: 99%; height: 600px"></iframe>
		</div>
	</div>
	
	<div style="text-align: right;">
		<input type="button" value="Cancel" onclick="window.location='patientSummaryReportDefinitionEditor.form?uuid=${template.reportDesign.reportDefinition.uuid}'"/> <input type="submit" value="Save"/>
	</div>
</div>

<div style="clear: both"></div>

</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>