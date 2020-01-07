<%--<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Home Page"/>
</jsp:include>--%>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <%--    <link rel="stylesheet" href="./css/admin.css">--%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</head>
<body>

<div class="container m-4 p-4">
    <h3>Something went wrong! </h3>
    <br>
    <h5>${message}</h5>
    <a class="btn btn-primary" href="/blog">Go Home</a>
</div>


