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

            <div class="row">
                <div class="col-md-12">
                    <div class="x_panel">
                        <div class="x_content">
                            <div class="row">
                                <c:choose>
                                    <c:when test="${not empty users}">
                                        <c:forEach items="${users}" var="user">
                                            <div class="col-md-6 col-sm-6 col-xs-12 profile_details">
                                                <div class="well profile_view">
                                                    <div class="col-sm-12">
                                                        <div class="left col-xs-7">
                                                            <h2>${user.getName()}</h2>
                                                            <p><strong><fmt:message
                                                                    key="dashboard.form.users.group"/>:</strong> ${user.getRoleName()}
                                                            </p>
                                                            <ul class="list-unstyled">
                                                                <li><strong><fmt:message
                                                                        key="dashboard.form.users.balance"/>:</strong> ${user.getBalance()}
                                                                    <i
                                                                            class="fa fa-eur"></i></li>
                                                                <li><strong><fmt:message
                                                                        key="dashboard.form.users.email"/>:</strong> ${user.getEmail()}
                                                                </li>
                                                            </ul>
                                                        </div>
                                                        <div class="right col-xs-5 text-center">
                                                            <img src="/assets/dashboard/images/img.jpg" style="float:right"
                                                                 class="img-circle img-responsive">
                                                        </div>
                                                    </div>
                                                    <div class="col-xs-12 bottom text-center">
                                                        <div class="col-xs-12 col-sm-8 emphasis"
                                                             style="text-align: left; padding-top: 4px;">
                                                            <i class="fa fa-sign-in"></i> <strong><fmt:message
                                                                key="dashboard.form.users.reg_date"/>:</strong> ${f:formatDate(user.getRegistrationDate(),locale)}
                                                        </div>
                                                        <div class="col-xs-12 col-sm-4 emphasis">
                                                            <a href="/dashboard/users/view?id=${user.getId()}"
                                                               class="btn btn-primary btn-xs">
                                                                <i class="fa fa-user"> </i> <fmt:message
                                                                    key="dashboard.form.users.view_profile"/>
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <p><fmt:message key="users.empty"/></p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <ctg:pagination total="${totalUsers}" limit="${limitUsers}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /page content -->
</layout:dashboard>