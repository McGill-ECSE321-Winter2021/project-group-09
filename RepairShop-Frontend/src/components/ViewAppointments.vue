<template>
  <div>
    <h1>My Appointments</h1>
    <div id="ViewServices">
      <template>
        <div>
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
            >
              <template #cell(cancellation)="row">
                <b-button
                  size="sm"
                  v-on:click="cancelAppointment(row.item.ID)"
                  class="mr-2"
                  variant="danger"
                  v-if="checkIfWeekAhead(row.item.ID)"
                >
                  Cancel Appointment
                </b-button>
                <b-button
                  size="sm"
                  v-on:click="cancelAppointment(row.item.ID)"
                  class="mr-2"
                  disabled
                  v-else
                >
                  Cancel Appointment
                </b-button>
              </template></b-table
            >
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { CANCEL_APPOINTMENT_ENDPOINT } from "../constants/constants";
var config = require("../../config");
var AXIOS = axios.create({
  baseURL: "http://" + config.dev.backendHost + ":" + config.dev.backendPort,
});

export default {
  data() {
    return {
      errorViewServices: "",
      appointments: [],
      fields: ["ID", "Service", "start", "end", "cancellation"],
      items: [],
      idToDateTimeMap: {},
    };
  },

  //fetch all of this customer's appointments and display them in a table
  created: function () {
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
    //cancels appointment by id if it is valid, forcing a component refresh once list is updated
    cancelAppointment(id) {
      AXIOS.delete(CANCEL_APPOINTMENT_ENDPOINT + id, {
        headers: {
          token: this.$root.$data.token,
        },
      })
        .then((response) => {
          this.items = [];
          this.getAppointments();
          console.log(response);
        })
        .catch((error) => {
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
    },
    //checks if an appointment (by id) is at least a week away from today
    checkIfWeekAhead(id) {
      let target = new Date(this.idToDateTimeMap[id]);
      target.setDate(target.getDate() - 7);
      if (target > new Date()) return true;
      return false;
    },
    //fetches all of customer's appointments
    getAppointments() {
      AXIOS.get("/api/customer/" + this.$root.$data.email + "/appointments", {
        headers: {
          token: this.$root.$data.token,
        },
      })
        .then((response) => {
          this.appointments = response.data;

          this.appointments.forEach((item) => {
            this.items.push({
              ID: item.appointmentID,
              Service: item.serviceDto.name,
              start: this.displayDateTime(item.timeSlotDto.startDateTime),
              end: this.displayDateTime(item.timeSlotDto.endDateTime),
            });
            this.idToDateTimeMap[item.appointmentID] =
              item.timeSlotDto.startDateTime;
          });
        })
        .catch((e) => {
          console.log(e);
        });
    },
  },
};
</script>

<style>
#ViewServices {
  margin-top: 4%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>
