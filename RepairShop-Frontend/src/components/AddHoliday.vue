<template>
  <div>
    <h1>Add a Holiday</h1>

    <div class="formContainer" id="addHolidayForm">
      <b-form @submit="onSubmit">
        <div>
          <div class="d-inline-block mr-5" style="max-width: 300px">
            <b-form-group label="Start Date:">
              <b-calendar
                v-model="startDate"
                @context="onCalandarStartDateTime"
                locale="en-US"
              ></b-calendar>
            </b-form-group>

            <b-form-group label="Start Time:">
              <b-form-input
                v-model="startTime"
                placeholder="Time (HH:MM)"
              ></b-form-input>
            </b-form-group>
          </div>

          <div class="d-inline-block" style="max-width: 300px">
            <b-form-group label="End Date:">
              <b-calendar
                v-model="endDate"
                @context="onCalandarEndDateTime"
                locale="en-US"
              ></b-calendar>
            </b-form-group>

            <b-form-group label="End Time:">
              <b-form-input
                v-model="endTime"
                placeholder="Time (HH:MM)"
              ></b-form-input>
            </b-form-group>
          </div>
        </div>

        <p v-if="errorAddHoliday" style="color: red">
          Error: {{ errorAddHoliday }}
        </p>
        <p v-if="successAddHoliday" style="color: #04571b">
          {{ successAddHoliday }}
        </p>

        <b-button type="submit" variant="primary"> Add Holiday</b-button>
      </b-form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { BACKEND } from "../constants/constants";

var config = require("../../config");
var AXIOS = axios.create({
  baseURL: BACKEND
});

const clearErrors = function(val, oldVal) {
  this.errorAddHoliday = "";
  this.successAddHoliday = "";
};
export default {
  data() {
    return {
      startDate: "", // specific format
      endDate: "",
      calandarStartDateTime: "", //  bunch of different formats from calandar
      calandarEndDateTime: "",
      errorAddHoliday: "",
      successAddHoliday: "",
      startTime: "",
      endTime: "",
      startTimestamp: "",
      endTimestamp: ""
    };
  },
  created: function() {
    if (this.$root.$data.userType != "Admin") this.$router.push("/");
  },
  methods: {
    onSubmit(event) {
      event.preventDefault();

      if (
        this.calandarStartDateTime.activeDate >
        this.calandarEndDateTime.activeDate
      ) {
        this.errorAddHoliday =
          "The start date and time must be before the end date and time.";
      } else if (!this.validateTimes()) {
        this.errorAddHoliday = "Invalid time entered.";
      } else {
        AXIOS.post(
          "api/business/create/holidays",
          {
            startDateTime: this.startTimestamp,
            endDateTime: this.endTimestamp
          },
          {
            headers: {
              token: this.$root.$data.token
            }
          }
        )
          .then(response => {
            this.errorAddHoliday = "";

            this.successAddHoliday =
              "The new holiday has been added successfully ✓";
          })
          .catch(e => {
            this.errorAddHoliday = e.response.data;
            this.successAddHoliday = "";
          });
      }
    },
    onCalandarStartDateTime(startDateTime) {
      this.calandarStartDateTime = startDateTime;
    },
    onCalandarEndDateTime(endDateTime) {
      this.calandarEndDateTime = endDateTime;
    },
    validateTimes() {
      let startTimeCheck =
        this.startTime.length === 5 &&
        this.startTime.search(/(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])/) === 0;
      let endTimeCheck =
        this.endTime.length === 5 &&
        this.endTime.search(/(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])/) === 0;
      if (startTimeCheck && endTimeCheck) {
        this.startTimestamp = Date.parse(this.startDate + " " + this.startTime);
        this.endTimestamp = Date.parse(this.endDate + " " + this.endTime);
        if (this.startTimestamp > this.endTimestamp) return false;
        else return true;
      } else return false;
    }
  },

  watch: {
    startDate: clearErrors,
    endDate: clearErrors,
    startDate: clearErrors,
    startTime: clearErrors
  }
};
</script>

<style></style>
