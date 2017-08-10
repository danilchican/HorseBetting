<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>

<layout:account>
    <div class="panel panel-default profile-settings">
        <div class="panel-heading">
            <h3 class="panel-title">Основные настройки</h3>
        </div>
        <div class="panel-body">
            <form action="/pofile/settings/update" method="post">
                <div class="form-group">
                    <label for="user-name">Имя</label>
                    <input type="text" id="user-name" placeholder="Ваше имя"
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

</layout:account>