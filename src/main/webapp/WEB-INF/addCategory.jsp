<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Login"/>
</jsp:include>

<div class = "container">
    <div class="wrapper">
<%--        <c:if test="${category} != 'null'>
            <p>Category created :   <a href="/posts?tag=${category}">${category}</a></p>
        </c:if>--%>
        <form action="posts/category" method="post" name="Login_Form" class="form-signin">

            <h3 class="form-signin-heading">Welcome! Please Sign In</h3>
            <hr class="colorgraph"><br>

            <input type="text" class="form-control" name="category" placeholder="Category" required="" autofocus="" />
            <br>
            <button class="btn btn-lg btn-primary btn-block"  name="Submit" value="Add Category" type="Submit">Add Category</button>
        </form>
    </div>
</div>

<%@ include file="layout/footer.html"%>