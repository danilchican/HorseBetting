<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:dashboard>
    <jsp:attribute name="includeStyles">
        <!-- bootstrap-datetimepicker -->
        <link href="${pageContext.request.contextPath}/assets/dashboard/bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.css"
              rel="stylesheet">
    </jsp:attribute>

    <jsp:attribute name="includeScripts">
        <!-- bootstrap-datetimepicker -->
        <script src="${pageContext.request.contextPath}/assets/dashboard/bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js"></script>

        <script>
            $('#betEndDatePricker').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss'
            });

            $('#betStartedAtPricker').datetimepicker({
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
                                <form id="demo-form2" method="post" action="/dashboard/races/create"
                                      data-parsley-validate
                                      class="form-horizontal form-label-left">

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="race-title"><fmt:message key="dashboard.form.races.title"/>
                                            <span class="required">*</span>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input type="text" id="race-title" name="race-title" required="required"
                                                   class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="race-place"><fmt:message key="dashboard.form.races.place"/>
                                            <span
                                                    class="required">*</span>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input id="race-place" name="race-place" required="required" type="text"
                                                   class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="race-min-rate"><fmt:message key="dashboard.form.races.min_rate"/> ($) <span
                                                    class="required">*</span>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input id="race-min-rate" data-parsley-min="1" step="0.01"
                                                   name="race-min-rate" min="0"
                                                   required="required" type="number"
                                                   class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                               for="race-track-length"><fmt:message key="dashboard.form.races.track_length"/> <span
                                                    class="required">*</span>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input id="race-track-length" data-parsley-min="1" name="race-track-length"
                                                   required="required" data-parsley-type="integer"
                                                   type="number" class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                               for="race-bet-end-date"><fmt:message key="dashboard.form.races.bet_end_date"/> <span class="required">*</span>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <div class="input-group date" id="betEndDatePricker">
                                                <input id="race-bet-end-date" name="race-bet-end-date" type="text"
                                                       class="form-control col-md-7 col-xs-12" required="required">
                                                <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="race-started-at"><fmt:message key="dashboard.form.races.started_at"/> <span class="required">*</span>
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <div class="input-group date" id="betStartedAtPricker">
                                                <input id="race-started-at" name="race-started-at" type="text"
                                                       class="form-control col-md-7 col-xs-12" required="required">
                                                <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12"><fmt:message key="dashboard.form.races.is_finished"/> <span
                                                class="required">*</span></label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <div id="race-is-finished" class="btn-group" data-toggle="buttons">
                                                <label class="btn btn-default" data-toggle-class="btn-primary"
                                                       data-toggle-passive-class="btn-default">
                                                    <input type="radio" required="required" name="race-is-finished"
                                                           value="1">
                                                    &nbsp; <fmt:message key="button.yes"/> &nbsp;
                                                </label>
                                                <label class="btn btn-primary" data-toggle-class="btn-primary"
                                                       data-toggle-passive-class="btn-default">
                                                    <input type="radio" required="required" name="race-is-finished"
                                                           value="0">
                                                    <fmt:message key="button.no"/>
                                                </label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="ln_solid"></div>
                                    <race-horses></race-horses>

                                    <div class="ln_solid"></div>
                                    <div class="form-group">
                                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                            <button class="btn btn-primary" type="reset"><fmt:message
                                                    key="button.reset"/></button>
                                            <button type="submit" class="btn btn-success"><fmt:message
                                                    key="button.save"/></button>
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