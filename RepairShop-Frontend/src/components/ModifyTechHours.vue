<template>
  <div>
    <h1>Modify a Technician's Work Hours</h1>
    <main class="formContainer">
      <div class="inputWidth">
        <div v-if="formSection == 1" class="mt-4">
          <b-link to="deleteHours" class="my-3">Delete hours</b-link>

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

            <p class="mt-3"><b>Selected technician: </b>{{ technicianEmail }}</p>
          </div>

          <p v-else class="text-danger">
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

          <p class="mt-3"><b>Choose dates and times for the hours to add</b></p>

          <b-form-group
            label="Start date and time"
            description="Times must be rounded to the nearest half hour"
            class="mt-4 form-inline"
          >
            <b-form-select
              v-model="startDate"
              :options="weekDays"
            ></b-form-select>
            <b-form-input
              v-model="startTime"
              placeholder="Time (HH:MM)"
              class="mr-3"
            ></b-form-input>
          </b-form-group>

          <b-form-group
            label="End date and time"
            description="Times must be rounded to the nearest half hour"
            class="mt-4 form-inline"
          >
            <b-form-select
              v-model="endDate"
              :options="weekDays"
            ></b-form-select>
            <b-form-input
              v-model="endTime"
              placeholder="Time (HH:MM)"
              class="mr-3"
            ></b-form-input>
          </b-form-group>

          <b-button
            variant="secondary"
            class="mt-3 mr-3"
            @click="toPart1"
            >Back</b-button
          >
          <b-button variant="primary" class="mt-3" @click="toPart3"
            >Next</b-button
          >
        </div>

        <div v-if="formSection == 3" class="mt-4">
          <p class="mb-3"> <b>Confirm your modification</b> </p>
          <p class="mt-3"><b>Selected technician: </b>{{ technicianEmail }}</p>
          <p class="mt-3">
            <b>Selected hours to add: </b>
            {{
              displayDay(startDate) +
              " at " +
              startTime +
              " to " +
              displayDay(endDate) +
              " at " +
              endTime
            }}
          </p>

          <b-button
            variant="secondary"
            class="mt-3 mr-3"
            @click="toPart2"
            >Back</b-button
          >
          <b-button variant="danger" class="mt-3" @click="addHours"
            >Confirm</b-button
          >
        </div>

        <div v-if="formSection == 4" class="text-center mt-4">
          <p class="mb-3 text-success">The selected hours have been added.</p>
          <b-button variant="primary" class="mt-4" to="/"
            >Homepage</b-button
          >
        </div>

        <p class="text-danger mt-4" v-if="error">{{ error }}</p>
        <p class="text-danger mt-4" v-if="appError">{{ appError }}</p>
      </div>
    </main>
  </div>
</template>

<script>
import { LOCALHOST_BACKEND, TECHNICIAN_ENDPOINT } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      error: "",
      appError: "",
      technicianEmail: "",
      technicians: [],
      weekDays: [
        // Using known dates - days of the week will be mapped to future dates
        { text: "Monday", value: "2021-03-01" },
        { text: "Tuesday", value: "2021-03-02" },
        { text: "Wednesday", value: "2021-03-03" },
        { text: "Thursday", value: "2021-03-04" },
        { text: "Friday", value: "2021-03-05" },
        { text: "Saturday", value: "2021-03-06" },
        { text: "Sunday", value: "2021-03-07" },
      ],
      startDate: "2021-03-01", // Default is Monday
      endDate: "2021-03-01",
      startTime: "",
      endTime: "",
      startTimestamp: "",
      endTimestamp: "",
      formSection: 1,
    };
  },

  created: function () {
    // get all technicians
    axios
      .get(LOCALHOST_BACKEND + TECHNICIAN_ENDPOINT + "all", {
        headers: { token: this.$root.$data.token },
      })
      .then((r) => {
        this.technicians = r.data;
        this.appError = "";
      })
      .catch((e) => {
        this.appError = e;
      });
  },

  methods: {
    toPart1() {
      this.formSection = 1;
      this.appError=""; this.error="";
    },

    toPart2() {
      this.appError=""; this.error="";
      if (this.technicianEmail) {
        this.formSection = 2;
        this.appError = "";
      } else this.error = "Please select a technician";
    },

    toPart3() {
      this.appError=""; this.error="";
      if (this.validateTimes()) this.formSection = 3;
      else
        this.error =
          "Please specify times as HH:MM (ex: 09:00 or 17:30). The start date and time must also be before the end date and time.";
    },

    validateTimes() {
      let startTimeCheck =
        this.startTime.length === 5 &&
        this.startTime.search(/(0[0-9]|1[0-9]|2[0-3]):[0,3](0)/) === 0;
      let endTimeCheck =
        this.endTime.length === 5 &&
        this.endTime.search(/(0[0-9]|1[0-9]|2[0-3]):[0,3](0)/) === 0;
      if (startTimeCheck && endTimeCheck) {
        this.startTimestamp = Date.parse(this.startDate + " " + this.startTime);
        this.endTimestamp = Date.parse(this.endDate + " " + this.endTime);
        if (this.startTimestamp > this.endTimestamp) return false;
        else return true;
      } else return false;
    },

    displayDay(day) {
      for (let item of this.weekDays) {
        if (item.value == day) return item.text;
      }
    },

    addHours() {
      // Delete specific work hours
      axios
        .post(
          LOCALHOST_BACKEND +
            TECHNICIAN_ENDPOINT +
            this.technicianEmail +
            "/add_time_slot",
          {
            startDateTime: this.startTimestamp,
            endDateTime: this.endTimestamp,
          },
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
  },

  watch: {
    // if a value is set, reset error
    technicianEmail: function (val, oldVal) {
      if (oldVal === "") this.error = "";
    },
    startTime: function (val, oldVal) {
      this.error = "";
    },
    endTime: function (val, oldVal) {
      this.error = "";
    },
  },
};
</script>

<style>
a {
  cursor: pointer;
}
</style>