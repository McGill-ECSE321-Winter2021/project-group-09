import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Login from '@/components/Login'
import Register from '@/components/Register'
<<<<<<< HEAD
import TechnicianSchedule from '@/components/TechnicianSchedule'
import TechnicianAppointments from '@/components/TechnicianAppointments'
import ModifyBusinessInfo from '@/components/ModifyBusinessInfo'
=======
import Logout from '@/components/Logout'
>>>>>>> 05c0418cd64f347fc9816c5a19c861d5c505b93e
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
    }, {
      path: '/logout',
      name: 'Logout',
      component : Logout
    }



  ]
})
