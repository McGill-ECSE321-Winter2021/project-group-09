<template>
  <div>
    <h1 v-show="success">Logged Out Successfully!</h1>
    <h1 v-show="!success">You were not logged in.</h1>
  </div>
</template>

<script>
import { LOGOUT_ENDPOINT, LOCALHOST_BACKEND } from "../constants/constants";
import axios from "axios";
export default {
  data() {
    return {
      success: true
    };
  },
  mounted() {
    if (this.$root.$data.email === null) {
      console.log(this.$root.$data.email);
      //user was not logged in
      this.success = false;
      this.$router.push("/login");
      return;
    }
    axios
      .post(LOCALHOST_BACKEND + LOGOUT_ENDPOINT, {
        email: this.$root.$data.email,
        password: this.$root.$data.password,
        userType: this.$root.$data.userType
      })
      .then(
        response => {
          this.$root.$data.email = null;
          this.$root.$data.password = null;
          this.$root.$data.userType = null;
          this.$root.$data.token = null;
          this.$router.push("/");
        },
        error => {
          console.error(error);
        }
      );
  }
};
</script>