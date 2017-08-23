<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session" />
<fmt:setBundle basename="/localization/lang"/>

<div class="list-group">
    <a href="/profile" class="list-group-item ${commandName eq 'profile::get' ? 'active' : ''}"><fmt:message key="account.menu.sidebar.main_info"/></a>
    <a href="/profile/payment" class="list-group-item ${commandName eq 'profile.payment::get' ? 'active' : ''}"><fmt:message key="account.menu.sidebar.replenish"/></a>
    <a href="/profile/bets" class="list-group-item ${commandName eq 'profile.bets::get' ? 'active' : ''}"><fmt:message key="account.menu.sidebar.bets"/></a>
    <a href="/profile/settings" class="list-group-item ${commandName eq 'profile.settings::get' ? 'active' : ''}"><fmt:message key="account.menu.sidebar.settings"/></a>
</div>