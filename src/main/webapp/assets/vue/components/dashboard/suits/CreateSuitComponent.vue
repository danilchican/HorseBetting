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

    export default {
        data() {
            return {
                name: '',
                disable: false
            }
        },

        methods: {

            setDisable() {
                $('input').attr('disabled', 'disabled');
                this.disable = true;
                $('#box-table-suits').find('.x_panel').append(loading_box);
            },

            unsetDisable() {
                $('input').attr('disabled', false);
                this.disable = false;
                $('#box-table-suits').find('.overlay').remove();
            },

            /**
             * Check if the request sent.
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
                                if (data.messages !== undefined) {
                                    $.each(data.messages, function (key, value) {
                                        toastr.success(value, 'Success')
                                    });
                                }

                                vm.name = '';
                                vm.$emit('suitCreated', savedSuit);
                            } else {
                                if (data.errors !== undefined) {
                                    // error callback
                                    $.each(data.errors, function (key, value) {
                                        toastr.error(value, 'Error')
                                    });
                                } else {
                                    toastr.error('Что-то пошло не так...', 'Error')
                                }
                            }

                            vm.unsetDisable();
                        })
                        .fail(function (data) {
                            vm.unsetDisable();
                            // error callback

                            if (data.errors !== undefined) {
                                $.each(data.errors, function (key, value) {
                                    toastr.error(value, 'Error')
                                });
                            }
                        });
            }
        }
    }
</script>