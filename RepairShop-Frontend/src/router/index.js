import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Login from '@/components/Login'
import Register from '@/components/Register'
import AddService from '@/components/AddService'
import ViewServices from '@/components/ViewServices'

import Logout from '@/components/Logout'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    }, {
      path: '/',
      name: "Hello",
      component: Hello
    }, {
      path: '/register',
      name: 'Register',
      component : Register
    }, {
      path: '/addService',
      name: 'Add Service',
      component : AddService
    }, {
      path: '/viewServices',
      name: 'View Services',
      component : ViewServices
    },{
      path: '/logout',
      name: 'Logout',
      component : Logout
    }
  ]
})
