

<!-- ==========================    TEMPLATE   ===============================-->

<template>
  <div id="ViewServices">
    <h2>View Services</h2>
    <template>
      <div>
        <!-- <table>
          <tr>
            <th>Service</th>
            <th>Duration <br/>(number of 30 minutes slot)</th>
            <th>Price ($)</th>
          </tr>
          <p>
            <span v-if="errorViewServices" style="color: red">
              {{ errorViewServices }}
            </span>
          </p>
          <tr v-for="s in services" :key="s">
            <td>{{ s.name }}</td>
            <td>{{ s.duration }}</td>
            <td>{{ s.price }}</td>
          </tr>
        </table> -->

        <!-- IF no services -->
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

<!-- ==========================    SCRIPT   ===============================-->


<script>
import { SERVICE_ENDPOINT, LOCALHOST_BACKEND } from "../constants/constants";
import axios from "axios";

var config = require("../../config");
var AXIOS = axios.create({
  //DEVELOPMENT
  baseURL: "http://" + config.dev.backendHost + ":" + config.dev.backendPort,
  //baseURL: "http://" + LOCALHOST_BACKEND,

  //PRODUCTION
  //baseURL: "http://" + config.build.backendHost + ":" + config.build.backendPort,

  headers: {
    "Access-Control-Allow-Origin":
      "http://" + config.dev.host + ":" + config.dev.port, //127.0.0.1:8089
  },
});

export default {
  data() {
    return {
      errorViewServices: "",
      services: [],
      // items:[
      //   <v-for="s in services": key="s">
      //       { "name": s.name, "duration": s.duration, "price": s.price }
      // ]

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
        this.errorViewServices = "No service";
      });
  },
  methods: {},
};
</script>


<!-- ==========================    STYLE   ===============================-->


<style>
#ViewServices {
  margin-top: 4%;
  margin-left: 5%;
  margin-right: 5%;
}

/* ul {
  list-style-type: disc;
  text-align: left;
  padding: 1;
}
li {
  margin: 1 10px;
}
table {
  border-collapse: separate;
  border-spacing: 0 15px;
}
th,
td {
  width: 150px;
  text-align: center;
  border: 1px solid black;
  padding: 5px;
} */
</style>
