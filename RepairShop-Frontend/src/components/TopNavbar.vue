<template>
  <b-navbar fixed="top" :sticky="true" type="dark" variant="info">
    <b-navbar-brand to="/">Repair Shop</b-navbar-brand>

    <b-navbar-nav v-show="!this.$root.$data.email">
      <b-nav-item to="Login">Log In</b-nav-item>
      <b-nav-item to="Register">Register</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav>
      <b-nav-item
        to="technician_schedule"
        v-show="this.$root.$data.userType === 'Technician'"
        >Technician Schedule</b-nav-item
      >
      <b-nav-item
        to="technician_appointments"
        v-show="this.$root.$data.userType === 'Technician'"
        >Technician Appointments</b-nav-item
      >
    </b-navbar-nav>

    <b-navbar-nav>
      <b-nav-item
        to="modify_business_info"
        v-show="this.$root.$data.userType === 'Admin'"
        >Modify Business Information</b-nav-item
      >
    </b-navbar-nav>

    <b-navbar-nav>
      <b-nav-item to="technician_schedule_admin" v-show="this.$root.$data.userType === 'Admin'">
        Technician Schedules
      </b-nav-item>
    <b-navbar-nav v-show="this.$root.$data.userType == 'Admin'">
      <b-nav-item to="Register">Register New User</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav v-show="this.$root.$data.userType == 'Customer'">
      <b-nav-item to="book">Book Appointment</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav v-show="this.$root.$data.userType == 'Admin'">
      <b-nav-item to="AddService">Add Service</b-nav-item>
      <b-nav-item to="AddHoliday">Add Holiday</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav v-show="this.$root.$data.email">
      <b-nav-item to="ViewServices">View Services</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav v-show="this.$root.$data.userType == 'Admin'">
      <b-nav-item to="ViewDeleteHoliday">Holidays: View and Delete</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav v-show="this.$root.$data.userType == 'Customer'">
      <b-nav-item to="ViewAppointments">View Your Appointments</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav>
      <b-nav-item to="ContactUs">Contact Us</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav class="ml-auto" v-show="this.$root.$data.email">
      <b-nav-item disabled
        >Logged in as: {{ this.$root.$data.email }}</b-nav-item
      >
      <b-nav-item to="ChangePass">Change Password</b-nav-item>
      <b-nav-item to="Logout">Logout</b-nav-item>
    </b-navbar-nav>
  </b-navbar>
</template>

<script>
export default {
  name: "TopNavbar"
};

//
//
</script>

<style></style>
