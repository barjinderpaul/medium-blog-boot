<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Home Page"/>
</jsp:include>

<div class="container m-4 p-4">
    <h1>Something went wrong! </h1>
    <h2>Our Engineers are on it</h2>
    <br>
    <h1>${message}</h1>
    <a class="btn btn-primary" href="/blog">Go Home</a>
</div>


<%@ include file="layout/footer.html"%>
