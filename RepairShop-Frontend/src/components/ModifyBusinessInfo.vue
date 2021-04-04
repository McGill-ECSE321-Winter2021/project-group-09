<template>
  <div>
    <h1>Modify Business Information</h1>
    <div class="formContainer" id="modify_business">
      <b-form @submit="onSubmit" class="inputWidth">
        <b-form-group
          id="input-group-2"
          label="Business Name:"
          label-for="input-2"
        >
          <b-form-input
            id="input-2"
            v-model="form.name"
            type="text"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group
          id="input-group-4"
          label="Phone Number:"
          label-for="input-4"
          description="Format xxx-xxx-xxxx"
        >
          <b-form-input
            id="input-4"
            v-model="form.phone"
            type="tel"
            pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group
          id="input-group-1"
          label="Email Address:"
          label-for="input-1"
        >
          <b-form-input
            id="input-1"
            v-model="form.email"
            type="email"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group id="input-group-3" label="Address:" label-for="input-3">
          <b-form-input
            id="input-3"
            v-model="form.address"
            type="text"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group
          id="input-group-5"
          label="Number of Repair Spots:"
          label-for="input-5"
        >
          <b-form-input
            id="input-5"
            v-model="form.numRepairSpots"
            type="number"
            required
          ></b-form-input>
        </b-form-group>
        <p v-if="errorModifyBusinessInfo" style="color: red">
          Error: {{ errorModifyBusinessInfo }}
        </p>
        <p v-else-if="successModifyBusinessInfo" style="color: green">
          Success: {{ successModifyBusinessInfo }}
        </p>
        <b-button type="submit" variant="primary">Save changes</b-button>
      </b-form>
    </div>
  </div>
</template>

<script>
import { BACKEND } from "../constants/constants";
import axios from "axios";
export default {
  data() {
    return {
      errorModifyBusinessInfo: "",
      successModifyBusinessInfo: "",
      form: {
        email: "",
        name: "",
        address: "",
        phone: "",
        numRepairSpots: ""
      },
      result: {
        email: "",
        name: "",
        address: "",
        phone: "",
        numRepairSpots: ""
      }
    };
  },

  created: function() {
    if (this.$root.$data.userType!='Admin') this.$router.push("/");

    let url = BACKEND + "/api/business/info";
    axios.get(url).then(
      response => {
        this.form.email = response.data.email;
        this.form.address = response.data.address;
        this.form.phone = response.data.phoneNumber;
        this.form.numRepairSpots = response.data.numberOfRepairSpots;
        this.form.name = response.data.name;
      },
      error => {
        console.log(error.response.data);
      }
    );
  },

  methods: {
    onSubmit(event) {
      event.preventDefault();
      this.errorModifyBusinessInfo = "";
      this.successModifyBusinessInfo = "";
      let url = BACKEND + "/api/business/update";
      axios
        .put(
          url,
          {
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
        )
        .then(
          response => {
            this.successModifyBusinessInfo = "Business successfully updated.";
            this.$root.$emit("updateName",this.form.name);
          },
          error => {
            console.log(error.response.data);
            this.errorModifyBusinessInfo =
              "Something went wrong. Please try again.";
          }
        );
    }
  }
};
</script>

<style></style>
