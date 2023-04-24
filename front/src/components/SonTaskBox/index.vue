/** 
单个事项信息的组件
@author: hym
@editor: hym
@lastUpdate hym 10.22

传参说明：
taskBoxDialogVisible: bool 控制对话框显示
taskId: Long 事项id
*/
<template>
  <div style="scale:0.8">
    <!--对话框-->
    <el-dialog
      :visible.sync="dialogVisible"
      width="1000px"
      @close="handleClose"
      append-to-body
    >

      <el-container>

        <!--header-->
        <el-header>
          <!--勾选框-->
          <el-radio
            class="isDone_radio"
            v-model="taskInfo.isdone"
            :label="true"
            @click.native.prevent="onTaskDoneRadioChange()"
          >
            <span></span>
          </el-radio>
          <!--事项状态-->
          <el-tag :type='tagStateColor'>{{taskInfo.taskState}}</el-tag>

          <!--事项与ddl的时间差-->
          <font :color="timeLengthColor">{{timeLengthBetweenDDL}}</font>

          <div style="float:right">
            <el-button
              size="mini"
              @click="saveTask()"
            ><i class="el-icon-document-checked"></i></el-button>
            <el-button
              size="mini"
              @click="deleteTask()"
            ><i class="el-icon-delete"></i></el-button>
          </div>
        </el-header>
        <el-container>

          <el-aside width="69em">
            <!--事项详情-->
            <div class="detailPanel">
              <h2 class="taskTitle">{{taskInfo.taskTitle}}</h2>
              <!--点击编辑详情-->
              <el-button
                size="mini"
                @click="changeEditState()"
                class="editButton"
              ><i
                  :class="isEditting ? 'el-icon-document-checked' : 'el-icon-edit'"></i>
              </el-button>

              <div v-if="!isEditting">
                {{taskInfo.taskDetail}}
              </div>
              <el-input
                v-else
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 5}"
                placeholder="请输入内容"
                v-model="taskInfo.taskDetail"
              >
              </el-input>
            </div>

            <!--事项属性-->
            <el-form label-width="0px">
              <!--分类-->
              <el-form-item class="formItem">
                <i class="iconfont icon-fenlei"></i>分类：
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
              <el-form-item class="formItem">
                <i class="el-icon-price-tag"></i>个性化标签：
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
              <el-form-item class="formItem">
                <i class="iconfont icon-louti"></i>优先级：
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

              <!--时间-->
              <el-form-item class="formItem">
                <i class="el-icon-time"></i>时间：
                <span class="block">
                  <el-date-picker
                    v-model="taskInfo.timeRange"
                    type="datetimerange"
                    :picker-options="pickerOptions"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    align="right"
                    size="mini"
                  >
                  </el-date-picker>
                </span>
              </el-form-item>
            </el-form>

          </el-aside>

        </el-container>
      </el-container>

    </el-dialog>

  </div>
</template>

<script>
import { patchOneTask, getTaskById } from '@/api/task.js'
import { getAllClassificationTitle } from '@/api/classification.js'
import CreateTaskBox from '@/components/CreateTaskBox'
// import TaskBox from '@/components/TaskBox'
export default {
  name: "SonTaskBox",
  components: { CreateTaskBox, },
  data () {
    return {
      userId: this.$store.getters.id,
      dialogVisible: false,//是否弹出对话框
      createTaskBoxDialogVisible: false,//创建事项组件的显示控制
      isEditting: false,//是否正在编辑，用于动态class
      isSaved: true,//标志当前的修改是否已经被保存
      chosen_taskId: 0,

      //事项详细数据
      taskInfo: {
        id: '',
        taskTitle: '背单词',
        taskDetail: "先背两小时单词",//详情的文字
        isdone: null,//初始时计算得出，不是从后端请求来的
        taskState: '进行中',
        classification: '学习',
        priority: '无优先级',
        timeRange: [new Date(), ''],
        familyPosition: 'parent',
        familyMembers: [],
        isInDustbin: 0
      },

      typeList: [{
        value: '学习',
        label: '学习'
      }, {
        value: '工作',
        label: '工作'
      }, {
        value: '生活',
        label: '生活'
      }],
      tagList: [{
        value: '室内',
        label: '室内'
      }, {
        value: '户外',
        label: '户外'
      }],

      //日历选择
      pickerOptions: {
        shortcuts: [{
          text: '今天',
          onClick (picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick (picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick (picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },

    }
  },
  props: ["taskBoxDialogVisible", "taskId"],
  methods: {
    //显示组件
    openDialog () {
      this.dialogVisible = true;
    },
    //切换"编辑"/"保存"按钮
    changeEditState () {
      this.isEditting = !this.isEditting;
    },
    //处理isdone勾选框事件
    onTaskDoneRadioChange () {
      this.taskInfo.isdone = !this.taskInfo.isdone;

      //通过isdone来获取state
      if (this.taskInfo.isdone) {
        if (new Date() > new Date(this.taskInfo.timeRange[1])) {
          this.taskInfo.taskState = '延期完成';
          this.$message({
            message: "延期完成了一个事项，记得下次要准时完成！",
            type: "warning",
          });
        }
        else {
          this.taskInfo.taskState = '已完成';
          this.$message({
            message: "恭喜你，按时完成了一个事项！请再接再厉！",
            type: "success",
          });
        }
      }
      else {
        if (new Date() > this.taskInfo.timeRange[1])
          this.taskInfo.taskState = '延期中';
        else if (new Date < this.taskInfo.timeRange[0])
          this.taskInfo.taskState = '未开始';
        else
          this.taskInfo.taskState = '进行中';
        this.$message({
          message: "取消事项完成",
          type: "info",
        });
      }
    },
    //处理优先级下拉框的指令
    handlePriorityCommand (command) {
      this.taskInfo.priority = command;
    },
    //确定isdone属性
    async setIsdone () {
      this.taskInfo.isdone = this.taskInfo.taskState.indexOf("完成") >= 0;
    },
    timeTrans (time, type) {
      let date = new Date(new Date(time).getTime() + 8 * 3600 * 1000)
      date = date.toJSON();
      if (type === 1) {
        date = date.substring(0, 10)
      }
      else {
        date = date.substring(0, 19).replace('T', ' ')
      }
      return date
    },
    //删除事项
    deleteTask () {
      //确认信息
      this.$confirm('您确定要删除事项吗？删除的事项可在回收站中恢复！', '确认信息', {
        distinguishCancelAndClose: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
        .then(() => {
          this.taskInfo.isInDustbin = new Date();
          let backendTaskInfo = this.getBackendTaskInfo();
          patchOneTask(backendTaskInfo)
            .then((res) => {
              console.log(res);
              this.$message({
                message: "事项删除成功！",
                type: "success",
              });
              this.isSaved = true;
              this.dialogVisible = false;
            })
            .catch((err) => {
              console.log("要删除的事项id是：", this.taskInfo.id.toString())
              console.log("事项删除失败！", err);
              this.$message({
                message: "事项删除失败！",
                type: "danger",
              });
              this.isSaved = true;
              this.dialogVisible = false;
            })
        })
        .catch(() => {
          this.$message({
            message: "取消删除事项",
            type: "info",
          });
        })


    },
    getBackendTaskInfo () {
      let _taskState = 0;
      switch (this.taskInfo.taskState) {
        case '进行中':
          _taskState = 0;
          break;
        case '延期中':
          _taskState = 0;
          break;
        case '已完成':
          _taskState = 1;
          break;
        case '延期完成':
          _taskState = 2;
          break;
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
      let backendTaskInfo = {
        taskId: this.taskInfo.id,
        taskTitle: this.taskInfo.taskTitle,
        taskDetail: this.taskInfo.taskDetail,
        taskState: _taskState,
        classificationTitle: this.taskInfo.classificationTitle,
        priority: _priority,
        startTime: this.taskInfo.timeRange[0],
        endTime: this.taskInfo.timeRange[1],
        isParent: this.taskInfo.familyPosition == 'parent' ? 1 : 0,
        isInDustbin: this.taskInfo.isInDustbin,
        relativeTask: []//todo
      }
      return backendTaskInfo;
    },
    updateDBTaskInfo () {
      let backendTaskInfo = this.getBackendTaskInfo();

      patchOneTask(backendTaskInfo)
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        })
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
          console.log(err);
        })
    },
    //保存事项，向后端发patch请求
    saveTask () {
      let backendTaskInfo = this.getBackendTaskInfo();

      patchOneTask(backendTaskInfo)
        .then((res) => {
          console.log(res);
          this.$message({
            type: 'success',
            message: '保存事项成功！'
          })
          this.isSaved = true;
        })
        .catch((err) => {
          console.log(err);
          this.$message({
            type: 'danger',
            message: '保存事项失败'
          })
        })
    },
    //关闭dialog瞬间的回调
    handleClose () {
      if (!this.isSaved) {
        this.dialogVisible = true;
        this.$confirm('检测到未保存的内容，是否在离开页面前保存修改？', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '保存',
          cancelButtonText: '放弃修改'
        })
          .then(() => {
            this.$message({
              type: 'success',
              message: '保存修改'
            });
            this.isSaved = true;
            this.dialogVisible = false;
          })
          .catch(action => {
            // this.$message({
            //   type: 'info',
            //   message: action === 'cancel'
            //     ? '放弃保存并离开页面'
            //     : '停留在当前页面'
            // })
            this.$message({
              type: 'info',
              message: action === 'cancel'
                ? '放弃保存'
                : '放弃保存'
            })
            this.dialogVisible = false;
          });
      }

    },
    //关闭CreateTaskBox的回调
    resetDialogVisible () {
      this.createTaskBoxDialogVisible = false;
      this.setFrontendTaskObj(this.taskId);
    },
    addOneSonTask () {
      this.createTaskBoxDialogVisible = true;
    },
    getFrontendState (backendState, startTime, endTime) {
      if (backendState == 1)
        return '已完成';
      else if (backendState == 2)
        return '延期完成';

      else {
        if (new Date() > Date.parse(endTime))
          return '延期中';
        else if (new Date() < Date.parse(startTime))
          return '未开始';
        else
          return '进行中';
      }
    },
    //根据父页面传来的taskId，向后端获取整套数据
    async setFrontendTaskObj (taskId) {
      console.log("向后端请求儿子数据！")
      await getTaskById(taskId)
        .then((res) => {
          console.log("儿子数据的事项获取成功！", res)
          //然后要把后端拉回来的task对象转换成前端要用的对象
          if (res.data != null || res.data != {}) {

            let backendDataList = res.data;
            if (backendDataList == null)
              return;
            let value = res.data;
            if (value.isInDustbin == '0' || value.isInDustbin == null) {
              //获取taskState
              let _taskState = this.getFrontendState(value.taskState, value.startTime, value.endTime);

              //获取isdone
              let _isdone = _taskState.indexOf("完成") >= 0 ? true : false;

              //获取priority
              let _priority = '';
              switch (value.priority) {
                case 0:
                  _priority = "无优先级";
                  break;
                case 1:
                  _priority = "低优先级";
                  break;
                case 2:
                  _priority = "中优先级";
                  break;
                case 3:
                  _priority = "高优先级";
                  break;
              }

              let taskInfo = {
                id: value.taskId,
                taskTitle: value.taskTitle,
                taskDetail: value.taskDetail,
                isdone: _isdone,
                taskState: _taskState,
                classification: value.classificationTitle,
                priority: _priority,
                timeRange: [value.startTime, value.endTime],
                familyPosition: value.isParent == 1 ? 'parent' : 'child',
                familyMembers: value.relativeTask,
                isInDustbin: value.isInDusbin
              }

              if (taskInfo.familyMembers != null && taskInfo.familyMembers != []) {
                //获取familyMembers
                for (let item of taskInfo.familyMembers) {
                  //获取taskState
                  item['taskState'] = this.getFrontendState(item.taskState, item.startTime, item.endTime);

                  //获取isdone
                  item['isdone'] = _taskState.indexOf("完成") >= 0 ? true : false;

                  //获取priority
                  switch (item.priority) {
                    case 0:
                      item.priority = "无优先级";
                      break;
                    case 1:
                      item.priority = "低优先级";
                      break;
                    case 2:
                      item.priority = "中优先级";
                      break;
                    case 3:
                      item.priority = "高优先级";
                      break;
                  }

                  //获取timeRange
                  item['timeRange'] = [item.startTime, item.endTime];
                }//end of for
              }

              this.taskInfo = taskInfo;

            }
          }//end of if

        })
        .catch((err) => {
          console.log(err);
        })
    },


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

    //计算ddl与此刻的相对时间差
    timeLengthBetweenDDL () {
      let ddlDate = this.taskInfo.timeRange[1];
      if (ddlDate == '' || !ddlDate) {
        return null;
      }

      var dateEnd = new Date(ddlDate);
      var dateBegin = new Date();

      var dateDiff = dateEnd.getTime() - dateBegin.getTime(); //时间差的毫秒数
      var dayDiff = Math.floor(dateDiff / (24 * 3600 * 1000)); //计算出相差天数

      var leave1 = dateDiff % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
      var hours = Math.floor(leave1 / (3600 * 1000)); //计算出小时数
      //计算相差分钟数
      var leave2 = leave1 % (3600 * 1000); //计算小时数后剩余的毫秒数
      var minutes = Math.floor(leave2 / (60 * 1000)); //计算相差分钟数
      //计算相差秒数
      var leave3 = leave2 % (60 * 1000); //计算分钟数后剩余的毫秒数
      var seconds = Math.round(leave3 / 1000);


      //将时间差表示为一个对象
      //负数不应该地板除，应该天花板除
      var timeLength = {
        day: dayDiff < 0 ? dayDiff + 1 : dayDiff,
        hour: hours < 0 ? hours + 1 : hours,
        minute: minutes < 0 ? minutes + 1 : minutes,
        second: seconds < 0 ? seconds + 1 : seconds,
      }
      console.log(timeLength)

      var Str = '';
      if (Math.abs(timeLength.day) >= 1) {
        Str = timeLength.day + "天";
      }
      else if (Math.abs(timeLength.hour) >= 1) {
        Str = timeLength.hour + "小时";
      }
      else if (Math.abs(timeLength.minute) >= 1) {
        Str = timeLength.minute + "分钟";
      }


      if (dateDiff > 0) {
        Str += '后';
      }
      else if (dateDiff < 0) {
        Str += '前';
      }
      else {
        Str = "事项状态更新中..."
      }

      //删除首位负号
      if (Str[0] == '-') {
        Str = Str.slice(1);
      }

      return Str;
    },

    //确定显示时间差的颜色
    timeLengthColor () {
      if (!this.timeLengthBetweenDDL)
        return null;

      return this.timeLengthBetweenDDL.endsWith("前") ? "red" : "blue";
    },

    //确定状态标签的颜色
    tagStateColor () {
      var color = ''
      switch (this.taskInfo.taskState) {
        case '已完成':
          color = 'success';
          break;
        case '延期完成':
          color = 'warning';
          break;
        case '延期中':
          color = 'danger';
          break;
        default:
          color = '';
          break;
      }
      return color;
    },



  },
  watch: {
    taskBoxDialogVisible: {
      handler (newVal) {
        this.dialogVisible = newVal;
      },
      deep: true,
      immediate: true
    },
    taskInfo: {
      handler () {
        this.isSaved = false;
        console.log("子事项：检测到事项信息修改！")
      },
      deep: true,
      immediate: true
    },
    dialogVisible: {
      handler (newVal) {
        console.log("接收到爸爸事项传参：", newVal)

        //打开页面，向后端请求
        if (newVal == true) {

          this.setFrontendTaskObj(this.taskId);
        }
        //如果子组件被关闭了，把dialogVisible=false的值同步给父组件
        if (newVal == false && this.isSaved) {
          let backendTaskInfo = this.getBackendTaskInfo();

          patchOneTask(backendTaskInfo)
            .then((res) => {
              console.log(res);
              this.$emit('resetDialogVisible');
            })
            .catch((err) => {
              console.log(err);
            })
        }
        else {
          this.$emit('resetDialogVisible');
        }
      }
    },
  },
  mounted: function () {
    console.log("儿子this.taskId")

    this.setThisUserAllClassificaitonTitle();//获取该用户的所有分组名称
    this.setFrontendTaskObj(this.taskId)//接收从父组件传来的事项信息
      .then(() => {
        this.dialogVisible = this.taskBoxDialogVisible;
        this.setIsdone()
          .then(() => {
            this.isSaved = true;
            console.log("isSaved!")
          })
      })


  }
}
</script>

<style lang="less" scoped>
//编辑按钮
.editButton {
  margin-left: 18px;
  margin-top: 12px;
  margin-bottom: 20px;
}

//属性表单里的一项
.formItem {
  text-align: left;
}

//页面布局css
.el-container {
  height: 530px;
  border: 1px solid rgb(240, 243, 238);
  background-color: #eaf6ea;

  .el-header {
    background-color: #ffffff;
    color: #333;
    line-height: 60px;
    //勾选框
    .isDone_radio {
      margin-right: 0px;

      //从默认圆改成方形勾
      ::v-deep .el-radio__inner {
        border-radius: 2px;
        border-width: 2px;
        border-color: var(--color-primary) !important; //外框颜色
      }
    }
    .el-tag {
      margin-left: 1em;
    }
  }

  .el-container {
    height: 0%; //保证不溢出对话框
    .el-aside {
      color: #333;
      line-height: 10px;
      overflow: hidden; // 超出部分隐藏
      overflow-y: scroll; // 设置y轴方向的滚动条

      //事项标题
      .taskTitle {
        margin-top: 21px;
        margin-bottom: 25px;
        float: left;
      }

      //详情信息
      .detailPanel {
        margin-bottom: 20px;
      }
    }

    .el-main {
      background-color: #ffffff;
      color: #333;
      //line-height: 5px;
    }
  }
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-header,
.el-container:nth-child(6) .el-header {
  line-height: 260px;
}

.el-container:nth-child(7) .el-header {
  line-height: 320px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}
.el-icon-arrow-down {
  font-size: 12px;
}

//单选框样式
.isDone_radio {
  margin-right: 0px;

  //从默认圆改成方形勾
  ::v-deep .el-radio__inner {
    border-radius: 2px;
    border-width: 2px;
    border-color: var(--color-primary) !important; //外框颜色
  }

  ::v-deep .el-radio__input.is-checked .el-radio__inner::after {
    content: "";
    width: 10px;
    height: 5px;
    border: 1px solid white;
    border-top: transparent;
    border-right: transparent;
    text-align: center;
    display: block;
    position: absolute;
    top: 0px;
    left: 0px;
    vertical-align: middle;
    transform: rotate(-45deg);
    border-radius: 0px;
    background: none;
  }
}

//修改下拉框
::v-deep .el-select {
  width: 7em;

  ::v-deep .el-input__inner {
    background-color: transparent !important;
  }
}
</style>