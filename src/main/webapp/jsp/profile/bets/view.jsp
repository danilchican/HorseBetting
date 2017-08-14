<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:account>
    <h3><fmt:message key="block.bets.title"/> #${bet.getId()}</h3>
    <div class="col-md-12">
        <p><b><fmt:message key="form.bets.amount"/>:</b> ${bet.getAmount()}$</p>
        <p><b><fmt:message key="form.participants.coefficient"/>:</b> ${participant.getCoefficient()}</p>
        <p>
            <b><fmt:message key="form.bets.estimated_returns"/>:</b> ${bet.getAmount() * participant.getCoefficient()}$
        </p>
        <p>
            <b><fmt:message key="dashboard.form.races.race"/>:</b> <a
                href="/races/view?id=${race.getId()}">${race.getTitle()}</a>
        </p>
        <p><b><fmt:message key="dashboard.form.races.place"/>:</b> ${race.getPlace()}</p>
        <p><b><fmt:message key="form.bets.participant"/>:</b> ${participant.getJockeyName()}</p>
        <p><b><fmt:message key="form.bets.created_at"/>:</b> ${f:formatDate("yyyy/MM/dd HH:mm",bet.getCreatedAt())}</p>
        <!-- TODO if race is finished then display `is_winner` else display `in progress` text -->
    </div>
</layout:account>