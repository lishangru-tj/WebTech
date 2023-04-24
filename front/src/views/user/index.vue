<template>
  <div class="box">
    <div style="font-size: 30px">个人资料</div>

    <div style="font-size: 20px; margin-top: 20px; font-weight: bold">
      个人设置
    </div>
    <el-form
      :model="userForm"
      :rules="rules"
      ref="userForm"
      label-width="100px"
      class="userForm"
      label-position="left"
    >
      <el-form-item label="头像" prop="avatar">
        <el-upload
          class="avatar-uploader"
          action="none"
          :on-change="checkType"
          :auto-upload="false"
          :show-file-list="false"
        >
          <img
            v-if="userForm.imageUrl"
            :src="userForm.imageUrl"
            class="avatar"
            style="height: 200px"
          />
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="用户名" prop="name" style="justify-content: center">
        <el-input
          type="text"
          v-model="userForm.name"
          autocomplete="on"
          placeholder="请输入用户名"
          style="width: 100%"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input-number
          v-model="userForm.age"
          style="width: 100%"
        ></el-input-number>
      </el-form-item>
      <el-form-item label="简介" prop="introduce">
        <el-input
          type="text"
          v-model="userForm.introduce"
          autocomplete="on"
          placeholder="个性签名"
          style="width: 200%"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="绑定邮箱" prop="introduce">
        {{userForm.email}}
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm()">保存</el-button>
        <el-button @click="resetForm('userForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  name: "User",
  data() {
    var checkName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("用户名不能为空"));
      }
      callback();
    };
    return {
      userForm: {
        name: "",
        age: "",
        introduce: "",
        imageUrl: "",
        avatarFile: "",
        email:"",
      },

      rules: {
        name: [{ validator: checkName, trigger: "blur" }],
      },
    };
  },
  computed: {},
  methods: {
    checkType(file, filelist) {
      let filetype = file.name.substring(file.name.lastIndexOf(".") + 1);
      filelist = [];
      console.log(filetype);
      if (filetype != "jpeg" && filetype != "jpg" && filetype != "png") {
        return false;
      }
      if (file.size / 1024 / 1024 > 1.5) {
        return false;
      }
      this.userForm.avatarFile = file;
      this.userForm.imageUrl = URL.createObjectURL(
        this.userForm.avatarFile.raw
      );
    },
    submitForm() {
      this.loading = true;
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          this.$store
            .dispatch("user/updateInfo", this.userForm)
            .then(() => {
              this.loading = false;
              this.resetForm(userForm);
            })
            .catch(() => {
              this.loading = false;
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.userForm.name = this.$store.getters.name;
      this.userForm.age = this.$store.getters.age;
      this.userForm.introduce = this.$store.getters.introduce;
      this.userForm.imageUrl = this.$store.getters.avatar;
      this.userForm.email = this.$store.getters.email;
    },
  },
  mounted() {
    this.resetForm("userForm");
  },
};
</script>
<style>
.box {
  padding: 50px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.userForm {
}
</style>
