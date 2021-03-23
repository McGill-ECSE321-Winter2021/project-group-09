<template>
  <div>
    <keep-alive>
      <top-navbar />
    </keep-alive>
    <router-view></router-view>
  </div>
</template>

<script>
import TopNavbar from "./components/TopNavbar.vue";
export default {
  components: { TopNavbar },
  name: "app"
};
</script>

<style></style>
