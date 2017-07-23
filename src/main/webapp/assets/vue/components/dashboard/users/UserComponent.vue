<template>
    <div class="row">
        <view-user v-for="user in list" :user="user"></view-user>
    </div>
</template>

<script>
    import ViewUser from './ViewUserComponent.vue'

    export default {
        data() {
            return {
                list: [],
                step: 10,
                page: 1
            }
        },
        created: function () {
            this.getUsersList();
        },

        methods: {

            /**
             * Get step to retrieve users.
             */
            getStep() {
                return this.step;
            },

            /**
             * Set current page.
             */
            nextPage() {
                return this.page++;
            },

            /**
             * Get users from storage.
             */
            getUsersList() {
                Vue.http.get('/ajax/dashboard/users?page=' + this.nextPage())
                        .then((response) => {
                            this.processRequest(response.body.users);
                        });
            },

            /**
             * Process data for request.
             */
            processRequest(users) {
                console.log(users);

                if(users === undefined) {
                    return;
                }

                for (var i = 0; i < users.length; i++) {
                    this.list.push(users[i]);
                }
            },
        },

        components: {
            'view-user': ViewUser
        }
    }
</script>