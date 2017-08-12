<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:app>
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
                                <td>${f:formatDate("yyyy/MM/dd HH:mm",race.getStartedAt())}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>
        <div class="row">
            <div class="col-md-4">
                <h2>Heading</h2>
                <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor
                    mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada
                    magna mollis euismod. Donec sed odio dui. </p>
                <p><a class="btn btn-default" href="#" role="button">View details »</a></p>
            </div>
            <div class="col-md-4">
                <h2>Heading</h2>
                <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor
                    mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada
                    magna mollis euismod. Donec sed odio dui. </p>
                <p><a class="btn btn-default" href="#" role="button">View details »</a></p>
            </div>
            <div class="col-md-4">
                <h2>Heading</h2>
                <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id
                    ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris
                    condimentum nibh, ut fermentum massa justo sit amet risus.</p>
                <p><a class="btn btn-default" href="#" role="button">View details »</a></p>
            </div>
        </div>
    </div>
    <!-- /container -->
</layout:app>