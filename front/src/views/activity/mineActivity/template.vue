<template>
  <div>
    <el-button @click="returnTemplatePage">返回</el-button>

    某个模板{{ id }}
  </div>
</template>
<script>
export default {
  name: "TemplateView",
  data() {
    return {
      id: "",
    };
  },
  computed: {
    user_id: {
      get: function () {
        return this.$store.getters.id;
      },
      set: function (newVal) {},
    },
    user_name: {
      get: function () {
        return this.$store.getters.name;
      },
      set: function (newVal) {},
    },
  },
  methods: {
    //返回模板列表页面
    returnTemplatePage() {
      this.$router.push({ name: "MineActivity", query: { value: "template" } });
    },
  },
  mounted() {
    this.id = this.$route.query.id;
  },
};
</script>
<style lang="less" scoped>
</style>
