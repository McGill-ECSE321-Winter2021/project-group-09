import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Login from '@/components/Login'
import Register from '@/components/Register'
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
    }
  ]
})
