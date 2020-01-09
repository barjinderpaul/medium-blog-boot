<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%--
  Created by IntelliJ IDEA.
  User: barjinder
  Date: 21/12/19
  Time: 2:11 PM
  To change this template use File | Settings | File Templates.
--%>

<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Home Page"/>
</jsp:include>
<br>

<div class="container">
    <h1>Welcome to Blog </h1>


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
                        <span> <a href="/posts?tag=${category.categoryName}" class="btn btn-outline-primary">${category.categoryName}</a> </span>
                    </c:forEach>
                    <c:if test="${fn:length(categories) lt 1}">
                        <span>No categories found</span>
                    </c:if>

                </p>
                <a href="/posts/${list.id}" class="btn btn-primary">Read More</a>
                <c:set var = "currentUser" scope = "session" value = "<%= SecurityContextHolder.getContext().getAuthentication().getName() %>"/>
                <c:set var = "currentUserRole" scope="session" value="<%= SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString()%>"/>

                <c:if test = "${ (list.getUser().getUsername() == currentUser ) || currentUserRole == '[ROLE_ADMIN]'}">
                    <a href="/posts/update/${list.id}" class="btn btn-warning">Edit Post</a>
                </c:if>

                <c:if test = "${(list.getUser().getUsername() == currentUser)  || currentUserRole == '[ROLE_ADMIN]'}">
                    <a href="/posts/delete/${list.id}" class="btn btn-danger">Delete Post</a>
                </c:if>


            </div>
        </div>
        <hr>
        <br>
    </c:forEach>

    <ul>
        <c:forEach items="${numbers}" var="pageNumber">
            <%
                String query = "";
                if( !(request.getQueryString() == null)){
                    query = request.getQueryString();
                }
                String newQuery = "";
                 if(!(query.contains("page")) ){
                    newQuery = query+"&page="+pageContext.getAttribute("pageNumber")+"&size=2";
                }
                else {
                    newQuery = query.replaceAll("page=[0-9]+", "page=" + pageContext.getAttribute("pageNumber"));
                }
                pageContext.setAttribute("newQuery", newQuery);
            %>
            <a class="btn btn-outline-dark" href="/?${newQuery}">${pageNumber + 1} </a>
        </c:forEach>
    </ul>

</div>

<%@ include file="layout/footer.html"%>