<template>
    <main class="container">
        <h2 class="my-4">Book an Appointment</h2>

        <div v-if="formSection == 1">
          <b-form-group label="Select a service" class="mt-4">
              <b-form-radio v-for="s in services" :key="s.name" v-model="service" name="service" :value="s.name">
                {{ s.name + ", for " + s.duration + ", for " + s.price }}
              </b-form-radio>
          </b-form-group>
          <p class="mt-3">Selected service: {{ service }}</p>
          <b-button variant="outline-primary" class="mt-3" @click="toPart2">Next</b-button>
        </div>

        <div v-if="formSection == 2">
          <b-form-group label="Select a date and time" class="mt-4">
              <b-form-radio v-for="t in availableTimes" :key="t.startTime" v-model="start" name="service" :value="t.startTime">
                {{ t.startTime + " to " + t.endTime }}
              </b-form-radio>
          </b-form-group>
          <p class="mt-3">Selected start time: {{ start }}</p>
          <b-button variant="outline-secondary" class="mt-3 mr-3" @click="toPart1">Back</b-button>
          <b-button variant="outline-primary" class="mt-3" @click="toPart3">Next</b-button>
        </div>

        <div v-if="formSection == 3">
          <p class="mb-3">Confirm your appointment</p>
          <p class="mt-3">Selected service: {{ service }}</p>
          <p class="mt-3">Selected start time: {{ start }}</p>
          <b-button variant="outline-secondary" class="mt-3 mr-3" @click="toPart2">Back</b-button>
          <b-button variant="outline-primary" class="mt-3" @click="book">Book now</b-button>
        </div>

        <p class="text-danger mt-4" v-if="error">{{ error }}</p>

    </main>
</template>

<script>
  export default {
    data() {
      return {
        error: '',
        service: '',
        services: [
          { name: 'Service 1', duration: '1.5 hours', price: '$60' },
          { name: 'Service 2', duration: '3 hours', price: '$500' },
          { name: 'Service 3', duration: '2 hours', price: '$180' }
        ],
        start: '',
        availableTimes: [
          { startTime: "2021-03-02 10:00", endTime: "2021-03-02 12:30" },
          { startTime: "2021-03-02 10:30", endTime: "2021-03-02 13:00" },
          { startTime: "2021-03-02 11:00", endTime: "2021-03-02 14:00" },
          { startTime: "2021-03-02 11:30", endTime: "2021-03-02 14:30" }
        ],
        formSection: 1
      }
    },
    methods: {
      toPart1() { this.formSection = 1; },
      toPart2() {
        if (this.service) this.formSection = 2;
        else this.error = 'Please select a service';
      },
      toPart3() {
        if (this.start) this.formSection = 3;
        else this.error = 'Please select a start time';
      },
      book() {
        console.log("Appointment was booked");
      }
    },
    watch: { // if a service or start time is set, reset error
      service: function (val, oldVal) {
        if (oldVal === '') this.error = '';
      },
      start: function (val, oldVal) {
        if (oldVal === '') this.error = '';
      }
    }
  }
</script>