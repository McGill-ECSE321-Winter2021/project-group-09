<template>
    <main class="container py-4">
        <div class="mx-auto my-4" style="max-width: 600px">

          <h2 class="my-4 text-center">Book an Appointment</h2>

          <div v-if="formSection == 1">

            <b-form-group label="Select a service" class="mt-4">
                <b-form-radio v-for="s in services" :key="s.name" v-model="service" name="service" :value="s.name">
                  {{ s.name + ", for " + displayDuration(s.duration) + " ($" + s.price + ")" }}
                </b-form-radio>
                <p v-show="services.length === 0">There are currently no services available.</p>
            </b-form-group>

            <p class="mt-3">Selected service: {{ service }}</p>

            <b-button variant="outline-primary" class="mt-3" :disabled="!service" @click="toPart2">Next</b-button>

          </div>

          <div v-if="formSection == 2">

            <b-form-group label="Select a date and time" class="mt-4">
                <b-form-radio v-for="t in availableTimes" :key="t.startDateTime" v-model="start" name="start" :value="t">
                  {{ displayDateTime(t.startDateTime) + " to " + displayDateTime(t.endDateTime) }}
                </b-form-radio>
                <p v-show="availableTimes.length === 0">There are currently no time slots available.</p>
            </b-form-group>

            <p class="mt-3">Selected start time: {{ displayDateTime(start.startDateTime) }}</p>

            <b-form-group label="Or enter a future date to see possible schedules for that week:" class="mt-3 form-inline">
              <b-button variant="outline-secondary" @click="setToday">Today</b-button>
              <b-form-input v-model="targetDate" placeholder="YYYY-MM-DD" class="ml-3"></b-form-input>
              <b-button variant="outline-secondary" class="ml-3" @click="updateTargetDate">Go</b-button>
            </b-form-group>

            <b-button variant="outline-secondary" class="mt-3 mr-3" @click="toPart1">Back</b-button>
            <b-button variant="outline-primary" class="mt-3" :disabled="!start" @click="toPart3">Next</b-button>

          </div>

          <div v-if="formSection == 3">

            <p class="mb-3">Confirm your appointment</p>
            <p class="mt-3">Selected service: {{ service }}</p>
            <p class="mt-3">Selected start time: {{ displayDateTime(start.startDateTime) }}</p>

            <b-button variant="outline-secondary" class="mt-3 mr-3" @click="toPart2">Back</b-button>
            <b-button variant="outline-primary" class="mt-3" @click="book">Book now</b-button>

          </div>

          <div v-if="formSection == 4" class="text-center">
            <p class="mb-3">Your appointment has been booked.</p>
            <p>A confirmation email has been sent.</p>
            <b-button variant="outline-primary" class="mt-4" to="/">Homepage</b-button>
          </div>

          <p class="text-danger mt-4" v-if="error">{{ error }}</p>
          <p class="text-danger mt-4" v-if="appError">{{ appError }}</p>

        </div>
    </main>
</template>

<script>

  import { 
    LOCALHOST_BACKEND, 
    ALL_SERVICES_ENDPOINT, 
    POSSIBLE_APPOINTMENTS_ENDPOINT,
    CREATE_APPOINTMENT_ENDPOINT
  } from "../constants/constants";
  import axios from "axios";

  export default {

    data() {
      return {
        error: '',
        appError: '',
        service: '',
        services: [],
        targetDate: '',
        start: { startDateTime: '', endDateTime: '' },
        availableTimes: [],
        formSection: 1
      }
    },

    created: function () {
      // get all services
      axios.get(LOCALHOST_BACKEND + ALL_SERVICES_ENDPOINT).then(r => {
        this.services = r.data;
        this.appError = '';
      }).catch(e => {
        this.appError = e;
      });
    },

    methods: {

      toPart1() { this.formSection = 1; },
      
      toPart2() {
        if (this.service) {
          this.getPossibleAppointments();
        } else this.error = 'Please select a service';
      },

      toPart3() {
        if (this.start) this.formSection = 3;
        else this.error = 'Please select a start time';
      },

      getPossibleAppointments() {
        axios.get(LOCALHOST_BACKEND + POSSIBLE_APPOINTMENTS_ENDPOINT, {
          params: {
            "startDate": this.targetDate,
            "serviceName": this.service
          },
          headers: {
            token: this.$root.$data.token
          }
        }).then(r => {
          this.availableTimes = r.data;
          this.formSection = 2;
          this.appError = '';
        }).catch(e => {
          if (e.response.status == 400) this.appError = e.response.data;
          else this.appError = e;
        });
      },

      updateTargetDate() {
        let newTarget = new Date(this.targetDate);
        if (this.targetDate.length != 10 || newTarget == "Invalid Date") this.error = "Please select a valid date";
        else if (newTarget < new Date()) this.error = "Please enter a future date";
        else {
          this.error = "";
          this.getPossibleAppointments();
        }
      },

      setToday() {
        this.targetDate = '';
        this.getPossibleAppointments();
      },

      // Convert a Timestamp format (2021-03-02T15:00:00.000+00:00) to YYYY-MM-DD at HH:mm (in local timezone)
      displayDateTime(dateTime) {
        let date = new Date(dateTime).toString(); // Should output something like "Tue Mar 02 2021 10:00:00 GMT-0500 (Eastern Standard Time)"
        if (date == "Invalid Date") return '';
        else return date.slice(0, 10) + ", " + date.slice(11, 15) + " at " + date.slice(16, 21);
      },

      displayDuration(duration) {
        if (duration === 2) return "1 hour"
        else return (duration / 2.0) + " hours";
      },

      book() {
        axios.post(LOCALHOST_BACKEND + CREATE_APPOINTMENT_ENDPOINT, {
          "startTime": this.start.startDateTime,
          "serviceName": this.service,
          "customerEmail": this.$root.$data.email
        }, {
          headers: { "token": this.$root.$data.token }
        }).then(r => {
          this.formSection = 4;
          this.appError = '';
        }).catch(e => {
          if (e.response.status == 400) this.appError = e.response.data;
          else this.appError = e;
        });
      }

    },

    watch: {
      // if a value is set, reset error
      service: function (val, oldVal) {
        if (oldVal === '') this.error = '';
      },
      start: function (val, oldVal) {
        if (oldVal === '') this.error = '';
      },
      targetDate: function (val, oldVal) {
        this.error = '';
      }
    }

  }
</script>