<template>
  <div>
      <h1 v-bind:style="{ color: this.textColor }">{{ this.titleText }}</h1>

    <div class="formContainer" id="changePassForm">
      <b-form @submit="onSubmit" v-if="show" class="inputWidth">
        <b-form-group
          id="input-group-1"
          label="Please enter your current password:"
          label-for="input-1"
        >
          <b-form-input
            id="input-1"
            v-model="form.currPassword"
            type="password"
            placeholder="Enter current password"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group
          id="input-group-2"
          label="Please enter your new password:"
          label-for="input-2"
        >
          <b-form-input
            id="input-2"
            v-model="form.newPassword"
            placeholder="Enter new password"
            required
            type="password"
          ></b-form-input>
        </b-form-group>

        <b-button type="submit" variant="primary">Submit</b-button>
      </b-form>
    </div>
  </div>
</template>

<script>

import {
  CHANGE_PASS_TECH,
  CHANGE_PASS_ADMIN,
  CHANGE_PASS_CUS,
  LOCALHOST_BACKEND
} from "../constants/constants";
import axios from "axios";
export default {
  data() {
    return {
      textColor: "black",
      form: {
        currPassword: "",
        newPassword: "",
        userType: this.$root.$data.userType
      },
      show: true,
      titleText: "Change Password"
    };
  },
  methods: {
    onSubmit(event) {
      event.preventDefault();
      if (
        this.form.currPassword.localeCompare(this.$root.$data.password) != 0
      ) {
        this.textColor = "red";
        this.titleText = "Current password is not valid.";
        return;
      }
      let apiurl = "";
      switch (this.form.userType) {
        case "Admin":
          apiurl = CHANGE_PASS_ADMIN;
          break;
        case "Customer":
          apiurl = CHANGE_PASS_CUS;
          break;
        case "Technician":
          apiurl = CHANGE_PASS_TECH;
          break;
      }
      axios
        .post(
          LOCALHOST_BACKEND + apiurl + this.$root.$data.email,
          this.form.newPassword,
          {
            headers: {
              token: this.$root.$data.token,
              "Content-Type": "text/plain"
            }
          }
        )
        .then(
          response => {
            // update global state with new pass
            this.$root.$data.password = this.form.newPassword;
            console.log(this.$root.$data);
            this.show = false;
            this.textColor = "green";
            this.titleText = "Password changed successfully.";
          },
          error => {
            this.titleText = "Something went wrong. Please try again.";
            this.textColor = "red";
            console.log(error);
          }
        );
    }
  },
  created: function(){
    if(!this.$root.$data.userType) this.$router.push("/");
  }

};
</script>

<style>
</style>
