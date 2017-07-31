<template>
    <div class="x_panel">
        <div class="x_title">
            <h2>Add suit</h2>
            <ul class="nav navbar-right panel_toolbox">
                <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form method="POST" @submit.prevent="createSuit()" class="form-horizontal">
                <div class="form-group">
                    <label class="control-label">Name</label>
                    <div class="col-md-12 col-sm-12 col-xs-12">
                        <div class="row">
                            <input id="suit-title"
                                   type="text"
                                   class="form-control"
                                   :value="title"
                                   v-model="title"
                                   placeholder="Enter the name"
                                   :disabled="disable == 1">
                        </div>
                    </div>
                </div>
                <button type="submit" class="btn btn-success save-button">Submit</button>
            </form>
        </div>
    </div>
</template>

<script>

    var loading_box = '<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>';

    export default {
        data() {
            return {
                title: '',
                disable: false,
            }
        },

        methods: {

            setDisable() {
                this.disable = true;
                $('#create-suit').append(loading_box);
            },

            unsetDisable() {
                this.disable = false;
                $('#create-suit').find('.overlay').remove();
            },

            /**
             * Check if the request sended.
             */
            isDisabled() {
                return this.disable;
            },

            /**
             * Create new suit.
             */
            createSuit() {
                if (this.isDisabled())
                    return;

                this.setDisable();

                this.$http.post('/dashboard/suits', {title: this.title})
                        .then((data) => {
                            // success callback
                            var savedSuit = data.body.suit;

                            if (data.body.success === true) {
                                var messages = data.body.messages;

                                $.each(messages, function (key, value) {
                                    toastr.success(value, 'Success')
                                });
                            } else {
                                toastr.error('Что-то пошло не так...', 'Error')
                            }

                            this.$emit('suitCreated', savedSuit);
                            this.title = '';

                            this.unsetDisable();
                        }, (data) => {
                            this.unsetDisable();
                            // error callback
                            var errors = data.body;
                            $.each(errors, function (key, value) {
                                if (data.status === 422) {
                                    toastr.error(value[0], 'Error')
                                } else {
                                    toastr.error(value, 'Error')
                                }
                            });
                        });
            }
        }
    }
</script>