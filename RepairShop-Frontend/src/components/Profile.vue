<template>
  <div>
    <h1>User Profile</h1>
    <div class="formContainer" id="registerForm">
      <div class="inputWidth">
        
        <p><b>Name: </b> {{form.name}}</p>
        <p><b>Email: </b> {{form.email}}</p>
        <p><b>User type: </b> {{this.$root.$data.userType}}</p>
        <p><b>Phone number: </b> {{form.phoneNumber}}</p>
        <p><b>Address: </b> {{form.address}}</p>

        <p v-if="this.appError" style="color: red">
          {{ this.appError }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import {
  BACKEND
} from "../constants/constants";
import axios from "axios";
export default {
  data() {
    return {
      form: {
        name: "",
        phoneNumber: "",
        address: "",
        email: "",
        password: "",
        userType: null
      },
      appError: ""
    };
  },
  created: function() {

      // get user info
    axios
      .get(BACKEND + "/api/" + this.$root.$data.userType.toLowerCase() + "/get/" + this.$root.$data.email, {
          headers: { token: this.$root.$data.token }
      })
      .then(r => {
        this.form = r.data;
        this.appError = "";
      })
      .catch(e => {
        this.appError = e;
      });
  }
};
</script>

<style>
</style>
