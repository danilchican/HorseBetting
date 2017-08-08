<%@tag description="Dashboard layout tag" pageEncoding="UTF-8" %>
<%@ attribute name="includeStyles" required="false"  fragment="true" %>
<%@ attribute name="includeScripts"  required="false"  fragment="true" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="${pageContext.request.contextPath}/jsp/dashboard/partials/head.jsp"/>
    <jsp:invoke fragment="includeStyles" />
</head>

<body class="nav-md">
<div class="container body">
    <div class="main_container" id="app">

        <jsp:include page="${pageContext.request.contextPath}/jsp/dashboard/partials/sidebar.jsp"/>
        <jsp:include page="${pageContext.request.contextPath}/jsp/dashboard/partials/top_nav.jsp"/>
        <jsp:doBody/>

        <!-- footer content -->
        <footer>
            <div class="pull-right">HorseBetting.com</div>
            <div class="clearfix"></div>
        </footer>
        <!-- /footer content -->
    </div>
</div>

<jsp:include page="${pageContext.request.contextPath}/jsp/dashboard/partials/footer.jsp"/>
<jsp:invoke fragment="includeScripts" />

</body>
</html>