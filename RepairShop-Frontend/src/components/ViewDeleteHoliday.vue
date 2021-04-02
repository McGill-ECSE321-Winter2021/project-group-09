<template>
  <div id="ViewDeleteHoliday">
    <h2>Holidays: View and Delete</h2>

    <template>
      <div>


    <p>  
          <span v-if="successDeleteHoliday" style="color: #04571b">
            {{ successDeleteHoliday }}
          </span>

    </p>

        <div v-if="errorViewDeleteHolidays">
          <span v-if="errorViewDeleteHolidays" style="color: red">
            {{ errorViewDeleteHolidays }}
          </span>
        </div>
   

        <div v-else>
          <b-table
            :items="items"
            :fields="fields"
            :outlined="true"
            :key="this.items.length"
          >
            <template #cell(delete)="row">
              <b-button
                size="sm"
                v-on:click="deleteHoliday(row.item.startDateTime, row.item.endDateTime)"
                class="mr-2"
                variant="danger"
              >
                Delete Holiday
              </b-button>
            </template></b-table
          >
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import axios from "axios";
import {
  DELETE_HOLIDAY_ENDPOINT,
  ALL_HOLIDAYS_ENDPOINT,
} from "../constants/constants";

var config = require("../../config");
var AXIOS = axios.create({
  baseURL: "http://" + config.dev.backendHost + ":" + config.dev.backendPort,
});
export default {
  data() {
    return {
      errorViewDeleteHolidays: "",
      successDeleteHoliday: "",
      holidays: [],
      fields: ["Start Date", "End Date", "delete"],
      items: [],
    };
  },
  //fetch all holidays and display them in a table
  created: function () {
    this.getHolidays();
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
    },
    //Deletes holiday by start date and end date, forcing a component refresh once list is updated
    deleteHoliday(start, end) {
      AXIOS.post(
        DELETE_HOLIDAY_ENDPOINT,
        {
          startDateTime: start,
          endDateTime: end,
        },
        {
          headers: {
            token: this.$root.$data.token,
          },
        }
      )
        .then((response) => {
          this.items = [];
          this.getHolidays();
          console.log(response);
          this.successDeleteHoliday = "The holiday has been deleted successfully ✓ ";

        })
        .catch((e) => {
          console.log(e);
          this.errorViewDeleteHolidays = e.response.data;
        });
    },

    //fetches all holidays
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
            this.errorViewDeleteHolidays = e.response.data;
          else this.errorViewDeleteHolidays = e;
        });
    }
  }
};
</script>
    
<style>
#ViewDeleteHoliday {
  margin-top: 4%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>