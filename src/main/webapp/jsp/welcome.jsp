<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<layout:app>
    <div class="container">
        <h1>Главная</h1>
        <p>Command: ${commandName}</p>
        <p><ctg:info-time/></p>
    </div>
</layout:app>