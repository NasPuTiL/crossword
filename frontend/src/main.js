import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import vuetify from './plugins/vuetify';
import '@babel/polyfill';
import '@mdi/font/css/materialdesignicons.css';
import VueResource from 'vue-resource'
import VueSession from 'vue-session';

Vue.use(VueResource);
Vue.use(VueSession);

Vue.config.productionTip = false;

new Vue({
    router,
    store,
    vuetify,
    render: h => h(App)
}).$mount('#app');
