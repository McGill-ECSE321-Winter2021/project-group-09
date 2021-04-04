<template>
  <div>
    <h1>Weekly Appointment Schedule</h1>
    <div class="formContainer" id="technicianSchedule">
      <div class="ourTable">
        <div id="datePicker">
          <b-form @submit="getSchedule">
            <div>
              <label for="schedule-datepicker">Choose a date</label>
              <b-form-datepicker
                id="schedule-datepicker"
                v-model="date"
                class="mb-2"
                :date-disabled-fn="dateDisabled"
              ></b-form-datepicker>
            </div>
            <p v-if="noAppointments" style="color: red">{{ noAppointments }}</p>
            <p v-else-if="errorAppointments" style="color: red">
              {{ errorAppointments }}
            </p>
            <b-button type="submit" variant="primary" style="margin-top:15px"
              >Get Schedule</b-button
            >
          </b-form>
        </div>

        <div v-if="date && items.length != 0">
          <b-table
            :fields="fields"
            :items="items"
            responsive="sm"
            style="margin-top: 50px"
          >
            <!-- A virtual composite column -->
            <template #cell(dayTime)="data"> {{ data.item }}. </template>
          </b-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { BACKEND } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      noAppointments: "",
      errorAppointments: "",
      date: "",
      fields: [{ key: "dayTime", label: "Day and Time" }],
      items: []
    };
  },

  methods: {
    dateDisabled(ymd, date) {
      const weekday = date.getDay();
      return weekday != 1;
    },// Convert a Timestamp format (2021-03-02T15:00:00.000+00:00) to YYYY-MM-DD from HH:mm to HH::mm(in local timezone)
    displayDateTime(startDateTime, endDateTime) {
      let startDate = new Date(startDateTime).toString(); // Should output something like "Tue Mar 02 2021 10:00:00 GMT-0500 (Eastern Standard Time)"
      let endDate = new Date(endDateTime).toString();
      
      if (startDate == "Invalid Date" || endDate == "Invalid Date") return "";

      else
        return (
          startDate.slice(0, 10) +
          ", " +
          startDate.slice(11, 15) +
          " from " +
          startDate.slice(16, 21) +
          " to " +
          endDate.slice(16,21)
        );
    },

    getSchedule(event) {
      event.preventDefault();
      this.errorAppointments = "";
      this.noAppointments = "";

      var url =
        BACKEND + "/api/technician/" + this.$root.$data.email + "/schedule";
      var tempSchedule = [];

      axios
        .get(
          url,
          //changed wekStartDate to header. Need to test if it works
          {
            headers: {
              weekStartDate: this.date,
              token: this.$root.$data.token
            }
          }
        )
        .then(
          response => {
            var formattedSchedule = [];

            if (response.data === "No upcoming appointments") {
              this.noAppointments = response.data;
            } else {
              tempSchedule = response.data;
              tempSchedule.forEach(thisDayTime => {

                  var dayTime =this.displayDateTime(thisDayTime.startDateTime,thisDayTime.endDateTime );

                formattedSchedule.push(dayTime);
              });
              this.items = formattedSchedule;
            }
          },
          error => {
            console.log(error.response.data);
            this.errorAppointments = "Something went wrong. Please try again.";
          }
        );
    }
  },
  watch: {
    date: function(val, oldVal) {
      this.items = [];
      this.errorAppointments = "";
      this.noAppointments = "";
    }
  }
};
</script>

<style></style>
