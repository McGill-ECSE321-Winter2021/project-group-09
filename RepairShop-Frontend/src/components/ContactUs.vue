
<template>
  <div id="contactUs">
    <div id="titleContainer">
      <h1>Contact Us</h1>
    </div>
    <div v-if="errorGetBusiness">
      <span v-if="errorGetBusiness" style="color: red">
        {{ errorGetBusiness }}
      </span>
    </div>

    <div id="businessName">
      {{ name }}
    </div>

    <div id="businessInfo">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-geo-alt" viewBox="0 0 16 16">
        <path d="M12.166 8.94c-.524 1.062-1.234 2.12-1.96 3.07A31.493 31.493 0 0 1 8 14.58a31.481 31.481 0 0 1-2.206-2.57c-.726-.95-1.436-2.008-1.96-3.07C3.304 7.867 3 6.862 3 6a5 5 0 0 1 10 0c0 .862-.305 1.867-.834 2.94zM8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10z"/>
        <path d="M8 8a2 2 0 1 1 0-4 2 2 0 0 1 0 4zm0 1a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"/>
      </svg>
      {{ address }}<br />
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-telephone" viewBox="0 0 16 16">
        <path d="M3.654 1.328a.678.678 0 0 0-1.015-.063L1.605 2.3c-.483.484-.661 1.169-.45 1.77a17.568 17.568 0 0 0 4.168 6.608 17.569 17.569 0 0 0 6.608 4.168c.601.211 1.286.033 1.77-.45l1.034-1.034a.678.678 0 0 0-.063-1.015l-2.307-1.794a.678.678 0 0 0-.58-.122l-2.19.547a1.745 1.745 0 0 1-1.657-.459L5.482 8.062a1.745 1.745 0 0 1-.46-1.657l.548-2.19a.678.678 0 0 0-.122-.58L3.654 1.328zM1.884.511a1.745 1.745 0 0 1 2.612.163L6.29 2.98c.329.423.445.974.315 1.494l-.547 2.19a.678.678 0 0 0 .178.643l2.457 2.457a.678.678 0 0 0 .644.178l2.189-.547a1.745 1.745 0 0 1 1.494.315l2.306 1.794c.829.645.905 1.87.163 2.611l-1.034 1.034c-.74.74-1.846 1.065-2.877.702a18.634 18.634 0 0 1-7.01-4.42 18.634 18.634 0 0 1-4.42-7.009c-.362-1.03-.037-2.137.703-2.877L1.885.511z"/>
      </svg>
      {{ phoneNumber }} <br />
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-envelope" viewBox="0 0 16 16">
        <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2zm13 2.383-4.758 2.855L15 11.114v-5.73zm-.034 6.878L9.271 8.82 8 9.583 6.728 8.82l-5.694 3.44A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.739zM1 11.114l4.758-2.876L1 5.383v5.73z"/>
      </svg>
      {{ email }} <br />

      <div v-show="this.$root.$data.userType == 'Admin'">
        <span>
          <b>🛠️ Number of repair spots:</b> {{ numberOfRepairSpots }}<br />
        </span>
      </div>
    </div>

    <h2>Holidays</h2>

    <div v-if="errorViewHolidays">
      <span v-if="errorViewHolidays" style="color: red">
        {{ errorViewHolidays }}
      </span>
    </div>
    <div v-else id="holidayInfo">

        <b> The repair shop will be closed on these days:</b>

      <b-table
        :items="items"
        :fields="fields"
        :key="this.items.length"
        :outlined="true"
        :small="true"
      >
      </b-table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {
  GET_BUSINESS_ENDPOINT,
  ALL_HOLIDAYS_ENDPOINT,
} from "../constants/constants";
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
@import url('https://fonts.googleapis.com/css2?family=Libre+Baskerville:wght@700&display=swap');
@import url("https://fonts.googleapis.com/css2?family=Lato&display=swap");
#contactUs {
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

#businessName {
  font-family: 'Libre Baskerville', serif;
  font-size: 200%;
}

#businessInfo {
  font-family: "Lato", sans-serif;
  line-height: 2.0;
  font-size: 18px;
  padding: 30px;

}

#holidayInfo {
  /* //font-family: "Lato", sans-serif; */
  font-family: 'Libre Baskerville', serif;
  font-size: 18px;
  line-height: 1.8;

}
</style>