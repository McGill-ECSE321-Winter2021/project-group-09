
<template>
  <div id="ViewServices">
    <h2>View Services</h2>
    <template>
      <div>
        <div v-if="errorViewServices">
          <span v-if="errorViewServices" style="color: red">
            {{ errorViewServices }}
          </span>
        </div>
        <div v-else>
          <b-table :items="items" :fields="fields" :outlined="true"> </b-table>
        </div>
      </div>
    </template>
  </div>
</template>

<script>

import axios from "axios";

var config = require("../../config");
var AXIOS = axios.create({
  baseURL: "http://" + config.dev.backendHost + ":" + config.dev.backendPort,
});

export default {
  data() {
    return {
      errorViewServices: "",
      services: [],
      fields: ["service", "duration", "price"],
      items: []
    };
  },
  created: function () {
    AXIOS.get("/api/service/all")
      .then((response) => {
        this.services = response.data;

        this.services.forEach((item, index) => {
          this.items.push({
            service: item.name,
             duration: (item.duration * 30)+" min.",
            price: item.price+" $",
          });
          
        });
      })
      .catch((e) => {
        this.errorViewServices = "There are currently no services";
      });
  },
  methods: {},
};
</script>

<style>
#ViewServices {
  margin-top: 4%;
  margin-left: 5%;
  margin-right: 5%;
}

</style>
