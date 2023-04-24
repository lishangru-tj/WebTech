/*举报单组件
creator:黄彦铭
editor:黄彦铭
需要提交举报单的模块都可以使用这个组件，提交之后的操作都由管理员模块来处理，你们的模块只需要跳出这样一张举报单
不管是哪个模块填写的举报单，最终传给管理员的数据都是那几个，区别只在于哪些信息由用户填写

selection参数用于选择按钮的样式，这个样式是你们告诉我然后我再写的
传入则代表这些参数在举报单中写死了，不传入则代表这个信息由用户填写
至于哪些信息需要用户填写，看各个模块的具体需求

组件有以下3个参数：
1.selection              选择按钮样式。这个样式你们选好了告诉我
2.targetType            决定是活动举报单还是用户举报单，填入字符"0"是举报活动，填入字符"1"是举报用户
3.userId / activityId    选其一传入，取决于你举报活动还是用户
*/

<template>
  <div>
    <el-button
      v-if="selection == 'test'"
      @click="dialogOpen()"
      size="mini"
      type="danger"
      >提交举报单</el-button
    >

    <el-dialog
      class="serviceDialog"
      title="举报单提交"
      :visible.sync="dialogVisible"
      width="1000px"
      append-to-body
    >
      <div class="dialog-content">
        <el-form
          :model="form"
          :rules="rules"
          ref="form"
          label-width="200px"
          class="demo-ruleForm"
        >
          <el-form-item v-if="targetType == '1'" class="center">
            <h2>将举报用户：{{ name }}</h2>
          </el-form-item>
          <el-form-item v-else-if="targetType == '0'" class="center">
            <h2>将举报活动：{{ name }}</h2>
          </el-form-item>

          <!--多选框-->
          <el-form-item label="举报分类" prop="type" v-if="targetType == '0'">
            <el-select v-model="form.type" placeholder="请选择">
              <el-option
                v-for="item in activityReportTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item
            label="举报分类"
            prop="type"
            v-else-if="targetType == '1'"
          >
            <!--多选框-->
            <el-select v-model="form.type" placeholder="请选择">
              <el-option
                v-for="item in userReportTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="举报详述" prop="detail">
            <el-input
              type="textarea"
              :rows="5"
              v-model="form.detail"
              placeholder="请输入内容"
            ></el-input>
          </el-form-item>

          <el-form-item label="举报截图" prop="pic">
            <UploadImg
              @FileChange="HandleUploadImgInput"
              :limit="3"
            ></UploadImg>
          </el-form-item>
        </el-form>
        <!-- 底部的slot插槽 -->
      </div>
      <span slot="footer" class="dialogFooter">
        <el-button type="primary" @click="onSubmit()">确认</el-button>
        <el-button @click="Cancel()">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { postFile } from "@/api/file";
import UploadImg from "@/components/UploadImg";
import { postOneReport } from "@/api/admin.js";
export default {
  name: "ReportBox",
  components: { UploadImg },
  data() {
    return {
      dialogVisible: false,
      file_formData: "",
      form: {
        informerId: this.$store.getters.id,
        activityId: this.activityId,
        userId: this.userId,
        type: "string",
        detail: "string",
        image: "string",
        targetType: this.targetType,
      },

      userReportTypes: [
        {
          value: "言语粗俗",
          label: "言语粗俗",
        },
        {
          value: "传播谣言",
          label: "传播谣言",
        },
        {
          value: "涉嫌诈骗",
          label: "涉嫌诈骗",
        },
        {
          value: "恶意骚扰",
          label: "恶意骚扰",
        },
        {
          value: "其他",
          label: "其他",
        },
      ],

      activityReportTypes: [
        {
          value: "聚众赌博",
          label: "聚众赌博",
        },
        {
          value: "邪教传播",
          label: "邪教传播",
        },
        {
          value: "非法组织",
          label: "非法组织",
        },
        {
          value: "贩卖毒品",
          label: "贩卖毒品",
        },
        {
          value: "其他",
          label: "其他",
        },
      ],

      rules: {},
    };
  },
  //先执行props传参，再执行上面form中的初始化，然后再执行mounted
  props: ["selection", "targetType", "informerId", "userId", "activityId","name"],

  watch: {
    dialogVisible: {
      immediate: true,
      deep: true,
      handler: function (newVal) {
        this.dialogVisible = newVal;
      },
    },
  },

  methods: {
    //接收组件传来的formData
    HandleUploadImgInput(FormData) {
      this.file_formData = FormData;
      console.log("this.file_formData", this.file_formData);
    },
    dialogOpen() {
      this.dialogVisible = true;
    },
    onSubmit() {
      this.$msgbox("提交中...", "举报单提交", {
        confirmButtonText: "正在努力提交中",
        confirmButtonLoading: true,
      });

      //post给后端
      this.form.activityId = this.activityId;
      this.form.userId = this.userId;
      console.log("post report:form", this.form);
      postOneReport(this.form)
        .then(async (res) => {
          console.log("postOneReport:res:", res);
          //report id
          let report_id = res.data.report.reportId;
          // 组装upload的formData

          if (this.file_formData) {
            this.file_formData.append("folderPath", "/report/illus");

            this.file_formData.append("entity", "report");
            this.file_formData.append("id", report_id);
          }

          //上传图片,并额外传递参数，使得这些图片与report绑定
          await postFile(this.file_formData)
            .then((res) => {
              console.log("postFile:res:", res);
            })
            .catch((err) => {
              console.log("postFile:err:", err);
            });

          this.$msgbox.close();
          this.$message({
            type: "success",
            message: "举报单提交成功！",
          });
          this.dialogVisible = false;

          //this.uploadPhoto(res.data.message.id);
        })
        .catch((err) => {
          console.log(err);
          this.$msgbox.close();
          this.$message({
            type: "info",
            message: "举报单提交失败",
          });
        });
    },
    Cancel() {
      this.dialogVisible = false;
      this.$message({
        type: "info",
        message: "提交已取消",
      });

      //this.MakeFormEmpty();
    },

    // 文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
    onUploadChange(file, fileList) {},
  },
  handleRemove(file, fileList) {
    setTimeout(() => {
      this.fileList = fileList;
    }, 1000);
  },
  handlePictureCardPreview(file) {
    this.dialogImageUrl = file.url;
    this.dialogVisible = true;
  },

  mounted: function () {},
};
</script>


<style lang="less" scoped>
.disabled /deep/ .el-upload--picture-card {
  display: none !important;
}

.center {
  position: relative;
  right: 5em;
}
</style>