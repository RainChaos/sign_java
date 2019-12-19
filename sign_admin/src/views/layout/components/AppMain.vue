<template>
  <section class="app-main" style="height: calc(100% - 80px)">
    <transition name="fade-transform" mode="out-in">
      <keep-alive >
        <router-view v-if="$route.meta.keepAlive"/>
      </keep-alive>
    </transition>
    <transition name="fade-transform" mode="out-in">
      <router-view v-if="!$route.meta.keepAlive"/>
      <!--<router-view/>-->
    </transition>
  </section>
</template>
<script>
export default {
  name: 'AppMain'
}
</script>
