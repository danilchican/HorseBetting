<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>

<layout:account>
    <h3>Текущий баланс: ${user.getBalance()}$</h3>

    <div class="panel panel-default profile-settings">
        <div class="panel-heading">
            <h3 class="panel-title">Пополнение баланса</h3>
        </div>
        <div class="panel-body">
            <form action="/profile/payment" method="post">
                <div class="form-group">
                    <label for="payment-amount">Сумма платежа</label>
                    <div class="input-group">
                        <span class="input-group-addon">$</span>
                        <input type="number" id="payment-amount" name="payment-amount" step="0.01" min="5" value="5"
                               class="form-control">
                    </div>
                    <small style="color:#828282;"><i>Минимальная сумма: 5$</i></small>
                </div>
                <button type="submit" class="btn btn-primary save-button">Пополнить</button>
            </form>
        </div>
    </div>
</layout:account>