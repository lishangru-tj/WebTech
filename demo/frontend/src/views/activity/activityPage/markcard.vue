<template>
  <el-card class="mc_view">
    <h2 class="mc_rate_hint">给这个活动评分吧！</h2>
    <el-rate
      class="mc_rate_rater"
      v-model="activity_rate_val"
      :colors="rate_colors"
      :disabled="hadrate"
    >
    </el-rate>

    <el-button
      v-if="!hadrate"
      class="mc_rate_confirm"
      type="primary"
      @click="ClickPostMark"
      >确认</el-button
    >
  </el-card>
</template>

<script>
import { postActMark } from "@/api/mark";
export default {
  name: "ActivityMarkCard",
  data() {
    return {
      activity_rate_val: 0,
      rate_colors: ["#99A9BF", "#F7BA2A", "#FF9900"],
    };
  },
  props: {
    /**
     * 用户个人信息
     */
    user_id: {
      type: String,
      required: true,
      default: () => {
        return -1;
      },
    },
    activity_id: {
      type: String,
      required: true,
      default: () => {
        return -1;
      },
    },
    hadrate: {
      type: Boolean,
      required: true,
      default: () => {
        return false;
      },
    },
    showVal: {
      type: Number,
      required: false,
      default: () => {
        return 0;
      },
    },
  },
  methods: {
    test() {
      // console.log("activity_rate_val", this.activity_rate_val);
      // console.log("showVal", this.showVal);
    },
    ClickPostMark() {
      //弹窗确认
      const h = this.$createElement;
      this.$msgbox({
        title: "确认评分",
        // message:
        // h("p", null, [
        //   h("span", null, "将把管理员 "),
        //   h("i", { style: "color: teal" }, mem.name),
        //   h("span", null, " 降级为一般成员"),
        // ]),
        // dangerouslyUseHTMLString: true,
        showCancelButton: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        beforeClose: async (action, instance, done) => {
          //点击了 确认
          if (action === "confirm") {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = "执行中...";
            //向后端接口请求
            //拼凑post data
            let now = new Date();
            let mark_time = now.toLocaleString("zh-CN").replace(/[/]/, "-");
            mark_time = mark_time.replace(/[/]/, "-");
            let post_data = {
              user_id: this.user_id,
              activity_id: this.activity_id,
              mark_time: mark_time,
              mark: this.activity_rate_val,
            };
            await postActMark(post_data)
              .then((res) => {
                console.log("postActMark:res:", res);
                this.$message({
                  type: "success",
                  message: "活动评分成功",
                });
                this.ConfirmRate();
              })
              .catch((err) => {
                console.log("postActMark:err:", err);
              })
              .finally(() => {
                instance.confirmButtonLoading = false;
                done();
              });
          } else {
            done();
          }
        },
      }).then((action) => {
        // this.$message({
        //   type: "info",
        //   message: "action: " + action,
        // });
      });
    },

    //emit event
    ConfirmRate() {
      this.$emit("MarkActivity", this.activity_rate_val);
    },
  },
  mounted() {},
  created() {
    this.activity_rate_val = this.showVal;
    // this.test();
  },
};
</script>
<style lang="less" scoped>
.mc_view {
  width: 40%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  .mc_rate_hint {
  }
  .mc_rate_rater {
    /deep/ .el-rate__icon {
      font-size: 35px;
    }
  }
  .mc_rate_confirm {
    margin-top: 2em;
  }
}
</style>