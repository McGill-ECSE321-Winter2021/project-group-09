<template>
  <div id="technicianSchedule">

    <div>
      <b-form @submit="getSchedule">

      <b-form-group id="input-group-2" label="Enter a date:" label-for="input-1">
        <b-form-input id="input-1" v-model="date" placeholder="Enter date" required></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">Get Schedule</b-button>

    </b-form>
    </div>

    <p></p>
    <p></p>

    <div>
      <b-table :fields="fields" :items="items" responsive="sm">
        <!-- A virtual column -->
        <template #cell(index)="data">
          {{ data.index + 1 }}
        </template>

        <!-- A virtual composite column -->
        <template #cell(dayTime)="data">
          {{ data.item }}.
        </template>

      </b-table>
    </div>


  </div>
</template>

<script>
  import {
    LOCALHOST_BACKEND
  } from "../constants/constants";
  import axios from "axios";
  
  
  export default {
    data() {
      
      return {
        date,
        fields: [
          'index',
          { key: 'dayTime', label: 'Day and Time' },
          
        ],
        items: ["Monday from 10:00 to 10:30", 
                "Tuesday from 10:00 to 10:30",
                ]
      }
    },

    
    methods: {

      getSchedule(event){
        var email = this.$root.$data.email;
        var token = this.$root.$data.token;
        var url = LOCALHOST_BACKEND + "api/technician/" + email + "/schedule";
        var tempSchedule;

        axios.get(url, this.date, token).then(
          response => {
            tempSchedule = response.data
          },
          error => {
            console.log(error); 
          }
        );

        var formattedSchedule = tempSchedule.map(thisDayTime => {
            var day = getDay(thisDayTime.startDateTime.substring(0, 10));
            var dayTime = day + thisDayTime.startDateTime.substring(11, 16) + thisDayTime.endDateTime.substring(11, 16);
            return dayTime;
        });
        
        if(formattedSchedule === null){
          this.items = formattedSchedule;
        }else {
          items = ["Default", "Default"];
        }
        
        
      }

    }
    
    
  }
</script>

<style>
#technicianSchedule {
  margin-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>