
<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Register"/>
</jsp:include>
<br>
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
        <form action="/register/admin" method="post" name="Register-form" class="form-signin">

            <h3 class="form-signin-heading">Register yourself</h3>
            <hr class="colorgraph"><br>

            <input type="text" class="form-control" name="username" placeholder="Username" required="" autofocus="" />
            <br><input type="password" class="form-control" name="password" placeholder="Password" required=""/>
            <br><input type="email" class="form-control" name="email" placeholder="Enter email" required=""/>
            <br><input type="password" class="form-control" name="key" placeholder="Enter secret key" required="" autofocus="" />
            <br><button class="btn btn-lg btn-primary btn-block"  name="Submit" value="Register" type="Submit">Register</button>
        </form>
    </div>
</div>

<%@ include file="layout/footer.html"%>