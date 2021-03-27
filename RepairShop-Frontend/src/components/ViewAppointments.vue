<template>
  <div id="ViewServices">
    <h2>Your Appointments</h2>
    <template>
      <div>
        <div v-if="errorViewServices">
          <span v-if="errorViewServices" style="color: red">
            {{ errorViewServices }}
          </span>
        </div>
        <div v-else>
          <b-table :items="items" :fields="fields" :outlined="true">
            <template #cell(cancellation)="row">
              <b-button
                size="sm"
                v-on:click="cancelAppointment(row.item.ID)"
                class="mr-2"
              >
                Cancel Appointment
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
import { CANCEL_APPOINTMENT_ENDPOINT } from "../constants/constants";
var config = require("../../config");
var AXIOS = axios.create({
  baseURL: "http://" + config.dev.backendHost + ":" + config.dev.backendPort
});

export default {
  data() {
    return {
      errorViewServices: "",
      appointments: [],
      fields: ["ID", "Service", "start", "end", "cancellation"],
      items: []
    };
  },

  created: function() {
    AXIOS.get("/api/customer/" + this.$root.$data.email + "/appointments", {
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
            end: this.displayDateTime(item.timeSlotDto.endDateTime)
          });
        });
        console.log(this.items);
      })
      .catch(e => {
        console.log(e);
      });
  },
  methods: {
    displayDateTime(dateTime) {
      console.log("here");
      let date = new Date(dateTime).toString(); // Should output something like "Tue Mar 02 2021 10:00:00 GMT-0500 (Eastern Standard Time)"
      console.log("there");
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
    cancelAppointment(id) {
      console.log(id);
      AXIOS.delete(CANCEL_APPOINTMENT_ENDPOINT + id, {
        headers: {
          token: this.$root.$data.token
        }
      })
        .then(response => {
          console.log(response);
        })
        .catch(error => {
          if (error.response) {
            if (error.response.status === 400) {
              alert(
                "You can only cancel appointments at least 7 days in  advance!"
              );
            }
            if (error.response.status === 500) {
              alert("Something went wrong.");
            }
          }
        });
    }
  }
};
</script>

<style>
#ViewServices {
  margin-top: 4%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>
