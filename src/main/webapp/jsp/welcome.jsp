<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:app>
    <div class="jumbotron">
        <div class="container">
            <h1><fmt:message key="dashboard.message.welcome"/>!</h1>
            <p><fmt:message key="index.about"/></p>
            <p>
                <a class="btn btn-primary btn-lg" href="/auth/register" role="button">
                    <fmt:message key="button.join"/> &raquo
                </a>
            </p>
        </div>
    </div>
    <div class="container">
        <c:if test="${not empty races}">
            <div class="row">
                <h2 class="sub-header"><fmt:message key="block.nearest.races.title"/></h2>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th><fmt:message key="dashboard.form.races.title"/></th>
                            <th><fmt:message key="dashboard.form.races.place"/></th>
                            <th><fmt:message key="dashboard.form.races.min_rate"/> ($)</th>
                            <th><fmt:message key="dashboard.form.races.started_at"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${races}" var="race">
                            <tr>
                                <td>${race.getId()}</td>
                                <td><a href="/races/view?id=${race.getId()}">${race.getTitle()}</a></td>
                                <td>${race.getPlace()}</td>
                                <td>${race.getMinRate()}</td>
                                <td>${f:formatDate(race.getStartedAt(),locale)}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>
    </div>
</layout:app>