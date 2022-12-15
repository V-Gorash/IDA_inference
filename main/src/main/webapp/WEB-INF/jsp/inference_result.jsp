<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Результат обработки - COVID-19 Detector</title>
    <%@include file="../include/links.html" %>
</head>
<body>

<%@include file="../include/navbar.jsp" %>

<div id="headerwrap">
    <div class="container centered align-content-center">
        <div class="row">
            <div class="col">
                <h1>Результат обработки</h1>
            </div>
        </div>
        <c:if test="${!isValid}">
            <div class="alert alert-danger" role="alert">
                Система считает, что это изображение не является КТ-снимком легких
            </div>
        </c:if>
        <div class="row>">
            <div class="col">
                <img src="data:image/png;base64,${base64Image}" alt="image"/>
            </div>
        </div>
        <div class="row">
            <div class="col d-grid gap-2">
                <a href="${pageContext.request.contextPath}/"><button type="button" class="btn btn-success btn-lg">Назад</button></a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
