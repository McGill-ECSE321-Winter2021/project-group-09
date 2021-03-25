<template>
  <b-navbar fixed="top" sticky="true" type="dark" variant="info">
    <!-- Title button -->
    <b-navbar-brand to="/">Repair Shop</b-navbar-brand>
    <!-- Left aligned buttons -->
    <b-navbar-nav>
      <b-nav-item to="Login">Log In</b-nav-item>
      <b-nav-item to="Register">Register</b-nav-item>
    </b-navbar-nav>

    <!-- For testing -->
    <b-navbar-nav>
      <b-nav-item to="TechnicianSchedule">Technician Schedule</b-nav-item>
    </b-navbar-nav>
    <b-navbar-nav>
      <b-nav-item to="TechnicianAppointments">Technician Appointments</b-nav-item>
    </b-navbar-nav>
    <b-navbar-nav>
      <b-nav-item to="ModifyBusinessInfo">Modify Business Information</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav class="ml-auto">
      <b-nav-item v-show="this.$root.$data.email" disabled
        >Logged in as: {{ this.$root.$data.email }}</b-nav-item
      >
      <b-nav-item to="Logout">Logout</b-nav-item>
    </b-navbar-nav>
  </b-navbar>
</template>

<script>
export default {
  name: "TopNavbar"
};
</script>

<style></style>
