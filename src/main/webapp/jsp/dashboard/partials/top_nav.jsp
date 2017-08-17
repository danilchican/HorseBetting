<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>
<c:set var="lang">${(sessionScope.locale.language == 'en') ? 'ru' : 'en'}</c:set>

<!-- top navigation -->
<div class="top_nav">
    <div class="nav_menu">
        <nav>
            <div class="nav toggle">
                <a id="menu_toggle"><i class="fa fa-bars"></i></a>
            </div>
            <ul class="nav navbar-nav navbar-right">
                <li class="">
                    <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown"
                       aria-expanded="false">
                        <img src="/assets/dashboard/images/img.jpg"
                             alt="">${user.getName()}
                        <span class=" fa fa-angle-down"></span>
                    </a>
                    <ul class="dropdown-menu dropdown-usermenu pull-right">
                        <li><a href="/profile"><fmt:message key="account.profile"/></a></li>
                        <li>
                            <a href="/profile/settings"><fmt:message key="account.menu.sidebar.settings"/></a>
                        </li>
                        <li><a href="#"
                               onclick="event.preventDefault(); document.getElementById('logout-form').submit();">
                            <i class="fa fa-sign-out pull-right"></i> <fmt:message key="account.logout"/></a>
                            <form action="/auth/logout" id="logout-form" method="post"
                                  style="display: none;"></form>
                        </li>
                    </ul>
                </li>
                <li>
                    <a href="/locale/change?lang=${lang}">${sessionScope.locale.language}</a>
                </li>
            </ul>
        </nav>
    </div>
</div>
<!-- /top navigation -->
