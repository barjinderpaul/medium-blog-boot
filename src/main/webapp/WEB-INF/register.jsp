
<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Register"/>
</jsp:include>

<%
    if(session.getAttribute("Username") != null ) {
        response.sendRedirect("admin");
    }
%>

<div class = "container">
    <div class="wrapper">
        <form action="register" method="post" name="Register-form" class="form-signin">

            <h3 class="form-signin-heading">Register yourself</h3>
            <hr class="colorgraph"><br>

            <input type="text" class="form-control" name="username" placeholder="Username" required="" autofocus="" />
            <br><input type="password" class="form-control" name="password" placeholder="Password" required=""/>
            <br><input type="email" class="form-control" name="email" placeholder="Enter email" required=""/>
            <br>
            <button class="btn btn-lg btn-primary btn-block"  name="Submit" value="Register" type="Submit">Register</button>
        </form>
    </div>
</div>

<%@ include file="layout/footer.html"%>