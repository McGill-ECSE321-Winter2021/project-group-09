<template>
  <div>
    <h1>Technician's Weekly Schedule</h1>
    <div class="formContainer" id="technicianSchedule">
      <div class="ourTable">
        <div id="dataInput">
          <b-form @submit="getSchedule">
            <div id="techEmailInput">
              <b-form-group label="Select a technician" class="mt-4">
                <b-form-radio
                  v-for="tech in technicians"
                  :key="tech.email"
                  v-model="techEmail"
                  :value="tech.email"
                >
                  {{ tech.name + " (" + tech.email + ")" }}
                </b-form-radio>
                <p v-show="technicians.length === 0">
                  No technicians registered.
                </p>
              </b-form-group>
            </div>

            <div id="datePicker">
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

        <div v-if="date && this.items.length != 0">
          <b-table
            :fields="fields"
            :items="items"
            responsive="sm"
            style="margin-top: 50px"
          >
            <!-- A virtual composite column -->
            <template #cell(dayTime)="data"> {{ data.item }}</template>
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
      technicians: [],
      techEmail: "",
      noAppointments: "",
      errorAppointments: "",
      date: "",
      fields: [{ key: "dayTime", label: "Day and Time" }],
      items: []
    };
  },

  created: function() {
   if (this.$root.$data.userType!='Admin') this.$router.push("/");

    let url = BACKEND + "/api/technician/all";
    axios
      .get(url, {
        headers: {
          token: this.$root.$data.token
        }
      })
      .then(response => {
        this.technicians = response.data;
      })
      .catch(error => {
        this.errorAppointments = error;
      });
  },

  methods: {
    dateDisabled(ymd, date) {
      const weekday = date.getDay();
      return weekday != 1;
    },    // Convert a Timestamp format (2021-03-02T15:00:00.000+00:00) to YYYY-MM-DD from HH:mm to HH::mm(in local timezone)
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
      this.noAppointments = "";
      this.errorAppointments = "";

      if (this.date == "") {
        this.errorAppointments = "Please choose a date";
        return;
      }

      this.items = [];
      var url = BACKEND + "/api/technician/" + this.techEmail + "/schedule";
      var tempSchedule = [];

      axios
        .get(url, {
          headers: {
            weekStartDate: this.date,
            token: this.$root.$data.token
          }
        })
        .then(
          response => {
            var formattedSchedule = [];
            this.noAppointments = "";
            this.errorAppointments = "";

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
    techEmail: function(val, oldVal) {
      this.date = "";
      this.items = [];
      this.errorAppointments = "";
      this.noAppointments = "";
    }
  }
};
</script>

<style></style>
