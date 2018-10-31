<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Details</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
    <div id="wrapper">
        <h1>Todo Details</h1>
        <fieldset>
            <legend>Details</legend>
            <table>
                <tbody>
                    <tr>
                        <th>Title</th>
                        <td>${f:h(todo.todoTitle)}</td>
                    </tr>
                    <tr>
                        <th>Created</th>
                        <td><fmt:formatDate value="${todo.createdAt}" pattern="yyyy-MM-dd" /></td>
                    </tr>
                    <tr>
                        <th>Status</th>
                        <td>${todo.finished ? 'Finished.' : 'Working.'}</td>
                    </tr>
                </tbody>
            </table>
            <c:set var="findCondition" value="${f:query(findForm)}&${f:query(pageInfo)}" />
            <form method="post" action="${pageContext.request.contextPath}/pagination/model/${todo.todoId}?${findCondition}">
                <sec:csrfInput />
                <button name="finish" ${todo.finished ? 'disabled' : ''}>Finish</button>
                <button name="delete">Delete</button>
            </form>
        </fieldset>
    </div>
</body>
</html>
