import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Login from '@/components/Login'
import Register from '@/components/Register'
import TechnicianSchedule from '@/components/TechnicianSchedule'
import TechnicianAppointments from '@/components/TechnicianAppointments'
import ModifyBusinessInfo from '@/components/ModifyBusinessInfo'
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
      path: '/logout',
      name: 'Logout',
      component : Logout
    }, {
      path: '/technician_schedule',
      name: 'technician_schedule',
      component : TechnicianSchedule
    }, {
      path: '/technician_appointments',
      name: 'technician_appointments',
      component : TechnicianAppointments
    }, {
      path: '/modify_business_info',
      name: 'modify_business_info',
      component : ModifyBusinessInfo
    }



  ]
})
