<template>
  <div id="loginForm">
    <b-form @submit="onSubmit" v-if="show">
      <b-form-group
        id="input-group-1"
        label="Email address:"
        label-for="input-1"
      >
        <b-form-input
          id="input-1"
          v-model="form.email"
          type="email"
          placeholder="Enter email"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="Password:" label-for="input-2">
        <b-form-input
          id="input-2"
          v-model="form.password"
          placeholder="Enter name"
          required
          type="password"
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-3" label="User Type:" label-for="input-3">
        <b-form-select
          id="input-3"
          v-model="form.userType"
          :options="userType"
          required
        ></b-form-select>
      </b-form-group>
      <b-button type="submit" variant="primary">Submit</b-button>
    </b-form>
  </div>
</template>

<script>
import { LOGIN_ENDPOINT, LOCALHOST_BACKEND } from "../constants/constants";
import axios from "axios";
export default {
  data() {
    return {
      form: {
        email: "",
        password: "",
        userType: null
      },
      userType: [
        { text: "Select One", value: null },
        "Admin",
        "Customer",
        "Technician"
      ],
      show: true
    };
  },
  methods: {
    onSubmit(event) {
      event.preventDefault();
      axios
        .post(LOCALHOST_BACKEND + LOGIN_ENDPOINT, {
          email: this.form.email,
          password: this.form.password,
          userType: this.form.userType
        })
        .then(
          response => {
            // set global state of logged in user
            this.$root.$data.email = this.form.email;
            this.$root.$data.password = this.form.password;
            this.$root.$data.userType = this.form.userType;
            this.$root.$data.token = response.data;
            console.log(this.$root.$data);
            alert("Login Success.");
          },
          error => {
            console.log(error);
            if (error.response) {
              if (error.response.status === 400) {
                alert("Invalid Password.");
              }
              if (error.response.status === 500) {
                alert("Email does not exist.");
              }
            }
          }
        );
    }
  }
};
</script>

<style>
#loginForm {
  margin-top: 8%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>
