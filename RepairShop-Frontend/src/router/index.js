import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import BookAppointment from '@/components/BookAppointment'
import Login from '@/components/Login'
import Register from '@/components/Register'
import AddService from '@/components/AddService'
import ViewServices from '@/components/ViewServices'
import AddHoliday from '@/components/AddHoliday'
import Logout from '@/components/Logout'
import ViewHolidays from '@/components/ViewHolidays'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    }, {
      path: '/',
      name: "Home",
      component: Hello
    }, {
      path: '/book',
      name: 'Book Appointment',
      component: BookAppointment
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
    }, {
      path: '/logout',
      name: 'Logout',
      component : Logout
    }, {
      path: '/addHoliday',
      name: 'Add Holiday',
      component : AddHoliday
    }, {
      path: '/viewHolidays',
      name: 'View Holidays',
      component : ViewHolidays
    }
  ]
})
