<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:app>
    <jsp:attribute name="includeScripts">
        <script src="/assets/js/custom.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="container">
            <h1>${race.getTitle()}</h1>
            <div class="col-md-12">
                <p><b><fmt:message key="dashboard.form.races.place"/>:</b> ${race.getPlace()}</p>
                <p><b><fmt:message key="dashboard.form.races.min_rate"/>:</b> ${race.getMinRate()}$</p>
                <p><b><fmt:message key="dashboard.form.races.track_length"/>:</b> ${race.getTrackLength()}</p>
                <p><b><fmt:message
                        key="dashboard.form.races.started_at"/>:</b> ${f:formatDate("yyyy/MM/dd HH:mm",race.getStartedAt())}
                </p>
                <p><b><fmt:message
                        key="dashboard.form.races.bet_end_date"/>:</b> ${f:formatDate("yyyy/MM/dd HH:mm",race.getBetEndDate())}
                </p>
            </div>
            <div class="col-md-7">
                <c:choose>
                    <c:when test="${not empty participants}">
                        <div class="row">
                            <h3 class="sub-header"><fmt:message key="block.participants.title"/>:</h3>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th><fmt:message key="form.participants.jockey"/></th>
                                        <th><fmt:message key="form.participants.coefficient"/></th>
                                        <th><fmt:message key="form.participants.place_bet"/></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${participants}" var="participant">
                                        <tr>
                                            <td>${participant.getId()}</td>
                                            <td>${participant.getJockeyName()}</td>
                                            <td>${participant.getCoefficient()}</td>
                                            <td>
                                                <button type="button"
                                                        data-participant="${participant.getId()}"
                                                        data-coefficient="${participant.getCoefficient()}"
                                                        data-jockey="${participant.getJockeyName()}"
                                                        class="btn btn-default place-bet">
                                                    <fmt:message key="form.participants.place_bet"/>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <h4>Haven't any participant of race</h4>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col-md-5">

            </div>
        </div>
        <!-- /container -->
        <!-- Place Bet Modal -->
        <div class="modal fade" id="placeBetModal" tabindex="-1" role="dialog" aria-labelledby="placeBetModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="placeBetModalLabel"><fmt:message key="block.bets.title"/></h4>
                    </div>
                    <div class="modal-body">
                        <div id="messages"></div>
                        <form>
                            <input type="hidden" name="participant" id="participant">
                            <label for="jockey"><fmt:message key="form.participants.jockey"/></label>
                            <div class="form-group">
                                <input type="text" value="" id="jockey" class="form-control" disabled>
                            </div>

                            <label for="coefficient"><fmt:message key="form.participants.coefficient"/></label>
                            <div class="form-group">
                                <input type="text" value="" id="coefficient" class="form-control" disabled>
                            </div>

                            <label for="bet-amount"><fmt:message key="form.bets.amount"/></label>
                            <div class="input-group">
                                <span class="input-group-addon">$</span>
                                <input type="number" min="${race.getMinRate()}" value="${race.getMinRate()}"
                                       id="bet-amount"
                                       name="amount" class="form-control">
                            </div>
                            <br/>
                            <p><b><fmt:message key="form.bets.estimated_returns"/>:</b> <span
                                    id="estimated-returns"></span></p>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            <fmt:message key="button.cancel"/>
                        </button>
                        <button type="button" class="btn btn-primary" id="place-bet-btn">
                            <fmt:message key="form.participants.place_bet"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</layout:app>