<template>
  <div>
    <h1>View Appointments</h1>
    <div class="formContainer" id="ViewAppointments">
      <div class="ourTable">
        <div v-if="errorViewServices">
          <span v-if="errorViewServices" style="color: red">
            {{ errorViewServices }}
          </span>
        </div>
        <div v-else>
          <b-table
            :items="items"
            :fields="fields"
            :outlined="true"
            :key="this.items.length"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { BACKEND } from "../constants/constants";
import axios from "axios";
var config = require("../../config");
var AXIOS = axios.create({
  baseURL: BACKEND
});

export default {
  data() {
    return {
      errorViewServices: "",
      appointments: [],
      fields: ["Service", "start", "end", "customer"],
      items: [],
      idToDateTimeMap: {}
    };
  },

  //fetch all of this technician's appointments and display them in a table
  created: function() {
    if (this.$root.$data.userType!='Technician') this.$router.push("/");
    this.getAppointments();
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
    //checks if an appointment (by id) is at least a week away from today
    checkIfWeekAhead(id) {
      let target = new Date(this.idToDateTimeMap[id]);
      target.setDate(target.getDate() - 7);
      if (target > new Date()) return true;
      return false;
    },
    //fetches all of technician's appointments
    getAppointments() {
      AXIOS.get("/api/technician/" + this.$root.$data.email + "/appointments", {
        headers: {
          token: this.$root.$data.token
        }
      })
        .then(response => {
          this.appointments = response.data;

          this.appointments.forEach(item => {
            this.items.push({
              ID: item.appointmentID,
              Service: item.serviceDto.name,
              start: this.displayDateTime(item.timeSlotDto.startDateTime),
              end: this.displayDateTime(item.timeSlotDto.endDateTime),
              customer: item.customerDto.name
            });
            this.idToDateTimeMap[item.appointmentID] =
              item.timeSlotDto.startDateTime;
          });
        })
        .catch(e => {
          console.log(e);
        });
    }
  }
};
</script>

<style></style>
