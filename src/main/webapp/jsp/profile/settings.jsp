<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:account>
    <div class="panel panel-default profile-settings">
        <div class="panel-heading">
            <h3 class="panel-title"><fmt:message key="block.settings.main.title"/></h3>
        </div>
        <div class="panel-body">
            <form action="/profile/settings/update" method="post">
                <div class="form-group">
                    <label for="user-name"><fmt:message key="form.profile.name"/></label>
                    <input type="text" name="user-name" id="user-name" value="${user.getName() != null ? user.getName() : ''}"
                           class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="email"><fmt:message key="form.email"/></label>
                    <input type="text" id="email" value="${user.getEmail()}" class="form-control" disabled>
                </div>
                <div class="form-group">
                    <label for="user-role"><fmt:message key="form.profile.group"/></label>
                    <input type="text" id="user-role" value="${user.getRoleName()}" class="form-control" disabled>
                </div>
                <div class="form-group">
                    <label for="user-registration-date"><fmt:message key="form.profile.register_date"/></label>
                    <input type="text" id="user-registration-date"
                           value="${f:formatDate("yyyy-MM-dd HH:mm",user.getRegistrationDate())}" class="form-control"
                           disabled>
                </div>
                <button type="submit" class="btn btn-primary save-button"><fmt:message key="button.save"/></button>
            </form>
        </div>
    </div>
    <div class="panel panel-default profile-security">
        <div class="panel-heading">
            <h3 class="panel-title"><fmt:message key="block.settings.security.title"/></h3>
        </div>
        <div class="panel-body">
            <form action="/profile/security/update" method="post">
                <div class="form-group">
                    <label for="password"><fmt:message key="form.profile.new_password"/></label>
                    <input type="password" name="password" id="password" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="confirmation"><fmt:message key="form.password.confirmation"/></label>
                    <input type="password" name="confirmation" id="confirmation" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-primary save-button"><fmt:message
                        key="button.password.change"/></button>
            </form>
        </div>
    </div>
</layout:account>