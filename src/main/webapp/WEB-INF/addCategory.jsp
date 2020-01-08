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

<div class = "container">
    <div class="wrapper">
        <br>
        <form action="/posts/category" method="post" name="Login_Form" class="form-signin">
            <c:if test="${not empty category}">
                <p class="text-center font-weight-bold">Category created :   <a href="/posts?tag=${category}">${category}</a></p>
            </c:if>
            <h3 class="text-center">Enter category name</h3>
            <hr class="colorgraph"><br>

            <input type="text" class="form-control" name="category" placeholder="Category" required="" autofocus="" />
            <br>
            <button class="btn btn-lg btn-primary btn-block"  name="Submit" value="Add Category" type="Submit">Add Category</button>
        </form>
    </div>
</div>

<%@ include file="layout/footer.html"%>