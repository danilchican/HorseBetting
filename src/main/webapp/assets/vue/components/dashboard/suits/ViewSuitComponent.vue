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
    export default {
        props: ['suit'],

        methods: {
            editSuit(suit) {
                this.$emit('suitEdited', suit);
            },

            /**
             * Remove suit from DB.
             *
             * @param suit
             */
            removeSuit(suit) {
                var vm = this;

                $.post('/ajax/dashboard/suits/remove', {id: suit.id})
                        .done(function (data) {
                            if (data.success === true) {
                                var messages = data.messages;

                                $.each(messages, function (key, value) {
                                    toastr.success(value, 'Success')
                                });
                            } else {
                                toastr.error('Что-то пошло не так...', 'Error')
                            }

                            vm.$emit('suitRemoved');
                        })
                        .fail(function (data, statusText, xhr) {
                            // error callback
                            var errors = data;
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