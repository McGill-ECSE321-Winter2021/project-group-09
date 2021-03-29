<template>

  <b-navbar fixed="top" :sticky="true" type="dark" variant="info">

    <b-navbar-brand to="/">Repair Shop</b-navbar-brand>

    <b-navbar-nav v-show="!this.$root.$data.email">
      <b-nav-item to="Login">Log In</b-nav-item>
      <b-nav-item to="Register">Register</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav v-show="this.$root.$data.userType == 'Customer'">
      <b-nav-item to="book">Book Appointment</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav v-show="this.$root.$data.userType == 'Admin'">
      <b-nav-item to="AddService">Add Service</b-nav-item>
      <b-nav-item to="AddHoliday">Add Holiday</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav>
      <b-nav-item to="ViewServices">View Services</b-nav-item>
      <b-nav-item to="ViewHolidays">View Holidays</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav class="ml-auto" v-show="this.$root.$data.email">
      <b-nav-item disabled>Logged in as: {{ this.$root.$data.email }}</b-nav-item>
      <b-nav-item to="Logout">Logout</b-nav-item>
    </b-navbar-nav>

  </b-navbar>

</template>


<script>
export default {
  name: "TopNavbar",
};
</script>

<style></style>
