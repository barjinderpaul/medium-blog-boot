<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@ page import="com.blog.medium.model.Post" %>
<%@ page import="java.util.Arrays" %><%--
  Created by IntelliJ IDEA.
  User: barjinder
  Date: 21/12/19
  Time: 2:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Blog Posts</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<%--    <link rel="stylesheet" href="./css/admin.css">--%>
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
            <li>
                <form action="/posts/search">
                    <input type="text" name="query" placeholder="Enter keyword">
                    <input class="btn btn-primary" type="submit" value="Search">
                </form>
            </li>
        </ul>
    </div>
</nav>
<br>
<div class="container">
    <h1>Welcome to Blog </h1>
    <p>Sort by :
        <span><a class="btn btn-primary m-auto" href="/posts/filter?orderBy=UpdateDateTime">Last Updation</a></span>
        <span><a class="btn btn-primary m-auto" href="/posts/filter?orderBy=CreateDateTime">Last Published</a></span>
    </p>
    <p>Filter by Tag:
        <c:forEach items="${allCategories}" var="list">
           <span> <a href="/posts/filter?tag=${list.categoryName}" class="btn btn-outline-primary">${list.categoryName}</a> </span>
        </c:forEach>
    </p>
    <p>Filter by Author:
        <c:forEach items="${allUsers}" var="list">
            <span> <a href="/posts/filter?user=${list.username}" class="btn btn-outline-primary">${list.username}</a> </span>
        </c:forEach>
    </p>
    <br>
    <c:forEach items="${allPosts}" var="list">
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
                        <span> <a href="/posts/filter?tag=${category.categoryName}" class="btn btn-outline-primary">${category.categoryName}</a> </span>
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
</div>

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
