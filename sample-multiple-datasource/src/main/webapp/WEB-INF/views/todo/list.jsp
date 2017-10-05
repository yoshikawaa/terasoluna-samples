<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Todo List</title>
</head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/app/css/todo.css">
<body>
    <h1>Todo List</h1>

    <div id="todoForm">
        <t:messagesPanel />

        <form:form servletRelativeAction="create" method="post" modelAttribute="todoForm">
            <form:input path="todoTitle" />
            <form:errors path="todoTitle" cssClass="text-error" />
            <form:button>Create Todo</form:button>
        </form:form>
    </div>
    <hr />
    <div id="todoList">
        <ul>
            <c:forEach items="${todos}" var="todo">
                <li><c:choose>
                        <c:when test="${todo.finished}">
                            <span class="strike">${f:h(todo.todoTitle)}</span>
                        </c:when>
                        <c:otherwise>
                            ${f:h(todo.todoTitle)}
                            <form:form servletRelativeAction="finish" method="post" modelAttribute="todoForm"
                                cssStyle="display: inline-block;">
                                <form:hidden path="todoId" value="${f:h(todo.todoId)}" />
                                <form:button>Finish</form:button>
                            </form:form>
                        </c:otherwise>
                    </c:choose> <!-- (1) --> <form:form servletRelativeAction="delete" method="post" modelAttribute="todoForm"
                        cssStyle="display: inline-block;">
                        <!-- (2) -->
                        <form:hidden path="todoId" value="${f:h(todo.todoId)}" />
                        <form:button>Delete</form:button>
                    </form:form></li>
            </c:forEach>
        </ul>
    </div>
</body>
</html>