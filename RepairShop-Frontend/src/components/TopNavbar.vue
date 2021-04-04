<template>
  <b-navbar fixed="top" :sticky="true" type="dark" variant="info" toggleable="lg">
    <b-navbar-brand to="/">{{name}}</b-navbar-brand>

<b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
<b-collapse id ="nav-collapse" is-nav>
    <b-navbar-nav v-show="!this.$root.$data.email">
      <b-nav-item to="Login">Log In</b-nav-item>
      <b-nav-item to="Register">Register</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav>
      <b-nav-item to="ContactUs">Contact Us</b-nav-item>
      <b-nav-item to="ViewServices">View Services</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav v-show="this.$root.$data.userType === 'Technician'">
      <b-nav-item to="technician_schedule">Schedule</b-nav-item>
      <b-nav-item to="technician_appointments">Appointments</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav v-show="this.$root.$data.userType == 'Admin'">
      <b-nav-item to="AddService">Add Service</b-nav-item>
      <b-nav-item to="Register">Register New User</b-nav-item>
      <b-nav-item to="modify_business_info">Modify Business Information</b-nav-item>

      <b-nav-item-dropdown text="Holidays" right>
        <b-dropdown-item to="AddHoliday">Add</b-dropdown-item>
        <b-dropdown-item to="ViewDeleteHoliday">View & Delete</b-dropdown-item>
      </b-nav-item-dropdown>

      <b-nav-item-dropdown text="Technicians" right>
        <b-dropdown-item to="technician_schedule_admin">Schedules</b-dropdown-item>
        <b-dropdown-item to="cancelAppointmentAdmin">Cancel Appointments</b-dropdown-item>
        <b-dropdown-item to="modifyHours">Add Work Hours</b-dropdown-item>
        <b-dropdown-item to="deleteHours">Delete Work Hours</b-dropdown-item>
      </b-nav-item-dropdown>
    </b-navbar-nav>

    <b-navbar-nav v-show="this.$root.$data.userType == 'Customer'">
      <b-nav-item to="book">Book Appointment</b-nav-item>
      <b-nav-item to="ViewAppointments">Appointments</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav class="ml-auto" v-show="this.$root.$data.email">
      <b-nav-item-dropdown :text="this.$root.$data.email" right>
        <b-dropdown-item to="Profile">Profile</b-dropdown-item>
        <b-dropdown-item to="ChangePass">Change Password</b-dropdown-item>
        <b-dropdown-item to="Logout">Logout</b-dropdown-item>
      </b-nav-item-dropdown>
    </b-navbar-nav>
    
    </b-collapse>
  </b-navbar>
</template>

<script>
import axios from "axios";
import { BACKEND, GET_BUSINESS_ENDPOINT } from "../constants/constants";
var config = require("../../config");
var AXIOS = axios.create({
  baseURL: BACKEND,
});
export default {
   data() {
    return {
      name: "",
      errorGetBusiness: "",
    };
  },name: "TopNavbar",  created: function () {
    AXIOS.get(GET_BUSINESS_ENDPOINT)
      .then((response) => {
        this.name = response.data.name;
      })
      .catch((e) => {
        if (e.response.status == 404) this.errorGetBusiness = e.response.data;
        else this.errorGetBusiness = e;
      });
  },
};
</script>

<style></style>
