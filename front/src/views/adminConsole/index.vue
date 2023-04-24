
<template>
  <div class="views">
    <el-row>
      <el-col :span="14" class="lightgreen-box">
        <el-card class="box-card">
          <el-row>
            <el-col :span="2">
              <el-avatar
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
              ></el-avatar>
            </el-col>
            <el-col :span="12" class="font-size1">
              你好,{{user_name}}
              <!-- <font size="1">role</font><br> -->
              <el-tag size="mini">管理员</el-tag>
              <br>
              <font size="2em">管理员编号：{{user_id}}</font>
            </el-col>
            
            <el-col :span="10" class="font-size1"> 

              
            </el-col>


          </el-row>
        </el-card>
        <br />



        <br />
        <el-card class="box-card font-size2">
          <div style="margin-bottom: 30px">工作台</div>

          <div style="margin-bottom: 20px">
            用户举报单审核<br />
            <el-button style="margin-left: 90px" @click="toExamineUser" round
              >待审核：{{ auditLog.userReportTodoNum }}</el-button
            >
          </div>

  
          <div style="margin-bottom: 20px">
            活动举报单审核<br />
            <el-button style="margin-left: 90px" @click="toExamineAct" round
              >待审核：{{ auditLog.activityReportTodoNum }}</el-button
            >
          </div>
        </el-card>
      </el-col>

      <!--下面是日历-->
      <el-col :span="10">
        <el-card class="box-card">
          <div
            style="
              width: 470px;
              height: 630px;
              position: relative;
              overflow: hidden;
            "
          >
            <div
              id="abc"
              style="overflow-x: hidden; overflow-y: scroll; position: absolute"
            >
              <div style="width: 470px; height: 565px">
                <el-calendar v-model="value"> </el-calendar>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- <ReportBox selection="test" targetType="0" activityId="1" name="name" /> -->
  </div>
</template>

<script>
import ReportBox from "@/components/ReportBox";
import { getAuditLog } from "@/api/admin.js";
import Axios from "axios";

export default {
  name: "adminConsole",
  components: { ReportBox },
  data() {
    return {
      flag: true,
      auditLog: {
        userReportTodoNum: 0,
        activityReportTodoNum: 0,
      },
      role: "无数据",
      position: "无数据",
      value: "",
    };
  },
  computed: {
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
  methods: {
    toExamineUser() {
      this.$router.push({
        name: "AdminExmaineUser",
      });
    },
    toExamineAct() {
      this.$router.push({
        name: "AdminExamineActivity",
      });
    },


    button_close(data) {
      this.flag = data;
    },
    getConsoleNum() {
      getAuditLog(0)
        .then((res) => {
          this.auditLog.userReportTodoNum = res.data.user;
          this.auditLog.activityReportTodoNum = res.data.activity;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  //初始向后端请求数据
  mounted: function () {
    // const loading = this.$loading({
    //   lock: true,
    //   text: 'Loading',
    //   spinner: 'el-icon-loading',
    //   background: 'rgba(0, 0, 0, 0.7)'
    // });

    //获取待审核数目
    console.log("hahahaha");
    this.getConsoleNum();

    //获取管理员个人信息
    // getManager(this.ID)
    //   .then((res) => {
    //     let item = JSON.parse(res.data)[0];

    //     this.name = item.NAME;
    //     this.role = "管理员";
    //     this.position = item.POSITION;
    //     loading.close();
    //   })
    //   .catch((err) => {
    //     if (err.message == "Request failed with status code 403" && this.flag) {
    //       this.flag = false;
    //       AccessTokenFailed();
    //     }

    //     console.log("后端请求失败！");
    //     console.log(err);
    //     loading.close();
    //   })
  },
};
</script>

// lang选择less语法，scoped限制该样式只在本文件使用，不影响其他组件
<style lang="less" scoped>
    .upPanel{
        width:500px;
        height:200px;
        border:0px solid #000;
        margin-right:50px;
        background-color:#EDEDED;
        float:left;
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
        //display:inline-block;
    }

    .upPanel2{
        width:500px;
        height:200px;
        border:0px solid #000;
        margin-right:50px;
        background-color:#EDEDED;
        float:right;
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
        //display:inline-block;
    }
    

    .downPanel{
        width:1120px;
        height:400px;
        border:0px solid #000;
        background-color:#F4F4F4;
        float:left;
        margin-top:30px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
    }

    //下面的是抄的
    .scanText{
        margin-left:80px;
    }

    .box-card {
    background:white;
    border-radius: 30px;
  }
.font-size1{
    font-weight: bold;
    font-family: "Hiragino Sans GB";
    font-size: 16px;
    line-height:1.5;
}
.font-size2{
    font-weight: bold;
    font-family: "Hiragino Sans GB";
    font-size: 20px;
    line-height:1.7;
}

.el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 10px;
    margin: 0;
}

.el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
}
</style>
