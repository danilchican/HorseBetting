<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<layout:dashboard>
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
                                <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                            </ul>
                            <div class="clearfix"></div>
                        </div>
                        <div class="x_content">
                            <jsp:include page="${pageContext.request.contextPath}/jsp/partials/messages.jsp"/>
                            <br/>
                            <form id="demo-form2" method="post" action="/dashboard/horses/update" data-parsley-validate
                                  class="form-horizontal form-label-left">

                                <input type="hidden" name="horse-id" value="${horse.getId()}">

                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="horse-name">Name <span
                                            class="required">*</span>
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="text" id="horse-name" value="${horse.getName()}" name="horse-name"
                                               required="required"
                                               class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="horse-age">Age <span
                                            class="required">*</span>
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input id="horse-age" data-parsley-min="1" data-parsley-max="45"
                                               name="horse-age" required="required" data-parsley-type="integer"
                                               type="number" class="form-control col-md-7 col-xs-12"
                                               value="${horse.getAge()}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12">Suit <span
                                            class="required">*</span></label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <select required="required" class="form-control" name="horse-suit">
                                            <c:forEach items="${suits}" var="suit">
                                                <c:choose>
                                                    <c:when test="${suit.getId() == horse.getSuitId()}">
                                                        <option value="${suit.getId()}"
                                                                selected>${suit.getName()}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${suit.getId()}">${suit.getName()}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12">Gender <span
                                            class="required">*</span></label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <div id="gender" class="btn-group" data-toggle="buttons">
                                            <label class="btn btn-default <c:if test="${horse.isMale()}">active</c:if>"
                                                   data-toggle-class="btn-primary"
                                                   data-toggle-passive-class="btn-default">
                                                <input type="radio" required="required" name="gender" value="male"
                                                       <c:if test="${horse.isMale()}">checked</c:if> >
                                                &nbsp; Male &nbsp;
                                            </label>
                                            <label class="btn btn-primary <c:if test="${horse.isFemale()}">active</c:if>"
                                                   data-toggle-class="btn-primary"
                                                   data-toggle-passive-class="btn-default">
                                                <input type="radio" required="required" name="gender" value="female"
                                                       <c:if test="${horse.isFemale()}">checked</c:if> >
                                                Female
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="ln_solid"></div>
                                <div class="form-group">
                                    <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                        <button class="btn btn-primary" type="button">Cancel</button>
                                        <button type="submit" class="btn btn-success">Update</button>
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
</layout:dashboard>