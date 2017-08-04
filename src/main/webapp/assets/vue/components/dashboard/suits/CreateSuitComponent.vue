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
                            <input id="suit-name"
                                   type="text"
                                   class="form-control"
                                   :value="name"
                                   v-model="name"
                                   placeholder="Enter the name"
                                   :disabled="disable">
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

    import axios from 'axios';

    export default {
        data() {
            return {
                name: '',
                disable: false
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
                var vm = this;

                $.post('/ajax/dashboard/suits/create', {name: this.name})
                        .done(function (data) {
                            // success callback
                            console.log("data createSuit:");
                            console.log(data);

                            var savedSuit = data.suit;

                            console.log("savedSuit: ");
                            console.log(savedSuit);

                            if (data.success === true) {
                                var messages = data.messages;

                                $.each(messages, function (key, value) {
                                    toastr.success(value, 'Success')
                                });
                            } else {
                                toastr.error('Что-то пошло не так...', 'Error')
                            }

                            vm.$emit('suitCreated', savedSuit);
                            vm.name = '';

                            vm.unsetDisable();
                        })
                        .fail(function (data, statusText, xhr) {
                            vm.unsetDisable();
                            // error callback
                            var errors = data;
                            $.each(errors, function (key, value) {
                                if (xhr.status === 422) {
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