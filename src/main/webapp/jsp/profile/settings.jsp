<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>

<layout:account>
    <div class="panel panel-default profile-settings">
        <div class="panel-heading">
            <h3 class="panel-title">Основные настройки</h3>
        </div>
        <div class="panel-body">
            <form action="/profile/settings/update" method="post">
                <div class="form-group">
                    <label for="user-name">Имя</label>
                    <input type="text" id="user-name" placeholder="Ваше имя" name="user-name"
                           value="${user.getName() != null ? user.getName() : ''}" class="form-control">
                </div>
                <div class="form-group">
                    <label for="user-email">Email</label>
                    <input type="text" id="user-email" value="${user.getEmail()}" class="form-control" disabled>
                </div>
                <div class="form-group">
                    <label for="user-role">Группа</label>
                    <input type="text" id="user-role" value="${user.getRoleName()}" class="form-control" disabled>
                </div>
                <div class="form-group">
                    <label for="user-registration-date">Дата регистрации</label>
                    <input type="text" id="user-registration-date"
                           value="${f:formatDate("yyyy-MM-dd HH:mm",user.getRegistrationDate())}" class="form-control"
                           disabled>
                </div>
                <button type="submit" class="btn btn-primary save-button">Сохранить</button>
            </form>
        </div>
    </div>
    <div class="panel panel-default profile-security">
        <div class="panel-heading">
            <h3 class="panel-title">Безопасность</h3>
        </div>
        <div class="panel-body">
            <form action="/profile/security/update" method="post">
                <div class="form-group">
                    <label for="password">Новый пароль</label>
                    <input type="password" name="password" id="password" class="form-control">
                </div>
                <div class="form-group">
                    <label for="password-confirmation">Подтвердите пароль</label>
                    <input type="password" name="password-confirmation" id="password-confirmation" class="form-control">
                </div>
                <button type="submit" class="btn btn-primary save-button">Изменить пароль</button>
            </form>
        </div>
    </div>
</layout:account>