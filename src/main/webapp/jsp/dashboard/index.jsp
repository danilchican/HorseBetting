<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:dashboard>
    <jsp:attribute name="includeScripts">
        <script>
            $(document).ready(function () {
                function init_chart_doughnut() {
                    if ("undefined" != typeof Chart && (console.log("init_chart_doughnut"), $(".canvasDoughnut").length)) {
                        var a = {
                            type: "doughnut",
                            tooltipFillColor: "rgba(51, 51, 51, 0.55)",
                            data: {
                                labels: [
                                    <c:forEach items="${raceStats}" var="stat" varStatus="loopStatus">
                                    <c:choose>
                                    <c:when test="${stat.key == null}">
                                    "<fmt:message key="races.status.expect"/>"
                                    </c:when>
                                    <c:otherwise>
                                    "<fmt:message key="races.status.${stat.key}"/>"
                                    </c:otherwise>
                                    </c:choose>
                                    <c:if test="${!loopStatus.last}">, </c:if>
                                    </c:forEach>
                                ],
                                datasets: [{
                                    data: [
                                        <c:forEach items="${raceStats}" var="stat" varStatus="loopStatus">
                                        <c:out value="${stat.value}"/>
                                        <c:if test="${!loopStatus.last}">, </c:if>
                                        </c:forEach>
                                    ],
                                    backgroundColor: ["#BDC3C7", "#9B59B6", "#E74C3C"],
                                    hoverBackgroundColor: ["#CFD4D8", "#B370CF", "#E95E4F"]
                                }]
                            },
                            options: {legend: !1, responsive: !1}
                        };
                        $(".canvasDoughnut").each(function () {
                            var b = $(this);
                            new Chart(b, a)
                        })
                    }
                }

                init_chart_doughnut();
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <!-- page content -->
        <div class="right_col" role="main">
            <!-- top tiles -->
            <div class="row top_tiles">
                <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                    <div class="tile-stats">
                        <div class="icon"><i class="fa fa-check-square-o"></i></div>
                        <div class="count">${totalUsers}</div>
                        <h3><fmt:message key="block.total.users"/></h3>
                    </div>
                </div>
                <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                    <div class="tile-stats">
                        <div class="icon"><i class="fa fa-magnet"></i></div>
                        <div class="count">${totalHorses}</div>
                        <h3><fmt:message key="block.total.horses"/></h3>
                    </div>
                </div>
                <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                    <div class="tile-stats">
                        <div class="icon"><i class="fa fa-road"></i></div>
                        <div class="count">${totalRaces}</div>
                        <h3><fmt:message key="block.total.races"/></h3>
                    </div>
                </div>
                <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                    <div class="tile-stats">
                        <div class="icon"><i class="fa fa-signal"></i></div>
                        <div class="count">${totalBets}</div>
                        <h3><fmt:message key="block.total.bets"/></h3>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 col-sm-6 col-xs-12">
                    <div class="x_panel tile fixed_height_320 overflow_hidden">
                        <div class="x_title">
                            <h2><fmt:message key="block.races.statistics"/></h2>
                            <ul class="nav navbar-right panel_toolbox">
                                <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                <li><a class="close-link"><i class="fa fa-close"></i></a></li>
                            </ul>
                            <div class="clearfix"></div>
                        </div>
                        <div class="x_content">
                            <table class="" style="width:100%">
                                <tr>
                                    <th style="width:50%;"></th>
                                    <th>
                                        <div class="col-lg-7 col-md-7 col-sm-7 col-xs-7">
                                            <p class=""><fmt:message key="dashboard.form.races.status"/></p>
                                        </div>
                                        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
                                            <p style="text-align: center;"><fmt:message key="block.total"/></p>
                                        </div>
                                    </th>
                                </tr>
                                <tr>
                                    <td>
                                        <canvas class="canvasDoughnut" height="140" width="140"
                                                style="margin: 15px 10px 10px 0"></canvas>
                                    </td>
                                    <td>
                                        <table class="tile_info">
                                            <c:forEach items="${raceStats}" var="stat">
                                                <tr>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${stat.key == null}">
                                                                <p>
                                                                    <i class="fa fa-square <fmt:message
                                                                        key="races.status.color.expect"/>"></i><fmt:message
                                                                        key="races.status.expect"/>
                                                                </p>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <p><i class="fa fa-square <fmt:message
                                                                        key="races.status.color.${stat.key}"/>"></i><fmt:message
                                                                        key="races.status.${stat.key}"/></p>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${stat.value}</td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- /page content -->
    </jsp:body>
</layout:dashboard>
