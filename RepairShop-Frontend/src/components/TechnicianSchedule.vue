<template>
<div>
      <h1>Weekly schedule</h1>
 <div id="technicianSchedule">

    <div id="datePicker">
      <b-form @submit="getSchedule">
        <div>
          <label for="schedule-datepicker">Choose a date</label>
          <b-form-datepicker id="schedule-datepicker" v-model="date" class="mb-2" :date-disabled-fn="dateDisabled"></b-form-datepicker>
        </div>
        <b-button type="submit" variant="primary">Get Schedule</b-button>
      </b-form>
    </div>

    <div>

      <b-table :fields="fields" :items="items" responsive="sm">
        <!-- A virtual composite column -->
        <template #cell(dayTime)="data"> {{ data.item }}. </template>
      </b-table>

    </div>

    <p v-if="noAppointments" style="color: blue">{{ noAppointments }}</p>
    <p v-else-if="errorAppointments" style="color: red">{{ errorAppointments }}</p>


  </div>
</div>
 
</template>

<script>
import { LOCALHOST_BACKEND } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      noAppointments: "",
      errorAppointments: "",
      date: "",
      fields: [{ key: "dayTime", label: "Day and Time" }],
      items: ["Default", "Default"]
    };
  },

  methods: {

    dateDisabled(ymd, date){
      const weekday = date.getDay();
      return weekday != 1;
    },

    getSchedule(event) {
      event.preventDefault();
      this.errorAppointments = "";
      this.noAppointments = "";

      var url =
        LOCALHOST_BACKEND +
        "/api/technician/" +
        this.$root.$data.email +
        "/schedule";
      var tempSchedule = [];

      axios
        .get(
          url,
          //changed wekStartDate to header. Need to test if it works
          {
            headers: {
              weekStartDate: this.date,
              token: this.$root.$data.token
            }
          }
        )
        .then(
          response => {
            var formattedSchedule = [];

            if (response.data === "No upcoming appointments") {
              this.noAppointments = response.data;
            } else {
              tempSchedule = response.data;
              tempSchedule.forEach(thisDayTime => {
                var date = thisDayTime.startDateTime.substring(0, 10);
                console.log(date);

                const dayOfWeek = new Date(date).getDay();
                var day = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"][dayOfWeek];

                var dayTime = day + " from " + thisDayTime.startDateTime.substring(11, 16) + " to " + thisDayTime.endDateTime.substring(11, 16);
                formattedSchedule.push(dayTime);
              });
              this.items = formattedSchedule;
            }
          },
          error => {
            console.log(error.response.data);
            this.errorAppointments = "Something went wrong. Please try again.";
          }
        );
    }
  }
};
</script>

<style>
#technicianSchedule {
  margin-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
}

#datePicker {
  margin-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
  margin-bottom: 5%;
}
</style>
