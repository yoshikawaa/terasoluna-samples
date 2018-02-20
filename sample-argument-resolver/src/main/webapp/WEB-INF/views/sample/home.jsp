<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Request Headers</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
    <div id="wrapper">
        <h1>Request Headers</h1>
        <ul>
            <li>userAgent is ${requestHeader.userAgent}</li>
            <li>accept is ${requestHeader.accept}</li>
            <li>acceptLanguage is ${requestHeader.acceptLanguage}</li>
            <li>acceptEncoding is ${requestHeader.acceptEncoding}</li>
        </ul>
    </div>
</body>
</html>
