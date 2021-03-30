import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import BookAppointment from '@/components/BookAppointment'
import Login from '@/components/Login'
import Register from '@/components/Register'
import AddService from '@/components/AddService'
import ViewServices from '@/components/ViewServices'
import ChangePassword from '@/components/ChangePassword'
import ViewAppointments from '@/components/ViewAppointments'
import Logout from '@/components/Logout'
import ModifyTechHours from '@/components/ModifyTechHours'
import DeleteTechHours from '@/components/DeleteTechHours'

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
    }
  ]
})
