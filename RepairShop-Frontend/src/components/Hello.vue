<template>
  <div v-if="errorGetBusiness">
    <span v-if="errorGetBusiness" style="color: red">
      {{ errorGetBusiness }}
    </span>
  </div>

  <div v-else>
    
    <h1>Welcome!</h1>

    <div v-if="name">
      <h1>Welcome to {{ name }}!</h1>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import { GET_BUSINESS_ENDPOINT } from "../constants/constants";
var config = require("../../config");
var AXIOS = axios.create({
  baseURL: "http://" + config.dev.backendHost + ":" + config.dev.backendPort,
});
export default {
  data() {
    return {
      name: "",
      errorGetBusiness: "",
    };
  },
  created: function () {
    AXIOS.get(GET_BUSINESS_ENDPOINT)
      .then((response) => {
        this.name = response.data.name;
      })
      .catch((e) => {
        if (e.response.status == 404) this.errorGetBusiness = e.response.data;
        else this.errorGetBusiness = e;
      });
  },
};
</script>

<style>
</style>
