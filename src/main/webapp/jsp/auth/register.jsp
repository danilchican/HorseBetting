<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:app>
    <div class="container">
        <jsp:include page="${pageContext.request.contextPath}/jsp/partials/messages.jsp"/>
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <div class="panel panel-default">
                    <div class="panel-heading"><fmt:message key="block.register.title"/></div>
                    <div class="panel-body">
                        <form class="form-horizontal" role="form" method="POST" action="/auth/register">
                            <div class="form-group">
                                <label for="user-name" class="col-md-4 control-label"><fmt:message
                                        key="form.profile.name"/>
                                    *</label>
                                <div class="col-md-6">
                                    <input id="user-name" type="text" class="form-control"
                                           value="<ctg:oldInputFormAttribute name="user-name" />" name="user-name" required
                                           autofocus>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="email" class="col-md-4 control-label"><fmt:message key="form.email"/>
                                    *</label>
                                <div class="col-md-6">
                                    <input id="email" type="email" class="form-control" name="email"
                                           value="<ctg:oldInputFormAttribute name="email" />" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="password" class="col-md-4 control-label"><fmt:message key="form.password"/>
                                    *</label>
                                <div class="col-md-6">
                                    <input id="password" type="password" class="form-control" name="password" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="password-confirm" class="col-md-4 control-label"><fmt:message
                                        key="form.password.confirmation"/> *</label>
                                <div class="col-md-6">
                                    <input id="password-confirm" type="password" class="form-control"
                                           name="confirmation" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-md-6 col-md-offset-4">
                                    <button type="submit" class="btn btn-primary"><fmt:message
                                            key="button.register"/></button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</layout:app>