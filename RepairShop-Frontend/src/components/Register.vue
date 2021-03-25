<template>
  <div>
    <div id="titleContainer">
      <h1>
        Register
      </h1>
    </div>
    <div id="registerForm">
      <b-form @submit="onSubmit" v-if="show">
        <b-form-group id="input-group-0" label="Full Name:" label-for="input-0">
          <b-form-input
            id="input-0"
            v-model="form.name"
            type="text"
            placeholder="Enter Full Name"
            required
          ></b-form-input>
        </b-form-group>

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

        <!-- render this block if user logged in as admin -->
        <b-form-group
          id="input-group-3"
          label="User Type:"
          label-for="input-3"
          v-if="this.$root.$data.userType === 'Admin'"
        >
          <b-form-select
            id="input-3"
            v-model="form.userType"
            :options="userType"
            required
          ></b-form-select>
        </b-form-group>

        <!-- render this block if user is not admin -->
        <b-form-group
          id="input-group-3.1"
          label="User Type:"
          label-for="input-3.1"
          v-else
        >
          <b-form-select
            id="input-3.1"
            v-model="form.userType"
            :options="userType2"
            required
          ></b-form-select>
        </b-form-group>

        <b-form-group
          id="input-group-4"
          label="Phone Number:"
          label-for="input-4"
          description="Format xxx-xxx-xxxx"
        >
          <b-form-input
            id="input-4"
            v-model="form.phoneNumber"
            type="tel"
            pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}"
            placeholder="Enter Phone Number"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group id="input-group-5" label="Address:" label-for="input-5">
          <b-form-input
            id="input-5"
            v-model="form.address"
            type="text"
            placeholder="Enter Address"
            required
          ></b-form-input>
        </b-form-group>
        <b-button type="submit" variant="primary">Submit</b-button>
      </b-form>
    </div>
  </div>
</template>

<script>
import {
  REGISTER_CUSTOMER_ENDPOINT,
  REGISTER_ADMIN_ENDPOINT,
  REGISTER_TECHNICIAN_ENDPOINT,
  LOCALHOST_BACKEND
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
      userType: [
        { text: "Select One", value: null },
        "Admin",
        "Customer",
        "Technician"
      ],
      userType2: [{ text: "Select One", value: null }, "Customer"],
      show: true
    };
  },
  methods: {
    onSubmit(event) {
      event.preventDefault();
      let user_url = "";
      //determine which login endpoint to call based on usertype
      switch (this.form.userType) {
        case "Admin":
          user_url = REGISTER_ADMIN_ENDPOINT;
          break;
        case "Customer":
          user_url = REGISTER_CUSTOMER_ENDPOINT;
          break;
        case "Technician":
          user_url = REGISTER_TECHNICIAN_ENDPOINT;
          break;
        default:
          console.error("Invalid usertype");
          return;
      }
      axios
        .post(
          LOCALHOST_BACKEND + user_url,
          {
            email: this.form.email,
            password: this.form.password,
            userType: this.form.userType,
            name: this.form.name,
            phoneNumber: this.form.phoneNumber,
            address: this.form.address
          },
          {
            headers: {
              token: this.$root.$data.token
            }
          }
        )
        .then(
          response => {
            console.log(response);
            alert(
              "Account created for " +
                response.data.name +
                " with email " +
                response.data.email +
                ".\nConfirmation email sent.\nProceed to login."
            );
            console.log(this.loading);
          },
          error => {
            console.log(error);
            if (error.response) {
              if (error.response.status === 400) {
                alert("Email already taken.");
              }
            }
          }
        );
    }
  }
};
</script>

<style>
#registerForm {
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
