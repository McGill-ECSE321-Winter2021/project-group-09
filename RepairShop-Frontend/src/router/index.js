import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import BookAppointment from '@/components/BookAppointment'
import Login from '@/components/Login'
import Register from '@/components/Register'
import TechnicianSchedule from '@/components/TechnicianSchedule'
import TechnicianAppointments from '@/components/TechnicianAppointments'
import ModifyBusinessInfo from '@/components/ModifyBusinessInfo'
import AddService from '@/components/AddService'
import ViewServices from '@/components/ViewServices'
import TechnicianScheduleAdmin from '@/components/TechnicianScheduleAdmin'

import ChangePassword from '@/components/ChangePassword'
import ViewAppointments from "@/components/ViewAppointments"
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
    },{
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
    }, {
      path: '/technician_schedule_admin',
      name: '/technician_schedule_admin',
      component: TechnicianScheduleAdmin
    },{
      path: '/changePass',
      name: "ChangePassword",
      component: ChangePassword
    },{
      path : '/viewAppointments',
      name: "ViewAppointments",
      component : ViewAppointments
    }



  ]
})
