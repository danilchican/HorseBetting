<template>
    <tr>
        <td>{{ suit.id }}.</td>
        <td>{{ suit.name }}</td>
        <td>
            <div class="btn-group">
                <button type="button" @click="editSuit(suit)" class="btn btn-info btn-xs" data-toggle="modal"
                        data-target="#editSuitModal">Edit
                </button>
                <button type="button" @click="removeSuit(suit)" class="btn btn-danger btn-xs">Delete</button>
            </div>
        </td>
    </tr>
</template>

<script>
    var loading_box = '<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>';

    export default {
        props: ['suit'],

        data() {
            return {
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

            editSuit(suit) {
                this.$emit('suitEdited', suit);
            },

            /**
             * Remove suit from DB.
             *
             * @param suit
             */
            removeSuit(suit) {
                if (this.isDisabled())
                    return;

                this.setDisable();
                var vm = this;

                $.post('/ajax/dashboard/suits/remove', {id: suit.id})
                        .done(function (data) {
                            if (data.success === true) {
                                if (data.messages !== undefined) {
                                    $.each(data.messages, function (key, value) {
                                        toastr.success(value, 'Success')
                                    });
                                }

                                vm.$emit('suitRemoved');
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