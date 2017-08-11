<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:account>
    <h3><fmt:message key="profile.payment.current_balance"/>: ${user.getBalance()}$</h3>

    <div class="panel panel-default profile-settings">
        <div class="panel-heading">
            <h3 class="panel-title"><fmt:message key="block.payment.replenish"/></h3>
        </div>
        <div class="panel-body">
            <form action="/profile/payment" method="post">
                <div class="form-group">
                    <label for="payment-amount"><fmt:message key="form.profile.payment_amount"/></label>
                    <div class="input-group">
                        <span class="input-group-addon">$</span>
                        <input type="number" id="payment-amount" name="payment-amount" step="0.01" min="5" max="5000.00" value="5"
                               class="form-control">
                    </div>
                    <small style="color:#828282;"><i><fmt:message key="profile.payment.min_amount"/>: 5$</i></small>
                </div>
                <button type="submit" class="btn btn-primary save-button"><fmt:message key="button.replenish"/></button>
            </form>
        </div>
    </div>
</layout:account>