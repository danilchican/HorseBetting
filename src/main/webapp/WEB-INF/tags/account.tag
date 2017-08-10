<%@ tag description="App layout tag" pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>

<layout:app>
    <div class="container">
        <div class="row">
            <div class="col-md-3">
                <jsp:include page="${pageContext.request.contextPath}/jsp/profile/partials/sidebar.jsp"/>
            </div>
            <div class="col-md-9">
                <div class="row">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <div class="col-md-12">
                                <div class="row">
                                    <jsp:include page="${pageContext.request.contextPath}/jsp/partials/messages.jsp"/>
                                </div>
                            </div>
                            <jsp:doBody/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /container -->
</layout:app>