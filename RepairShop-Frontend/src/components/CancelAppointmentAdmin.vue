<template>
  <div>
    <h1>Cancel an Appointment</h1>
    <main class="formContainer">
      <div class="inputWidth">
        <div v-if="formSection == 1" class="mt-4">
          <div v-if="technicians.length">
            <b-form-group label="Select a technician" class="mt-4">
              <b-form-radio
                v-for="t in technicians"
                :key="t.email"
                v-model="technicianEmail"
                name="technicianEmail"
                :value="t.email"
              >
                {{ t.email }}
              </b-form-radio>
            </b-form-group>

            <p class="mt-3">
              <b>Selected technician: </b>{{ technicianEmail }}
            </p>
          </div>

          <p v-show="noTechnicians" class="text-danger">
            There are currently no technicians available.
          </p>

          <b-button
            variant="primary"
            class="mt-3"
            :disabled="!technicianEmail"
            @click="toPart2"
            >Next</b-button
          >
        </div>

        <div v-if="formSection == 2" class="mt-4">
          <p class="mt-3"><b>Selected technician: </b>{{ technicianEmail }}</p>

          <b-form-group label="Select an appointment to cancel" class="mt-4">
            <b-form-radio
              v-for="a in appointments"
              :key="a.appointmentID"
              v-model="appointment"
              name="appointment"
              :value="a"
              class="radioStyle"
            >
              <p>
                <b>{{ displayDateTime(a.timeSlotDto.startDateTime) }} </b>
              </p>
              <p>
                <b>Service: </b>
                {{ a.serviceDto.name }}
              </p>

              <p><b>Customer: </b>{{ a.customerDto.name }}</p>
            </b-form-radio>
            <p v-show="appointments.length === 0" class="text-danger mt-3">
              There are no appointments for {{ technicianEmail }}.
            </p>
          </b-form-group>

          <div
            class="mt-3 radioStyle"
            v-show="appointment.serviceDto.name != ''"
          >
            <div style="padding-bottom: 20px">
              <b>Selected appointment:</b>
            </div>

            <p>
              <b
                >{{ displayDateTime(appointment.timeSlotDto.startDateTime) }}
              </b>
            </p>
            <p>
              <b>Service: </b>
              {{ appointment.serviceDto.name }}
            </p>

            <p><b>Customer: </b>{{ appointment.customerDto.name }}</p>
          </div>

          <b-button variant="secondary" class="mt-3 mr-3" @click="toPart1"
            >Back</b-button
          >
          <b-button
            variant="primary"
            :disabled="appointments.length == 0"
            class="mt-3"
            @click="toPart3"
            >Next</b-button
          >
        </div>

        <div v-if="formSection == 3" class="mt-4">
          <p class="mb-3"><b>Confirm your modification</b></p>
          <p class="mt-3"><b>Selected technician: </b>{{ technicianEmail }}</p>
         
         
 <div
            class="mt-3 radioStyle"
            v-show="appointment.serviceDto.name != ''"
          >
            <div style="padding-bottom: 20px">
         <b>Selected appointment to cancel: </b>

            </div>

            <p>
              <b
                >{{ displayDateTime(appointment.timeSlotDto.startDateTime) }}
              </b>
            </p>
            <p>
              <b>Service: </b>
              {{ appointment.serviceDto.name }}
            </p>

            <p><b>Customer: </b>{{ appointment.customerDto.name }}</p>
          </div>

          <b-button variant="secondary" class="mt-3 mr-3" @click="toPart2"
            >Back</b-button
          >
          <b-button variant="danger" class="mt-3" @click="cancelAppointment"
            >Confirm</b-button
          >
        </div>

        <div v-if="formSection == 4" class="text-center mt-4">
          <p class="mb-3 text-success">The appointment has been cancelled.</p>
          <b-button variant="primary" class="mt-4" to="/">Homepage</b-button>
        </div>

        <p class="text-danger mt-4" v-if="error">{{ error }}</p>
        <p class="text-danger mt-4" v-if="appError">{{ appError }}</p>
      </div>
    </main>
  </div>
</template>

<script>
import { BACKEND, TECHNICIAN_ENDPOINT } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      error: "",
      appError: "",
      technicianEmail: "",
      technicians: [],
      noTechnicians: false,
      appointment: {
        serviceDto: { name: "" },
        customerDto: { name: "" },
        timeSlotDto: { startDateTime: "" },
      },
      appointments: [],
      formSection: 1,
    };
  },

  created: function () {
    // get all technicians
    axios
      .get(BACKEND + TECHNICIAN_ENDPOINT + "all", {
        headers: { token: this.$root.$data.token },
      })
      .then((r) => {
        this.technicians = r.data;
        this.appError = "";
        if (this.technicians.length == 0) this.noTechnicians = true;
        else this.noTechnicians = false;
      })
      .catch((e) => {
        this.appError = e;
      });
  },

  methods: {
    toPart1() {
      this.formSection = 1;
      this.appError = "";
      this.error = "";
    },

    toPart2() {
      this.appError = "";
      this.error = "";
      if (this.technicianEmail) this.getTechnicianAppointments();
      else this.error = "Please select a technician";
    },

    toPart3() {
      this.appError = "";
      this.error = "";
      if (this.appointment) this.formSection = 3;
      else this.error = "Please select an appointment to cancel";
    },

    getTechnicianAppointments() {
      // Get a technician's appointments
      axios
        .get(
          BACKEND +
            TECHNICIAN_ENDPOINT +
            this.technicianEmail +
            "/appointments",
          {
            headers: { token: this.$root.$data.token },
          }
        )
        .then((r) => {
          this.appointments = r.data;
          this.formSection = 2;
          this.appError = "";
        })
        .catch((e) => {
          if (e.response.status == 400) this.appError = e.response.data;
          else this.appError = e;
        });
    },

    cancelAppointment() {
      // Cancel appointment
      axios
        .delete(
          BACKEND +
            "/api/appointment/cancel/" +
            this.appointment.appointmentID,
          {
            headers: { token: this.$root.$data.token },
          }
        )
        .then((r) => {
          this.formSection = 4;
          this.appError = "";
        })
        .catch((e) => {
          if (e.response.status == 400) this.appError = e.response.data;
          else this.appError = e;
        });
    },
    // Convert a Timestamp format (2021-03-02T15:00:00.000+00:00) to YYYY-MM-DD at HH:mm (in local timezone)
    displayDateTime(dateTime) {
      let date = new Date(dateTime).toString(); // Should output something like "Tue Mar 02 2021 10:00:00 GMT-0500 (Eastern Standard Time)"
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
  },

  watch: {
    // if a value is set, reset error
    technicianEmail: function (val, oldVal) {
      this.error = "";
    },
    appointment: function (val, oldVal) {
      this.error = "";
    },
  },
};
</script>

<style>
a {
  cursor: pointer;
}

.radioStyle {
  margin-top: 10px;
}

.radioStyle p {
  padding: 0px;
  margin-bottom: 5px;
}
</style>