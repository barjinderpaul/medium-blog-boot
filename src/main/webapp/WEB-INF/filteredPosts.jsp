<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: barjinder
  Date: 22/12/19
  Time: 7:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Single Post</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div>
        <a class="navbar-brand" href="#">
            <img src="https://img.icons8.com/ios-filled/50/000000/medium-logo.png" alt="medium-logo">
        </a>
        <span>Medium</span>
    </div>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav ml-auto ">
            <li class="nav-item active">
                <a class="nav-link" href="/">
                    <p>Home</p>
                </a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="/posts/add">
                    <p>Create Post</p>
                </a>
            </li>
        </ul>
    </div>
</nav>
<br>

<c:forEach items="${posts}" var="list">
    <div class="card">
        <div class="card-header bg-success">
                ${list.title}
        </div>
        <div class="card-body bg-light">
            <p class="card-text">
                <c:choose>
                    <c:when test = "${list.content.length() < 77}"> ${list.content} </c:when>
                    <c:otherwise> ${fn:substring(list.content,0,77)} <span>...</span> </c:otherwise>
                </c:choose>
            </p>
            <c:set var = "categories" scope = "session" value = "${list.getCategories()}"/>
            <p class="font-weight-bold">Categories :
                <c:forEach items="${categories}" var="category">
                    <span> <a href="/posts/tag/${category.categoryName}/${category.category_id}" class="btn btn-outline-primary">${category.categoryName}</a> </span>
                </c:forEach>
                <c:if test="${fn:length(categories) lt 1}">
                    <span>No categories found</span>
                </c:if>
            </p>
            <a href="/posts/${list.id}" class="btn btn-primary">Read More</a>
            <a href="/posts/update/${list.id}" class="btn btn-warning">Edit Post</a>
            <a href="/posts/delete/${list.id}" class="btn btn-danger">Delete Post</a>

        </div>
    </div>
    <hr>
    <br>
</c:forEach>

<ul>
    <c:set var = "queryString" value = "<%=request.getQueryString()%>"/>
    <c:forEach items="${numbers}" var="pageNumber">
        <%
            String query = request.getQueryString();
            String newQuery = query.replaceAll("page=[0-9]+", "page=" + pageContext.getAttribute("pageNumber"));
            pageContext.setAttribute("newQuery",newQuery);
        %>
        <a href="/posts/filter?${newQuery}">${pageNumber + 1} </a>
    </c:forEach>
</ul>



<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
        integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>

</body>
</html>
