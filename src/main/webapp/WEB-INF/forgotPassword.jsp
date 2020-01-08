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
<div class = "container">
    <div class="wrapper">
        <br>
        <form action="forgot-password" method="post" name="Login_Form" class="form-signin">
            <h4 class="form-signin-heading">Please Enter your username</h4>
            <hr class="colorgraph"><br>
            <input type="text" class="form-control" name="username" placeholder="Username" required=""/>
            <br>
            <button class="btn btn-lg btn-primary btn-block"  name="Submit" value="Forgot Password" type="Submit">Login</button>
        </form>
    </div>
</div>

<%@ include file="layout/footer.html"%>