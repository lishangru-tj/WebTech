<template>
  <div>
    <el-upload
      ref="UploadImgRef"
      action=""
      :auto-upload="false"
      list-type="picture-card"
      :on-preview="handlePictureCardPreview"
      :before-remove="beforeRemove"
      :on-remove="handleRemove"
      :on-change="handleChange"
      :on-success="handleUploadSuccess"
      :on-exceed="handleUploadExceed"
      accept=".jpeg,JPEG,.jpg,.JPG,.png,.PNG,gif,.GIF"
      :before-upload="beforeAvatarUpload"
      :limit="limit"
    >
      <i class="el-icon-plus" />
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" top="2vh" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
    <!-- <el-button @click="UploadFileList"
      >子组件内按钮：确认上传图片list</el-button
    > -->
  </div>
</template>

<script>
import { postFile } from "@/api/file";

export default {
  name: "UploadImg",
  props: {
    value: {
      default: "",
      type: String,
    },
    // 图片限制数量，默认3个
    limit: {
      type: Number,
      default() {
        return 3;
      },
    },
  },
  data() {
    return {
      dialogImageUrl: "",
      dialogVisible: false,
      resourcesUrl: "",
      fileList: [],
      // 此处路径配置成自己的上传文件地址
      //   uploadPath: process.env.VUE_APP_BASE_API + "/api/file/test/upload",
    };
  },
  computed: {
    emit_FormData() {
      const formData = new FormData();
      this.fileList.forEach((file) => {
        formData.append("uploadFile", file.raw);
      });

      this.$emit("FileChange", formData);
      return formData;
    },
  },
  methods: {
    // 图片自动上传的事件
    handleUploadSuccess(response, file, fileList) {
      console.log("handleUploadSuccess");
      console.log(file);
      console.log(fileList);
      const pics = fileList
        .map((file) => {
          if (typeof file.response === "object") {
            return file.response.fileUrl;
          } else {
            return file.response;
          }
        })
        .join(",");
      console.log(pics);
      //   this.$emit("input", pics);
    },
    handleUploadExceed(files, fileList) {
      this.loading = false;
      this.$message.warning(`对不起，最多上传个` + fileList.length + `图片`);
    },

    // 文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
    handleChange(file, fileList) {
      this.fileList = fileList;
      this.emit_FormData;
    },
    // 限制图片上传大小
    beforeAvatarUpload(file) {
      const isPic =
        file.type === "image/jpeg" ||
        file.type === "image/png" ||
        file.type === "image/gif" ||
        file.type === "image/jpg";
      const isLt8M = file.size / 1024 / 1024 < 8;
      if (!isPic) {
        this.$message.error("上传图片只能是 JPG、JPEG、PNG、GIF 格式!");
        return false;
      }
      if (!isLt8M) {
        this.$message.error("上传图片大小不能超过 8MB!");
      }
      return isPic && isLt8M;
    },
    // 删除文件之前的钩子，参数为上传的文件和文件列表，若返回 false 或者返回 Promise 且被 reject，则停止删除
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除该图片？`);
    },
    handleRemove(file, fileList) {
      const pics = fileList
        .map((file) => {
          return file.response;
        })
        .join(",");
      this.fileList = fileList;
      this.emit_FormData;
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },

    //真正上传
    async UploadFileList() {
      //判断是否有文件再上传
      if (this.fileList.length === 0) {
        return this.$message.warning("请选取文件后再上传");
      }
      const formData = new FormData();
      this.fileList.forEach((file) => {
        formData.append("uploadFile", file.raw);
      });
      // formData.append("folderPath", "D:/Upload/activity/");

      console.log("this.fileList", this.fileList);
      console.log("formData", formData);

      await postFile(formData)
        .then((res) => {
          console.log("postFile:res:", res);
        })
        .catch((err) => {
          console.log("postFile:err:", err);
        });
    },
  },
};
</script>