<template>
  <div>
      <h1>Log In</h1>
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
            placeholder="Enter password"
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
          <div v-if="errorLogin" style="color: red">
              {{ errorLogin }}
          </div>

        <b-button type="submit" variant="primary">Submit</b-button>
      </b-form>
    </div>
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
        userType: null,
      },        
      errorLogin:"",

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
            this.errorLogin="Login Success";
            this.$router.push("/");
          },
          error => {
            console.log(error);
            if (error.response) {
              if (error.response.status === 400) {
               this.errorLogin="Invalid Password";
                console.log("HERE password");
              }
              if (error.response.status === 500) {
                this.errorLogin="Email does not exist";
                console.log("HERE email");

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
  margin-top: 2%;
  margin-left: 5%;
  margin-right: 5%;
}
#titleContainer {
  margin-top: 2%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
