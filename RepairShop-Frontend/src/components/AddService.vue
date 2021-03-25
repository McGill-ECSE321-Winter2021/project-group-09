
<template>
  <div id="addServiceForm">
    <h2>Add New Service</h2>

    <p>
      <span v-if="successAddService" style="color: green">
        {{ successAddService }}
      </span>
    </p>

    <b-form>
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
          id="input-2"
          v-model="form.duration"
          placeholder="Enter Integer"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-3" label="Price ($):" label-for="input-3">
        <b-form-input
          id="input-3"
          v-model="form.price"
          placeholder="Enter Price"
          required
        ></b-form-input>
      </b-form-group>

      <p v-if="errorAddService" style="color: red">
        Error: {{ errorAddService }}
      </p>

      <b-button
        type="submit"
        variant="primary"
        @click="createService(form.name.trim(), form.duration, form.price)"
        >Create Service</b-button
      >
    </b-form>
  </div>
</template>

<script>
import axios from "axios";

var config = require("../../config");
var AXIOS = axios.create({
  //DEVELOPMENT
  baseURL: "http://" + config.dev.backendHost + ":" + config.dev.backendPort,

  //PRODUCTION
  //baseURL: "http://" + config.build.backendHost + ":" + config.build.backendPort,

  headers: {
    "Access-Control-Allow-Origin":
      "http://" + config.dev.host + ":" + config.dev.port, 
  },
});

export default {
  data() {
    return {
      form: {
        name: "",
        duration: "",
        price: "",
      },
      errorAddService: "",
      successAddService: "",
    };
  },
  methods: {

    createService: function (serviceName, serviceDuration, servicePrice) {
      AXIOS.post(
        "api/service/create",
        {
          name: serviceName,
          duration: serviceDuration,
          price: servicePrice,
        },
        {
          headers: {
            token: this.$root.$data.token,
          },
        }
      )
        .then((response) => {

          this.errorAddService = "";
          this.form.name = "";
          this.form.duration = "";
          this.form.price = "";

          this.successAddService =
            "The new service has been added successfully";
        })
        .catch((e) => {
          this.errorAddService = e.response.data;
          this.successAddService ="";
        });
    },
  },
};
</script>

<style>
#addServiceForm {
  margin-top: 4%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>
