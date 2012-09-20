<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/view/module/reporting/localHeaderMinimal.jsp"%>

<c:if test="${fn:length(parameterErrors) > 0}">
<spring:message code="fix.error" />
<br />
</c:if>
<c:if test="${not empty errorDetails}">
<span class="error">${errorDetails}</span>
<br />
</c:if>

<c:choose>
	<c:when test="${showParametersForm}">
		<form>
			<table cellpadding="5" cellspacing="0" align="left">
				<c:forEach items="${parameters}" var="parameter">
				<tr>
					<td valign="top">${parameter.label}:</td>
					<td valign="top">
						<c:choose>
						<c:when test="${parameter.collectionType != null}">
						<wgt:widget id="${parameter.name}" name="${parameter.name}" type="${parameter.collectionType.name}" genericTypes="${parameter.type.name}" defaultValue="${parameterValueMap[parameter.name]}" attributes="${parameter.widgetConfigurationAsString}"/>	
						</c:when>
						<c:otherwise>
						<wgt:widget id="${parameter.name}" name="${parameter.name}" type="${parameter.type.name}" defaultValue="${parameterValueMap[parameter.name]}" attributes="${parameter.widgetConfigurationAsString}"/>	
						</c:otherwise>
					</c:choose>
					</td>
					<td valign="top">
						<span class="error" <c:if test="${empty parameterErrors[parameter.name]}">style="display: none"</c:if>>
							<spring:message code="${parameterErrors[parameter.name]}" arguments="${parameter.label}" />
						</span>
					</td>
				</tr>
				</c:forEach>
				<tr>
					<td></td>
					<td valign="top" style="padding-top: 10px" colspan="2">					
						<input type="hidden" name="reportDesignUuid" value="${reportDesignUuid}" />
						<input type="hidden" name="patientId" value="${patientId}" />			
						<input type="submit" value="<spring:message code="general.submit" />" />
					</td>
				</tr>
			</table>
		</form>
	</c:when>
	<c:otherwise>
		${generatedSummary}
	</c:otherwise>
</c:choose>
