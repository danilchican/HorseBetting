<%@tag description="App layout tag" pageEncoding="UTF-8" %>
<%@ attribute name="includeStyles" required="false"  fragment="true" %>
<%@ attribute name="includeScripts"  required="false"  fragment="true" %>
<%@ taglib prefix="ctg" uri="customtags" %>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="/assets/images/favicon.ico" type="image/x-icon">
    <title><ctg:pageTitle title="${pageTitle}" subTitle="${pageSubTitle}"/></title>

    <!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/fonts.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">

    <jsp:invoke fragment="includeStyles" />
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
    <jsp:include page="${pageContext.request.contextPath}/jsp/partials/head.jsp"/>
    <jsp:doBody/>
    <jsp:include page="${pageContext.request.contextPath}/jsp/partials/footer.jsp"/>
    <jsp:invoke fragment="includeScripts" />
</body>
</html>
