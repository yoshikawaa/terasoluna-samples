<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>List</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
    <div id="wrapper">
        <h1>Find Todos</h1>
        <fieldset>
            <legend>Find Condition</legend>
            <form:form method="get" modelAttribute="findForm" class="form">
                <div>
                    <form:label path="todoTitle" class="form-label">Title</form:label>
                    <form:input path="todoTitle" class="form-item" />
                    <form:errors path="todoTitle" class="form-error" />
                </div>
                <div>
                    <form:label path="createdAt" class="form-label">CreatedAt</form:label>
                    <form:input path="createdAt" class="form-item" />
                    <form:errors path="createdAt" class="form-error" />
                </div>
                <div>
                    <label class="form-label">Status</label>
                    <div class="form-item">
                        <form:radiobutton path="finished" value="" label="All" />
                        <form:radiobutton path="finished" value="true" label="Finished" />
                        <form:radiobutton path="finished" value="false" label="Working" />
                        <form:errors path="finished" class="form-error" />
                    </div>
                </div>
                <form:button>Find</form:button>
            </form:form>
        </fieldset>
        <hr>
        <fieldset>
            <legend>Todos by Condition</legend>
            <c:choose>
                <c:when test="${page != null && page.totalPages != 0}">
                    <t:pagination page="${page}" criteriaQuery="${f:query(findForm)}" outerElementClass="pagination" />
                    <table>
                        <thead>
                            <tr>
                                <th>No.</th>
                                <th>Title</th>
                                <th>Created</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="baseCount" value="${page.number * page.size}" />
                            <c:forEach var="todo" items="${page.content}" varStatus="status">
                                <tr>
                                    <td>${baseCount + status.count}</td>
                                    <td><a href="${pageContext.request.contextPath}/pagination/session/${todo.todoId}">
                                            ${f:h(todo.todoTitle)}</a></td>
                                    <td><fmt:formatDate value="${todo.createdAt}" pattern="yyyy-MM-dd" /></td>
                                    <td>${todo.finished ? 'Finished.' : 'Working.'}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
    	    	  Not found...
    	    </c:otherwise>
            </c:choose>
        </fieldset>
    </div>
</body>
</html>
