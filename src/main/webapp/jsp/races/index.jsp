<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:app>
    <div class="container">
        <c:choose>
            <c:when test="${not empty races}">
                <div class="row">
                    <h2 class="sub-header"><fmt:message key="block.races.title"/></h2>
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
                                    <td>${f:formatDate(race.getStartedAt(), locale)}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="col-md-12" style="text-align: center">
                    <div class="row">
                        <ctg:pagination total="${totalRaces}" limit="${limitRaces}"/>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <h4><fmt:message key="races.empty"/></h4>
            </c:otherwise>
        </c:choose>
    </div>
</layout:app>