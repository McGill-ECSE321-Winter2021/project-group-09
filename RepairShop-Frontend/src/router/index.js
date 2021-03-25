import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Login from '@/components/Login'
import Register from '@/components/Register'
import TechnicianSchedule from '@/components/TechnicianSchedule'
import TechnicianAppointments from '@/components/TechnicianAppointments'
import ModifyBusinessInfo from '@/components/ModifyBusinessInfo'
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
      path: '/technician_schedule',
      name: 'TechnicianSchedule',
      component : TechnicianSchedule
    }, {
      path: '/technician_appointments',
      name: 'TechnicianAppointments',
      component : TechnicianAppointments
    }, {
      path: '/modify_business_info',
      name: 'ModifyBusinessInfo',
      component : ModifyBusinessInfo
    }



  ]
})
