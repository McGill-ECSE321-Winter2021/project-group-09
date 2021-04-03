<template>
  <div v-if="errorGetBusiness" style="color: red">
    {{ errorGetBusiness }}
  </div>

  <div v-else>
    <div v-if="name">
      <h1>Welcome to {{ name }}!</h1>
    </div>
    <div v-else>
      <h1>Welcome!</h1>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { GET_BUSINESS_ENDPOINT, BACKEND } from "../constants/constants";
var config = require("../../config");
var AXIOS = axios.create({
  baseURL: BACKEND
});
export default {
  data() {
    return {
      name: "",
      errorGetBusiness: ""
    };
  },
  created: function() {
    console.log(process.env.VUE_APP_PROD_BACKEND);
    AXIOS.get(GET_BUSINESS_ENDPOINT)
      .then(response => {
        this.name = response.data.name;
      })
      .catch(e => {
        if (e.response.status == 404) this.errorGetBusiness = e.response.data;
        else this.errorGetBusiness = e;
      });
  }
};
</script>

<style></style>
