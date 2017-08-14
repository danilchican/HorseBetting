<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:account>
    <h3><fmt:message key="block.profile.bets.title"/></h3>
    <c:choose>
        <c:when test="${not empty bets}">
            <div class="row">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th><fmt:message key="form.bets.participant"/></th>
                            <th><fmt:message key="form.bets.amount"/> ($)</th>
                            <th><fmt:message key="form.bets.created_at"/></th>
                            <th><fmt:message key="dashboard.message.action"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${bets}" var="bet">
                            <tr>
                                <td><a href="/profile/bets/view?id=${bet.getId()}">${bet.getId()}</a></td>
                                <td>${bet.getParticipantName()}</td>
                                <td>${bet.getAmount()}</td>
                                <td>${f:formatDate("yyyy/MM/dd HH:mm",bet.getCreatedAt())}</td>
                                <td>
                                    <a href="/profile/bets/view?id=${bet.getId()}" class="btn btn-success btn-xs">
                                        <fmt:message key="button.view"/>
                                    </a>
                                    <a href="/profile/bets/edit?id=${bet.getId()}" class="btn btn-primary btn-xs">
                                        <fmt:message key="button.edit"/>
                                    </a>
                                    <form action="/profile/bets/remove" method="post" style="display:initial">
                                        <input type="hidden" name="id" value="${bet.getId()}">
                                        <button class="btn btn-danger btn-xs"><fmt:message
                                                key="button.delete"/></button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col-md-12" style="text-align: center">
                <div class="row">
                    <ctg:pagination total="${totalBets}" limit="${limitBets}"/>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <h4>Haven't any bets</h4>
        </c:otherwise>
    </c:choose>
</layout:account>