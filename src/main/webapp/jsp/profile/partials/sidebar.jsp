<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="list-group">
    <a href="/profile" class="list-group-item ${commandName eq 'profile::get' ? 'active' : ''}">Основная информация</a>
    <a href="/profile/payment" class="list-group-item ${commandName eq 'profile.payment::get' ? 'active' : ''}">Пополнить баланс</a>
    <a href="/profile/bets" class="list-group-item ${commandName eq 'profile.bets::get' ? 'active' : ''}">Ставки</a>
    <a href="/profile/comments" class="list-group-item ${commandName eq 'profile.comments::get' ? 'active' : ''}"><span class="badge pull-right">0</span> Комментарии</a>
    <a href="/profile/settings" class="list-group-item ${commandName eq 'profile.settings::get' ? 'active' : ''}">Настройки</a>
</div>