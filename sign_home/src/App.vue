<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>
<style>
  #nprogress .bar {
    background: lightskyblue !important;
  }
  #nprogress .peg {
    box-shadow: 0 0 10px lightskyblue, 0 0 5px lightskyblue;
    opacity: 1.0;
  }
  #app{
    height:100%;
  }
</style>
