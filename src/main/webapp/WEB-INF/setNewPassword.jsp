<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Login"/>
</jsp:include>

<div class = "container">
    <div class="wrapper">
        <form action="/forget-account-password" method="post" name="Password_Reset" class="form-signin">

            <h3 class="form-signin-heading">Welcome! Please, set your new password</h3>
            <hr class="colorgraph"><br>

            <input type="text" class="form-control" name="username" placeholder="Username" required="" value="${username}" autofocus="" />
            <br>
            <input type="password" class="form-control" name="password" placeholder="Password" required=""/>
            <br>
            <button class="btn btn-lg btn-primary btn-block"  name="Submit" value="Login" type="Submit">Login</button>
        </form>
    </div>
</div>

<%@ include file="layout/footer.html"%>