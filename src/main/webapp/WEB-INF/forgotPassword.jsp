<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Login"/>
</jsp:include>

<div class = "container">
    <div class="wrapper">
        <form action="/forgot-password" method="post" name="Login_Form" class="form-signin">

            <h3 class="form-signin-heading">Forgot Password? Please Enter your username</h3>
            <hr class="colorgraph"><br>
            <input type="text" class="form-control" name="username" placeholder="Username" required=""/>
            <br>
            <button class="btn btn-lg btn-primary btn-block"  name="Submit" value="Forgot Password" type="Submit">Login</button>
        </form>
    </div>
</div>

<%@ include file="layout/footer.html"%>