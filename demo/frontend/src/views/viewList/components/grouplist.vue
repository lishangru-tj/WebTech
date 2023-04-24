<template>
  <div class="mpvlgl_view">
    <draggable
      :list="listsData"
      group="gg"
      class="mpvg_big_drag"
    >

      <transition-group
        class="mpvg_groups"
        style="min-height: 3em; display: flex; flex-direction: row"
      >
        <span
          :span="12"
          v-for="group in listsData"
          :key="group.classificationTitle"
          class="mpvg_group"
        >
          <span class="mpvg_group_title">{{ group.classificationTitle }}
            <el-button
              size="mini"
              type="danger"
              @click="deleteClassification(group.classificationTitle)"
            ><i class="el-icon-delete"></i></el-button>
          </span>
          <!-- 在该分组新建task -->
          <el-card
            v-if="!group.isShow_quickCreate_new_task"
            @click.native="group.isShow_quickCreate_new_task = true"
          >新建事项</el-card>
          <!-- 点击快捷新建，输入框 -->
          <el-card
            v-else
            class="mpvg_group_createNewTask"
          >
            <el-input
              v-model="group.quickCreate_task_tmp.taskTitle"
              placeholder="请输入内容"
            ></el-input>
            <el-button
              type="primary"
              style="float: right; margin-top: 0.5em; margin-bottom: 0.5em"
              @click="CreateQuickTaskCreate(group)"
            >创建</el-button>
            <el-button
              type="default"
              style="float: right; margin: 0.5em 0.5em 0.5em"
              @click="group.isShow_quickCreate_new_task = false"
            >取消</el-button>
          </el-card>
          <div class="mpvg_group_tasklist">
            <draggable
              class="task_item_drag"
              :list="group.taskList"
              group="task"
              @choose="beforeDragged"
              @unchoose="afterDragged"
            >
              <!--事项列表-->
              <transition-group style="min-height: 3em; display: block">
                <div
                  class="mpvg_task_item"
                  v-for="(task, task_i) in group.taskList"
                  :key="task_i"
                >
                  <el-card>
                    <el-radio
                      class="isDone_radio"
                      v-model="task.isdone"
                      :label="true"
                      @click.native.prevent="onTaskDoneRadioChange(task)"
                    ><span></span>
                    </el-radio>
                    {{ task.taskTitle }}
                  </el-card>
                </div>
              </transition-group>
            </draggable>
          </div>
        </span>
      </transition-group>
    </draggable>
    <el-card class="mpvg_quickCreate_group">
      <span
        v-if="!isShow_input_quickCreate_group"
        @click="isShow_input_quickCreate_group = true"
      >新建分组</span>
      <span v-else>
        <el-input
          placeholder="请输入分组名"
          @blur="isShow_input_quickCreate_group = false"
          v-model="quickCreate_group_name"
        ></el-input>
        <el-button
          @mousedown.native="CreateQucikGroup"
          style="float: right; margin-top: 2em"
          type="primary"
        >创建</el-button>
      </span>
    </el-card>
  </div>
</template>
<script>
// import glistitem from "./glistitem";
import draggable from "vuedraggable";
import { getAllSortedTask, postOneNewClassification, deleteOneClassification } from "@/api/classification.js"
import { postOneNewTask, patchOneTask } from "@/api/task.js"

export default {
  name: "grouplist",
  props: {},
  components: {
    draggable,
  },
  data () {
    return {
      userId: this.$store.getters.id,
      tmpListsData: [],
      /**显示的数据源 */
      listsData: [
        {
          classificationTitle: "分组1ID",
          taskList: [
            {
              taskId: "g1t1_ID",
              taskTitle: "g1t1_NAME",
              isdone: false,
            },
            {
              taskId: "g1t2_ID",
              taskTitle: "g1t2_NAME",
              isdone: false,
            },
          ],
          /**
           * 快捷创建新的事项
           */
          isShow_quickCreate_new_task: false,
          //临时存储 新事项
          quickCreate_task_tmp: {
            taskTitle: "",
          },
        },
        {
          classificationTitle: "分组2ID",
          taskList: [
            {
              taskTitle: "g2t1_NAME",
              id: "g2t1_ID",
              isdone: false,
            },
            {
              taskTitle: "g2t2_NAME",
              id: "g2t2_ID",
              isdone: false,
            },
          ],
          /**
           * 快捷创建新的事项
           */
          isShow_quickCreate_new_task: false,
          //临时存储 新事项
          quickCreate_task_tmp: {
            taskTitle: "",
          },
        },
      ],

      /*快捷创建新的分组 */
      quickCreate_group_name: "",
      quickCreate_group: {
        name: "",
        id: "",
        taskList: [],
        /**
         * 快捷创建新的事项
         */
        isShow_quickCreate_new_task: false,
        //临时存储 新事项
        quickCreate_task_tmp: {
          taskTitle: "",
        },
      },
      isShow_input_quickCreate_group: false,
    };
  },
  methods: {
    /*task check */
    onTaskDoneRadioChange (model) {
      model.isdone = !model.isdone;

      let _taskState = 0;
      if (model.isdone == 1) {
        _taskState = new Date() > model.endTime ? 2 : 1;
      }

      let backendTaskInfo = {
        taskId: model.taskId,
        taskState: _taskState,
        isParent: model.isParent
      }
      //修改后端
      patchOneTask(backendTaskInfo)
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        })

    },
    /**
     *
     * 快捷创建新的事项
     *
     *
     */
    //确认创建新的事项
    CreateQuickTaskCreate (group) {
      //api
      let newTaskObj = {
        taskTitle: group.quickCreate_task_tmp.taskTitle,
        classificationTitle: group.classificationTitle,
        userId: this.userId,
        taskState: 0,
        isParent: 1,
        isInDustbin: "0",
      }
      postOneNewTask(newTaskObj)
        .then((res) => {
          console.log(res);
          this.$message({
            type: 'success',
            message: '成功创建新事项！'
          })

          //修改前端页面
          for (let i = 0; i < this.listsData.length; i++) {
            if (this.listsData[i].classificationTitle == newTaskObj.classificationTitle)
              this.listsData[i].taskList.push({
                taskTitle: newTaskObj.taskTitle,
                classificationTitle: newTaskObj.classificationTitle,
                isdone: '0'
              })
          }
        })
        .catch((err) => {
          console.log(err);
          this.$message({
            type: 'danger',
            message: '创建新事项失败'
          })
        })

      //关闭输入框
      group.isShow_quickCreate_new_task = false;
    },

    /**
     * 快捷创建新分组
     */
    CreateQucikGroup () {
      //api
      postOneNewClassification(this.userId, this.quickCreate_group_name)
        .then((res) => {
          console.log(res);
          this.$message({
            type:'success',
            message:'添加分组成功！'
          })
        })
        .catch((err) => {
          console.log(err);
          this.$message({
            type:'danger',
            message:'添加分组失败'
          })
        })
      //请求为这个项目新增一个空分组
      //let new_group_id = this.quickCreate_group_name + "_ID";

      this.listsData.unshift({
        classificationTitle: this.quickCreate_group_name,
        taskList: [],
        /**
         * 快捷创建新的事项
         */
        isShow_quickCreate_new_task: false,
        //临时存储 新事项
        quickCreate_task_tmp: {
          taskTitle: "",
        },
      });

      //滚动条到最左
      var div = document.querySelector(".mpvv_G_content");
      div.scrollLeft = 0;
      //清空输入
      this.quickCreate_group_name = "";
      this.isShow_input_quickCreate_group = false;
    },

    //滚动条监听
    //主滚动条设置
    setScrollBar () {
      this.$nextTick(function () {
        var div = document.querySelector(".mpvv_G_content");

        div.style.height = window.innerHeight - 10 + "px";
      });
    },

    /*向后端获取taskList*/
    setListsData () {
      getAllSortedTask(this.userId)
        .then((res) => {
          console.log(res);
          let frontendListData = []
          let backendListData = res.data

          backendListData.forEach((value) => {
            //前端ListData格式
            frontendListData.push({
              classificationTitle: value.classificationTitle,
              userId: value.userId,
              taskList: value.taskList,
              isShow_quickCreate_new_task: false,
              quickCreate_task_tmp: {
                taskTitle: "",
              },
            })
          })
          //给每个task添加aisdone属性
          for (let i = 0; i < frontendListData.length; i++) {
            for (let j = 0; j < frontendListData[i].taskList.length; j++) {

              frontendListData[i].taskList[j]['isdone'] = frontendListData[i].taskList[j].taskState == 0 ? 0 : 1;
            }
          }
          this.listsData = frontendListData;

        })
        .catch((err) => {
          console.log(err);
        })
    },
    //拖动开始时的回调函数
    beforeDragged () {
      console.log("before回调函数！")
      this.tmpListsData = []

      //浅拷贝！
      for (var i = 0; i < this.listsData.length; i++) {
        this.tmpListsData.push(JSON.parse(JSON.stringify(this.listsData[i])));
      }
    },
    //拖动结束后的回调函数
    afterDragged () {
      console.log("after回调函数！")
      const oldListsData = this.tmpListsData;
      const newListsData = this.listsData;

      console.log("old", oldListsData)
      console.log("new", newListsData)

      let src_index = -1;
      let dst_index = -1;

      let count = 0;

      for (let i = 0; i < oldListsData.length; i++) {
        if (oldListsData[i].taskList.length != newListsData[i].taskList.length) {
          count++;
          if (oldListsData[i].taskList.length < newListsData[i].taskList.length)
            dst_index = i;
          else
            src_index = i;
        }

        if (count >= 2)
          break;
      }
      console.log(count)
      if (count != 2)
        return;

      //找到老与新分组的taskList
      let srcTaskList = oldListsData[src_index].taskList;
      let dstTaskList = newListsData[dst_index].taskList;

      //找到新旧的分组名称
      let srcClassificationTitle = newListsData[src_index].classificationTitle;
      let dstClassificationTitle = newListsData[dst_index].classificationTitle;

      //接下来找到被拖拽事项的taskId就行了
      //这个taskId即在老taskList出现过，又在新taskList出现过
      let targetTaskId = -1;
      let breakFlag = false;
      for (let i = 0; i < srcTaskList.length; i++) {
        for (let j = 0; j < dstTaskList.length; j++) {
          if (srcTaskList[i].taskId == dstTaskList[j].taskId) {
            targetTaskId = srcTaskList[i].taskId;
            breakFlag = true;
            break;
          }
        }
        if (breakFlag)
          break;
      }

      //发patch请求改变事项的分组
      let newTaskObj = {
        taskId: targetTaskId,
        classificationTitle: dstClassificationTitle,
        isParent: 1,
      }

      patchOneTask(newTaskObj)
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        })

    },
    deleteClassification (classificationTitle) {
      if (classificationTitle == "默认分组") {
        this.$message({
          type: 'warning',
          message: '不能删除默认分组！'
        })
      }
      else {
        this.$confirm('您确定要删除分组吗吗？原分组内的所有事项将自动移入默认分组', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
          .then(() => {
            console.log("断点")
            deleteOneClassification(this.userId, classificationTitle)
              .then((res) => {
                console.log(res);
                this.$message({
                  type: 'success',
                  message: '成功删除分组！'
                })
                this.setListsData();
              })
              .catch((err) => {
                console.log(err);
                this.$message({
                  type: 'danger',
                  message: '删除分组失败'
                })
              })
          })

      }

    }
  },
  mounted () {
    /**
     * 所有group共存的大盒子,滚动条设置
     *
     */
    this.$nextTick(function () {
      var div = document.querySelector(".mpvv_G_content");
      div.style.height = window.innerHeight + "px";
    });
    this.setScrollBar();
    // 浏览器缩放，更新ScrollBar位置
    window.addEventListener("resize", this.setScrollBar);

    //向后端请求taskList
    this.setListsData();
  },
  beforeDestroy () {
    var div = document.querySelector(".mpvv_G_content");
    window.removeEventListener("resize", this.setScrollBar);
  },
};
</script>
<style lang="less" scoped>
.mpvlgl_view {
  display: flex;
  //   width: 100%;
  .mpvg_big_drag {
    .mpvg_groups {
      .mpvg_group {
        margin-left: 2em;
        // border: solid black 2px;
        width: 20em;
        .mpvg_group_title {
          font-size: 1.2em;
          font-weight: bold;
        }
        .mpvg_group_createNewTask {
          width: 100%;
        }
        .mpvg_group_tasklist {
          display: flex;
          flex-direction: column;
          .task_item_drag {
            .mpvg_task_item {
              .isDone_radio {
                margin-right: 0px;

                //从默认圆改成方形勾
                ::v-deep .el-radio__inner {
                  border-radius: 2px;
                  border-width: 2px;
                  //   border-color: var(--color-primary) !important; //外框颜色
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
            }
          }
        }
      }
    }
  }
  .mpvg_quickCreate_group {
    width: 20em;
    height: 10em;
    margin-left: 2em;
    margin-right: 5em;
    border: solid black 2px;
  }
}
</style>
