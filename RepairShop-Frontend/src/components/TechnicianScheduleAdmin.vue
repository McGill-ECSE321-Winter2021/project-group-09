<template>
  <div id="technicianSchedule">
    <h1>Technician's weekly schedule</h1>

    <div id="dataInput">
      <b-form @submit="getSchedule">
        <div id="techEmailInput">
          <label for="tech-email">Enter technician's email</label>
          <b-form-input
            id="tech-email"
            v-model="techEmail"
            type="email"
          ></b-form-input>
        </div>

        <div id="datePicker">
          <label for="schedule-datepicker">Choose a date</label>
          <b-form-datepicker
            id="schedule-datepicker"
            v-model="date"
            class="mb-2"
          ></b-form-datepicker>
        </div>

        <b-button type="submit" variant="primary">Get Schedule</b-button>
      </b-form>
    </div>

    <div>
      <b-table :fields="fields" :items="items" responsive="sm">
        <!-- A virtual column -->
        <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>

        <!-- A virtual composite column -->
        <template #cell(dayTime)="data"> {{ data.item }}. </template>
      </b-table>
    </div>

    <b-card class="mt-3" header="Message">
      <pre class="m-0">{{ message }}</pre>
    </b-card>
  </div>
</template>

<script>
import { LOCALHOST_BACKEND } from "../constants/constants";
import axios from "axios";

export default {
  data() {
    return {
      techEmail: "",
      message: "",
      date: "",
      fields: ["index", { key: "dayTime", label: "Day and Time" }],
      items: ["Default", "Default"]
    };
  },

  methods: {
    getSchedule(event) {
      event.preventDefault();
      var url =
        LOCALHOST_BACKEND + "/api/technician/" + this.techEmail + "/schedule";
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
              this.message = response.data;
            } else {
              tempSchedule = response.data;
              tempSchedule.forEach(thisDayTime => {
                var date = thisDayTime.startDateTime.substring(0, 10);
                console.log(date);

                const dayOfWeek = new Date(date).getDay();
                var day = [
                  "Sunday",
                  "Monday",
                  "Tuesday",
                  "Wednesday",
                  "Thursday",
                  "Friday",
                  "Saturday"
                ][dayOfWeek];

                var dayTime =
                  day +
                  " from " +
                  thisDayTime.startDateTime.substring(11, 16) +
                  " to " +
                  thisDayTime.endDateTime.substring(11, 16);
                formattedSchedule.push(dayTime);
              });
              this.items = formattedSchedule;
            }
          },
          error => {
            console.log(error.response.data);
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

#dataInput {
  margin-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
  margin-bottom: 5%;
}
</style>
