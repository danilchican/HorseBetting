<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<layout:dashboard>
    <!-- page content -->
    <div class="right_col" role="main">
        <div class="">
            <div class="page-title">
                <div class="title_left">
                    <h3>${pageSubTitle}</h3>
                </div>

                <div class="title_right">
                    <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
                        <div class="input-group">
                            <input type="text" class="form-control" placeholder="Search for...">
                            <span class="input-group-btn">
                      <button class="btn btn-default" type="button">Go!</button>
                    </span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="clearfix"></div>

            <div class="row">
                <div class="col-md-12">
                    <div class="x_panel">
                        <div class="x_title">
                            <h2>${pageSubTitle}</h2>
                            <ul class="nav navbar-right panel_toolbox">
                                <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                </li>
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                                       aria-expanded="false"><i class="fa fa-wrench"></i></a>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="#">Settings 1</a>
                                        </li>
                                        <li><a href="#">Settings 2</a>
                                        </li>
                                    </ul>
                                </li>
                                <li><a class="close-link"><i class="fa fa-close"></i></a>
                                </li>
                            </ul>
                            <div class="clearfix"></div>
                        </div>
                        <div class="x_content">
                            <a href="/dashboard/horses/create">
                                <button type="button" class="btn btn-round btn-success">New Horse</button>
                            </a>
                            <!-- start horse list -->
                            <table class="table table-striped projects">
                                <thead>
                                <tr>
                                    <th style="width: 1%">#</th>
                                    <th style="width: 25%">Name</th>
                                    <th>Suit</th>
                                    <th style="width: 4%">Age</th>
                                    <th style="width: 10%">Gender</th>
                                    <th style="width: 20%">Action</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${horses}" var="horse">
                                    <tr>
                                        <td>${horse.getId()}</td>
                                        <td>${horse.getName()}</td>
                                        <td>${horse.getSuitName()}</td>
                                        <td>${horse.getAge()}</td>
                                        <td>${horse.getGender() ? "Male" : "Female"}</td>
                                        <td>
                                            <a href="/dashboard/horses/edit?id=${horse.getId()}"
                                               class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> Edit
                                            </a>
                                            <a href="#"
                                               onclick="event.preventDefault(); document.getElementById('delete-horse-form-${horse.getId()}').submit();"
                                               class="btn btn-danger btn-xs"><i class="fa fa-trash-o"></i>
                                                Delete
                                            </a>
                                            <form action="/dashboard/horses/remove"
                                                  id="delete-horse-form-${horse.getId()}" method="post"
                                                  style="display: none;">
                                                <input type="hidden" name="horse-id" value="${horse.getId()}">
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <c:if test="${totalHorses == 0}">
                                <p>Haven't any horses.</p>
                            </c:if>
                            <!-- end horse list -->
                            <ctg:pagination total="${totalHorses}" limit="${limitHorses}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /page content -->

</layout:dashboard>