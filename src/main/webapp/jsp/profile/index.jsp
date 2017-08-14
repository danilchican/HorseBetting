<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:account>
    <div class="col-md-12">
        <div class="row">
            <img src="/assets/dashboard/images/img.jpg" style="float:left;" alt="Profile photo">
            <ul style="list-style: none; float: left;">
                <li><b><fmt:message key="form.profile.name"/>:</b> ${user.getName()}</li>
                <li><b><fmt:message key="form.email"/>:</b> ${user.getEmail()}</li>
                <li><b><fmt:message key="form.profile.group"/>:</b> ${user.getRoleName()}</li>
                <li><b><fmt:message key="profile.payment.current_balance"/>:</b> ${user.getBalance()}$</li>
                <li><b><fmt:message key="profile.bets.count"/>:</b> ${totalUserBets}</li>
            </ul>
        </div>
        <div class="row">
            <%--<h3>Топ 10 ставок</h3>--%>
            <!-- TODO Create top 10 bets -->
        </div>
    </div>
</layout:account>