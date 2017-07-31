<template>
    <tr>
        <td>{{ suit.id }}.</td>
        <td>{{ suit.title }}</td>
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
                this.$http.delete('/dashboard/suits/' + suit.id).then((data) => {
                    // success callback
                    if (data.body.success === true) {
                        var messages = data.body.messages;

                        $.each(messages, function (key, value) {
                            toastr.success(value, 'Success')
                        });
                    } else {
                        toastr.error('Что-то пошло не так...', 'Error')
                    }

                    this.$emit('suitRemoved');
                }, (data) => {
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