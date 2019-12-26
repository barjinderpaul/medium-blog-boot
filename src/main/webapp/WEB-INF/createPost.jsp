<%--
  Created by IntelliJ IDEA.
  User: barjinder
  Date: 22/12/19
  Time: 10:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>Post form</title>
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
<div class="container">

<h1>${heading}</h1><br>

<c:if test="${customAction == 'postCreated'}">
    <div class="alert alert-success" role="alert">
        <p>Post created : ${title} &nbsp; <a class="btn btn-primary" href="/posts/${id}">Go to Post</a> </p>
    </div>
</c:if>

<form action= "
    <c:choose>
        <c:when test="${customAction == 'addPost' || customAction == 'postCreated'}"> add </c:when>
        <c:otherwise> /posts/update/${id} </c:otherwise>
    </c:choose>"
    method="post">
<div class="form-group">
    <label for="title">Title</label>
    <input id="title" type="text" class="form-control" id="exampleInputEmail1" placeholder="Enter title" name="title"
           required value="<c:if test="${customAction == 'updatePost'}">${title}</c:if>">
    <small id="emailHelp" class="form-text text-muted">First 50 characters from content will be displayed as
        excerpt.</small>
</div>

<div class="md-form">
    <label for="content">Enter content</label>
    <textarea id="content" placeholder="Enter content" class="md-textarea form-control" rows="10" name="content" required><c:if test="${customAction != 'addPost'}">${fn:trim(content)}</c:if></textarea>
</div>
<button type="submit" class="btn btn-primary">Save Post</button>
</form>

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
