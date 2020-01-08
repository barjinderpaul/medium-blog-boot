<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Login"/>
</jsp:include>
<style>
    .form-signin {
        max-width: 420px;
        padding: 30px 38px 66px;
        margin: 0 auto;
        background-color: #eee;
        border: 3px dotted rgba(0,0,0,0.1);
    }

</style>
<br>
<div class = "container">
<%--    <div class="wrapper">--%>
    <form action="/login" method="post" name="Login_Form" class="form-signin">
        <c:if test="${not empty message}">
            <c:choose>
                <c:when test = "${message == 'Logout success!'}">
                    <p class="text-success text-center">${message}</p>
                </c:when>
                <c:otherwise>
                    <p class="text-danger text-center">${message}</p>
                </c:otherwise>
            </c:choose>
        </c:if>
        <h3 class="text-center">Welcome! Please Sign In</h3>
        <hr><br>
        <input type="text" class="form-control" name="username" placeholder="Username" required="" autofocus="" />
        <br>
        <input type="password" class="form-control" name="password" placeholder="Password" required=""/>
        <br>
        <button class="btn btn-primary btn"  name="Submit" value="Login" type="Submit">Login</button>
        <a class=" mx-2 btn btn-warning" href="/forgot-password">Forgot Password</a>
    </form>
</div>

<%@ include file="layout/footer.html"%>