/**
全日历页面
@author yy
@editor yy, hym
@lastUpdata hym 10.22
*/
<template>
  <div>
    <FullCalendar
      v-if="this.displayCalendar==true"
      ref="fullCalendar"
      :options="calendarOptions"
    />

    <!--用于每次调用子组件都按照当前时间重新生成DOM元素-->
    <CreateTaskBox
      v-if="this.displayCalendar==true"
      :createTaskBoxDialogVisible="createTaskBoxDialogVisible"
      :timeRange="chosen_dateObj"
      @resetDialogVisible="resetDialogVisible()"
    />
    <TaskBox
      v-if="this.displayCalendar==true"
      :taskBoxDialogVisible="taskBoxDialogVisible"
      :taskId="chosen_taskId"
      :key="new Date().toString()"
      @resetDialogVisible="resetDialogVisible()"
    />
  </div>
</template>


<script>
import { getAllTaskAndRelative, patchOneTask } from "@/api/task.js"
// 引入日历组件
import CreateTaskBox from "../../components/CreateTaskBox";
import TaskBox from "../../components/TaskBox";
import "@fullcalendar/core/vdom"; // solves problem with Vite
import FullCalendar from "@fullcalendar/vue";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import resourceTimelinePlugin from "@fullcalendar/resource-timeline";
import listPlugin from "@fullcalendar/list";
import timeGridPlugin from "@fullcalendar/timegrid";
import moment from "moment";
import * as HolidayUtil from "./utils/holiday.js";
import * as GetdomUtil from "./utils/getdom.js"


export default {
  name: "CalenderView",
  components: { FullCalendar, CreateTaskBox, TaskBox },
  data () {
    return {
      userId: this.$store.getters.id,
      displayCalendar: false,
      fisrt: true,
      chosen_taskId: 0,
      chosen_dateObj: {}, //当前选中的日期
      chosen_taskObj: {//当前选中的任务
        taskTitle: '背单词',
        taskDetail: "先背两小时单词",//详情的文字
        isdone: null,//初始时计算得出，不是从后端请求来的
        taskState: '进行中',
        classification: '学习',
        priority: '无优先级',
        timeRange: [new Date(), ''],
        familyPosition: 'parent',
        familyMembers: [],
      },
      //需要维护的视图当前信息
      titleDom: '',
      currentViewInfo: {
        currentViewType: '',//当前视图种类
        currentViewStartTime: '',//视图开始时间
        currentViewEndTime: '',//视图结束时间
      },

      createTaskBoxDialogVisible: false,
      taskBoxDialogVisible: false,
      isDateClicking: false,
      calendarOptions: {
        plugins: [
          dayGridPlugin,
          interactionPlugin,
          resourceTimelinePlugin,
          listPlugin,
          timeGridPlugin,
          //bootstrap5Plugin,
        ],
        locale: "zh",
        timeZone: "local",
        buttonText: {
          today: "今天",
          month: "月",
          week: "周",
          day: "日",
        },
        headerToolbar: {
          left: "prev,next today",
          center: "title",
          right: "dayGridMonth,timeGridWeek,timeGridDay",
        },
        initialView: "dayGridMonth",
        //themeSystem: 'bootstrap5',
        selectable: true,
        editable: true, //拖拽编辑
        nowIndicator: true, //当前时间指示器

        //事件的回调函数
        select: this.handleDateSelect,
        eventClick: this.handleEventClick,
        eventDrop: this.handleEventDragAndResize,
        eventResize: this.handleEventDragAndResize,

        //每格显示数量限制
        dayMaxEventRows: true,
        views: {
          timeGrid: {
            dayMaxEventRows: 6, // adjust to 6 only for timeGridWeek/timeGridDay
          },
        },
        events: [],
        resources: []

      },

      //事项列表
      taskList: [
        {
          id: '001',
          taskTitle: '背单词1',
          taskDetail: "先背两小时单词",//详情的文字
          isdone: true,
          taskState: '进行中',
          classification: '学习',
          priority: '无优先级',
          timeRange: ["2022-11-01T12:30:00", "2022-11-02T12:30:00"],
          familyPosition: 'parent',
          familyMembers: [],
        },
        {
          id: '002',
          taskTitle: '背单词2',
          taskDetail: "先背两小时单词",//详情的文字
          isdone: true,
          taskState: '延期中',
          classification: '学习',
          priority: '无优先级',
          timeRange: ["2022-11-02T12:30:00", "2022-11-02T13:30:00"],
          familyPosition: 'parent',
          familyMembers: [],
        }
      ]
    };
  },
  methods: {
    resetDialogVisible () {
      this.createTaskBoxDialogVisible = false;
      this.taskBoxDialogVisible = false;

      //重新从后端拿取数据，然后渲染
      this.displayCalendar = false;
      this.getFrontendTaskList();
    },
    /*
      处理回调
    */
    //选中日期，进入创建事项
    handleDateSelect: function (arg) {
      this.chosen_dateObj = [arg.start, arg.end];
      // console.group("日期信息");
      // console.log(this.chosen_dateObj)
      // console.groupEnd("日期信息");
      this.createTaskBoxDialogVisible = true;
    },
    //选中事项，查看事项详情
    handleEventClick: function (arg) {
      for (let i = 0; i < this.taskList.length; i++) {
        if (this.taskList[i].id == arg.event.id) {
          this.chosen_taskId = this.taskList[i].id;
          console.log("this.chosen_taskId", this.chosen_taskId);
          break;
        }
      }
      this.taskBoxDialogVisible = true;
      //this.updateCurrentViewInfo();
    },
    //处理drag和resize
    handleEventDragAndResize: function (arg) {
      //1.更新taskList
      for (let i = 0; i < this.taskList.length; i++) {
        if (this.taskList[i].id == arg.event.id) {
          this.taskList[i].timeRange = [arg.event.start, arg.event.end];
        }
      }

      //2.更新event
      for (let i = 0; i < this.calendarOptions.events.length; i++) {
        if (this.calendarOptions.events[i].id == arg.event.id) {
          this.calendarOptions.events[i].start = arg.event.start;
          this.calendarOptions.events[i].end = arg.event.end;

          this.taskList[i].timeRange = [arg.event.start, arg.event.end];
          //3.向后端发patch请求
          //let backendTaskList
          patchOneTask(this.getBackendTaskList()[i])
            .then((res) => {
              console.log("patch请求成功！", res);
            })
            .catch((err) => {
              console.log(err);
            })
        }
      }
    },

    /*
      获取events和resources
    */
    getEvents () {
      let eventList = []

      this.taskList.forEach((value) => {

        //计算要不要allDay
        let my_allDay = value['timeRange'] === [] || value.taskState.indexOf("完成") >= 0 ? true : false;

        //如果完成了，就标灰
        let my_color;
        if (value.taskState.indexOf("完成") >= 0)
          my_color = "#378006"//完成：灰色       
        else if (value.taskState == "进行中")
          my_color = "#d1d2c8"//进行中：绿色
        else if (value.taskState == "延期中")
          my_color = "#c6cb25"//延期：黄色


        //往event列表里加
        eventList.push({
          id: value['id'],
          title: value['taskTitle'],
          start: value['timeRange'][0],
          end: value['timeRange'][1],
          allDay: my_allDay,
          displayEventTime: false,
          resourceIds: [value['id']],
          color: my_color
        })
      })

      return eventList;



    },//end of method
    getResources () {
      let resourceList = []
      let taskList = []

      taskList.forEach((value) => {
        //计算要不要children
        let my_children = [];
        if (value['familyPosition'] == 'parent') {
          my_children = value['familyMembers'].slice(0);
        }

        //往resource列表里加
        resourceList.push({
          id: value['id'],
          title: value['taskTitle'],
          children: my_children
        })
      })
      return resourceList

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

    async getFrontendTaskList () {

      //从后端拉取数据
      getAllTaskAndRelative(this.userId)
        .then((res) => {

          let taskList = []
          //然后要把后端拉回来的task对象转换成前端要用的对象
          if (res.data != null || res.data != []) {
            let backendDataList = res.data;

            if (backendDataList == null)
              return;
            backendDataList.forEach((value) => {

              if (value.isInDustbin == '0') {
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

                taskList.push({
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
                })
              }
            })
          }//end of if

          this.taskList = taskList;
          this.calendarOptions.events = this.getEvents();
          console.group("this.calendarOptions")
          console.log(res)
          console.log(taskList)
          console.log(this.getEvents())
          console.log(this.calendarOptions)
          console.groupEnd("this.calendarOptions")
          this.calendarOptions.resources = this.getResources();
          this.displayCalendar = true;

          //确定节假日的代码
          this.setHolidayInfo();
          this.addWatchDom();

        })//end of then
        .catch((err) => {
          this.displayCalendar = true;
          console.log("月份后端请求失败！", err);
          return null;
        })
      return null;
    },//end of method
    setHolidayInfo () {
      this.$nextTick(() => {
        //获取dom元素，确定节假日
        let domList = document.getElementsByClassName("fc-daygrid-day-top")

        for (let dom of domList) {
          let dateDom = dom.innerHTML;
          let year = GetdomUtil.getDomYear(dateDom);
          let month = GetdomUtil.getDomMonth(dateDom);
          let day = GetdomUtil.getDomDay(dateDom);
          let holidayOrLunar = HolidayUtil.getHolidayInfo(year, month, day);
          dom.setAttribute('data-text', holidayOrLunar);
        }
      })
    },
    getBackendTaskList () {
      let backendTaskList = []
      this.taskList.forEach((value) => {

        let _taskState = 0;
        switch (value.taskState) {
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
        switch (value.priority) {
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

        backendTaskList.push({
          taskId: value.id,
          taskTitle: value.taskTitle,
          taskDetail: value.taskDetail,
          taskState: _taskState,
          classificationTitle: value.classificationTitle,
          priority: _priority,
          startTime: value.timeRange[0],
          endTime: value.timeRange[1],
          isParent: value.familyPosition == 'parent' ? 1 : 0,
          relativeTask: []//todo
        })
      })
      return backendTaskList;
    },//end of method

    updateHolidayAndLunarDom () {
      console.log("监听到dom元素变化！")
      this.setHolidayInfo();
    },
    addWatchDom () {
      this.$nextTick(() => {
        console.log("添加监听！")
        let domList = document.getElementsByTagName("button");
        for (let dom of domList) {
          dom.addEventListener("click", this.updateHolidayAndLunarDom);
        }

      })
    }
  },
  beforeDestroy:function(){
    //页面销毁时移除dom元素监听
    window.removeEventListener("click", this.updateHolidayAndLunarDom);
  },

  watch: {
    calendarOptions: {
      handler (newVal) {
        //console.log(newVal);
      },
      deep: true
    },
    titleDom: {
      handler (newVal) {
        //this.updateCurrentViewInfo();
      }
    }
  },

  mounted: function () {
    console.log("mounted!!!!!!!!!!!!!!!!!!!")
    this.getFrontendTaskList()
    .then(()=>{
      console.group("eventList")
      console.log(this.getEvents())
      console.groupEnd("eventList")
    })
  },

};
</script>

<style lang="less" scoped>
//改变日历主题色
/deep/ .fc-button-primary {
  background-color: green !important;
  border-color: green !important;
}

/deep/ .fc-button-primary:hover {
  background-color: green !important;
  border-color: green !important;
}

/deep/ .fc-button-primary:active {
  background-color: green !important;
  border-color: green !important;
}

//隐藏盗版
/deep/ .fc-license-message {
  opacity: 0 !important;
}

//修改下拉框
::v-deep .el-select {
  width: 7em;
}

::v-deep .el-input__inner {
  background-color: transparent;
}

//添加节假日
::v-deep .fc-daygrid-day-top::after {
  content: attr(data-text);
  position: relative;
  right: 0.4em;
  top: 0.25em;
}

::v-deep .fc-daygrid-day-number {
  // position: relative;
  // left: 3em;
}
</style>