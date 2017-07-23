<template>
    <div class="col-md-6 col-sm-6 col-xs-12 profile_details">
        <div class="well profile_view">
            <div class="col-sm-12">
                <div class="left col-xs-7">
                    <h2>{{ user.name }}</h2>
                    <p><strong>Role:</strong> {{ user.roleId }} </p>
                    <ul class="list-unstyled">
                        <li><strong>Balance:</strong> {{ user.balance }} <i
                                class="fa fa-eur"></i></li>
                        <li><strong>E-mail:</strong> {{ user.email }}</li>
                    </ul>
                </div>
                <div class="right col-xs-5 text-center">
                    <img src="/assets/dashboard/images/img.jpg"
                         alt="" class="img-circle img-responsive">
                </div>
            </div>
            <div class="col-xs-12 bottom text-center">
                <div class="col-xs-12 col-sm-6 emphasis"
                     style="text-align: left; padding-top: 4px;">
                    <i class="fa fa-sign-in"></i> <strong>Registration
                    date:</strong> {{ user.createdAt }}
                </div>
                <div class="col-xs-12 col-sm-6 emphasis">
                    <button type="button" class="btn btn-success btn-xs"><i
                            class="fa fa-user">
                    </i> <i class="fa fa-comments-o"></i></button>
                    <button type="button" class="btn btn-primary btn-xs">
                        <i class="fa fa-user"> </i> View Profile
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        props: ['user']
    }
</script>