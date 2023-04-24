<template>
  <div>
    <el-card>
      <el-date-picker
        v-model="chosenTime"
        type="month"
        placeholder="选择月"
        :editable="false"
        size="mini"
        @change="getBackendToFrontend()"
      >
      </el-date-picker>
    </el-card>

    <el-card>
      <el-row :gutter="10">
        <!--上面是顶部展示栏-->

        <!--header-->
        <div
          slot="header"
          class="clearfix"
        >
          <span>专家方案</span>
        </div>

        <el-col
          :xs="bigSize*upLeftPanelRate"
          :sm="smallSize*upLeftPanelRate"
          :md="smallSize*upLeftPanelRate"
          :lg="smallSize*upLeftPanelRate"
          :xl="smallSize*upLeftPanelRate"
        >
          <div id="col1">
            <div><i class="el-icon-edit"></i></div>
            <div>
              <h1>{{allStateNum.finishedStateNum}}件</h1>
            </div>
            <div>
              <h3>已完成</h3>
            </div>
          </div>
        </el-col>

        <el-col
          :xs="bigSize*upLeftPanelRate"
          :sm="smallSize*upLeftPanelRate"
          :md="smallSize*upLeftPanelRate"
          :lg="smallSize*upLeftPanelRate"
          :xl="smallSize*upLeftPanelRate"
        >
          <div id="col2">
            <i class="el-icon-edit"></i>
            <div>
              <h1>{{allStateNum.doingStateNum}}件</h1>
            </div>
            <div>
              <h3>进行中</h3>
            </div>
          </div>
        </el-col>

        <el-col
          :xs="bigSize*upLeftPanelRate"
          :sm="smallSize*upLeftPanelRate"
          :md="smallSize*upLeftPanelRate"
          :lg="smallSize*upLeftPanelRate"
          :xl="smallSize*upLeftPanelRate"
        >
          <div id="col3">
            <i class="el-icon-edit"></i>
            <div>
              <h1>{{allStateNum.delayingStateNum}}件</h1>
            </div>
            <div>
              <h3>延期中</h3>
            </div>
          </div>
        </el-col>

        <el-col
          :xs="bigSize*upLeftPanelRate"
          :sm="smallSize*upLeftPanelRate"
          :md="smallSize*upLeftPanelRate"
          :lg="smallSize*upLeftPanelRate"
          :xl="smallSize*upLeftPanelRate"
        >
          <div id="col4">
            <i class="el-icon-edit"></i>
            <div>
              <h1>{{allStateNum.delayedFinishedStateNum}}件</h1>
            </div>
            <div>
              <h3>延期完成</h3>
            </div>
          </div>
        </el-col>

      </el-row>
    </el-card>
    <br>

    <!--下面的主展示行-->
    <el-row :gutter="10">
      <!--曲线图-->
      <el-col
        :xs="24"
        :sm="16"
        :md="16"
        :lg="16"
        :xl="16"
      >
        <el-card shadow="always">
          <div
            slot="header"
            class="clearfix"
          >
            <span>数据趋势</span>
            <span class="card-div-desc">lineCardTitle</span>
            <el-radio-group
              style="float: right; padding: 3px 0"
              size="mini"
            >
              <el-radio-button label="order">新建事项数</el-radio-button>
              <el-radio-button label="sale">完成事项数</el-radio-button>
            </el-radio-group>
          </div>
          <div>
            <LineHeapChart
              height="600px"
              :xAxisData="lineXAxisData"
              :seriesData="lineSeriesData"
            />
          </div>
        </el-card>
      </el-col>

      <el-col
        :xs="12"
        :sm="8"
        :md="8"
        :lg="8"
        :xl="8"
      >
        <el-card shadow="always">
          <!--圆饼图-->
          <PieFlatChart :seriesData="pieSeriesData" />
        </el-card>

        <el-card shadow="always">
          <!--柱状图-->
          <BarChart
            :seriesData="barSeriesData"
            v-if="barChartVisible"
          />
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { getAnalyzeData } from '@/api/dataAnalyze.js'
import { getCurrentYear, getCurrentMonth } from '@/utils/time.js'
import LineHeapChart from './components/LineHeapChart'
import PieFlatChart from './components/PieFlatChart'
import PieFilletChart from './components/PieFilletChart'
import BarChart from './components/BarChart'
export default {
  name: "DataReport",
  components: {
    LineHeapChart,
    PieFlatChart,
    PieFilletChart,
    BarChart
  },
  data () {
    return {
      userId: this.$store.getters.id,
      bigSize: 3,
      smallSize: 2,
      upLeftPanelRate: 3,
      barChartVisible: true,

      chosenTime: getCurrentYear() + '-' + getCurrentMonth(),
      chosen_year: getCurrentYear(),
      chosen_month: getCurrentMonth(),

      //上方显示的各状态事项数
      allStateNum: {
        finishedStateNum: 0,
        doingStateNum: 0,
        delayingStateNum: 0,
        delayedFinishedStateNum: 0
      },
      //状态圆饼图的数据
      pieSeriesData: [
        {
          name: '状态数据',
          type: 'pie',
          radius: '50%',
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
          itemStyle: {
            normal: {

              label: {
                show: true,
                overflow: 'none',
                formatter: '{b} : {c} ({d}%)',
                //设置标签溢出显示
                position: 'outer',
                alignTo: 'edge',
                margin: 0
              },
              labelLine: { show: true }
            }
          },
          data: [
            { value: 10, name: '已完成' },
            { value: 13, name: '进行中' },
            { value: 2, name: '延期中' },
            { value: 4, name: '延期完成' }
          ]
        }
      ],
      barSeriesData: [
        {
          name: '完成率',
          value: 0.3,
          itemStyle: {
            color: 'green'
          }
        },
        {
          name: '及时率',
          value: 0.5,
          itemStyle: {
            color: 'blue'
          }
        },
        {
          name: '延期率',
          value: 0.7,
          itemStyle: {
            color: 'orange'
          }
        },
      ],

      lineSeriesData: [
        {
          name: '完成事项数',
          type: 'line',
          stack: '总量',
          areaStyle: {},
          emphasis: {
            focus: 'series'
          },
          smooth: true,
          data: [120, 132, 101, 134, 90, 230, 210, 3466, 67, 36]
        },
        {
          name: '新建事项数',
          type: 'line',
          stack: '总量',
          emphasis: {
            focus: 'series'
          },
          areaStyle: {
            // 改变区域颜色
            //color: '#fadb14'
          },
          //光滑线条
          smooth: true,
          data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        }
      ],
      lineXAxisData: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15',
        '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30'],

    };


  },

  watch: {
    chosenTime: {
      handler (newVal) {
        this.chosen_year = newVal.getFullYear();
        this.chosen_month = newVal.getMonth() + 1;
      }
    }
  },
  methods: {
    getBackendToFrontend () {
      getAnalyzeData(this.userId, this.chosen_year, this.chosen_month)
        .then((res) => {
          console.log("请求数据分析成功！", res)
          const backendData = res.data;

          //柱状图数据
          this.barSeriesData[0].value = backendData.finishRate;
          this.barSeriesData[1].value = backendData.timelyRate;
          this.barSeriesData[2].value = backendData.delayRate;

          //上方数据
          this.allStateNum = {
            finishedStateNum: backendData.finishedStateNum,
            doingStateNum: backendData.doingStateNum,
            delayingStateNum: backendData.delayingStateNum,
            delayedFinishedStateNum: backendData.delayedFinishedStateNum
          }

          //圆饼图数据
          this.pieSeriesData[0].data = [
            { name: '已完成', value: backendData.finishedStateNum },
            { name: '进行中', value: backendData.doingStateNum },
            { name: '延期中', value: backendData.delayingStateNum },
            { name: '延期完成', value: backendData.delayedFinishedStateNum }
          ]

          //折线图天数
          switch (backendData.daysOfMonth) {
            case 28:
              this.lineXAxisData = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15',
                '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28'];
              break;
            case 29:
              this.lineXAxisData = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15',
                '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29'];
              break;
            case 30:
              this.lineXAxisData = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15',
                '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30'];
              break;
            case 31:
              this.lineXAxisData = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15',
                '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31'];
              break;
          }

          //折线图数据
          let finishListData = []
          let createListData = []
          for (let i = 1; i <= backendData.daysOfMonth; i++) {
            finishListData.push(backendData.oneMonthFinishedNumMap[i.toString()]);
            createListData.push(backendData.oneMonthNewCreatedNumMap[i.toString()])
          }
          this.lineSeriesData[0].data = finishListData;
          this.lineSeriesData[1].data = createListData;
        })
        .catch((err) => {
          console.log(err)
        })
    }
  },
  mounted: function () {
    this.getBackendToFrontend()
  }




};
</script>

<style lang="less" scoped>
// .divCenter {
// margin-left: 30px;
// }

div {
  .el-card {
    margin-bottom: 1em;
  }
  .el-card {
    .el-row {
      .el-col {
        div#col1 {
          margin-left: 5em;
          i {
            position: relative;
            left: 1em;
          }
          h3 {
            color: rgb(68, 68, 236);
          }
        }
      }
      .el-col {
        div#col2 {
          margin-left: 5em;
          i {
            position: relative;
            left: 1em;
          }
          h3 {
            color: green;
          }
        }
      }
      .el-col {
        div#col3 {
          margin-left: 5em;
          i {
            position: relative;
            left: 1em;
          }
          h3 {
            color: red;
          }
        }
      }
      .el-col {
        div#col4 {
          margin-left: 5em;
          i {
            position: relative;
            left: 1em;
          }
          h3 {
            color: rgb(199, 199, 13);
          }
        }
      }
    }
  }
  // .el-card{

  // }
}
</style>
