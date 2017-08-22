<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="f" uri="http://horsebetting.com/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="/localization/lang"/>

<layout:dashboard>
    <!-- page content -->
    <div class="right_col" role="main">
        <div class="">
            <div class="page-title">
                <div class="title_left">
                    <h3>${pageSubTitle}</h3>
                </div>
            </div>

            <div class="clearfix"></div>
            <jsp:include page="${pageContext.request.contextPath}/jsp/partials/messages.jsp"/>
            <div class="clearfix"></div>

            <div class="row">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="x_panel">
                        <div class="x_content">
                            <div class="col-md-3 col-sm-3 col-xs-12 profile_left">
                                <div class="profile_img">
                                    <div id="crop-avatar">
                                        <img class="img-responsive avatar-view" src="/assets/dashboard/images/img.jpg"
                                             alt="Avatar">
                                    </div>
                                </div>
                                <h3>${viewedUser.getName()}</h3>

                                <ul class="list-unstyled user_data">
                                    <li><i class="fa fa-map-marker user-profile-icon"></i> San Francisco, California,
                                        USA
                                    </li>
                                    <li>
                                        <form action="/dashboard/users/role/update" method="post">
                                            <input type="hidden" name="user-id" value="${viewedUser.getId()}">
                                            <i class="fa fa-briefcase user-profile-icon"></i>
                                            <select name="user-role" id="user-role" onchange="this.form.submit()">
                                                <c:forEach items="${roles}" var="role">
                                                    <option value="${role.getId()}"
                                                            <c:if test="${role.getId() == viewedUser.getRoleId()}">selected</c:if>>
                                                            ${role.getName()}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </form>
                                    </li>

                                    <li class="m-top-xs">
                                        <i class="fa fa-external-link user-profile-icon"></i>
                                        <a href="mailto:${viewedUser.getEmail()}" target="_blank">${viewedUser.getEmail()}</a>
                                    </li>
                                </ul>
                            </div>
                            <div class="col-md-9 col-sm-9 col-xs-12">
                                <div class="" role="tabpanel" data-example-id="togglable-tabs">
                                    <ul id="myTab" class="nav nav-tabs bar_tabs" role="tablist">
                                        <li role="presentation" class="active"><a href="#tab_content1" id="home-tab"
                                                                                  role="tab" data-toggle="tab"
                                                                                  aria-expanded="true">Recent
                                            Activity</a>
                                        </li>
                                        <li role="presentation" class=""><a href="#tab_content2" role="tab"
                                                                            id="profile-tab" data-toggle="tab"
                                                                            aria-expanded="false">Projects Worked on</a>
                                        </li>
                                        <li role="presentation" class=""><a href="#tab_content3" role="tab"
                                                                            id="profile-tab2" data-toggle="tab"
                                                                            aria-expanded="false">Profile</a>
                                        </li>
                                    </ul>
                                    <div id="myTabContent" class="tab-content">
                                        <div role="tabpanel" class="tab-pane fade active in" id="tab_content1"
                                             aria-labelledby="home-tab">

                                            <!-- start recent activity -->
                                            <ul class="messages">
                                                <li>
                                                    <img src="images/img.jpg" class="avatar" alt="Avatar">
                                                    <div class="message_date">
                                                        <h3 class="date text-info">24</h3>
                                                        <p class="month">May</p>
                                                    </div>
                                                    <div class="message_wrapper">
                                                        <h4 class="heading">Desmond Davison</h4>
                                                        <blockquote class="message">Raw denim you probably haven't heard
                                                            of them jean shorts Austin. Nesciunt tofu stumptown aliqua
                                                            butcher retro keffiyeh dreamcatcher synth.
                                                        </blockquote>
                                                        <br/>
                                                        <p class="url">
                                                            <span class="fs1 text-info" aria-hidden="true"
                                                                  data-icon=""></span>
                                                            <a href="#"><i class="fa fa-paperclip"></i> User Acceptance
                                                                Test.doc </a>
                                                        </p>
                                                    </div>
                                                </li>
                                                <li>
                                                    <img src="images/img.jpg" class="avatar" alt="Avatar">
                                                    <div class="message_date">
                                                        <h3 class="date text-error">21</h3>
                                                        <p class="month">May</p>
                                                    </div>
                                                    <div class="message_wrapper">
                                                        <h4 class="heading">Brian Michaels</h4>
                                                        <blockquote class="message">Raw denim you probably haven't heard
                                                            of them jean shorts Austin. Nesciunt tofu stumptown aliqua
                                                            butcher retro keffiyeh dreamcatcher synth.
                                                        </blockquote>
                                                        <br/>
                                                        <p class="url">
                                                            <span class="fs1" aria-hidden="true" data-icon=""></span>
                                                            <a href="#" data-original-title="">Download</a>
                                                        </p>
                                                    </div>
                                                </li>
                                                <li>
                                                    <img src="images/img.jpg" class="avatar" alt="Avatar">
                                                    <div class="message_date">
                                                        <h3 class="date text-info">24</h3>
                                                        <p class="month">May</p>
                                                    </div>
                                                    <div class="message_wrapper">
                                                        <h4 class="heading">Desmond Davison</h4>
                                                        <blockquote class="message">Raw denim you probably haven't heard
                                                            of them jean shorts Austin. Nesciunt tofu stumptown aliqua
                                                            butcher retro keffiyeh dreamcatcher synth.
                                                        </blockquote>
                                                        <br/>
                                                        <p class="url">
                                                            <span class="fs1 text-info" aria-hidden="true"
                                                                  data-icon=""></span>
                                                            <a href="#"><i class="fa fa-paperclip"></i> User Acceptance
                                                                Test.doc </a>
                                                        </p>
                                                    </div>
                                                </li>
                                                <li>
                                                    <img src="images/img.jpg" class="avatar" alt="Avatar">
                                                    <div class="message_date">
                                                        <h3 class="date text-error">21</h3>
                                                        <p class="month">May</p>
                                                    </div>
                                                    <div class="message_wrapper">
                                                        <h4 class="heading">Brian Michaels</h4>
                                                        <blockquote class="message">Raw denim you probably haven't heard
                                                            of them jean shorts Austin. Nesciunt tofu stumptown aliqua
                                                            butcher retro keffiyeh dreamcatcher synth.
                                                        </blockquote>
                                                        <br/>
                                                        <p class="url">
                                                            <span class="fs1" aria-hidden="true" data-icon=""></span>
                                                            <a href="#" data-original-title="">Download</a>
                                                        </p>
                                                    </div>
                                                </li>

                                            </ul>
                                            <!-- end recent activity -->

                                        </div>
                                        <div role="tabpanel" class="tab-pane fade" id="tab_content2"
                                             aria-labelledby="profile-tab">

                                            <!-- start user projects -->
                                            <table class="data table table-striped no-margin">
                                                <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Project Name</th>
                                                    <th>Client Company</th>
                                                    <th class="hidden-phone">Hours Spent</th>
                                                    <th>Contribution</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>1</td>
                                                    <td>New Company Takeover Review</td>
                                                    <td>Deveint Inc</td>
                                                    <td class="hidden-phone">18</td>
                                                    <td class="vertical-align-mid">
                                                        <div class="progress">
                                                            <div class="progress-bar progress-bar-success"
                                                                 data-transitiongoal="35"></div>
                                                        </div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>2</td>
                                                    <td>New Partner Contracts Consultanci</td>
                                                    <td>Deveint Inc</td>
                                                    <td class="hidden-phone">13</td>
                                                    <td class="vertical-align-mid">
                                                        <div class="progress">
                                                            <div class="progress-bar progress-bar-danger"
                                                                 data-transitiongoal="15"></div>
                                                        </div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>3</td>
                                                    <td>Partners and Inverstors report</td>
                                                    <td>Deveint Inc</td>
                                                    <td class="hidden-phone">30</td>
                                                    <td class="vertical-align-mid">
                                                        <div class="progress">
                                                            <div class="progress-bar progress-bar-success"
                                                                 data-transitiongoal="45"></div>
                                                        </div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>4</td>
                                                    <td>New Company Takeover Review</td>
                                                    <td>Deveint Inc</td>
                                                    <td class="hidden-phone">28</td>
                                                    <td class="vertical-align-mid">
                                                        <div class="progress">
                                                            <div class="progress-bar progress-bar-success"
                                                                 data-transitiongoal="75"></div>
                                                        </div>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                            <!-- end user projects -->

                                        </div>
                                        <div role="tabpanel" class="tab-pane fade" id="tab_content3"
                                             aria-labelledby="profile-tab">
                                            <p>xxFood truck fixie locavore, accusamus mcsweeney's marfa nulla
                                                single-origin coffee squid. Exercitation +1 labore velit, blog sartorial
                                                PBR leggings next level wes anderson artisan four loko farm-to-table
                                                craft beer twee. Qui
                                                photo booth letterpress, commodo enim craft beer mlkshk </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /page content -->

</layout:dashboard>