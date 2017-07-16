<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="customtags" %>


<c:if test="${sessionScope.errors != null}">
    <ctg:pageErrors errors="${sessionScope.errors}"/>
</c:if>
