<template>
  <div class="viewList_view">
    <!--事项详情组件-->
    <TaskBox
      :taskBoxDialogVisible="taskBoxDialogVisible"
      :taskId="chosen_taskId"
      :key="new Date().toString()"
      @resetDialogVisible="resetDialogVisible()"
    />

    <!-- 顶部工具栏 -->
    <div class="drop_down_box">
      <div class="left_box">
        <!--导出按钮-->
        <ExportButton :taskList="tableData" />
        <!-- 左边的下拉框，可快捷筛选，即为表格的筛选器赋值
        <el-select
          @change="TopSelectValChange"
          v-model="select_allTasks_Option"
          placeholder="请选择"
        >
          <el-option
            v-for="item in select_allTasks_Options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select> -->
        <!--复选框-->
        <el-checkbox
          label="未开始"
          v-model="bool_showNoStart"
          @change="getPageData()"
        ></el-checkbox>
        <el-checkbox
          label="已完成"
          v-model="bool_showFinished"
          @change="getPageData()"
        ></el-checkbox>
        <el-checkbox
          label="进行中"
          v-model="bool_showDoing"
          @change="getPageData()"
        ></el-checkbox>
        <el-checkbox
          label="延期中"
          v-model="bool_showDelaying"
          @change="getPageData()"
        ></el-checkbox>
        <el-checkbox
          label="延期完成"
          v-model="bool_showDelayedFinished"
          @change="getPageData()"
        ></el-checkbox>

      </div>
      <div class="right_box">
        <!--搜索框-->
        <div class="search_allTasks">
          <el-autocomplete
            class="inline-input"
            v-model="searchText"
            style="width: 300px; float: right"
            size="mini"
            :fetch-suggestions="SearchTaskByKeyWord"
            placeholder="请输入事项关键词"
            @select="SelectTaskSearchSuggestion"
            @keyup.enter.native="getPageData"
          ></el-autocomplete>
        </div>
        <!-- 表头设置 -->
        <div
          shadow="hover"
          style="display: flex;"
          body-style="padding:0;margin:0 .3em 0 .3em;"
          class="arrange_head"
        >
          <!-- poper弹框 -->
          <el-popover
            class="headset_pop"
            placement="top"
            width="160"
            v-model="ShowHeadSet"
            @after-leave="HeadSetAfterLeave"
          >
            <!-- 可拖拽内容 -->
            <div class="headset_list">
              <draggable
                v-model="HeadSetData"
                filter=".undraggable"
              >
                <transition-group>
                  <div
                    v-for="element in HeadSetData"
                    :key="element.label"
                    :key-name="element.key"
                    :class="{
                      undraggable: element.key == 'name',
                    }"
                  >
                    <span>{{ element.label }}</span>

                    <el-radio
                      style="float: right"
                      v-model="element.isShow"
                      :label="true"
                      @click.native.prevent="
                        (event) => HeadSetClickRadio(event, element)
                      "
                    ><span> </span></el-radio>
                  </div>
                </transition-group>
              </draggable>
            </div>
            <!-- poper的底部按钮 -->
            <div
              style=""
              class="buttom_buttons"
            >
              <el-button
                size="mini"
                type="text"
                @click="HeadSetPopCancel"
              >
                取消
              </el-button>
              <el-button
                type="primary"
                size="mini"
                @click="HeadSetPopConfirm"
              >
                确定
              </el-button>
            </div>
            <!-- <el-button
              slot="reference"
              size="mini"
            >表头设置</el-button> -->
          </el-popover>
        </div>
      </div>
    </div>

    <!-- 表格部分 -->
    <div class="contents_table_box">
      <el-table
        :loading="loading"
        class="contents_table"
        :data="show_table"
        border
        :row-class-name="GetRowClass"
        :show-header="true"
        @cell-dblclick="
          (row, column, cell, event) => cellDbClick(row, column, cell, event)
        "
        @cell-click="
          (row, column, cell, event) => cellClick(row, column, cell, event)
        "
        :tree-props="{ children: 'children'}"
        row-key="taskId"
      >
        <!-- 列排序，每次表头顺序修改都重新渲染 -->
        <span
          v-for="(head_item, head_index) in head_data_common"
          :key="head_index"
        >
          <!-- <el-table-column width="1" /> -->
          <!-- fixed 名称 Col,name -->
          <el-table-column
            fixed
            :label="head_data_common_dict['name'].label"
            class-name="name_col"
            :width="head_data_common_dict['name'].width"
            v-if="
              head_item.key == 'name' && head_data_common_dict['name'].isShow
            "
            :key="head_data_common_dict['name'].prio"
            ref="name_col_ref"
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

              <span>{{ props.row.name }}</span>
            </template>
          </el-table-column>

          <!-- 事项开始时间Col,startTime -->
          <el-table-column
            :label="head_data_common_dict['startTime'].label"
            class-name="task_time_col"
            :width="head_data_common_dict['startTime'].width"
            v-if="
              head_item.key == 'startTime' &&
              head_data_common_dict['startTime'].isShow
            "
            :key="head_data_common_dict['startTime'].prio"
          >
            <template slot-scope="props">
              <span>{{ props.row.startTime }}</span>
            </template>
          </el-table-column>

          <!-- 事项结束时间Col,endTime -->
          <el-table-column
            :label="head_data_common_dict['endTime'].label"
            class-name="taskDoneTime_col"
            :width="head_data_common_dict['endTime'].width"
            v-if="
              head_item.key == 'endTime' &&
              head_data_common_dict['endTime'].isShow
            "
            :key="head_data_common_dict['endTime'].prio"
          >
            <template slot-scope="props">
              <span>{{ props.row.endTime }}</span>
            </template>
          </el-table-column>

          <!-- 分组Col,group -->
          <el-table-column
            :label="head_data_common_dict['group'].label"
            class-name="group_col"
            :width="head_data_common_dict['group'].width"
            v-if="
              head_item.key == 'group' && head_data_common_dict['group'].isShow
            "
            :key="head_data_common_dict['group'].prio"
          >
            <template slot-scope="props">
              <span>{{ props.row.group }}</span>
            </template>
          </el-table-column>

          <!-- 标签Col,group -->
          <el-table-column
            :label="head_data_common_dict['tag'].label"
            class-name="tag_col"
            :width="head_data_common_dict['tag'].width"
            v-if="
              head_item.key == 'tag' && head_data_common_dict['tag'].isShow
            "
            :key="head_data_common_dict['tag'].prio"
          >
            <template slot-scope="props">
              <span>{{ props.row.tag }}</span>
            </template>
          </el-table-column>

          <!-- 优先级Col,priority -->
          <el-table-column
            :label="head_data_common_dict['priority'].label"
            class-name="priority_col"
            :width="head_data_common_dict['priority'].width"
            v-if="
              head_item.key == 'priority' && head_data_common_dict['priority'].isShow
            "
            :key="head_data_common_dict['priority'].prio"
          >
            <template slot-scope="props">
              <span>{{ props.row.priority }}</span>
            </template>
          </el-table-column>

          <!-- 事项状态Col,state_label -->
          <el-table-column
            :label="head_data_common_dict['state_label'].label"
            class-name="state_label_col  click_col"
            :width="head_data_common_dict['state_label'].width"
            v-if="
              head_item.key == 'state_label' &&
              head_data_common_dict['state_label'].isShow
            "
            :key="head_data_common_dict['state_label'].prio"
            :filters="[
              { text: '已完成', value: '0' },
              { text: '进行中', value: '1' },
              { text: '延期中', value: '2' },
              { text: '延期完成', value: '3' },
            ]"
            :filter-method="StateFilterHandler"
            ref="state_col_ref"
          >
            <template slot-scope="props">
              <el-tag :type="StateTagName(props.row.state_val, props.row)">
                <span>{{ props.row["state_label"] }}</span>
              </el-tag>
            </template>
          </el-table-column>

          <!-- 我的完成时间Col,realFinishTime -->
          <el-table-column
            :label="head_data_common_dict['realFinishTime'].label"
            class-name="taskDoneMyTime_col"
            :width="head_data_common_dict['realFinishTime'].width"
            v-if="
              head_item.key == 'realFinishTime' &&
              head_data_common_dict['realFinishTime'].isShow
            "
            :key="head_data_common_dict['realFinishTime'].prio"
          >
            <template slot-scope="props">
              <span>{{ props.row.realFinishTime }}</span>
            </template>
          </el-table-column>

          <!-- 查看详情 -->
          <el-table-column
            label="操作"
            class-name="taskDoneMyTime_col"
            :width="head_data_common_dict['operation'].width"
            v-if="
              head_item.key == 'operation' &&
              head_data_common_dict['operation'].isShow
            "
          >
            <template slot-scope="props">
              <el-button
                @click="handleClickDetail(props.row)"
                type="text"
                size="small"
              >查看</el-button>
            </template>
          </el-table-column>

        </span>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="query.pageNum"
        :page-sizes="[5,10,20,30]"
        :page-size="query.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="query.total"
      ></el-pagination>
    </div>

  </div>
</template>

<script>
import { getAllTaskAndRelative, patchOneTask } from '@/api/task.js'
import TaskBox from '@/components/TaskBox'
import ExportButton from './ExportButton'
import draggable from "vuedraggable";
import moment from "moment";
export default {
  name: "tablelist",
  components: { draggable, TaskBox, ExportButton },
  data () {
    return {
      userId: this.$store.getters.id,
      taskBoxDialogVisible: false,
      chosen_taskObj: {},
      chosen_taskId: 0,
      backendData: [],
      commonTaskList: [],



      //复选框筛选
      bool_showNoStart: true,
      bool_showFinished: true,
      bool_showDoing: true,
      bool_showDelaying: true,
      bool_showDelayedFinished: true,

      //excel的数据
      filename: '',
      autoWidth: true,
      bookType: 'xlsx',


      /**
       * 表格数据
       */
      tableData: [],
      init_data: [],

      //分页数据
      show_table: [],
      query: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      loading: true,

      //表格真正使用的表头数据
      head_data_common: [
        //名称
        {
          key: "name",
          width: "200rem",
          isShow: true,
          label: "名称",
          prio: 0,
        },
        //事项开始时间
        {
          key: "startTime",
          width: "150rem",
          isShow: true,
          label: "事项开始时间",
          prio: 1,
        },
        //事项结束时间
        {
          key: "endTime",
          width: "150rem",
          isShow: true,
          label: "事项结束时间",
          prio: 9,
        },
        //分组
        {
          key: "group",
          width: "150rem",
          isShow: true,
          label: "分组",
          prio: 2,
        },
        //标签
        {
          key: "tag",
          width: "150rem",
          isShow: true,
          label: "标签",
          prio: 20,
        },
        //优先级
        {
          key: "priority",
          width: "150rem",
          isShow: true,
          label: "优先级",
          prio: 67,
        },

        //事项状态
        {
          key: "state_label",
          width: "100rem",
          isShow: true,
          label: "事项状态",
          prio: 4,
        },

        //我的完成时间
        {
          key: "realFinishTime",
          width: "200rem",
          isShow: true,
          label: "我的完成时间",
          prio: 10,
        },

        //我的完成时间
        {
          key: "operation",
          width: "150rem",
          isShow: true,
          label: "操作",
          prio: 10,
        },

      ],

      /**
       * 表头位置的的下拉框，快捷筛选
       */
      //下拉框元数据
      select_allTasks_Options: [
        {
          value: "all_task",
          label: "所有事项",
        },
        {
          value: "I_not_done",
          label: "我未完成",
        },
        {
          value: "I_am_doing",
          label: "我进行中",
        },
        {
          value: "I_am_delaying",
          label: "我延期中",
        },
        {
          value: "I_have_done",
          label: "我已完成",
        },
      ],
      //下拉框取得的数据
      select_allTasks_Option: "all_task",

      /**
       * 表头排序
       *
       */
      ShowHeadSet: false,
      HeadSetData: [
        //事项开始时间
        {
          key: "startTime",
          width: "150rem",
          isShow: true,
          label: "事项开始时间",
          prio: 1,
        },
        //事项结束时间
        {
          key: "endTime",
          width: "150rem",
          isShow: true,
          label: "事项结束时间",
          prio: 9,
        },
        //分组
        {
          key: "group",
          width: "150rem",
          isShow: true,
          label: "分组",
          prio: 2,
        },
        //标签
        {
          key: "tag",
          width: "150rem",
          isShow: true,
          label: "标签",
          prio: 20,
        },
        //优先级 
        {
          key: "priority",
          width: "150rem",
          isShow: true,
          label: "优先级",
          prio: 67,
        },

        //事项状态
        {
          key: "state_label",
          width: "100rem",
          isShow: true,
          label: "事项状态",
          prio: 4,
        },

        //我的完成时间
        {
          key: "realFinishTime",
          width: "150rem",
          isShow: true,
          label: "我的完成时间",
          prio: 10,
        },

        //操作
        // {
        //   key: "operation",
        //   width: "150rem",
        //   isShow: true,
        //   label: "操作",
        //   prio: 19,
        // },


      ],

      /**
       * 搜索框
       */
      searchText: "",
      /**
       * 单元格
       */
      //双击单元格的坐标信息。行下标,列class名称
      cellDbRowIndex: null,
      cellDbColClass: "",
      //单击单元格的坐标信息。行下标,列class名称
      cellSgRowIndex: null,
      cellSgColClass: "",

      /**
       * filter筛选
       */
      //我缓存的filter配置数据
      MyFilterConfig: {
        state: [],
      },
      //表头cell是否需要点击，来使得filter元素出现并获取到
      HeadCellNeddClick: true,
      //全部的filter元素的按钮，等待按下confirm按钮
      AllFilterCellsButtons: [],
      //表格中拥有filter的列ref
      tableFilterColRefs: {
        state: null,
      },
      //名称列
      NameColRef: null,
      /**
       * 事项时间
       **/
      //日期、时间选择器dialog的显示
      TaskTimePickerBlur: false,
      //日期选择器的快捷时间
      TaskTimeDatePickerOptions: {
        shortcuts: [
          {
            text: "最近一周",
            onClick (picker) {
              const start = new Date();
              const end = new Date();
              end.setTime(end.getTime() + 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick (picker) {
              const end = new Date();
              const start = new Date();
              end.setTime(end.getTime() + 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
            onClick (picker) {
              const end = new Date();
              const start = new Date();
              end.setTime(end.getTime() + 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },

      /***
       * 项目
       */
      //我的所有项目，缓存;
      MyProjects: -1,
      //项目下拉选择器的显示
      ShowProjectSelect: false,



    };
  },
  mounted () {
    this.$nextTick(() => {
      //contents data 初始化
      //***在它的then里调用了初始化分页的函数initPage() */
      this.SetContentsData();
      //用户的所有项目简洁数据
      this.GetMyProjects();
      //点击表头，使得filter元素出现
      // this.ClickTableHead();
    });
  },
  directives: {
    focus: {
      // 指令的定义
      inserted: function (el) {
        el.focus();
      },
    },
  },
  destroyed () {
    //监听事件解绑
  },
  methods: {
    TestClick () {
      console.log(this.HeadSetData);
    },
    /**
     * 单元格
     */
    //表格的单元格,双击
    cellDbClick (row, column, cell, event) {
      // console.group("cellDbClick");
      //点击cell，记录下行列信息

      this.cellDbRowIndex = row.rowIndex;
      this.cellDbColClass = column.className;

      // console.log("cellDbRowIndex", this.cellDbRowIndex);
      // console.groupEnd("cellDbClick");
    },
    //表格的单元格，单击事件
    cellClick (row, column, cell, event) {
      // console.group("cellClick");
      // console.log(this.cellSgRowIndex);
      // console.log(this.cellSgColClass);
      // console.log("----");
      this.cellSgRowIndex = row.rowIndex;
      this.cellSgColClass = column.className;
      //事项时间的列
      if (this.cellSgColClass == "task_time_col") {
        this.TaskTimePickerBlur = true;
      } else {
        this.TaskTimePickerBlur = false;
      }
      // console.log(this.cellSgRowIndex);
      // console.log(this.cellSgColClass);

      // console.groupEnd("cellClick");
    },

    //根据每行的childrenNum，返回对应的class
    GetRowClass (props) {
      //为每行更新rowIndex
      props.row.rowIndex = props.rowIndex;
      switch (props.row.childrenNum) {
        case -1:
          return "noBorder childRow";
        default:
          if (props.row.showChildren) {
            return "noBorder parentRow";
          } else {
            return "";
          }
      }
    },

    /**
     *
     * 顶部工具栏
     */

    //顶部左边的下拉框,值改变时
    //根据下拉框的值，给my筛选器重新赋值
    async TopSelectValChange (val) {
      /**
       * 事项状态的filter取值范围
       *    { text: '已完成', value: '0' },
            { text: '进行中', value: '1' },
            { text: '延期中', value: '2' },
            { text: '延期完成', value: '3' },
            
       */
      await this.MyFilterInit().then(async (res) => {
        if (val == "all_task") {
          //所有事项
          this.MyFilterConfig.state = ["0", "1", "2", "3"];
        } else if (val == "I_not_done") {
          //我未完成
          this.MyFilterConfig.state = ["1", "2"];
        } else if (val == "I_am_doing") {
          //我进行中
          this.MyFilterConfig.state = ["1"];
        } else if (val == "I_am_delaying") {
          //我延期中
          this.MyFilterConfig.state = ["2"];
        } else if (val == "I_have_done") {
          //已经完成的
          this.MyFilterConfig.state = ["0", "3"];
        }
        this.UseMyFilterConfig();
        this.ConfirmFilterCell();

        // await this.UseMyFilterConfig().then((res) => {
        //   this.ConfirmFilterCell();
        // });
      });
    },

    /**
     * 表头设置
     */
    //表头设置的click,弃用
    ClickHeadSet () {
      this.ShowHeadSet = !this.ShowHeadSet;
      // console.log("ClickHeadSet");
    },

    //表头设置pop的关闭事件,包括了所有的情况
    HeadSetAfterLeave () {
      // console.log("HeadSetAfterLeave");

      //HeadSet检查是否相较于head_data_common变化了
      let changed = false;
      let keys = ["key", "isShow"];
      for (let i = 0; i < this.HeadSetData.length; i++) {
        for (let key in keys) {
          // console.log(
          //   this.head_data_common[i + 1][keys[key]],
          //   this.HeadSetData[i][keys[key]]
          // );
          if (
            this.head_data_common[i + 1][keys[key]] !=
            this.HeadSetData[i][keys[key]]
          ) {
            changed = true;
            break;
          }
        }
        if (changed) {
          break;
        }
      }
      if (changed) {
        console.log("changed");
        this.HeadSetData = this.HeadSetData.map((ele, index) => {
          // console.log(ele, "#", index + 1);
          // if (ele.prio == index+1) {
          //   console.log(ele, "stable");
          // } else {
          //   console.log(ele, "unstable,", index);
          // }
          return {
            key: ele.key,
            width: ele.width,
            isShow: ele.isShow,
            label: ele.label,
            prio: Math.random(),
          };
        });

        // HeadSet数据赋值给head_data_common
        this.head_data_common.splice(1, this.HeadSetData.length);
        this.HeadSetData.forEach((ele) => {
          let newEle = { ...ele };
          this.head_data_common.push(newEle);
        });
      }

      // this.$forceUpdate();
      console.log("this.head_data_common", this.head_data_common);
    },
    //表头设置pop的按钮取消
    HeadSetPopCancel () {
      this.ShowHeadSet = false;
    },
    //表头设置pop的按钮确认
    HeadSetPopConfirm () {
      this.ShowHeadSet = false;
    },

    //表头元素isShow的勾选
    HeadSetClickRadio (event, element) {
      element.isShow = !element.isShow;
      // console.log("this.head_data_common", this.head_data_common);
      // this.$forceUpdate();
      // console.log(element.isShow);
    },
    /**
     * filter筛选
     */

    async MyFilterInit () {
      //是否需要重新初始化，
      //点击表头cell，获取表头filter元素，获取拥有filter元素的列
      if (this.HeadCellNeddClick) {
        await this.ClickTableHead().then((click_res) => {
          return new Promise((resolve, reject) => {
            setTimeout(async () => {
              //获取所有的filter cells按钮
              await this.GetAllFilterCell().then((res) => {
                //获取拥有filter的列
                this.GetAllFilterColRefs();
                this.HeadCellNeddClick = false;
                // console.log(
                //   "AllFilterCellsButtons",
                //   this.AllFilterCellsButtons
                // );
                this.ClickTableHead();

                resolve(true);
              });
            }, 20);
          });
        });
      }
    },

    //使用我缓存的filter config数据，赋值给对应的filter cell元素
    //后面需要获取filter cell元素，并且按下它们的confirm按钮即可筛选
    //达到的效果：不用点击表头筛选器也能筛选
    //场景：顶部工具栏，左侧下拉框可快捷筛选，单点下拉框后即可进行筛选
    async UseMyFilterConfig () {
      return new Promise((resolve, reject) => {
        //赋值state
        // console.group("UseMyFilterConfig");
        // console.log(
        //   "this.tableFilterColRefs.state",
        //   this.tableFilterColRefs.state
        // );

        // console.log("this.MyFilterConfig.state", this.MyFilterConfig.state);
        this.tableFilterColRefs.state.columnConfig.filteredValue =
          this.MyFilterConfig.state;
        resolve(true);
        // console.groupEnd("UseMyFilterConfig");
      });
    },

    //点击表头单元格，使得filter元素出现
    async ClickTableHead () {
      return new Promise((resolve, reject) => {
        //点击表头的cell，对应的filter才会出现
        let headersHTML = document.getElementsByClassName(
          "el-table__header-wrapper"
        );
        let headersArr = Array.from(headersHTML);
        headersArr.forEach((header) => {
          let cells = Array.from(header.getElementsByClassName("click_col"));
          // console.log("cells", cells);
          cells.forEach((cell) => {
            cell.click();
          });
        });
        resolve(true);
      });
    },
    //获取拥有filter的列
    GetAllFilterColRefs () {
      //事项状态列
      let state_;
      state_ = this.$refs.state_col_ref;
      if (state_ instanceof Array) {
        state_ = state_[0];
      }

      this.tableFilterColRefs = {
        state: state_,
      };
      // console.log("this.tableFilterColRefs ", this.tableFilterColRefs);
    },

    //获取所有filter cell元素，只是为了按click启动筛选
    async GetAllFilterCell () {
      return new Promise((resolve, reject) => {
        let filters_cell = document.getElementsByClassName("el-table-filter");
        let cellButtons = [];
        for (let c of filters_cell) {
          let bottom = Array.from(
            c.getElementsByClassName("el-table-filter__bottom")
          );
          let twoButtons = {
            confirm: "",
            reset: "",
          };
          // 获取到俩按钮，一个confirm，一个reset
          let buttons = Array.from(bottom[0].getElementsByTagName("button"));

          if (
            buttons[0].innerText.indexOf("筛选") != -1 ||
            buttons[0].innerText.indexOf("Confirm") != -1
          ) {
            twoButtons = {
              confirm: buttons[0],
              reset: buttons[1],
            };
          } else {
            twoButtons = {
              confirm: buttons[1],
              reset: buttons[0],
            };
          }

          cellButtons.push(twoButtons);
          // console.log("twoButtons", twoButtons);
        }
        this.AllFilterCellsButtons = cellButtons;
        resolve(this.AllFilterCellsButtons.length);
        console.log("AllFilterCellsButtons", this.AllFilterCellsButtons);
        // console.log(filters_cell);
      });
    },
    //按下所有filter cell元素的确定按钮
    ConfirmFilterCell () {
      // console.log("AllFilterCellsButtons", this.AllFilterCellsButtons);
      this.AllFilterCellsButtons.forEach((ele) => {
        ele.confirm.click();

        console.log(ele.confirm);
      });
    },

    //事项状态 state的真正filter
    //state按照这个模式来过滤
    StateFilterHandler (value, row, column) {
      /**
       *    { text: '已完成', value: '0' },
            { text: '进行中', value: '1' },
            { text: '延期中', value: '2' },
            { text: '延期完成', value: '3' },
            
       */

      return row.state_val == value;
    },
    //根据后端priority获取优先级的文字
    getPriorityTitle (priorityId) {
      switch (priorityId) { case 0: return '无优先级'; case 1: return '低优先级'; case 2: return '中优先级'; case 3: return '高优先级'; }
    },

    /**
     * 后端状态转前端状态
     */
    getFrontendState (backendState, startTime, endTime) {
      console.log(new Date(), endTime, startTime)
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
    //子组件的emit
    resetDialogVisible () {
      this.taskBoxDialogVisible = false;

      //刷新前端页面
      this.SetContentsData()
      this.GetMyProjects()
    },
    /**
     * 后端数据转前端数据
     */
    getFrontendDataByBackendData (backendData) {
      let frontendData = [];

      backendData.forEach((value) => {
        //获取孩子数据
        let childrenList = []

        if (value.relativeTask != null) {
          value.relativeTask.forEach((val) => {

            childrenList.push({
              taskId: val.taskId,
              name: val.taskTitle,
              startTime: this.GMTToStr(val.startTime),
              endTime: this.GMTToStr(val.endTime),
              group: val.classificationTitle,
              tag: val.tag,
              priority: this.getPriorityTitle(val.priority),
              state_val: this.getFrontendState(val.taskState),
              state_label: "",
              isdone: this.getFrontendState(val.taskState).indexOf("完成") >= 0 ? true : false,
              childrenNum: 0,
              showChildren: false,
              isParent: val.isParent,
              realFinishTime: this.GMTToStr(val.realFinishTime),
            })
          })

        }

        //获取这个事项的数据
        frontendData.push({
          taskId: value.taskId,
          name: value.taskTitle,
          startTime: this.GMTToStr(value.startTime),
          endTime: this.GMTToStr(value.endTime),
          group: value.classificationTitle,
          tag: value.tag,
          priority: this.getPriorityTitle(value.priority),
          state_val: this.getFrontendState(value.taskState, value.startTime, value.endTime),
          state_label: "",
          isdone: this.getFrontendState(value.taskState).indexOf("完成") >= 0 ? true : false,
          realFinishTime: this.GMTToStr(value.realFinishTime),
          childrenNum: value.relativeTask == null ? 0 : value.relativeTask.length,
          showChildren: true,
          children: childrenList,
          isParent: value.isParent
        })
      })

      this.init_data = frontendData;
      return frontendData;
    },
    //格林威治时间转标准时间戳
    GMTToStr (time) {
      if (time == null)
        return null;
      let date = new Date(time)
      let Str = date.getFullYear() + '-' +
        (date.getMonth() + 1) + '-' +
        date.getDate() + ' ' +
        date.getHours() + ':' +
        date.getMinutes() + ':' +
        date.getSeconds()
      return Str
    },


    /**
     *
     * 数据初始化与获取
     */
    //表格数据contents data初始化
    SetContentsData () {
      let backendData = []
      //从后端获取事项列表
      getAllTaskAndRelative(this.userId)
        .then((res) => {
          console.log("成功获取该用户的后端事项！", res);
          backendData = res.data;
          this.backendData = res.data;
          this.tableData = this.getFrontendDataByBackendData(this.backendData);
          this.commonTaskList = this.getcommonTaskList();
          this.initPage();
        })
        .catch((err) => {
          console.log(err);
        })
    },
    postKeyAndFetch () {
      this.show_table = this.searchResource();
    },

    // 请求某id事项的children事项，
    getTaskChildrenByID (tree, treeNode, resolve) {
      //向后端请求 该id事项的的children
      console.log("tree", tree);
      console.log("treeNode", treeNode);
    },

    /**
     * 改变勾选框勾选状态
     */
    onTaskDoneRadioChange (model) {
      console.group("勾选框行为！")
      console.log(model)
      console.groupEnd("勾选框行为！");
      model.isdone = !model.isdone;

      let _currentBackendState = 0;
      if (model.isdone == true) {
        if (new Date() > new Date(model.endTime) && model.endTime != null && model.endTime != "无")
          _currentBackendState = 2;
        else
          _currentBackendState = 1;
      }

      let backendData = {
        taskId: model.taskId,
        taskState: _currentBackendState,
        isParent: model.isParent
      }
      console.log(backendData)
      //向后端patch
      patchOneTask(backendData)
        .then((res) => {
          console.group("backendRes")
          console.log(res);
          console.groupEnd("backendRes")

          //改变前端页面
          //通过isdone来获取state
          
          if (model.isdone) {
            
            if (new Date() > new Date(model.endTime) && model.endTime != null && model.endTime != "无") {
              model.state_val = '延期完成';
              this.$message({
                message: "延期完成了一个事项，记得下次要准时完成！",
                type: "warning",
              });
            }
            else {
              model.state_val = '已完成';
              this.$message({
                message: "恭喜你，按时完成了一个事项！请再接再厉！",
                type: "success",
              });
            }

            console.group("断点")
          console.log("断点")
          console.groupEnd("断点")

            //前端记录我的完成时间
            model.realFinishTime = moment().format('YYYY-MM-DD HH:mm:ss');
            console.group("time")
            console.log(model.realFinishTime)
            console.groupEnd("time")
          }
          else {
            if (new Date() > new Date(model.endTime) && model.endTime != null && model.endTime != "无")
              model.state_val = '延期中';
            else if (new Date < new Date(model.startTime) && model.startTime != null && model.startTime != "无")
              model.state_val = '未开始';
            else
              model.state_val = '进行中';
            this.$message({
              message: "取消事项完成",
              type: "info",
            });

            //前端删除我的完成时间
            model.realFinishTime = null
          }
        })
        .catch((err) => {
          console.log(err);
        })

    },
    /***
     *
     * 事项时间
     */
    //dialog关闭前事件
    HandleCloseTaskTimeDialog (done) {
      done();
      this.TaskTimePickerBlur = false;
      this.cellSgColClass = "";
      // console.log(this.TaskTimePickerBlur);
    },
    //date picker;
    //值改变时,更新task_time_label
    HandleTaskTimeDateChange (val, model) {
      model.task_time_label.start_date = {
        year: val[0].getFullYear(),
        month: val[0].getMonth() + 1,
        day: val[0].getDate(),
      };
      model.task_time_label.end_date = {
        year: val[0].getFullYear(),
        month: val[1].getMonth() + 1,
        day: val[1].getDate(),
      };
    },
    //time picker,start time
    //值改变时,更新task_time_label
    HandleTaskTimeStartTimeChange (val, model) {
      model.task_time_label.start_time = {
        hour: val.getHours(),
        minute: val.getMinutes(),
      };
    },
    //time picker,end time
    //值改变时,更新task_time_label
    HandleTaskTimeEndTimeChange (val, model) {
      model.task_time_label.end_time = {
        hour: val.getHours(),
        minute: val.getMinutes(),
      };
      // console.log(model.task_time_label.end_time);
    },
    //事项时间，返回task_time的 月日 的打印字符串
    FormatTaskTime_Date (model) {
      const m = model;
      let str = "";
      str = `开始:${m.start_date.year}年${m.start_date.month}月${m.start_date.day}日`;
      str +=
        "\n" +
        `结束:${m.end_date.year}年${m.end_date.month}月${m.end_date.day}日`;
      return str;
    },

    //事项时间，返回task_time的 时分 的打印字符串
    FormatTaskTime_Time (model) {
      const m = model;
      let str = "";
      str += `${m.start_time.hour}:${m.start_time.minute}`;

      str += "\n~" + `${m.end_time.hour}:${m.end_time.minute}`;

      return str;
    },

    /**
     * 事项状态
     */
    //根据事项的状态值state_val，返回对应的type类型
    //同时为每一行的state_label赋值
    StateTagName (state_val, model) {
      //tag的类型
      let tag_name;
      switch (state_val) {
        case '进行中':
          tag_name = "success";
          break;
        case '未开始':
          tag_name = "";
          break;
        case '延期中':
          tag_name = "danger";
          break;
        case '延期完成':
          tag_name = "warning";
          break;
      }
      //state_label赋值
      let label = model.state_val;
      model.state_label = label;
      return tag_name;
    },

    /***
     * 项目
     */
    //项目,返回用户的所有项目简洁数据
    GetMyProjects () {
      //没有缓存，请求获取
      if (this.MyProjects == -1) {
        let projects = [];
        [1, 2, 3].forEach((i) => {
          projects = [
            {
              name: "项目" + i.toString() + ".1",
              taskId: "项目" + i.toString() + ".1-ID",
            },
            {
              name: "项目" + i.toString() + ".2",
              taskId: "项目" + i.toString() + ".2-ID",
            },
            {
              name: "项目" + i.toString() + ".3",
              taskId: "项目" + i.toString() + ".3-ID",
            },
          ];
        });

        this.MyProjects = projects;
      }

      return this.MyProjects;
    },
    //项目，project;下拉选择器 值变化时
    HandleProjectChange (prop) {
      // console.log(prop);
      // //回显后选择强制刷新
      this.$forceUpdate();
    },
    //项目，下拉选择器，判断能否取这个值
    DisbledProjectSelectOption () { },


    /**
     * 顶部搜索事项
     */
    //api
    //根据关键词，搜索匹配的事项
    SearchTaskByKeyWord (queryString, cb) {
      let results = this.tableData.map((ele) => {
        return {
          value: ele.name,
          taskId: ele.taskId,
        };
      });
      cb(results);
    },
    //点击搜索返回的建议
    SelectTaskSearchSuggestion (item) {
      console.log("搜索框点击task,", item);
    },
    //点击“查看详情”按钮
    handleClickDetail (row) {
      this.chosen_taskId = row.taskId;
      this.taskBoxDialogVisible = true;
    },
    getcommonTaskList () {
      let taskList = []
      let backendDataList = this.backendData;
      if (backendDataList == null)
        return;
      backendDataList.forEach((value) => {
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

          let item = {
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

          console.log(item)
          taskList.push(item);

        }//end of if

        this.commonTaskList = taskList;
        return taskList;
      })//end of then
      return taskList;
    },//end of method 


    /**
    * 分页所用函数start
    */
    //初始化分页
    initPage () {
      //初始化分页
      this.show_table = this.tableData.slice(0, 10);
      this.query.total = this.tableData.length;//总页数设置
      this.loading = false;
    },

    searchResource () {
      this.loading = true;
      let new_contents_data = this.tableData;
      const search = this.searchText;

      //搜索栏筛选
      if (search)//有内容才执行关键字筛选
      {
        new_contents_data = new_contents_data.filter(data => {
          return Object.keys(data).some(key => {
            return String(data[key]).toLowerCase().indexOf(search) > -1
          })
        })

      }

      const temp = new_contents_data.slice(0);
      new_contents_data = []
      //复选框筛选
      for (let item of temp) {
        if (!this.bool_showNoStart && item.state_val == "未开始")
          continue;
        if (!this.bool_showDoing && item.state_val == "进行中")
          continue;
        if (!this.bool_showFinished && item.state_val == "已完成")
          continue;
        if (!this.bool_showDelaying && item.state_val == "延期中")
          continue;
        if (!this.bool_showDelayedFinished && item.state_val == "延期完成")
          continue;

        //没被筛选框筛掉，才放入新的列表中
        new_contents_data.push(item);
      }

      //分页参数的更改
      let result = new_contents_data;
      this.query.total = result.length;//总页数设置
      this.loading = false;
      return result;
    },
    //切换当前页显示的数据条数，执行方法
    handleSizeChange (val) {
      console.log(`每页 ${val} 条`);
      this.query.pageSize = val;
      this.getPageData();
    },
    //切换页数，执行方法
    handleCurrentChange (val) {
      //console.log(`当前页: ${val}`);
      this.query.pageNum = val;
      this.getPageData();
    },
    getPageData () {
      console.log("调用getPageData!")
      this.postKeyAndFetch();
      const DataAll = this.show_table.slice(0);//深拷贝
      if(DataAll == null || DataAll.length == 0){
        this.query.total = 0;
        return;
      }

      //每次执行方法，将展示的数据清空
      this.show_table = [];
      //第一步：当前页的第一条数据在总数据中的位置
      let strlength = (this.query.pageNum - 1) * this.query.pageSize + 1;
      //第二步：当前页的最后一条数据在总数据中的位置
      let endlength = this.query.pageNum * this.query.pageSize;
      //第三步：此判断很重要，执行时机：当分页的页数在最后一页时，进行重新筛选获取数据时
      //获取本次表格最后一页第一条数据所在的位置的长度
      let resStrLength = 0;
      if (DataAll.length % this.query.pageSize == 0) {
        resStrLength = (parseInt(DataAll.length / this.query.pageSize) - 1) * this.query.pageSize + 1
      } else {
        resStrLength = parseInt(DataAll.length / this.query.pageSize) * this.query.pageSize + 1
      }
      //如果上一次表格的最后一页第一条数据所在的位置的长度 大于 本次表格最后一页第一条数据所在的位置的长度，则将本次表格的最后一页第一条数据所在的位置的长度 为最后长度
      if (strlength > resStrLength) {
        strlength = resStrLength
      }
      //第四步：此判断很重要，当分页的页数切换至最后一页，如果最后一页获取到的数据长度不足最后一页设置的长度，则将设置的长度 以 获取到的数据长度 为最后长度
      if (endlength > DataAll.length) {
        endlength = DataAll.length;
      }
      //第五步：循环获取当前页数的数据，放入展示的数组中
      for (let i = strlength - 1; i < endlength; i++) {
        this.show_table.push(DataAll[i]);
      }
      console.group("重新搜索后的show_table")
      console.log(this.show_table)
      console.groupEnd("重新搜索后的show_table")
      //数据的总条数
      this.query.total = DataAll.length;
    },
    /**
     * 分页所用函数end
     */

  },
  computed: {
    //方便直接获取head_data_common的值
    head_data_common_dict: {
      // getter
      get: function () {
        let dict = {};
        this.head_data_common.forEach((ele) => {
          dict[ele.key] = ele;
        });
        return dict;
      },
      // setter
      // set: function (newValue) {},
    },
  },
};
</script>
<style lang="less" scoped>
.viewList_view {
  height: 100%;
  //下拉框
  .drop_down_box {
    width: 100%;

    margin-bottom: 1em;
    display: flex;
    flex-direction: row;
    font-family: "HelveticaNeue-Light", "Helvetica Neue Light", "Helvetica Neue",
      Helvetica, Arial, "Lucida Grande", sans-serif;
    font-size: 0.9rem;
    // color: var(--head--grey);

    .left_box {
      //所有事项
      .filt_allTasks {
        // flex:30
      }
    }
    .right_box {
      margin-left: auto;
      display: flex;
      flex-direction: row;
      align-content: center;
      margin-right: 3em;
      font-size: 1.2em;
      //搜索
      .search_allTasks {
        // flex: 1;
        margin-right: 0.9em;
      }

      //表头设置
      .arrange_head {
        // flex: 1;
        .headset_pop {
          .buttom_buttons {
            text-align: right;
            margin: 0;
          }
        }
      }
    }
  }

  //内容表格
  .contents_table_box {
    .contents_table {
      width: 100%;

      /deep/.el-table__body-wrapper {
        /*解决固定列遮盖底部滚定条导致滚动条无法滚动的问题*/
        // z-index: 1 !important;
      }

      //事项时间col,另外引入全局样式，否则el-dialog不生效
      .task_time_col .el_dialog {
        width: 35%;
      }

      // 文字溢出成省略号
      //textflow需要加到文本所在的元素上
      /deep/ .textflow {
        width: 100%; /*一定要设置宽度，或者元素内含的百分比*/
        overflow: hidden; /*溢出的部分隐藏*/
        white-space: nowrap; /*文本不换行*/
        text-overflow: ellipsis; /*ellipsis:文本溢出显示省略号（...）；clip：不显示省略标记（...），而是简单的裁切*/
        .InputBig {
          width: 80%;
        }
        .InputSmall {
          width: 70%;
        }
      }

      .name_col {
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
  //透明
  .transparent {
    opacity: 0;
  }
}
</style>



