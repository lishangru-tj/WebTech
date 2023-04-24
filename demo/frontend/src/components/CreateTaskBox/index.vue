/** 
创建事项的组件
@author: hym
@editor: hym
@lastUpdate hym 10.22

传参说明：
1.createTaskBoxDialogVisible: bool 控制对话框显示
2.timeRange: 列表 时间范围 [Date(), Date()]
3.parentTaskId: 父事项的id，这样在post的时候会用post子事项的方法
*/
<template>
  <div>
    <el-dialog
      title="创建新事项"
      :visible.sync="dialogVisible"
      width="600px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="taskInfo"
        label-width="80px"
        :rules="createForm_rules"
      >
        <!--事项名称-->
        <el-form-item
          label="事项名称"
          prop="taskTitle"
        >
          <el-input
            v-model="taskInfo.taskTitle"
            placeholder="事项名称"
            maxlength="30"
            show-word-limit
          ></el-input>
        </el-form-item>

        <!--事项详述-->
        <el-form-item
          label="事项详述"
          prop="taskDetail"
        >
          <el-input
            type="textarea"
            placeholder="背景/信息说明"
            v-model="taskInfo.taskDetail"
            :autosize="{ minRows: 3, maxRows: 8 }"
            maxlength="200"
            show-word-limit
          ></el-input>
        </el-form-item>

        <!--分类-->
        <i class="iconfont icon-fenlei iconPosition" />
        <el-form-item class="formItem">
          <el-select
            v-model="taskInfo.classification"
            :popper-append-to-body="false"
          >

            <el-option
              v-for="item in typeList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>

         <!--标签-->
        <i class="el-icon-price-tag iconPosition"></i>
        <el-form-item class="formItem">
          <el-select
            v-model="taskInfo.tag"
            :popper-append-to-body="false"
          >

            <el-option
              v-for="item in tagList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <!--优先级-->
        <i class="iconfont icon-louti iconPosition" />
        <el-form-item class="formItem">
          <el-dropdown
            trigger="click"
            @command="handlePriorityCommand"
          >
            <span class="el-dropdown-link">
              <el-tag
                :type="priorityColor"
                effect="dark"
                class="el-icon-arrow-down el-icon--right"
              >{{taskInfo.priority}}</el-tag>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="高优先级"><i
                  class="iconfont icon-youxianji"
                  style="color:#d81e06"
                ></i>高优先级</el-dropdown-item>
              <el-dropdown-item command="中优先级"><i
                  class="iconfont icon-youxianji"
                  style="color:#f4ea2a"
                ></i>中优先级</el-dropdown-item>
              <el-dropdown-item command="低优先级"><i
                  class="iconfont icon-youxianji"
                  style="color:#1afa29"
                ></i>低优先级</el-dropdown-item>
              <el-dropdown-item command="无优先级"><i
                  class="iconfont icon-youxianji"
                  style="color:#bfbfbf"
                ></i>无优先级</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-form-item>

        <!--选择时间范围-->
        <i class="el-icon-time iconPosition" />
        <el-form-item prop="timeRange">
          <el-date-picker
            v-model="taskInfo.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>

      <!--按钮-->
      <div class="divToRight">
        <el-button
          type="primary"
          @click="buttonCertain()"
        >确认</el-button>
        <el-button
          type="info"
          @click="buttonCancel()"
        >取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
/* eslint-disable */
import { postOneNewTask, postOneSonTask } from "@/api/task.js"
import { getAllClassificationTitle } from '@/api/classification.js'
export default {
  name: "CreateTaskBox",
  data () {
    return {
      userId: this.$store.getters.id,
      dialogVisible: this.createTaskBoxDialogVisible,

      taskInfo: {
        userId: this.$store.getters.id,
        taskTitle: '',
        taskDetail: "",//详情的文字
        classification: '默认分组',
        tag:'无',
        priority: '无优先级',
        timeRange: this.timeRange,
        familyPosition: 'parent',
        familyMembers: [],
      },
      typeList: [{
        value: '选项1',
        label: '学习'
      }, {
        value: '选项2',
        label: '工作'
      }, {
        value: '选项3',
        label: '生活'
      }],
      tagList: [{
        value: '室内',
        label: '室内'
      }, {
        value: '户外',
        label: '户外'
      }, {
        value: '无',
        label: '无'
      }],
      createForm_rules: {
        taskTitle: [
          { required: true, message: "请输入事项名称", trigger: "blur" },
          { max: 30, message: "事项名称最多不超过30个字符", trigger: "blur" },
        ],
        taskDetail: [
          { max: 200, message: "事项详述最多不超过200个字符", trigger: "blur" },
        ],
      },
    };
  },
  props: ["createTaskBoxDialogVisible", "timeRange", "parentTaskId"],
  methods: {
    //处理分类的下拉框
    handleClassifyCommand () {
      this.createForm.classification = "学习";
    },

    //处理优先级下拉框的指令
    handlePriorityCommand (command) {
      this.taskInfo.priority = command;
    },

    //取消按钮
    buttonCancel () {
      this.$message({
        message: "取消事项创建",
        type: "info",
      });
      this.dialogVisible = false;
    },
    //确认按钮
    buttonCertain () {
      //判断是否填写事项标题
      if (this.taskInfo.taskTitle == '') {
        this.$message({
          message: "请填写事项标题！",
          type: 'warning'
        })
        return;
      }

      let _priority = 0;
      switch (this.taskInfo.priority) {
        case '无优先级':
          _priority = 0;
          break;
        case '低优先级':
          _priority = 1;
          break;
        case '中优先级':
          _priority = 2;
          break;
        case '高优先级':
          _priority = 3;
          break;
      }

      //如果是创建父事项
      if (this.parentTaskId == null) {
        //这里要写把新事项往后端存的代码
        //创建后端实体类
        let backendTaskInfo = {
          userId: this.taskInfo.userId,
          taskTitle: this.taskInfo.taskTitle,
          taskDetail: this.taskInfo.taskDetail,
          taskState: 0,
          classificationTitle: this.taskInfo.classification,
          priority: _priority,
          startTime: this.taskInfo.timeRange[0],
          endTime: this.taskInfo.timeRange[1],
          isParent: 1,
          //relativeTask: []
        }
        console.log("kk",backendTaskInfo)

        //发后端请求
        postOneNewTask(backendTaskInfo)
          .then((res) => {
            console.log(res);
            this.$message({
              message: "事项创建成功！",
              type: "success",
            });
            this.dialogVisible = false;
          })
          .catch((err) => {
            console.log(err);
            this.$message({
              message: "事项创建失败",
              type: "danger",
            });
            this.dialogVisible = false;
          })
      }
      //如果是创建子事项
      else {
        //这里要写把新事项往后端存的代码
        //创建后端实体类
        let backendSonTaskObj = {
          userId: this.taskInfo.userId,
          taskTitle: this.taskInfo.taskTitle,
          taskDetail: this.taskInfo.taskDetail,
          taskState: 0,
          classificationTitle: this.taskInfo.classification,
          priority: _priority,
          startTime: this.taskInfo.timeRange[0],
          endTime: this.taskInfo.timeRange[1],
          isParent: 0,
          relativeTask: []
        }

        //发后端请求
        postOneSonTask(this.parentTaskId, backendSonTaskObj)
          .then((res) => {
            console.log(res);
            this.dialogVisible = false;
            this.$message({
              message: "事项创建成功！",
              type: "success",
            });
            this.dialogVisible = false;
          })
          .catch((err) => {
            console.log(err);
            this.dialogVisible = false;
            this.$message({
              message: "事项创建失败",
              type: "danger",
            });
          })
      }
    },

    //获取该用户的所有分组名称
    setThisUserAllClassificaitonTitle () {
      this.typeList = []

      //向后端请求该用户的所有分类名
      getAllClassificationTitle(this.userId)
        .then((res) => {
          console.log(res);
          for (let it of res.data)
            this.typeList.push({
              value: it.classificationTitle,
              label: it.classificationTitle
            })
        })
        .catch((err) => {
          console.log(err,"请求失败！！！！！！！！！！！");
        })
    },
    // //添加子事项
    // addOneSonTask (userId, sonTaskObj) {
    //   postOneSonTask(userId, sonTaskObj)
    //     .then((res) => {
    //       console.log(res);
    //     })
    //     .catch((err) => {
    //       console.log(err);
    //     })
    // }
  },
  mounted: function () {
    this.dialogVisible = this.createTaskBoxDialogVisible;
    this.setThisUserAllClassificaitonTitle();
  },
  computed: {
    //计算优先级标签的颜色
    priorityColor () {
      let tagColor = 'info'
      switch (this.taskInfo.priority) {
        case '高优先级':
          tagColor = 'danger';
          break;
        case '中优先级':
          tagColor = 'warning';
          break;
        case '低优先级':
          tagColor = 'success';
          break;
        default:
          tagColor = 'info';
          break;
      }
      return tagColor;
    },
  },
  watch: {
    createTaskBoxDialogVisible: {
      handler (newVal) {
        this.dialogVisible = newVal;
        this.taskInfo.timeRange = this.timeRange;
      },
      deep: true,
      immediate: true,
    },
    dialogVisible: {
      handler (newVal) {
        //如果子组件被关闭了，把dialogVisible同步给父组件
        if (newVal == false)
          this.$emit('resetDialogVisible');
      }
    }
  },
};
</script>

<style lang="less" scoped>
//设置div块级元素靠右
.divToRight {
  text-align: right;
  display: flex;
  justify-content: flex-end;
  padding-right: 20px;
}

//icon位置
.iconPosition {
  float: left;
  position: relative;
  left: 50px;
  top: 14px;
  transform: scale(1.2);
}
</style>