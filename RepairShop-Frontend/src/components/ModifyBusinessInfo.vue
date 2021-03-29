<template>
  <div id="modify_business">
    <b-form @submit="onSubmit">

      <b-form-group id="input-group-1" label="Email address:" label-for="input-1">
        <b-form-input id="input-1" v-model="form.email" type="email"></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="Business Name:" label-for="input-2">
        <b-form-input id="input-2" v-model="form.name" type="text"></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-3" label="Address:" label-for="input-3">
        <b-form-input id="input-3" v-model="form.address" type="text"></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-4" label="Phone Number:" label-for="input-4">
        <b-form-input id="input-4" v-model="form.phone" type="text"></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-5" label="Number of repair spots:" label-for="input-5">
        <b-form-input id="input-5" v-model="form.numRepairSpots" type="number"></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">Save changes</b-button>


    </b-form>

    <div id="message">
      <p v-if="errorModifyBusinessInfo" style="color: red"> Error: {{ errorModifyBusinessInfo }}</p>
      <p v-else-if="successModifyBusinessInfo" style="color: green"> Success: {{ successModifyBusinessInfo }}</p>
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
        errorModifyBusinessInfo: "",
        successModifyBusinessInfo: "",
        form: {
          email: '',
          name: '',
          address: '',
          phone: '',
          numRepairSpots: ''
        },
        result: {
          email: '',
          name: '',
          address: '',
          phone: '',
          numRepairSpots: ''
        }
      }
    },

    created: function () {
          let url = LOCALHOST_BACKEND + "/api/business/info";
          axios.get(url).then(response => {
            this.form.email = response.data.email;
            this.form.address = response.data.address;
            this.form.phone = response.data.phoneNumber;
            this.form.numRepairSpots = response.data.numberOfRepairSpots;
            this.form.name = response.data.name;
          }, error => {
            console.log(error.response.data);
          });

          
    },

    methods: {  

        onSubmit(event) {
            event.preventDefault();
            this.errorModifyBusinessInfo = "";
            this.successModifyBusinessInfo = "";
            let url = LOCALHOST_BACKEND + "/api/business/update";
            axios.put(url, {
                email: this.form.email,
                name: this.form.name,
                address: this.form.address,
                phoneNumber: this.form.phone,
                numberOfRepairSpots: this.form.numRepairSpots
                }, 
                {
                  headers: {
                    token: this.$root.$data.token
                  }
                }
            ).then(
                response => {
                    this.successModifyBusinessInfo = "Business successfully updated."
                },
                error => {
                    console.log(error.response.data);
                    this.errorModifyBusinessInfo = error.response.data;
                }
            );
        }
      
    }
  }
</script>


<style>
#modify_business {
  margin-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
}
#message {
  margin-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>