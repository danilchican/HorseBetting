<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="customtags" %>

<c:if test="${sessionScope.messages != null}">
    <tags:flashMessages messages="${messages.findAll()}" type="MESSAGE" />
</c:if>
<c:if test="${sessionScope.errors != null}">
    <tags:flashMessages messages="${errors.findAll()}" type="ERROR" />
</c:if>