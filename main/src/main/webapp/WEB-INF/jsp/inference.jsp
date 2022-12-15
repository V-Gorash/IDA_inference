<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>COVID-19 Detector</title>
    <%@include file="../include/links.html" %>
</head>
<body>

<%@include file="../include/navbar.jsp" %>

<div id="headerwrap">
    <div class="container centered align-content-center">
        <div class="row">
            <div class="col">
                <h1>Разметка КТ-снимков легких для выявления COVID-19</h1>
            </div>
        </div>
        <div class="row>">
            <form action="${pageContext.request.contextPath}/" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="image-loader">Прикрепите изображение с КТ-снимком легких</label><br>
                    <input type="file" id="image-loader" name="image" required accept=".jpg, .jpeg, .png">
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-success btn-lg">Разметить</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>