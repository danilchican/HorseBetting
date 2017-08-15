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
                            <div class="form-group">
                                <label for="email" class="col-md-4 control-label"><fmt:message
                                        key="form.email"/></label>
                                <div class="col-md-6">
                                    <input id="email" type="email" class="form-control" name="email"
                                           value="<ctg:oldInputFormAttribute name="email" />" required autofocus>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-8 col-md-offset-4">
                                    <button type="submit" class="btn btn-primary"><fmt:message
                                            key="button.password.send_password"/></button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</layout:app>