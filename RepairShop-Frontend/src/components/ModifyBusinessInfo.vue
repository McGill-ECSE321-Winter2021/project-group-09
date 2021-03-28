<template>
  <div id="modify_business">
    <b-form @submit="onSubmit">

      <b-form-group id="input-group-1" label="Email address:" label-for="input-1">
        <b-form-input id="input-1" v-model="form.email" type="email" ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="Business Name:" label-for="input-2">
        <b-form-input id="input-2" v-model="form.name" type="text" ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-3" label="Address:" label-for="input-3">
        <b-form-input id="input-3" v-model="form.address" type="text" ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-4" label="Phone Number:" label-for="input-4">
        <b-form-input id="input-4" v-model="form.phone" type="text" ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-5" label="Number of repair spots:" label-for="input-5">
        <b-form-input id="input-5" v-model="form.numRepairSpots" type="number"></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">Save changes</b-button>


    </b-form>

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
    methods: {
        onSubmit(event) {
            this.message = "";
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
                    this.message = "Business successfully updated"
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
#modify_business {
  margin-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>