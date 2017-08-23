<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<div class="col-md-3 left_col">
    <div class="left_col scroll-view">
        <div class="navbar nav_title" style="border: 0;">
            <a href="/dashboard" class="site_title"><i class="fa fa-magnet"></i> <span>HorseBetting</span></a>
        </div>

        <div class="clearfix"></div>
        <!-- menu profile quick info -->
        <div class="profile clearfix">
            <div class="profile_pic">
                <img src="${pageContext.request.contextPath}/assets/dashboard/images/img.jpg" alt="..."
                     class="img-circle profile_img">
            </div>
            <div class="profile_info">
                <span><fmt:message key="dashboard.message.welcome"/>,</span>
                <h2>${user.getName()}</h2>
            </div>
        </div>
        <!-- /menu profile quick info -->
        <br/>

        <!-- sidebar menu -->
        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
            <div class="menu_section">
                <ul class="nav side-menu">
                    <li><a href="/dashboard"><i class="fa fa-home"></i> <fmt:message key="dashboard.menu.home"/></a>
                    </li>
                    <li><a href="/dashboard/users"><i class="fa fa-user"></i> <fmt:message key="dashboard.menu.users"/></a>
                    </li>
                    <li><a><i class="fa fa-desktop"></i> <fmt:message key="dashboard.menu.horses"/> <span
                            class="fa fa-chevron-down"></span></a>
                        <ul class="nav child_menu">
                            <li><a href="/dashboard/suits"><fmt:message key="dashboard.menu.horses.suits"/></a></li>
                            <li><a href="/dashboard/horses/create"><fmt:message key="dashboard.menu.horses.create"/></a>
                            </li>
                            <li><a href="/dashboard/horses"><fmt:message key="dashboard.menu.horses.index"/></a></li>
                        </ul>
                    </li>
                    <li><a><i class="fa fa-table"></i> <fmt:message key="dashboard.menu.races"/> <span
                            class="fa fa-chevron-down"></span></a>
                        <ul class="nav child_menu">
                            <li><a href="/dashboard/races/create"><fmt:message key="dashboard.menu.races.create"/></a>
                            </li>
                            <li><a href="/dashboard/races"><fmt:message key="dashboard.menu.races.index"/></a></li>
                        </ul>
                    </li>
                    <li><a href="/dashboard/bets"><i class="fa fa-bar-chart-o"></i> <fmt:message
                            key="dashboard.menu.bets"/></a></li>
                </ul>
            </div>
        </div>
        <!-- /sidebar menu -->
    </div>
</div>
