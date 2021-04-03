<template>
  <div>
      <h1> Register </h1>
    <div class="formContainer" id="registerForm">
      <b-form @submit="onSubmit" v-if="show" class="inputWidth">
        <b-form-group id="input-group-0" label="Full Name:" label-for="input-0" >
          <b-form-input
            id="input-0"
            v-model="form.name"
            type="text"
            placeholder="Enter Full Name"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group
          id="input-group-1"
          label="Email address:"
          label-for="input-1"
        >
          <b-form-input
            id="input-1"
            v-model="form.email"
            type="email"
            placeholder="Enter email"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group id="input-group-2" label="Password:" label-for="input-2">
          <b-form-input
            id="input-2"
            v-model="form.password"
            placeholder="Enter password"
            required
            type="password"
          ></b-form-input>
        </b-form-group>

        <!-- render this block if user logged in as admin -->
        <b-form-group
          id="input-group-3"
          label="User Type:"
          label-for="input-3"
          v-if="this.$root.$data.userType === 'Admin'"
        >
          <b-form-select
            id="input-3"
            v-model="form.userType"
            :options="userType"
            required
          ></b-form-select>
        </b-form-group>

        <!-- render this block if user is not admin -->
        <b-form-group
          id="input-group-3.1"
          label="User Type:"
          label-for="input-3.1"
          v-else
        >
          <b-form-select
            id="input-3.1"
            v-model="form.userType"
            :options="userType2"
            required
          ></b-form-select>
        </b-form-group>

        <b-form-group
          id="input-group-4"
          label="Phone Number:"
          label-for="input-4"
          description="Format xxx-xxx-xxxx"
        >
          <b-form-input
            id="input-4"
            v-model="form.phoneNumber"
            type="tel"
            pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}"
            placeholder="Enter Phone Number"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group id="input-group-5" label="Address:" label-for="input-5">
          <b-form-input
            id="input-5"
            v-model="form.address"
            type="text"
            placeholder="Enter Address"
            required
          ></b-form-input>
        </b-form-group>

        <!-- schedule of technician, only visible if user is admin and selects technician to register -->
        <div id="techSchedule"
          v-if="
            this.$root.$data.userType === 'Admin' &&
              form.userType === 'Technician'
          "
        >
          <div id="titleContainer">
            <h1>
              Technician's work schedule:
            </h1>
          </div>
          Sunday
          <b-form-timepicker
            v-model="form.techSchedule[0][0]"
            locale="en"
          ></b-form-timepicker>
          <b-form-timepicker
            v-model="form.techSchedule[0][1]"
            locale="en"
          ></b-form-timepicker>
          Monday
          <b-form-timepicker
            v-model="form.techSchedule[1][0]"
            locale="en"
          ></b-form-timepicker>
          <b-form-timepicker
            v-model="form.techSchedule[1][1]"
            locale="en"
          ></b-form-timepicker>
          Tuesday
          <b-form-timepicker
            v-model="form.techSchedule[2][0]"
            locale="en"
          ></b-form-timepicker>
          <b-form-timepicker
            v-model="form.techSchedule[2][1]"
            locale="en"
          ></b-form-timepicker>
          Wednesday
          <b-form-timepicker
            v-model="form.techSchedule[3][0]"
            locale="en"
          ></b-form-timepicker>
          <b-form-timepicker
            v-model="form.techSchedule[3][1]"
            locale="en"
          ></b-form-timepicker>
          Thursday
          <b-form-timepicker
            v-model="form.techSchedule[4][0]"
            locale="en"
          ></b-form-timepicker>
          <b-form-timepicker
            v-model="form.techSchedule[4][1]"
            locale="en"
          ></b-form-timepicker>
          Friday
          <b-form-timepicker
            v-model="form.techSchedule[5][0]"
            locale="en"
          ></b-form-timepicker>
          <b-form-timepicker
            v-model="form.techSchedule[5][1]"
            locale="en"
          ></b-form-timepicker>
          Saturday
          <b-form-timepicker
            v-model="form.techSchedule[6][0]"
            locale="en"
          ></b-form-timepicker>
          <b-form-timepicker
            v-model="form.techSchedule[6][1]"
            locale="en"
          ></b-form-timepicker>
        </div>
        <p v-if="this.successfulMessage" style="color: green">
          {{this.successfulMessage}}
        </p>
        <p v-if="this.techMessageF" style="color: red">
          There was an error
        </p>
        
        <b-button type="submit" variant="primary">Submit</b-button>
      </b-form>
    </div>
  </div>
</template>

<script>
import {
  REGISTER_CUSTOMER_ENDPOINT,
  REGISTER_ADMIN_ENDPOINT,
  REGISTER_TECHNICIAN_ENDPOINT,
  LOCALHOST_BACKEND
} from "../constants/constants";
import axios from "axios";
export default {
  data() {
    return {
      form: {
        name: "",
        phoneNumber: "",
        address: "",
        email: "",
        password: "",
        userType: null,
        techSchedule: [
          ["", ""],
          ["", ""],
          ["", ""],
          ["", ""],
          ["", ""],
          ["", ""],
          ["", ""]
        ]
      },
      userType: [
        { text: "Select One", value: null },
        "Admin",
        "Customer",
        "Technician"
      ],
      userType2: [{ text: "Select One", value: null }, "Customer"],
      show: true,
      techMessageS: false,
      techMessageF: false,
      successfulMessage:""
    };
  },
  methods: {
    //builds a list of validly entered work hours
    createSchedule() {
      let timeSlotDtos = [];
      let idx = 0;
      this.form.techSchedule.forEach(item => {
        if (item[0] != "" && item[1] != "") {
          timeSlotDtos.push({
            startDateTime: this.formatTimeToTimestamp(item[0], idx),
            endDateTime: this.formatTimeToTimestamp(item[1], idx)
          });
        }
        idx += 1;
      });
      return timeSlotDtos;
    },
    //formats the time to timestamp with the appropriate date based on 'day' param ( 0 : sunday ... 6: saturday)
    formatTimeToTimestamp(time, day) {
      let today = new Date();
      let todaysDayOfWeek = today.getDay();
      today.setDate(today.getDate() - (todaysDayOfWeek - day));
      let dd = String(today.getDate()).padStart(2, "0");
      let mm = String(today.getMonth() + 1).padStart(2, "0");
      let yyyy = today.getFullYear();
      today = yyyy + "-" + mm + "-" + dd;
      return today + "T" + time + "+00:00";
    },
    //send any validly entered time pairs as new work schedules to the technician
    addWorkHours() {
      var data = JSON.stringify({ timeSlots: this.createSchedule() });
      var config = {
        method: "post",
        url:
          "http://localhost:8080/api/technician/" +
          this.form.email +
          "/add_work_hours",
        headers: {
          token: this.$root.$data.token,
          "Content-Type": "application/json"
        },
        data: data
      };
      var that = this;
      axios(config)
        .then(function(response) {
          console.log(JSON.stringify(response.data));
          that.techMessageS = true;
        })
        .catch(function(error) {
          console.log(error);
          that.techMessageF = true;
        });
    },
    onSubmit(event) {
      event.preventDefault();
      this.techMessageS = false;
      this.techMessageF = false;
      let user_url = "";
      //determine which login endpoint to call based on usertype
      switch (this.form.userType) {
        case "Admin":
          user_url = REGISTER_ADMIN_ENDPOINT;
          break;
        case "Customer":
          user_url = REGISTER_CUSTOMER_ENDPOINT;
          break;
        case "Technician":
          user_url = REGISTER_TECHNICIAN_ENDPOINT;
          break;
        default:
          console.error("Invalid usertype");
          return;
      }
      axios
        .post(
          LOCALHOST_BACKEND + user_url,
          {
            email: this.form.email,
            password: this.form.password,
            userType: this.form.userType,
            name: this.form.name,
            phoneNumber: this.form.phoneNumber,
            address: this.form.address
          },
          {
            headers: {
              token: this.$root.$data.token
            }
          }
        )
        .then(
          response => {
            
            if (
              this.$root.$data.userType === "Admin" &&
              this.form.userType === "Technician"      
            ) {
              this.addWorkHours();
              this.successfulMessage="Successfully added work hours.\nAccount created for " +
                response.data.name +
                " with email " +
                response.data.email +
                ".\nConfirmation email sent.\nProceed to login.";
            }else{
              this.successfulMessage="Account created for " +
                response.data.name +
                " with email " +
                response.data.email +
                ".\nConfirmation email sent.\nProceed to login.";
            }


          },
          error => {
            console.log(error);
            if (error.response) {
              if (error.response.status === 400) {
                alert("Email already taken.");
              }
            }
          }
        );
    }
  }
};
</script>

<style>
#registerForm {
  margin-top: 2%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>
