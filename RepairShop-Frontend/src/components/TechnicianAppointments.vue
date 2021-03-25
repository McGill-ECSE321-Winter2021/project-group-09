<template>
  <div id="technicianAppointments">

    <div>
      <b-button type="button" variant="primary" @click="getAppointments">Get Appointments</b-button>
    </div>

    <p></p>
    <p></p>
    
    <div>
        <b-table :fields="fields" :items="items" responsive="sm">

            <template #cell(index)="data">
                {{ data.index + 1 }}
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
        fields: [
          'index',
          'service',
          'start time',
          'end time',
          'customer name'
        ],
        
        items: [
            { service: 40, 'date': '2021-04-02', 'start time': 'Dickerson', 'end time': 'Macdonald', 'customer name': "customer1"},
        ]
      }

    },
  
     
    methods: {

      getAppointments(event){
        var email = this.$root.$data.email;
        var token = this.$root.$data.token;
        var url = LOCALHOST_BACKEND + "api/technician/" + email + "/appointments";
        var tempAppList;

        axios.get(url, token).then(
          response => {
            tempAppList = response.data
          },
          error => {
            console.log(error); 
          }
        );

        var appointments = tempAppList.map(thisApp => {
            var app = {
                service: thisApp.serviceDto.name,
                'date': thisApp.timeSlotDto.startDateTime.substring(0, 10),
                'start time': thisApp.timeSlotDto.startDateTime.substring(11, 16),
                'end time': thisApp.timeSlotDto.endDateTime.substring(11, 16),
                'customer name': thisApp.customerDto.name
            }
            this.items = app;
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
#technicianAppointments {
  margin-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>