<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>

<layout:dashboard>
    <jsp:attribute name="includeStyles">
        <!-- Toastr -->
        <link href="${pageContext.request.contextPath}/assets/dashboard/toastr/toastr.min.css" rel="stylesheet">
    </jsp:attribute>
    <jsp:body>
        <!-- page content -->
        <div class="right_col" role="main">
            <div class="">
                <div class="page-title">
                    <div class="title_left">
                        <h3>${pageSubTitle}</h3>
                    </div>
                </div>

                <div class="clearfix"></div>

                <div class="row">
                    <suits title-page="${pageSubTitle}"></suits>
                </div>
            </div>
        </div>
        <!-- /page content -->
    </jsp:body>
</layout:dashboard>