require('./bootstrap.js');

window.Vue = require('vue');
var VueResource = require('vue-resource');

/**
 * Toastr notifier lib
 */
window.toastr = require('toastr');

window.toastr.options = {
    "timeOut": "5000"
}

Vue.use(VueResource);

Vue.component('suits', require('../components/dashboard/suits/SuitComponent.vue'));
Vue.component('race-horses', require('../components/dashboard/races/RaceComponent.vue'));

const app = new Vue({
    el: '#app'
});