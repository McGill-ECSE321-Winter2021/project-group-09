<template>
  <div id="contactUs">
    <h1>Contact Us</h1>
    <div v-if="errorGetBusiness">
      <span v-if="errorGetBusiness" style="color: red">
        {{ errorGetBusiness }}
      </span>
    </div>

    <h2>{{ name }} 👨‍🏭</h2>
    <p>
      <b>🗺️ Address: </b> {{ address }}<br />
      <b>📞 Phone Number: </b> {{ phoneNumber }} <br />
      <b>📧 Email address: </b> {{ email }} <br />
    </p>

    <div v-show="this.$root.$data.userType == 'Admin'">
      <span>
        <b>🛠️ Number of repair spots:</b> {{ numberOfRepairSpots }}<br />
      </span>
    </div>

    <h2>Holidays 🎄</h2>
    
    <div v-if="errorViewHolidays">
      <span v-if="errorViewHolidays" style="color: red">
        {{ errorViewHolidays }}
      </span>
    </div>
    <div v-else>
        <p>The repair shop will be closed on these days:</p>
      <b-table
        :items="items"
        :fields="fields"
        :outlined="true"
        :key="this.items.length"
      >
      </b-table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { GET_BUSINESS_ENDPOINT, ALL_HOLIDAYS_ENDPOINT } from "../constants/constants";
var config = require("../../config");
var AXIOS = axios.create({
  baseURL: "http://" + config.dev.backendHost + ":" + config.dev.backendPort,
});
export default {
  data() {
    return {
      errorGetBusiness: "",
      errorViewHolidays: "",
      name: "",
      email: "",
      address: "",
      phoneNumber: "",
      numberOfRepairSpots: "",
      holidays: [],
      fields: ["Start Date", "End Date"],
      items: [],
    };
  },
  created: function () {
      this.getHolidays();
    AXIOS.get(GET_BUSINESS_ENDPOINT)
      .then((response) => {
        this.name = response.data.name;
        this.address = response.data.address;
        this.email = response.data.email;
        this.phoneNumber = response.data.phoneNumber;
        this.numberOfRepairSpots = response.data.numberOfRepairSpots;
      })
      .catch((e) => {
        if (e.response.status == 404) this.errorGetBusiness = e.response.data;
        else this.errorGetBusiness = e;
      });
  },
  methods: {
    // Should output something like "Tue Mar 02 2021 10:00:00 GMT-0500 (Eastern Standard Time)" given a timestamp
    displayDateTime(dateTime) {
      let date = new Date(dateTime).toString();
      if (date == "Invalid Date") return "";
      else
        return (
          date.slice(0, 10) +
          ", " +
          date.slice(11, 15) +
          " at " +
          date.slice(16, 21)
        );
    }, //fetches all holidays
    getHolidays() {
      AXIOS.get(ALL_HOLIDAYS_ENDPOINT)
        .then((response) => {
          this.holidays = response.data;
          this.holidays.forEach((item) => {
            this.items.push({
              "Start Date": this.displayDateTime(item.startDateTime),
              "End Date": this.displayDateTime(item.endDateTime),
              startDateTime: item.startDateTime,
              endDateTime: item.endDateTime,
            });
          });
        })
        .catch((e) => {
          console.log(e);
          if (e.response.status == 400 || e.response.status == 404)
            this.errorViewHolidays = e.response.data;
          else this.errorViewHolidays = e;
        });
    },
  },
};
</script>

<style>
#contactUs {
  margin-top: 4%;
  margin-left: 5%;
  margin-right: 5%;
}
#header2 {
  font-weight: bold;
}
</style>
