<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first">
        <a href="${pageContext.request.contextPath}/admin">
            <spring:message code="admin.title.short" />
        </a>
    </li>
	<li <c:if test='<%= request.getRequestURI().contains("/manage") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/patientsummary/manage.form">
            <spring:message code="patientsummary.manage" />
        </a>
	</li>
	
	<li <c:if test='<%= request.getRequestURI().contains("/listAll") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/patientsummary/listAllSummaries.form">
            <spring:message code="patientsummary.listAllSummaries" />
        </a>
	</li>
</ul>
