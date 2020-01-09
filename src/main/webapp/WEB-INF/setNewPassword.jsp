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
        <form action="forget-account-password" method="post" name="Password_Reset" class="form-signin">
            <h3 class="form-signin-heading">Welcome! Please, set your new password</h3>
            <hr class="colorgraph"><br>

            <input type="text" class="form-control" name="username" placeholder="Username" readonly required="" value="${username}" autofocus="" />
            <br>
            <input type="text" class="form-control" name="confirmUsername" hidden readonly value="${confirmUsername}">
            <input type="text" class="form-control" name="confirmationToken" hidden readonly value="${confirmationToken}">
            <input type="password" class="form-control" name="password" placeholder="Password" required=""/>
            <br>
            <button class="btn btn-lg btn-primary btn-block"  name="Submit" value="Login" type="Submit">Submit</button>
        </form>
    </div>
</div>

<%@ include file="layout/footer.html"%>