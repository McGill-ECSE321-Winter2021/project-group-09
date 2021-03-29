<template>
    <main class="container py-4">
        <div class="mx-auto my-4" style="max-width: 600px">

          <h2 class="my-4 text-center">Modify a Technician's Work Hours</h2>

          <div v-if="formSection == 1">

            <b-form-group label="Select a technician" class="mt-4">
                <b-form-radio v-for="t in technicians" :key="t.email" v-model="technicianEmail" name="technicianEmail" :value="t.email">
                  {{ t.email }}
                </b-form-radio>
                <p v-show="technicians.length === 0">There are currently no technicians available.</p>
            </b-form-group>

            <p class="mt-3">Selected technician: {{ technicianEmail }}</p>

            <b-button variant="outline-primary" class="mt-3" :disabled="!technicianEmail" @click="toPart2">Next</b-button>

          </div>

          <div v-if="formSection == 2">

            <p class="mt-3">Selected technician: {{ technicianEmail }}</p>

            <b-form-group label="Pick a time slot to remove" class="mt-4">
                <b-form-radio v-for="h in workHours" :key="h.startDateTime" v-model="hours" name="hours" :value="h">
                  {{ displayDayTime(h.startDateTime) + " to " + displayDayTime(h.endDateTime) }}
                </b-form-radio>
                <p v-show="workHours.length === 0">There are currently no time slots available.</p>
            </b-form-group>

            <p class="mt-3">Selected hours: {{ hours.startDateTime ? displayDayTime(hours.startDateTime) + " to " + displayDayTime(hours.endDateTime) : '' }}</p>

            <b-button variant="outline-secondary" class="mt-3 mr-3" @click="toPart1">Back</b-button>
            <b-button variant="outline-primary" class="mt-3" :disabled="!hours.startDateTime" @click="toPart3">Next</b-button>

            <b-form-group label="Or remove their entire schedule" class="mt-4">
              <b-button variant="outline-danger" @click="nextDeleteSchedule">Delete Schedule</b-button>
            </b-form-group>

          </div>

          <div v-if="formSection == 3">

            <p class="mb-3">Confirm your modification</p>
            <p class="mt-3">Selected technician: {{ technicianEmail }}</p>
            <p class="mt-3" v-if="deleteSchedule">Decision: Delete their entire schedule</p>
            <p class="mt-3" v-else>Selected hours: {{ hours.startDateTime ? displayDayTime(hours.startDateTime) + " to " + displayDayTime(hours.endDateTime) : '' }}</p>
            <p class="mt-3">Please note that this cannot be undone.</p>

            <b-button variant="outline-secondary" class="mt-3 mr-3" @click="toPart2">Back</b-button>
            <b-button variant="outline-danger" class="mt-3" @click="deleteTargetHours">Confirm</b-button>

          </div>

          <div v-if="formSection == 4" class="text-center">
            <p class="mb-3" v-if="deleteSchedule">{{ technicianEmail + "'s schedule has been deleted."}}</p>
            <p class="mb-3" v-else>The selected hours have been deleted.</p>
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
    TECHNICIAN_ENDPOINT
  } from "../constants/constants";
  import axios from "axios";

  export default {

    data() {
      return {
        error: '',
        appError: '',
        technicianEmail: '',
        technicians: [],
        targetDate: '',
        hours: { startDateTime: '', endDateTime: '' },
        workHours: [],
        deleteSchedule: false,
        formSection: 1
      }
    },

    created: function () {
      // get all technicians
      axios.get(LOCALHOST_BACKEND + TECHNICIAN_ENDPOINT + 'all', {
        headers: { "token": this.$root.$data.token }
    }).then(r => {
        this.technicians = r.data;
        this.appError = '';
      }).catch(e => {
        this.appError = e;
      });
    },

    methods: {

      toPart1() { this.formSection = 1; },
      
      toPart2() {
        if (this.technicianEmail) {
            this.deleteSchedule = false;
            // Get work hours
            axios.get(LOCALHOST_BACKEND + TECHNICIAN_ENDPOINT + this.technicianEmail + '/work_hours', {
                headers: { "token": this.$root.$data.token }
            }).then(r => {
                this.workHours = r.data;
                this.formSection = 2;
                this.appError = '';
            }).catch(e => {
                if (e.response.status == 500) this.appError = e.response.data;
                else this.appError = e;
            });
        } else this.error = 'Please select a technician';
      },

      toPart3() {
        if (this.hours || this.deleteSchedule) this.formSection = 3;
        else this.error = 'Please select a time slot';
      },

      // Convert a Timestamp format (2021-03-02T15:00:00.000+00:00) to "Tue at 10:00" (in local timezone)
      displayDayTime(dateTime) {
        let date = new Date(dateTime).toString(); // Result: "Tue Mar 02 2021 10:00:00 GMT-0500 (Eastern Standard Time)"
        if (date == "Invalid Date") return '';
        else return date.slice(0, 3) + " at " + date.slice(16, 21);
      },

      nextDeleteSchedule() {
        this.deleteSchedule = true;
        this.toPart3();
      },

      deleteTargetHours() {
        if (this.deleteSchedule) {
            // Delete entire schedule
            axios.delete(LOCALHOST_BACKEND + TECHNICIAN_ENDPOINT + 'delete/schedule/' + this.technicianEmail, {
                headers: { "token": this.$root.$data.token }
            }).then(r => {
                this.formSection = 4;
                this.appError = '';
            }).catch(e => {
                if (e.response.status == 400) this.appError = e.response.data;
                else this.appError = e;
            });
        } else {
            // Delete specific work hours
            axios.post(LOCALHOST_BACKEND + TECHNICIAN_ENDPOINT + 'delete/hours/' + this.technicianEmail, {
                "startDateTime": this.hours.startDateTime,
                "endDateTime": this.hours.endDateTime
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
        
      }

    },

    watch: {
      // if a value is set, reset error
      technicianEmail: function (val, oldVal) {
        if (oldVal === '') this.error = '';
      },
      hours: function (val, oldVal) {
        if (oldVal === '') this.error = '';
      }
    }

  }
</script>