// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import BootstrapVue from "bootstrap-vue"
import App from './App'
import router from './router'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import './main.css'

Vue.use(BootstrapVue)
Vue.config.productionTip = false

// global state to keep track of login
// bad practice but #movefast
var store = {
  email: null,
  password: null,
  userType: null,
  token: null
}

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App },
  data: store
})
