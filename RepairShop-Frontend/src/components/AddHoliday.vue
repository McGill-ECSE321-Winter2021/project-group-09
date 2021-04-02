
<template>
  <div id="addHolidayForm">
    <h2>Add New Holiday</h2>

    <p>
      <strong>
        <span v-if="successAddHoliday" style="color: #04571b">
          {{ successAddHoliday }}
        </span>
      </strong>
    </p>

    <b-form @submit="onSubmit">
    <div>
        <b-form-group class="d-inline mr-5" 
            label="Start Date:"
        >
            <b-calendar v-model="startDate" @context="onCalandarStartDateTime" locale="en-US"></b-calendar>
        </b-form-group>

        <b-form-group class="d-inline"
            label="End Date:"
        >
            <b-calendar v-model="endDate" @context="onCalandarEndDateTime" locale="en-US"></b-calendar>
        </b-form-group>
    </div>
        <p v-if="errorAddHoliday" style="color: red">
            Error: {{ errorAddHoliday}}
        </p>

        <b-button type="submit" variant="primary"> Add Holiday</b-button>
    </b-form>
  </div>
</template>

<script>
import axios from "axios";

var config = require("../../config");
var AXIOS = axios.create({
  baseURL: "http://" + config.dev.backendHost + ":" + config.dev.backendPort
});

export default {
  data() {
    return {
        startDate: "", // specific format
        endDate: "",
        calandarStartDateTime : "", //  bunch of different formats from calandar
        calandarEndDateTime : "",
        errorAddHoliday: "",
        successAddHoliday: ""
    };
  },
  methods: {
    onSubmit(event) {
      event.preventDefault();

      if(this.calandarStartDateTime.activeDate>this.calandarEndDateTime.activeDate){
        this.errorAddHoliday ="The start date and time must be before the end date and time.";
      } else{
              AXIOS.post(
        "api/business/create/holidays",
        {
          startDateTime : this.calandarStartDateTime.activeDate,  
          endDateTime : this.calandarEndDateTime.activeDate
        },
        {
          headers: {
            token: this.$root.$data.token
          }
        })
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
        this.calandarStartDateTime = startDateTime
    },
    onCalandarEndDateTime(endDateTime) {
        this.calandarEndDateTime = endDateTime
    }
  }
};
</script>

<style>
#addHolidayForm {
  margin-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>
