<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/poplf per.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Работа мечты</title>
</head>
<body>
<div class="container pt-3">
    <jsp:include page="/WEB-INF/header.jsp"/>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                Кандидаты
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Имя</th>
                        <th scope="col">Фамилия</th>
                        <th scope="col">Пол</th>
                        <th scope="col">Город</th>
                        <th scope="col">Описание</th>
                        <th scope="col">Фото</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="candidate" items="${candidates}">
                        <tr>
                            <td>
                                <a href="${pageContext.request.contextPath}/candidate/edit.jsp?id=${candidate.id}">
                                    <i class="fa fa-edit mr-3"></i>
                                </a>
                                <c:out value="${candidate.name}"/>
                            </td>
                            <td>
                                <c:out value="${candidate.lastName}"/>
                            </td>
                            <td>
                                <c:out value="${candidate.sex}"/>
                            </td>
                            <td>
                                <c:out value="${candidate.city}"/>
                            </td>
                            <td>
                                <c:out value="${candidate.description}"/>
                            </td>
                            <td class="d-flex justify-content-between align-items-center">
                                <a href="${pageContext.servletContext.contextPath}/download?photoId=${candidate.photoId}">Download</a>
                                <img src="${pageContext.servletContext.contextPath}/download?photoId=${candidate.photoId}"
                                     width="100px" height="100px" alt="candidate_photo"/>
                                <a href="${pageContext.servletContext.contextPath}/delete?id=${candidate.id}">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fa fa-trash mr-3"></i>
                                    </button>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

</body>
</html>
