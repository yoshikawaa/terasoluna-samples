<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sample</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
    <div id="wrapper">
        <h1>Sample</h1>
        <form:form modelAttribute="sampleForm">
            <form:label path="date" />
            <form:input path="date" />
            <form:errors path="date" />
            <form:button>Submit</form:button>
        </form:form>
    </div>
</body>
</html>
