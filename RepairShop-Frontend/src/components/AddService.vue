<template>
  <div>
    <h1>Add a Service</h1>
    <div class="formContainer" id="addServiceForm">
      <b-form @submit="onSubmit" class="inputWidth">
        <b-form-group
          id="input-group-1"
          label="Service Name:"
          label-for="input-1"
        >
          <b-form-input
            id="input-1"
            v-model="form.name"
            placeholder="Enter Service Name"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group
          id="input-group-2"
          label="Duration:"
          label-for="input-2"
          description="The integer represents the number of 30 minutes slots"
        >
          <b-form-input
            type="number"
            id="input-2"
            v-model="form.duration"
            placeholder="Enter Integer"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group id="input-group-3" label="Price ($):" label-for="input-3">
          <b-form-input
            type="number"
            step="0.01"
            id="input-3"
            v-model="form.price"
            placeholder="Enter Price"
            required
          ></b-form-input>
        </b-form-group>

        <p v-if="errorAddService" style="color: red">
          Error: {{ errorAddService }}
        </p>
        <p v-if="successAddService" style="color: #04571b">
          {{ successAddService }}
        </p>

        <b-button type="submit" variant="primary">Create Service</b-button>
      </b-form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { BACKEND } from "../constants/constants";
var config = require("../../config");
var AXIOS = axios.create({
  baseURL: BACKEND
});

export default {
  data() {
    return {
      form: {
        name: "",
        duration: "",
        price: ""
      },
      errorAddService: "",
      successAddService: ""
    };
  },  created: function() {
    if (this.$root.$data.userType != "Admin") this.$router.push("/");
  },
  methods: {
    onSubmit(event) {
      event.preventDefault();
      AXIOS.post(
        "api/service/create",
        {
          name: this.form.name,
          duration: this.form.duration,
          price: this.form.price
        },
        {
          headers: {
            token: this.$root.$data.token
          }
        }
      )
        .then(response => {
          this.errorAddService = "";
          this.form.name = "";
          this.form.duration = "";
          this.form.price = "";

          this.successAddService =
            "The new service has been added successfully ✓";
        })
        .catch(e => {
          this.errorAddService = e.response.data;
          this.successAddService = "";
        });
    }
  }
};
</script>

<style></style>
