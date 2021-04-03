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
import CancelAppointmentAdmin from '@/components/CancelAppointmentAdmin'
import ViewDeleteHoliday from '@/components/ViewDeleteHoliday'
import AddHoliday from '@/components/AddHoliday'
import ChangePassword from '@/components/ChangePassword'
import ViewAppointments from '@/components/ViewAppointments'
import Logout from '@/components/Logout'
import ModifyTechHours from '@/components/ModifyTechHours'
import DeleteTechHours from '@/components/DeleteTechHours'
import ContactUs from '@/components/ContactUs'

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
    },{
      path: '/viewDeleteHoliday',
      name: 'View and Delete Holidays',
      component : ViewDeleteHoliday
    }, {
      path: '/addHoliday',
      name: 'Add Holiday',
      component : AddHoliday
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
    }, {
      path: '/modifyHours',
      name: 'Modify Technician Hours',
      component: ModifyTechHours
    }, {
      path: '/deleteHours',
      name: 'Delete Technician Hours',
      component: DeleteTechHours
    },{
      path : '/contactUs',
      name: "Constact Us",
      component : ContactUs
    }, {
      path: '/CancelAppointmentAdmin',
      name: 'CancelAppointmentAdmin',
      component: CancelAppointmentAdmin
    }
  ]
})
