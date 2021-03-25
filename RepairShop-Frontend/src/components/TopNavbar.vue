<template>
  <b-navbar fixed="top" :sticky="true" type="dark" variant="info">
    <!-- Title button -->
    <b-navbar-brand to="/">Repair Shop</b-navbar-brand>
    <!-- Left aligned buttons -->
    <b-navbar-nav v-show="!this.$root.$data.email">
      <b-nav-item to="Login">Log In</b-nav-item>
      <b-nav-item to="Register">Register</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav v-show="this.$root.$data.userType == 'Customer'">
      <b-nav-item to="book">Book Appointment</b-nav-item>
    </b-navbar-nav>

    <b-navbar-nav class="ml-auto" v-show="this.$root.$data.email">
      <b-nav-item disabled>Logged in as: {{ this.$root.$data.email }}</b-nav-item>
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
