
<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Register"/>
</jsp:include>

<style>
    .form-signin {
        max-width: 720px;
        padding: 30px 38px 66px;
        margin: 0 auto;
        background-color: #eee;
        border: 3px dotted rgba(0,0,0,0.1);
    }
</style>
<br>
<div class = "container">
    <div class="wrapper">
        <form action="register" method="post" name="Register-form" class="form-signin">
            <p>
                <h3>Register yourself
                    <span class="float-right">
                        <h5>Register as Admin? <span><a class="btn btn-outline-primary" href="/register/admin">Admin</a></span></h5>
                    </span>
                </h3>
            </p>
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