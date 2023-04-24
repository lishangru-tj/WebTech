<template>
  <div
    class="echart"
    id="mychart"
    :style="myChartStyle"
  ></div>
</template>

<script>
import * as echarts from "echarts";

export default {
  data () {
    return {
      xData: ["完成率", "及时率", "延期率"], //横坐标
      //yData: [0.8, 0.5, 0.5], //数据
      myChartStyle: { float: "left", width: "100%", height: "334px" } //图表样式
    };
  },
  props: {
    seriesData: {
      type: Array,
      default: () => {
        return [
          {
            value: 0.3,
            itemStyle: {
              color: 'green'
            }
          },
          {
            value: 0.5,
            itemStyle: {
              color: 'blue'
            }
          },
          {
            value: 0.7,
            itemStyle: {
              color: 'orange'
            }
          },
        ]
      }
    }
  },
  mounted () {
    this.$nextTick(() => {
      this.initEcharts();
    })
  },
  watch:{
    seriesData:{
      handler(newVal){
        this.initEcharts();
      },
      deep:true
    }
  },
  methods: {
    initEcharts () {
      // 基本柱状图
      const option = {
        tooltip: {
          trigger: 'item',
          // formatter: '{a} <br/>{b} : {c} ({d}%)',
          formatter: '{b} : {c}',
          backgroundColor: 'rgba(255,255,255,0.8)',
          color: 'black',
          borderWidth: '1',
          borderColor: 'gray',
          textStyle: {
            color: 'black'
          }
        },
        legend: {
          left: 'center',
          data: ['完成率', '及时率', '延期率']
        },
        xAxis: {
          data: this.xData
        },
        yAxis: {},
        series: [
          {
            type: "bar", //形状为柱状图
            data: this.seriesData
          }
        ]
      };
      const myChart = echarts.init(document.getElementById("mychart"));
      myChart.setOption(option);
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    }
  }
};
</script>

