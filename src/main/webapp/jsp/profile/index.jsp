<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>

<layout:account>
    <div class="col-md-12">
        <div class="row">
            <img src="/assets/dashboard/images/img.jpg" style="float:left;" alt="Profile photo">
            <ul style="list-style: none; float: left;">
                <li><b>Name:</b> ${user.getName()}</li>
                <li><b>Email:</b> ${user.getEmail()}</li>
                <li><b>Group:</b> ${user.getRoleName()}</li>
                <li><b>Balance:</b> ${user.getBalance()}$</li>
            </ul>
        </div>
        <div class="row">
            <h3>Топ 10 ставок</h3>
            <!-- TODO Create top 10 bets -->
        </div>
    </div>
</layout:account>