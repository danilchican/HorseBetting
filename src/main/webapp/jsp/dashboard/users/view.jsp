<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
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
            </div>

            <div class="clearfix"></div>
            <jsp:include page="${pageContext.request.contextPath}/jsp/partials/messages.jsp"/>
            <div class="clearfix"></div>

            <div class="row">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="x_panel">
                        <div class="x_content">
                            <div class="col-md-3 col-sm-3 col-xs-12 profile_left">
                                <div class="profile_img">
                                    <div id="crop-avatar">
                                        <img class="img-responsive avatar-view" src="/assets/dashboard/images/img.jpg"
                                             alt="Avatar">
                                    </div>
                                </div>
                                <h3>${viewedUser.getName()}</h3>

                                <ul class="list-unstyled user_data">
                                    <li>
                                        <strong><fmt:message
                                                key="profile.payment.current_balance"/>:</strong> ${viewedUser.getBalance()}
                                        <i class="fa fa-dollar user-profile-icon"></i>
                                    </li>
                                    <li>
                                        <form action="/dashboard/users/role/update" method="post">
                                            <input type="hidden" name="user-id" value="${viewedUser.getId()}">
                                            <i class="fa fa-briefcase user-profile-icon"></i> <strong><fmt:message
                                                key="dashboard.form.users.group"/>:</strong>
                                            <select name="user-role" id="user-role" onchange="this.form.submit()">
                                                <c:forEach items="${roles}" var="role">
                                                    <option value="${role.getId()}"
                                                            <c:if test="${role.getId() == viewedUser.getRoleId()}">selected</c:if>>
                                                            ${role.getName()}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </form>
                                    </li>

                                    <li class="m-top-xs">
                                        <i class="fa fa-external-link user-profile-icon"></i>
                                        <a href="mailto:${viewedUser.getEmail()}"
                                           target="_blank">${viewedUser.getEmail()}</a>
                                    </li>
                                    <li>
                                        <i class="fa fa-sign-in"></i> ${f:formatDate(viewedUser.getRegistrationDate(),locale)}
                                    </li>
                                </ul>
                            </div>
                            <div class="col-md-9 col-sm-9 col-xs-12">
                                <div class="" role="tabpanel" data-example-id="togglable-tabs">
                                    <ul id="myTab" class="nav nav-tabs bar_tabs" role="tablist">
                                        <li role="presentation" class="active">
                                            <a href="#user-bets" id="home-tab" role="tab" data-toggle="tab"
                                               aria-expanded="true">
                                                <fmt:message key="dashboard.user.bets.title"/>
                                            </a>
                                        </li>
                                    </ul>
                                    <div id="myTabContent" class="tab-content">
                                        <div role="tabpanel" class="tab-pane fade active in" id="user-bets"
                                             aria-labelledby="home-tab">
                                            <c:choose>
                                                <c:when test="${not empty bets}">
                                                    <!-- start user projects -->
                                                    <table class="data table table-striped no-margin">
                                                        <thead>
                                                        <tr>
                                                            <th>#</th>
                                                            <th><fmt:message key="form.bets.participant"/></th>
                                                            <th><fmt:message key="form.bets.amount"/> ($)</th>
                                                            <th><fmt:message key="form.bets.created_at"/></th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <c:forEach items="${bets}" var="bet">
                                                            <tr>
                                                                <td>${bet.getId()}</td>
                                                                <td>${bet.getParticipantName()}</td>
                                                                <td>${bet.getAmount()}</td>
                                                                <td>${f:formatDate(bet.getCreatedAt(), locale)}</td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                    <!-- end user projects -->

                                                    <div class="col-md-12" style="text-align: center">
                                                        <div class="row">
                                                            <ctg:pagination total="${totalBets}" limit="${limitBets}"/>
                                                        </div>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <h4><fmt:message key="bets.empty"/></h4>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /page content -->

</layout:dashboard>