<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:dashboard>
    <jsp:attribute name="includeStyles">
        <!-- bootstrap-datetimepicker -->
        <link href="${pageContext.request.contextPath}/assets/dashboard/bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.css"
              rel="stylesheet">
        <style>
            #race-horses {
                overflow: hidden;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="includeScripts">
        <!-- Parsley -->
        <script src="${pageContext.request.contextPath}/assets/dashboard/parsleyjs/dist/parsley.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/dashboard/parsleyjs/dist/i18n/${locale.getLanguage()}.js"></script>
        <!-- bootstrap-datetimepicker -->
        <script src="${pageContext.request.contextPath}/assets/dashboard/bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js"></script>

        <script>
            $('#betEndDatePricker').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss'
            });

            $('#betStartedAtPricker').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss'
            });

            $('#betCreatedAtPricker').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss'
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <!-- page content -->
        <div class="right_col" role="main">
            <div class="">
                <div class="page-title">
                    <div class="title_left">
                        <h3>${pageSubTitle}</h3>
                    </div>
                </div>
                <div class="clearfix"></div>
                <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                        <div class="x_panel">
                            <div class="x_title">
                                <h2>${pageSubTitle}</h2>
                                <ul class="nav navbar-right panel_toolbox">
                                    <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                </ul>
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content">
                                <jsp:include page="${pageContext.request.contextPath}/jsp/partials/messages.jsp"/>
                                <br/>
                                <form id="demo-form2" method="post" action="/dashboard/races/edit"
                                      data-parsley-validate
                                      class="form-horizontal form-label-left">

                                    <input type="hidden" name="race-id" value="${race.getId()}">

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="race-title">
                                            <fmt:message key="dashboard.form.races.title"/>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input type="text" id="race-title"
                                                   value="${race.getTitle()}"
                                                   name="race-title"
                                                   class="form-control col-md-7 col-xs-12" disabled>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="race-place">
                                            <fmt:message key="dashboard.form.races.place"/>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input id="race-place"
                                                   value="${race.getPlace()}"
                                                   name="race-place" type="text"
                                                   class="form-control col-md-7 col-xs-12" disabled>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="race-min-rate">
                                            <fmt:message key="dashboard.form.races.min_rate"/>($)
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input id="race-min-rate"
                                                   value="${race.getMinRate()}"
                                                   name="race-min-rate" type="number"
                                                   class="form-control col-md-7 col-xs-12" disabled>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                               for="race-track-length">
                                            <fmt:message key="dashboard.form.races.track_length"/>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input id="race-track-length"
                                                   value="${race.getTrackLength()}"
                                                   name="race-track-length"
                                                   type="number" class="form-control col-md-7 col-xs-12" disabled>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                               for="race-bet-end-date">
                                            <fmt:message key="dashboard.form.races.bet_end_date"/>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <div class="input-group date" id="betEndDatePricker">
                                                <input id="race-bet-end-date"
                                                       value="${race.getBetEndDate()}"
                                                       name="race-bet-end-date" type="text"
                                                       class="form-control col-md-7 col-xs-12" disabled>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-calendar"></span>
                                                </span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                               for="race-started-at">
                                            <fmt:message key="dashboard.form.races.started_at"/>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <div class="input-group date" id="betStartedAtPricker">
                                                <input id="race-started-at"
                                                       value="${race.getStartedAt()}"
                                                       name="race-started-at" type="text"
                                                       class="form-control col-md-7 col-xs-12" disabled>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-calendar"></span>
                                                </span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                               for="race-started-at">
                                            <fmt:message key="dashboard.form.races.created_at"/>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <div class="input-group date" id="betCreatedAtPricker">
                                                <input id="race-created-at"
                                                       value="${race.getCreatedAt()}"
                                                       name="race-created-at" type="text"
                                                       class="form-control col-md-7 col-xs-12" disabled>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-calendar"></span>
                                                </span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="race-status" class="control-label col-md-3 col-sm-3 col-xs-12">
                                            <fmt:message key="dashboard.form.races.status"/>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <select id="race-status" class="form-control"
                                                    name="race-status"
                                                    <c:if test="${!race.isAvailable()}">disabled</c:if>>
                                                <c:set var="oldSuitId"><ctg:oldInputFormAttribute
                                                        name="race-status"/></c:set>
                                                <option
                                                        <c:if test="${!race.isAvailable()}">selected</c:if> ></option>
                                                <option value="failed"
                                                        <c:if test="${race.getStatus() eq 'failed'}">selected</c:if>>
                                                    <fmt:message key="races.status.failed"/></option>
                                                <option value="completed"
                                                        <c:if test="${race.getStatus() eq 'completed'}">selected</c:if>>
                                                    <fmt:message key="races.status.completed"/></option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="race-status" class="control-label col-md-3 col-sm-3 col-xs-12">
                                            <fmt:message key="form.participants.is_winner"/>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <select id="race-winner" class="form-control"
                                                    name="race-winner"
                                                    <c:if test="${!race.isAvailable()}">disabled</c:if>>
                                                <option></option>
                                                <c:forEach items="${participants}" var="participant">
                                                    <option value="${participant.getId()}">
                                                            ${participant.getJockeyName()}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="ln_solid"></div>
                                    <div id="race-horses">
                                        <c:forEach items="${participants}" var="participant" varStatus="loop">
                                            <div class="race-horse-item col-md-12 col-sm-6 col-xs-12">
                                                <div class="form-group">
                                                    <label class="control-label col-md-3 col-sm-3 col-xs-12">
                                                        <fmt:message key="form.participants.jockey"/> ${loop.index + 1}
                                                    </label>
                                                    <input type="hidden" name="selected-horses"
                                                           value="${participant.getId()}"
                                                           <c:if test="${!race.isAvailable()}">disabled</c:if>>
                                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                                        <input type="text" class="form-control"
                                                               value="${participant.getJockeyName()}" disabled>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="control-label col-md-3 col-sm-3 col-xs-12">
                                                        <fmt:message key="form.participants.coefficient"/>
                                                        <c:if test="${race.isAvailable()}"><span
                                                                class="required">*</span></c:if>
                                                    </label>
                                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                                        <input data-parsley-min="1"
                                                               <c:if test="${!race.isAvailable()}">disabled</c:if>
                                                               value="${participant.getCoefficient()}"
                                                               name="horse-coefficients" step="0.01" type="number"
                                                               class="form-control col-md-7 col-xs-12">
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>

                                    <div class="ln_solid"></div>

                                    <div class="form-group">
                                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                            <button type="submit"
                                                    class="btn btn-success"
                                                    <c:if test="${!race.isAvailable()}">disabled</c:if>>
                                                <fmt:message key="button.update"/></button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- /page content -->
    </jsp:body>
</layout:dashboard>