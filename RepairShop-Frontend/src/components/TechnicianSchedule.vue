<template>
  <div id="technicianSchedule">

    <div id="datePicker">
      <b-form @submit="getSchedule">

        <div>
          <label for="schedule-datepicker">Choose a date</label>
          <b-form-datepicker id="schedule-datepicker" v-model="date" class="mb-2"></b-form-datepicker>
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
        var url = LOCALHOST_BACKEND + "/api/technician/" + this.$root.$data.email + "/schedule";
        var tempSchedule;

        axios.get(url,
        //changed wekStartDate to header. Need to test if it works
        {
          headers: {
            weekStartDate: this.date,
            token: this.$root.$data.token
          }
        }
        ).then(
          response => {
            tempSchedule = response.data

            var formattedSchedule = tempSchedule.map(thisDayTime => {
              var day = getDay(thisDayTime.startDateTime.substring(0, 10));
              var dayTime = day + thisDayTime.startDateTime.substring(11, 16) + thisDayTime.endDateTime.substring(11, 16);
              return dayTime;
            });
        
            if(formattedSchedule === null){
              this.items = formattedSchedule;
            }else {
              this.items = ["Default", "Default"];
            }
          },
          error => {
            console.log(error); 
          }
        );
 
        
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

#datePicker{
  margin-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
  margin-bottom: 5%;
}
</style>