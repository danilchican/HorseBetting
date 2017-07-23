require('./bootstrap.js');

window.Vue = require('vue');
var VueResource = require('vue-resource');

Vue.use(VueResource);

Vue.component('users', require('../components/dashboard/users/UserComponent.vue'));

const app = new Vue({
    el: '#app'
});