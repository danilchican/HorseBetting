<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:dashboard>
    <!-- page content -->
    <div class="right_col" role="main">
        <div class="">
            <div class="page-title">
                <div class="title_left">
                    <h3>${pageSubTitle}</h3>
                </div>

                <div class="title_right">
                    <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
                        <div class="input-group">
                            <input type="text" class="form-control" placeholder="Search for...">
                            <span class="input-group-btn">
                      <button class="btn btn-default" type="button">Go!</button>
                    </span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="clearfix"></div>
            <jsp:include page="${pageContext.request.contextPath}/jsp/partials/messages.jsp"/>
            <div class="clearfix"></div>

            <div class="row">
                <div class="col-md-12">
                    <div class="x_panel">
                        <div class="x_title">
                            <h2>${pageSubTitle}</h2>
                            <ul class="nav navbar-right panel_toolbox">
                                <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                </li>
                            </ul>
                            <div class="clearfix"></div>
                        </div>
                        <div class="x_content">
                            <a href="/dashboard/races/create">
                                <button type="button" class="btn btn-round btn-success"><fmt:message
                                        key="dashboard.button.races.create"/></button>
                            </a>
                            <!-- start project list -->
                            <table class="table table-striped projects">
                                <thead>
                                <tr>
                                    <th style="width: 1%">#</th>
                                    <th style="width: 25%"><fmt:message key="dashboard.form.races.title"/></th>
                                    <th><fmt:message key="dashboard.form.races.place"/></th>
                                    <th style="width: 10%"><fmt:message key="dashboard.form.races.status"/></th>
                                    <th style="width: 10%"><fmt:message key="dashboard.form.races.created_at"/></th>
                                    <th style="width: 20%"><fmt:message key="dashboard.message.action"/></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${races}" var="race">
                                    <tr>
                                        <td>${race.getId()}</td>
                                        <td>${race.getTitle()}</td>
                                        <td>${race.getPlace()}</td>
                                        <td>
                                            <fmt:message
                                                    key="races.status.${!race.isAvailable() ? race.getStatus() : 'expect'}"/>
                                        </td>
                                        <td>${f:formatDate(race.getCreatedAt(), locale)}</td>
                                        <td>
                                            <a href="/races/view?id=${race.getId()}"
                                               class="btn btn-primary btn-xs"><i class="fa fa-folder"></i> <fmt:message
                                                    key="button.view"/>
                                            </a>
                                            <a href="/dashboard/races/edit?id=${race.getId()}"
                                               class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <fmt:message
                                                    key="button.edit"/>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <!-- end project list -->
                            <ctg:pagination total="${totalRaces}" limit="${limitRaces}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /page content -->
</layout:dashboard>