<template>
    <div>
        User component
        <view-user></view-user>
    </div>
</template>

<script>
    import ViewUser from './ViewUserComponent.vue'

    export default {
        data() {
            return {
                msg: ''
            }
        },
        components: {
            'view-user': ViewUser
        }
    }
</script>