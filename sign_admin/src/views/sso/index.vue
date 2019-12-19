<template>
<div>

</div>
</template>

<script>
import {sso} from '@/api/login'
export default {
  data() {
    return {
      name: ''
    }
  },
  mounted() {
    this.login()
  },
  methods: {
    login(){
      let _this = this
      this.$store.dispatch('sso', {token: this.$route.query.token, appId: this.$route.query.appId}).then(response => {
        this.loading = false
        const data = response.data
        localStorage.setItem("info", JSON.stringify(data))
        // const sl = data.is_one === 1
        if (data.is_one) {
          this.$router.replace({ path: data.default_url, query: { sl: sl }})
        } else {
          this.$router.replace({ path: data.default_url })
        }
      }).catch(error => {
        // console.log(456)
        _this.$router.push({path: '/login'})
        _this.loading = false
      })
    }
  }
}
</script>
