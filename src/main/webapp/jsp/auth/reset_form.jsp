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
                    <div class="panel-heading"><fmt:message key="block.password.reset.title"/></div>
                    <div class="panel-body">
                        <form class="form-horizontal" role="form" method="POST" action="/password/reset">
                            <input type="hidden" name="token" value="${token}">
                            <div class="form-group">
                                <div class="col-md-12">
                                    <label for="password"><fmt:message key="form.profile.new_password"/></label>
                                    <input type="password" name="password" id="password" class="form-control" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-12">
                                    <label for="confirmation"><fmt:message key="form.password.confirmation"/></label>
                                    <input type="password" name="confirmation" id="confirmation" class="form-control"
                                           required>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary save-button"><fmt:message
                                    key="button.password.change"/></button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</layout:app>