<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
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
                            </ul>
                            <div class="clearfix"></div>
                        </div>
                        <div class="x_content">
                            <a href="/dashboard/races/create">
                                <button type="button" class="btn btn-round btn-success">New Race</button>
                            </a>
                            <!-- start project list -->
                            <table class="table table-striped projects">
                                <thead>
                                <tr>
                                    <th style="width: 1%">#</th>
                                    <th style="width: 25%">Title</th>
                                    <th>Place</th>
                                    <th style="width: 10%">Finished</th>
                                    <th style="width: 10%">Created at</th>
                                    <th style="width: 20%">Action</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${races}" var="race">
                                    <tr>
                                        <td>${race.getId()}</td>
                                        <td>${race.getTitle()}</td>
                                        <td>${race.getPlace()}</td>
                                        <td>${race.isFinished()}</td>
                                        <td>${f:formatDate("yyyy/mm/dd",race.getCreatedAt())}</td>
                                        <td>
                                            <a href="/dashboard/races/view?id=${race.getId()}"
                                               class="btn btn-primary btn-xs"><i class="fa fa-folder"></i> View
                                            </a>
                                            <a href="/dashboard/races/edit?id=${race.getId()}"
                                               class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> Edit
                                            </a>
                                            <a href="#"
                                               onclick="event.preventDefault(); document.getElementById('delete-race-form-${race.getId()}').submit();"
                                               class="btn btn-danger btn-xs"><i class="fa fa-trash-o"></i>
                                                Delete
                                            </a>
                                            <form action="/dashboard/races/remove"
                                                  id="delete-race-form-${race.getId()}" method="post"
                                                  style="display: none;">
                                                <input type="hidden" name="race-id" value="${race.getId()}">
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <!-- end project list -->
                            <ctg:pagination total="${totalRaces}" limit="${limitRaces}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /page content -->

</layout:dashboard>