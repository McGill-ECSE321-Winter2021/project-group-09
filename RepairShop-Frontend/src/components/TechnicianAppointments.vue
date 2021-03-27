<template>
  <div id="technicianAppointments">

    <h1>Upcoming Appointments</h1>

    <div id="AppButton">
      <b-button type="button" variant="primary" @click="getAppointments">Get Appointments</b-button>
    </div>

    
    <div>
        <b-table :fields="fields" :items="items" responsive="sm">

            <template #cell(index)="data">
                {{ data.index + 1 }}
            </template>

        </b-table>
    </div>


    <b-card class="mt-3" header="Message">
      <pre class="m-0">{{ message }}</pre>
    </b-card>


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
        message : "",
        fields: [
          'index',
          'service',
          'start time',
          'end time',
          'customer name'
        ],
        
        items: [
            { 'service': 'default', 'date': 'default', 'start time': 'default', 'end time': 'default', 'customer name': "default"},
        ]
      }

    },
  
     
    methods: {

      getAppointments(event){
        var url = LOCALHOST_BACKEND + "/api/technician/" + this.$root.$data.email + "/appointments";
        var appList = [];

        axios.get(url, 
        {
          headers: {
            token: this.$root.$data.token
          }
        }).then(
          response => {
            
            if(response.data === "No upcoming appointments"){
              this.message = response.data;
            } else{

              var tempAppList = response.data;
              tempAppList.forEach((element) => {
                var app = {
                  'service': element.serviceDto.name,
                  'date': element.timeSlotDto.startDateTime.substring(0, 10),
                  'start time': element.timeSlotDto.startDateTime.substring(11, 16),
                  'end time': element.timeSlotDto.endDateTime.substring(11, 16),
                  'customer name': element.customerDto.name
                }
                appList.push(app);
              });

              
              this.items = appList;
                    
            }
            
          },
          error => {
            console.log(error.response.data); 
          }
        );

        
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
#AppButton {
  margin-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
  margin-bottom: 5%;
}
</style>