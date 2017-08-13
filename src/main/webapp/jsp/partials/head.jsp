<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">${pageTitle}</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="${commandName eq 'index::get' ? 'active' : ''}"><a href="/"><fmt:message key="menubar.home"/></a></li>
                <li class="${commandName eq 'races::get' ? 'active' : ''}"><a href="/races"><fmt:message key="menubar.races"/></a></li>
                <li class="${commandName eq 'contact::get' ? 'active' : ''}"><a href="/contact"><fmt:message key="menubar.contact"/></a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <!-- TODO Change to Tag -->
                    <form action="/locale/change" method="get" style="margin: 15px 0 0;">
                        <select name="lang" onchange="this.form.submit()">
                            <option value="en"
                                    <c:if test="${sessionScope.locale.language == 'en'}">selected</c:if>>en
                            </option>
                            <option value="ru"
                                    <c:if test="${sessionScope.locale.language == 'ru'}">selected</c:if>>ru
                            </option>
                        </select>
                    </form>
                </li>
                <c:choose>
                    <c:when test="${sessionScope.authorized != null}">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    ${user.getName()} <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <c:if test="${user.isAdministrator()}">
                                    <li><a href="/dashboard"><fmt:message key="account.adminpanel"/></a></li>
                                </c:if>
                                <li><a href="/profile"><fmt:message key="account.profile"/></a></li>
                                <li>
                                    <a href="#"
                                       onclick="event.preventDefault(); document.getElementById('logout-form').submit();">
                                        <fmt:message key="account.logout"/>
                                    </a>
                                    <form action="/auth/logout" id="logout-form" method="post"
                                          style="display: none;"></form>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="${commandName eq 'auth.login::get' ? 'active' : ''}"><a href="/auth/login"><fmt:message key="menubar.auth.login"/></a></li>
                        <li class="${commandName eq 'auth.register::get' ? 'active' : ''}"><a href="/auth/register"><fmt:message key="menubar.auth.register"/></a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</div>