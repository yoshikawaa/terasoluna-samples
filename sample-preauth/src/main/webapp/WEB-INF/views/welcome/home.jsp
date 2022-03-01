<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Home</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
    <div id="wrapper">
        <h1>Hello <sec:authentication property="principal.account.firstName"/>!</h1>
        <p>The time on the server is ${serverTime}.</p>
        <form method="post" action="logout">
            <sec:csrfInput />
            <button>Logout</button>
        </form>
    </div>
</body>
</html>
