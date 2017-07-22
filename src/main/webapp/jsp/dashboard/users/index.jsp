<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>

<layout:dashboard>
    <!-- page content -->
    <div class="right_col" role="main">
        <div class="">
            <div class="page-title">
                <div class="title_left">
                    <h3>Registered Users</h3>
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

            <div class="row">
                <div class="col-md-12">
                    <div class="x_panel">
                        <div class="x_content">
                            <div class="row">
                                <c:forEach items="${users}" var="userItem">
                                    <div class="col-md-6 col-sm-6 col-xs-12 profile_details">
                                        <div class="well profile_view">
                                            <div class="col-sm-12">
                                                <div class="left col-xs-7">
                                                    <h2>${userItem.getName()}</h2>
                                                    <p><strong>Role:</strong> ${userItem.getRoleName()} </p>
                                                    <ul class="list-unstyled">
                                                        <li><strong>Balance:</strong> ${userItem.getBalance()} <i
                                                                class="fa fa-eur"></i></li>
                                                        <li><strong>E-mail:</strong> ${userItem.getEmail()}</li>
                                                    </ul>
                                                </div>
                                                <div class="right col-xs-5 text-center">
                                                    <img src="${pageContext.request.contextPath}/assets/dashboard/images/img.jpg"
                                                         alt="" class="img-circle img-responsive">
                                                </div>
                                            </div>
                                            <div class="col-xs-12 bottom text-center">
                                                <div class="col-xs-12 col-sm-6 emphasis"
                                                     style="text-align: left; padding-top: 4px;">
                                                    <i class="fa fa-sign-in"></i> <strong>Registration
                                                    date:</strong> ${userItem.getRegistrationDate("yyyy/MM/dd")}
                                                </div>
                                                <div class="col-xs-12 col-sm-6 emphasis">
                                                    <button type="button" class="btn btn-success btn-xs"><i
                                                            class="fa fa-user">
                                                    </i> <i class="fa fa-comments-o"></i></button>
                                                    <button type="button" class="btn btn-primary btn-xs">
                                                        <i class="fa fa-user"> </i> View Profile
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /page content -->

</layout:dashboard>