<template>
    <main class="container">
        <h2 class="my-4">Book an Appointment</h2>

        <div v-if="formSection == 1">
          <b-form-group label="Select a service" class="mt-4">
              <b-form-radio v-for="s in services" :key="s.name" v-model="service" name="service" :value="s.name">
                {{ s.name + ", for " + s.duration + ", for " + s.price }}
              </b-form-radio>
          </b-form-group>
          <p class="mt-3">Selected service: {{ service }}</p>
          <b-button variant="outline-primary" class="mt-3" @click="toPart2">Next</b-button>
        </div>

        <div v-if="formSection == 2">
          <b-form-group label="Select a date and time" class="mt-4">
              <b-form-radio v-for="t in availableTimes" :key="t.startDateTime" v-model="start" name="start" :value="t">
                {{ displayDateTime(t.startDateTime) + " to " + displayDateTime(t.endDateTime) }}
              </b-form-radio>
          </b-form-group>
          <p class="mt-3">Selected start time: {{ displayDateTime(start.startDateTime) }}</p>

          <b-form-group label="Or enter a future date to see possible schedules for that week:" class="mt-3 form-inline">
            <b-button variant="outline-secondary" @click="setToday">Today</b-button>
            <b-form-input v-model="targetDate" placeholder="YYYY-MM-DD" class="ml-3"></b-form-input>
            <b-button variant="outline-secondary" class="ml-3" @click="updateTargetDate">Go</b-button>
          </b-form-group>

          <b-button variant="outline-secondary" class="mt-3 mr-3" @click="toPart1">Back</b-button>
          <b-button variant="outline-primary" class="mt-3" @click="toPart3">Next</b-button>
        </div>

        <div v-if="formSection == 3">
          <p class="mb-3">Confirm your appointment</p>
          <p class="mt-3">Selected service: {{ service }}</p>
          <p class="mt-3">Selected start time: {{ displayDateTime(start.startDateTime) }}</p>
          <b-button variant="outline-secondary" class="mt-3 mr-3" @click="toPart2">Back</b-button>
          <b-button variant="outline-primary" class="mt-3" @click="book">Book now</b-button>
        </div>

        <div v-if="formSection == 4">
          <p class="mb-3">Your appointment has been booked</p>
        </div>

        <p class="text-danger mt-4" v-if="error">{{ error }}</p>
        <p class="text-danger mt-4" v-if="appError">{{ appError }}</p>

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
        customerEmail: '',
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
            this.appError = e;
          });
      },
      updateTargetDate() {
        let newTarget = new Date(this.targetDate);
        console.log('Target: ' + newTarget);
        console.log('Target: ' + newTarget);
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
      displayDateTime(dateTime) {
        // Convert a Timestamp format to YYYY-MM-DD at HH:mm 
        // Note: make sure there is a date first
        return (dateTime.length) ? dateTime.slice(0, 10) + " at " + dateTime.slice(11, 16) : '';
      },
      book() {
        axios.post(LOCALHOST_BACKEND + CREATE_APPOINTMENT_ENDPOINT, {
          body: {
            "startTime": this.start.startDateTime,
            "serviceName": this.service,
            "customerEmail": this.$root.$data.email,
            "token": this.$root.$data.token
          }
        }).then(r => {
          this.formSection = 4;
          this.appError = '';
        }).catch(e => {
          this.appError = e;
        });
      }
    },
    watch: { // if a service or start time is set, reset error
      service: function (val, oldVal) {
        if (oldVal === '') this.error = '';
      },
      start: function (val, oldVal) {
        if (oldVal === '') this.error = '';
      },
      targetDate: function (val, oldVal) { this.error = ''; }
    }
  }
</script>