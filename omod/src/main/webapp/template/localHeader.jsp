<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first">
        <a href="${pageContext.request.contextPath}/admin">
            <spring:message code="admin.title.short" />
        </a>
    </li>
	<li <c:if test='<%= request.getRequestURI().contains("/manageSummaries") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/patientsummary/manageSummaries.form">
            <spring:message code="patientsummary.listAll.title" />
        </a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/previewSummaries") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/patientsummary/previewSummaries.form">
            <spring:message code="patientsummary.previewSummaries" />
        </a>
	</li>
</ul>
