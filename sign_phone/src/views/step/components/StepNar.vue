<template>
    <step v-model="stepNumber" background-color='#fbf9fe'>
      <step-item title="基础信息"></step-item>
      <step-item title="完善信息"></step-item>
      <step-item title="在线交费"></step-item>
      <step-item title="完成报名"></step-item>
    </step>
</template>

<script>
  import { Step, StepItem } from 'vux'
  export default {
    name: "StepNar",
    props: {
      stepNumber: {
        type: Number,
        default: 0
      }
    },
    components: {
      Step,
      StepItem
    }
  }
</script>