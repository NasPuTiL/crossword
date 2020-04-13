import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '../views/Home.vue';

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/sign-in',
        name: 'signin',
        component: () => import('../views/Signin.vue')
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('../views/Register.vue')
    },
    {
        path: '/attribute',
        name: 'Attribute',
        component: () => import('../views/AddAttributes.vue')
    }, 
    {
        path: '/panel',
        name: 'Panel',
        component: () => import('../views/AdminPanel.vue')
    }   


];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

export default router;
