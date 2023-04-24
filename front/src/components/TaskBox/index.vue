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
  <div>
    <!--创建子事项的对话框-->
    <CreateTaskBox
      :createTaskBoxDialogVisible="createTaskBoxDialogVisible"
      :timeRange="taskInfo.timeRange"
      :parentTaskId="taskInfo.id"
      :key="new Date().toString()"
      @resetDialogVisible="resetDialogVisible()"
    />
    <!--子事项-->
    <!--要加递归终止条件-->
    <SonTaskBox
      :taskBoxDialogVisible="sonTaskBoxDialogVisible"
      :taskId="chosen_taskId"
      :key="(new Date()+1).toString()"
      @resetDialogVisible="resetDialogVisible()"
    />
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
                  v-model="taskInfo.classificationTitle"
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

              <el-form-item class="formItem">
                <i class="iconfont icon-org"></i>
                <span v-if="taskInfo.familyPosition=='child'">父事项：</span>

                <span v-else-if="taskInfo.familyPosition=='parent'">子事项：
                  <!--添加子事项的按钮-->
                  <el-button
                    type="success"
                    size="mini"
                    @click="addOneSonTask()"
                  ><i class="el-icon-circle-plus" /></el-button>
                </span>

                <!--子事项列表-->
                <el-table
                  ref="multipleTable"
                  :data="taskInfo.familyMembers"
                  tooltip-effect="dark"
                  style="width: 100%"
                >
                  <el-table-column
                    type="selection"
                    width="55"
                  >
                    <template slot-scope="props">
                      <!-- isDone的勾选 -->
                      <el-radio
                        class="isDone_radio"
                        v-model="props.row.isdone"
                        :label="true"
                        @click.native.prevent="onTaskDoneRadioChange(props.row)"
                      ><span></span>
                      </el-radio>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="名称"
                    prop="taskTitle"
                    width="120"
                  >
                  </el-table-column>
                  <el-table-column
                    label="状态"
                    prop="taskState"
                    width="120"
                  >
                  </el-table-column>
                  <el-table-column
                    label="时间段"
                    prop="timeRange"
                    width="420"
                  >
                    <template slot-scope="props">
                      <el-date-picker
                        v-model="props.row.timeRange"
                        type="datetimerange"
                        :picker-options="pickerOptions"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        align="right"
                        size="mini"
                      >
                      </el-date-picker>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="操作"
                    prop="operation"
                    width="120"
                  >
                    <template slot-scope="props">
                      <el-button
                        @click="handleClickDetail(props.row)"
                        type="text"
                        size="small"
                      >查看</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-form-item>
            </el-form>

          </el-aside>

          <!-- main
          <el-main>
            这里是团队项目的聊天窗口
          </el-main> -->

        </el-container>
      </el-container>

    </el-dialog>

  </div>
</template>

<script>
import { patchOneTask, getTaskById } from '@/api/task.js'
import { getAllClassificationTitle } from '@/api/classification.js'
import CreateTaskBox from '@/components/CreateTaskBox'
import SonTaskBox from '@/components/SonTaskBox'
import Axios from "axios";
export default {
  name: "TaskBox",
  components: { CreateTaskBox, SonTaskBox },
  data () {
    return {
      userId: this.$store.getters.id,
      dialogVisible: this.taskBoxDialogVisible,//是否弹出对话框
      createTaskBoxDialogVisible: false,//创建事项组件的显示控制
      sonTaskBoxDialogVisible: false,//子事项的显示控制
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
        classificationTitle: '学习',
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
      },
      {
        value: null,
        label: '无'
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
    onTaskDoneRadioChange (row) {
      console.log("row", row);
      //如果是父事项的勾选
      if (row == null) {
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
      }
      //如果是子事项的勾选
      else {
        row.isdone = !row.isdone;

        //如果是完成事项
        if (row.isdone) {
          if (new Date() > new Date(this.taskInfo.timeRange[1])) {
            row.taskState = '延期完成';
            this.$message({
              message: "延期完成了一个事项，记得下次要准时完成！",
              type: "warning",
            });
          }
          else {
            row.taskState = '已完成';
            this.$message({
              message: "恭喜你，按时完成了一个事项！请再接再厉！",
              type: "success",
            });
          }
        }

        //如果是取消完成
        else {
          if (new Date() > this.taskInfo.timeRange[1])
            row.taskState = '延期中';
          else if (new Date < this.taskInfo.timeRange[0])
            row.taskState = '未开始';
          else
            row.taskState = '进行中';
          this.$message({
            message: "取消事项完成",
            type: "info",
          });
        }
      }
      //向后端发请求，改变state属性
      let frontendTaskInfo = row == null ? this.taskInfo : row;
      let backendTaskInfo = this.getBackendTaskInfo(frontendTaskInfo);
      patchOneTask(backendTaskInfo)
        .then((res) => {
          console.log("改变后端isdone成功！", res);
        })
        .catch((err) => {
          console.log("改变后端isdone失败！", err);
        })
    },
    //处理优先级下拉框的指令
    handlePriorityCommand (command) {
      this.taskInfo.priority = command;
    },
    //确定isdone属性
    async setIsdone () {
      //确定父事项属性
      this.taskInfo.isdone = this.taskInfo.taskState.indexOf("完成") >= 0;

      //确定子事项属性
      if (this.taskInfo.familyMembers != null) {
        for (let i = 0; i < this.taskInfo.familyMembers.length; i++) {
          this.taskInfo.familyMembers[i].isdone = this.taskInfo.familyMembers[i].taskState.indexOf("完成") >= 0;
        }
      }
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
          let backendTaskInfo = this.getBackendTaskInfo(this.taskInfo);
          patchOneTask(backendTaskInfo)
            .then((res) => {
              console.log(res);
              this.$message({
                message: "事项删除成功！",
                type: "success",
              });
              this.dialogVisible = false;
            })
            .catch((err) => {
              console.log("要删除的事项id是：", this.taskInfo.id.toString())
              console.log("事项删除失败！", err);
              this.$message({
                message: "事项删除失败！",
                type: "danger",
              });
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
    getBackendTaskInfo (taskInfo) {
      let _taskState = 0;
      switch (taskInfo.taskState) {
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
      switch (taskInfo.priority) {
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
        taskId: taskInfo.id,
        taskTitle: taskInfo.taskTitle,
        taskDetail: taskInfo.taskDetail,
        taskState: _taskState,
        classificationTitle: taskInfo.classificationTitle,
        priority: _priority,
        startTime: taskInfo.timeRange[0],
        endTime: taskInfo.timeRange[1],
        isParent: taskInfo.familyPosition == 'parent' ? 1 : 0,
        isInDustbin: taskInfo.isInDustbin,
        tag: taskInfo.tag,
        relativeTask: []//todo
      }
      return backendTaskInfo;
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
    saveTask (msg) {
      let backendFatherTaskInfo = this.getBackendTaskInfo(this.taskInfo);
      let requestList = []
      if (this.taskInfo.familyMembers != null && this.taskInfo.familyMembers != []) {
        for (let item of this.taskInfo.familyMembers) {
          var request = patchOneTask(this.getBackendTaskInfo(item));
          requestList.push(request);
        }
      }

      Axios.all([patchOneTask(backendFatherTaskInfo), ...requestList])
        .then((res) => {
          console.log("saveTask success", res);
          this.$message({
            type: 'success',
            message: '保存事项成功！'
          })

          this.isSaved = true;

          //如果是退出组件时的保存事项，要给父组件发消息
          if (msg == 'exitComponent')
            this.$emit('resetDialogVisible');
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
      this.setFrontendTaskObj(this.taskId, true);
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
    async setFrontendTaskObj (taskId, forceUpdate) {
      //如果taskBox组件已经有信息了，就不要重复拉取后端了
      if (this.taskInfo.id && forceUpdate != true)
        return;
      await getTaskById(taskId)
        .then((res) => {
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
                classificationTitle: value.classificationTitle,
                priority: _priority,
                timeRange: [value.startTime, value.endTime],
                familyPosition: value.isParent == 1 ? 'parent' : 'child',
                familyMembers: value.relativeTask,
                isInDustbin: value.isInDusbin,
                tag: value.tag
              }

              if (taskInfo.familyMembers != null && taskInfo.familyMembers != []) {
                //获取familyMembers
                for (let item of taskInfo.familyMembers) {
                  let _sonTaskState = this.getFrontendState(item.taskState, item.startTime, item.endTime);
                  item['id'] = item['taskId'];
                  item['taskTitle'] = item['taskTitle']
                  item['taskDetail'] = item['taskDetail']
                  //获取isdone
                  item['isdone'] = _sonTaskState.indexOf("完成") >= 0 ? true : false;
                  //获取taskState
                  item['taskState'] = _sonTaskState
                  item['classificationTitle'] = item['classificationTitle']
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
                  item['familyPosition'] = 'child';
                  item['isInDustbin'] = item['isInDustbin']
                }//end of for
              }

              this.taskInfo = taskInfo;
              this.sonTaskBoxDialogVisible = false;
            }
          }//end of if

        })
        .catch((err) => {
          console.log(err);
          this.sonTaskBoxDialogVisible = false;
        })
    },
    //点击“查看详情”按钮
    handleClickDetail (row) {
      console.log("查看详情！")
      this.chosen_taskId = row.taskId;
      this.sonTaskBoxDialogVisible = true;
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
      handler (newVal) {
        console.log("检测到事项信息修改！")
        this.isSaved = false;
      },
      deep: true,
      immediate: true
    },
    dialogVisible: {
      handler (newVal) {
        console.log("接收到父组件传参：", this.taskId)
        //打开页面，向后端请求
        if (newVal == true) {
          this.setFrontendTaskObj(this.taskId);
        }
        //如果子组件被关闭了，把dialogVisible=false的值同步给父组件
        if (newVal == false && this.isSaved) {
          let backendTaskInfo = this.getBackendTaskInfo(this.taskInfo);

          //在这个函数里调用了emit
          this.saveTask('exitComponent');
        }
        else {
          this.$emit('resetDialogVisible');
        }
      }
    },
  },
  async mounted () {
    console.log("this.taskId", this.taskId)
    this.setThisUserAllClassificaitonTitle();//获取该用户的所有分组名称
    this.setFrontendTaskObj(this.taskId)//接收从父组件传来的事项信息
      .then(() => {//这里涉及到向后端请求数据的异步
        this.dialogVisible = this.taskBoxDialogVisible;
        this.setIsdone()
          .then(() => {//这里涉及到watch的异步
            this.isSaved = true;
            console.log("isSaved!")

            if (this.taskInfo.id != '' && this.taskInfo.id != null) {
              console.log("我taskId绝对不为空，我是", this.taskInfo.id)
              // 创建WebSocket连接
              var websocket = null;
              if ("WebSocket" in window) {
                console.group("检测到WebSocket!")
                console.log("yes!")
                console.groupEnd("检测到WebSocket!")

                websocket = new WebSocket(
                  // "ws://localhost:8081/api/webSocket?activityId=" + this.activityId
                  // "ws://42.192.45.95:8081/api/webSocket?activityId=" + this.activityId
                  "ws://" + process.env.VUE_APP_BASE_IP_PORT + "/api/taskExpireWebSocket?taskId=" + this.taskInfo.id
                );
              } else {
                alert("该浏览器不支持websocket！");
              }

              websocket.onopen = function (event) {
                console.log("建立连接");
              };
              websocket.onclose = function (event) {
                console.log("连接关闭");
              };
              websocket.onmessage = (event) => {



                //将字符串转为JSON对象
                var message = JSON.parse(event.data);
                console.log("收到消息", message);
                if (message.toContactId == this.taskInfo.id) {
                  console.group("成功了！")
                  console.log(message)
                  console.groupEnd("成功了！")

                //实时改变前端展现的状态
                  if (message.content == "未开始->进行中")
                    this.taskInfo.taskState = "进行中";
                  else if (message.content == "进行中->延期中")
                    this.taskInfo.taskState = "延期中";
                  console.log("前端状态转变完成！")
                }
              };
              websocket.onerror = function (event) {
                console.log("websocket通信发生错误");
              };

              window.onbeforeunload = function () {
                websocket.close();
              };
            }
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