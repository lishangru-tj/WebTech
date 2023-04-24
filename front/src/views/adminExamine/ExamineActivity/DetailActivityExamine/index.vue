
<template>
  <div class="views">
    <!--举报单信息-->
    <div>
      <el-descriptions
        title="活动举报单"
        :column="2"
        border
      >
        <el-descriptions-item label="举报单ID">{{ form.reportId }}
        </el-descriptions-item>
        <el-descriptions-item label="举报人ID">{{ form.informerId }}
        </el-descriptions-item>

        <el-descriptions-item label="活动ID">{{ form.activityId }}
        </el-descriptions-item>
        <el-descriptions-item label="举报分类">{{
          form.type
        }}</el-descriptions-item>
        <el-descriptions-item label="举报时间">{{ form.reportTime }}
        </el-descriptions-item>
        <el-descriptions-item label="举报详述">{{ form.detail }}
        </el-descriptions-item>
        <el-descriptions-item label="举报配图">
          <!-- 配图展示 -->
          <div class="images">
            <el-image
              style="margin-right: 1em; height: 20em"
              v-for="(img, img_i) in image_urlList"
              :key="img_i"
              :src="img"
            >
              <div
                slot="error"
                class="image-slot"
              >
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <!--活动信息-->
    <div>
      <el-descriptions
        title="活动详情"
        :column="2"
        border
      >
        <el-descriptions-item label="活动ID">{{ activityForm.activity_id }}
        </el-descriptions-item>
        <el-descriptions-item label="活动名字">{{ activityForm.title_name }}
        </el-descriptions-item>
        <el-descriptions-item label="参与容量">{{ activityForm.capacity }}
        </el-descriptions-item>

        <el-descriptions-item label="简介">{{ activityForm.summary }}
        </el-descriptions-item>

        <!-- <el-descriptions-item label="文字说明">{{ activityForm.detail_text }}
        </el-descriptions-item> -->

        <el-descriptions-item label="点击量">{{ activityForm.hit_num }}
        </el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ activityForm.start_time }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <!--惩罚形式选择-->
    <div>
      管理员裁决
      <el-select
        v-model="form.punishType"
        placeholder="请选择"
      >
        <el-option
          v-for="item in punishment"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
          <span style="float: left">{{ item.label }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{
            item.value
          }}</span>
        </el-option>
      </el-select>
    </div>

    <!--确认/取消按钮-->
    <div style="
        position: relative;
        left: 50%;
        width: 400px;
        height: 400px;
        margin-left: -200px;
        display: block;
      ">
      <el-button
        type="primary"
        @click="onSubmit()"
        style="margin-right: 1em"
      >提交</el-button>
      <el-button @click="Cancel()">取消</el-button>
    </div>
  </div>
</template>

<script>
import { patchOneReport, getOneActivity } from "@/api/admin";

export default {
  name: "DetailActivityExamine",
  components: {},
  data () {
    return {
      flag: true,
      image_url: "",
      punishment: [
        {
          value: "0",
          label: "不作惩罚",
        },
        {
          value: "1",
          label: "封禁活动",
        },
      ],

      punishType: "",
      form: {},
      activityForm: {},
      his_obj: {},
      loading: false,
      form: "",
      image_urlList: [],
    };
  },
  computed: {
    admin_id: {
      get: function () {
        return this.$store.getters.id;
      },
    },
    admin_name: {
      get: function () {
        return this.$store.getters.name;
      },
    },
  },
  methods: {
    onSubmit () {
      //审核单没填完整就直接跳出
      if (this.form.punishType == null || this.form.punishType == "") {
        this.$message({
          type: "info",
          message: "审核单未填写裁决方式！",
        });
        return false;
      }

      //向后端发送patch申请
      this.form["state"] = 1;

      let patch_form = {
        report_id: this.form.reportId,
        admin_id: this.admin_id,
        state: "1",
        handle_operation: this.form.punishType,
      };
      console.log("patch_form:", patch_form);
      patchOneReport(patch_form)
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });

      this.$message({
        type: "success",
        message: "审核完成！",
      });
      this.$router.push({ path: "/adminExamineActivity/index" });

      return true;
    },

    Cancel () {
      this.$message({
        type: "info",
        message: "取消审核",
      });
      this.dialogVisible = false;
      this.$router.push({ path: "/adminExamineActivity/index" });
    },
    FormIsFull () {
      if (this.form.PUNITIVEMEASURE == "" || this.form.ISREAL == "")
        return false;
      return true;
    },

    // 切割images字符串，直接通过url访问图片
    GetImgUrl () {
      const images = this.form.image;
      const image_urlList = [];
      if (images != "") {
        const image_paths = images.split(":");

        for (let i = 0; i < image_paths.length; i++) {
          /** 直接通过url访问图片 */
          let image_url;
          const image_path = image_paths[i];

          if (image_path.length > 0) {
            image_url = process.env.VUE_APP_BASE_API + "/api" + image_path;
            // image_url = "http://42.192.45.95:8081/api" + image_path;

            console.log("image_url", image_url);
            image_urlList.push(image_url);
          }
        }
      }
      this.image_urlList = image_urlList;
    },
  },
  mounted: function () {
    //上一个页面传来的参数
    let reportDetail = this.$route.query.res.data.report;
    this.form = reportDetail;
    this.GetImgUrl();
    for (let i in this.form) {
      if (this.form[i] == "" || this.form[i] == null) this.form[i] = "未知";
    }
    // this.getPhoto(this.form.ID);

    console.group("测试")
    console.log(reportDetail)
    console.groupEnd("测试")

    //根据活动ID，请求活动详情
    getOneActivity(reportDetail.activityId)
      .then((res) => {
        this.activityForm = res.data;
        for (let i in this.activityForm) {
          if (this.activityForm[i] == null || this.activityForm[i] == "")
            this.activityForm[i] = "无";
        }
        console.log("获取的活动详情为：", this.activityForm)
      })
      .catch((err) => {
        console.log(err);
      })

  },
};
</script>

// lang选择less语法，scoped限制该样式只在本文件使用，不影响其他组件
<style lang="less" scoped>
.myimage {
  width: 600px;
  height: 400px;
}
</style>
