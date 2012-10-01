<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<table style="width:100%;">
	<tr>
		<td style="white-space:nowrap; vertical-align:top; padding-right:10px;">
			<form>
				<b>Choose Patient:</b><br/>
				<openmrs_tag:patientField formFieldName="patientId" initialValue="${patientId}" /><br/><br/>
				<b>Choose Summary:</b><br/>
				<c:forEach items="${patientSummaries}" var="ps">
					<input type="radio" name="summaryId" value="${ps.id}" <c:if test="${summaryToPreview != null && summaryToPreview.id == ps.id}">checked</c:if>/> ${ps.name}<br/>
				</c:forEach>
				<br/>
				<input type="submit" value="Preview"/>
			</form>
		</td>
		<td style="width:95%; vertical-align:top;">
			<c:choose>
				<c:when test="${!empty errorDetails}">
					An error occurred generating the patient summary named ${summaryToPreview.name}:<br/>
					<pre>
						${errorDetails}
					</pre>
				</c:when>
				<c:when test="${!empty summaryToPreview}">
					Preview of ${summaryToPreview.name}:<hr/>
					${generatedSummary}
				</c:when>
				<c:otherwise>
					Select a Patient and a Patient Summary from the left and click Preview to view
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>