<template>
  <div>
    <h1>Delete a Technician's Work Hours</h1>
    <main class="formContainer">
      <div class="inputWidth">
        <div v-if="formSection == 1">
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
              <b>Selected technician:</b> {{ technicianEmail }}
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

        <div v-if="formSection == 2">
          <p class="mt-3"><b>Selected technician: </b>{{ technicianEmail }}</p>

          <div v-if="workHours.length">
            <b-form-group label="Pick a time slot to remove" class="mt-4">
              <b-form-radio
                v-for="h in workHours"
                :key="h.startDateTime"
                v-model="hours"
                name="hours"
                :value="h"
              >
                {{
                  displayDayTime(h.startDateTime) +
                    " to " +
                    displayDayTime(h.endDateTime)
                }}
              </b-form-radio>
            </b-form-group>

            <p class="mt-3" v-show="hours.startDateTime">
              <b>Selected hours:</b>
              {{
                displayDayTime(hours.startDateTime) +
                  " to " +
                  displayDayTime(hours.endDateTime)
              }}
            </p>
          </div>

          <p v-else class="mt-3 text-danger">
            There are currently no time slots available.
          </p>

          <b-button variant="secondary" class="mt-3 mr-3" @click="toPart1"
            >Back</b-button
          >
          <b-button
            variant="primary"
            class="mt-3"
            :disabled="!hours.startDateTime"
            @click="toPart3"
            >Next</b-button
          >
        </div>

        <div v-if="formSection == 3">
          <p class="mb-3"><b>Confirm your modification</b></p>
          <p class="mt-3"><b>Selected technician: </b>{{ technicianEmail }}</p>
          <p class="mt-3">
            <b>Selected hours: </b
            >{{
              displayDayTime(hours.startDateTime) +
                " to " +
                displayDayTime(hours.endDateTime)
            }}
          </p>
          <p class="mt-3 text-danger">
            Please note that this cannot be undone.
          </p>

          <b-button variant="secondary" class="mt-3 mr-3" @click="toPart2"
            >Back</b-button
          >
          <b-button variant="danger" class="mt-3" @click="deleteTargetHours"
            >Confirm</b-button
          >
        </div>

        <div v-if="formSection == 4" class="text-center">
          <p class="mb-3 text-success">The selected hours have been deleted.</p>
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
      targetDate: "",
      hours: { startDateTime: "", endDateTime: "" },
      workHours: [],
      formSection: 1
    };
  },

  created: function() {
    // get all technicians
    axios
      .get(BACKEND + TECHNICIAN_ENDPOINT + "all", {
        headers: { token: this.$root.$data.token }
      })
      .then(r => {
        this.technicians = r.data;
        this.appError = "";
        if (this.technicians.length == 0) this.noTechnicians = true;
        else this.noTechnicians = false;
      })
      .catch(e => {
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
      if (this.technicianEmail) {
        // Get work hours
        axios
          .get(
            BACKEND +
              TECHNICIAN_ENDPOINT +
              this.technicianEmail +
              "/work_hours",
            {
              headers: { token: this.$root.$data.token }
            }
          )
          .then(r => {
            this.workHours = r.data;
            this.formSection = 2;
            this.appError = "";
          })
          .catch(e => {
            if (e.response.status == 500) this.appError = e.response.data;
            else this.appError = e;
          });
      } else this.error = "Please select a technician";
    },

    toPart3() {
      this.appError = "";
      this.error = "";
      if (this.hours) this.formSection = 3;
      else this.error = "Please select a time slot";
    },

    // Convert a Timestamp format (2021-03-02T15:00:00.000+00:00) to "Tue at 10:00" (in local timezone)
    displayDayTime(dateTime) {
      let date = new Date(dateTime).toString(); // Result: "Tue Mar 02 2021 10:00:00 GMT-0500 (Eastern Standard Time)"
      if (date == "Invalid Date") return "";
      else return date.slice(0, 3) + " at " + date.slice(16, 21);
    },

    deleteTargetHours() {
      // Delete specific work hours
      axios
        .post(
          BACKEND +
            TECHNICIAN_ENDPOINT +
            "delete/hours/" +
            this.technicianEmail,
          {
            startDateTime: this.hours.startDateTime,
            endDateTime: this.hours.endDateTime
          },
          {
            headers: { token: this.$root.$data.token }
          }
        )
        .then(r => {
          this.formSection = 4;
          this.appError = "";
        })
        .catch(e => {
          if (e.response.status == 400) this.appError = e.response.data;
          else this.appError = e;
        });
    }
  },

  watch: {
    // if a value is set, reset error
    technicianEmail: function(val, oldVal) {
      if (oldVal === "") this.error = "";
    },
    hours: function(val, oldVal) {
      if (oldVal === "") this.error = "";
    }
  }
};
</script>
