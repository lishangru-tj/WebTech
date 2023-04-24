<template>
  <div class="actpi_view">
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="活动概览" name="overview">
        <ActivityPageOverView></ActivityPageOverView
      ></el-tab-pane>
      <el-tab-pane label="讨论" name="chat" v-if="is_member">
        <el-row>
          <el-col :span="16">
            <el-card class="card-item">
              <BigChatBox></BigChatBox>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="card-item">
              <Forum></Forum>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import ActivityPageOverView from "./overview.vue";
import Forum from "@/components/forum/index.vue";
import BigChatBox from "@/components/BigChatBox/index.vue";
import { getIsMember } from "@/api/activity";
export default {
  name: "ActivityPage",
  components: { ActivityPageOverView, BigChatBox, Forum },
  data() {
    return {
      activeName: "overview",
      activity_id: -1,
      is_member: false,
    };
  },
  methods: {
    // 用户是不是这个活动的参与者
    async Get_isMember() {
      console.log("this.activity_id:", this.activity_id);
      await getIsMember(this.user_id, this.activity_id)
        .then((res) => {
          console.log("IsMember:res:", res);
          this.is_member = res.data.is_member;
        })
        .catch((err) => {
          console.log("IsMember:err:", err);
        });
    },
  },
  async mounted() {
    this.activity_id = this.$route.query.id;
    await this.Get_isMember();

    console.log("activityPage:index:this.activity_id:", this.activity_id);
  },

  computed: {
    /**
     * 用户个人信息
     */
    user_id: {
      get: function () {
        return this.$store.getters.id;
      },
    },
    user_name: {
      get: function () {
        return this.$store.getters.name;
      },
    },
  },
};
</script>
<style lang="less" scoped>
.actpi_view {
}
.card-item {
  background: white;
  border-radius: 10px;
  margin-bottom: 15px;
  margin-right: 10px;
  margin-left: 10px;
  margin-top: 5px;
  padding: 5px;
}
</style>>


