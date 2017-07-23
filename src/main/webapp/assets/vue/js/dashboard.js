require('./bootstrap.js');

const Vue = require('vue');
require('vue-resource');

Vue.component('users', require('../components/dashboard/users/UserComponent.vue'));

const app = new Vue({
    el: '#app'
});