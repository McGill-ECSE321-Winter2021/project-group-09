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
              <b-form-radio v-for="t in availableTimes" :key="t.startTime" v-model="start" name="service" :value="t.startTime">
                {{ t.startTime + " to " + t.endTime }}
              </b-form-radio>
          </b-form-group>
          <p class="mt-3">Selected start time: {{ start }}</p>
          <b-button variant="outline-secondary" class="mt-3 mr-3" @click="toPart1">Back</b-button>
          <b-button variant="outline-primary" class="mt-3" @click="toPart3">Next</b-button>
        </div>

        <div v-if="formSection == 3">
          <p class="mb-3">Confirm your appointment</p>
          <p class="mt-3">Selected service: {{ service }}</p>
          <p class="mt-3">Selected start time: {{ start }}</p>
          <b-button variant="outline-secondary" class="mt-3 mr-3" @click="toPart2">Back</b-button>
          <b-button variant="outline-primary" class="mt-3" @click="book">Book now</b-button>
        </div>

        <p class="text-danger mt-4" v-if="error">{{ error }}</p>
        <p class="text-danger mt-4" v-if="appError">{{ appError }}</p>

    </main>
</template>

<script>
  import axios from 'axios';
  var config = require('../../config');

  var AXIOS = axios.create({
      baseURL: 'http://' + config.dev.backendHost + ':' + config.dev.backendPort,
      // baseURL: 'http://' + config.dev.backendHost,
      headers: { 'Access-Control-Allow-Origin': 'http://' + config.dev.host + ':' + config.dev.port }
  });

  export default {
    data() {
      return {
        error: '',
        appError: '',
        service: '',
        services: [],
        targetDate: '',
        start: '',
        availableTimes: [],
        formSection: 1
      }
    },
    created: function () {
      // get all services
      AXIOS.get('/api/service/all').then(r => {
        this.services = r.data;
      }).catch(e => {
        this.appError = e;
      });
    },
    methods: {
      toPart1() { this.formSection = 1; },
      toPart2() {
        if (this.service) {
          // Get all possible times
          AXIOS.get('/api/appointment/possibilities', {
            params: {
              "startDate": this.targetDate,
              "serviceName": this.service
            },
            headers: {
              token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJncm91cDlyZXBhaXJzaG9wQGdtYWlsLmNvbSIsImlhdCI6MTYxNjUzNzY4OSwiZXhwIjoxNjE2NTgwODg5fQ.AfQ7PH_ca8HA78Zuyh-yEBiK98RwEXacp92llgmlIu0"
            }
          }).then(r => {
            this.availableTimes = r.data;
            this.formSection = 2;
          }).catch(e => {
            this.appError = e;
          });
        } else this.error = 'Please select a service';
      },
      toPart3() {
        if (this.start) this.formSection = 3;
        else this.error = 'Please select a start time';
      },
      book() {
        console.log("Appointment was booked");
      }
    },
    watch: { // if a service or start time is set, reset error
      service: function (val, oldVal) {
        if (oldVal === '') this.error = '';
      },
      start: function (val, oldVal) {
        if (oldVal === '') this.error = '';
      }
    }
  }
</script>