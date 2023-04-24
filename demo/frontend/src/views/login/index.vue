<template>
  <div class="login-container">
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      :class="loginFormClass"
      auto-complete="on"
      label-position="top"
    >
      <div class="title-container">
        <h3 :class="titleClass">登录</h3>
      </div>

      <el-form-item prop="username" class="input-box">
        <span class="svg-container">
          <svg-icon icon-class="email" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="email"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password" class="input-box">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="Password"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon
            :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"
          />
        </span>
      </el-form-item>

      <div style="display: flex; justify-content: space-around">
        <el-button
          type="success"
          style="width: 25%"
          @click.native.prevent="handleRegister"
          >前往注册</el-button
        >
        <el-button type="text" @click="handleForget">忘记密码?</el-button>
        <el-button
          :loading="loading"
          type="primary"
          style="width: 25%"
          @click.native.prevent="handleLogin"
          >登录</el-button
        >
        <!-- <el-button type="primary" @click="HelloWorldAPI"
          >HelloWorldAPI</el-button
        > -->
      </div>

      <!-- <div class="tips">
        <span style="margin-right:20px;">username: admin</span>
        <span> password: any</span>
      </div> -->
    </el-form>
  </div>
</template>

<script>
import { HelloWorld } from "@/api/activity";
// import store from "@/store/index";
export default {
  name: "Login",
  data() {
    const validateUsername = (rule, value, callback) => {
      var reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
      if (!value) {
        callback(new Error("请输入邮箱"));
      }
      setTimeout(() => {
        if (value && !reg.test(value)) {
          callback(new Error("请输入正确的邮箱"));
        } else {
          callback();
        }
      }, 1000);
    };
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error("The password can not be less than 6 digits"));
      } else {
        callback();
      }
    };
    return {
      loginForm: {
        username: "657826973@qq.com",
        password: "yy657826973",
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", validator: validateUsername },
        ],
        password: [
          { required: true, trigger: "blur", validator: validatePassword },
        ],
      },
      loading: false,
      passwordType: "password",
      redirect: undefined,
      titleClass: "title",
      loginFormClass: this.$route.query.from
        ? "login-form-in-forget"
        : "login-form",
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
        //console.log()
      },
      immediate: true,
    },
  },
  methods: {
    HelloWorldAPI() {
      HelloWorld()
        .then((res) => {
          console.log("HelloWorld:res:", res);
        })
        .catch((err) => {
          console.log("HelloWorld:err:", err);
        });
    },
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    },
    handleLogin() {
      this.loading = true;
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          this.$store
            .dispatch("user/login", this.loginForm)
            .then(async () => {
              await this.$store.dispatch("user/getInfo");
              const roles = this.$store.getters.roles;
              console.log("login:roles:", roles);
              let name_s = "";
              let path_s = "";
              if (roles.includes("admin")) {
                name_s = "AdminConsole";
                path_s = "/adminConsole/index";
              } else if (roles.includes("client")) {
                name_s = "Dashboard";
                path_s = "/dashboard";
              }
              console.log("name_s:", name_s);
              console.log("path_s:", path_s);

              window.location.reload();


              // this.$router.push({ path: this.redirect || path_s });
              this.$router.push({ name: name_s });

              console.log("跳转");
              this.loading = false;
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
    handleRegister() {
      this.titleClass = "title-leave";
      setTimeout(() => {
        this.loginFormClass = "login-form-leave";
      }, 500);

      setTimeout(() => {
        this.$router.push("/register");
        this.loading = false;
      }, 1000);
    },
    handleForget() {
      this.titleClass = "title-leave";
      setTimeout(() => {
        this.loginFormClass = "login-form-leave-forget";
      }, 500);

      setTimeout(() => {
        this.$router.push("/forget");
        this.loading = false;
      }, 1000);
    },
  },
};
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .input-box {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
    animation: rotateInDownLeft; /* referring directly to the animation's @keyframe declaration */
    animation-duration: 1s; /* don't forget to set a duration! */
  }
  .login-form-in-forget {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
    animation: rollIn; /* referring directly to the animation's @keyframe declaration */
    animation-duration: 1s; /* don't forget to set a duration! */
  }
  .login-form-leave {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
    animation: rotateOutDownLeft; /* referring directly to the animation's @keyframe declaration */
    animation-duration: 1s; /* don't forget to set a duration! */
  }
  .login-form-leave-forget {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
    animation: rollOut; /* referring directly to the animation's @keyframe declaration */
    animation-duration: 1s; /* don't forget to set a duration! */
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
      animation: bounceInLeft; /* referring directly to the animation's @keyframe declaration */
      animation-duration: 1s; /* don't forget to set a duration! */
    }
    .title-leave {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
      animation: bounceOutRight; /* referring directly to the animation's @keyframe declaration */
      animation-duration: 2s; /* don't forget to set a duration! */
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
