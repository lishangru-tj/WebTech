<template>
  <span>
    <!--导出按钮-->
    <el-button
      :loading="downloadLoading"
      style="margin:0 1.5em 1em 1em;"
      type="primary"
      size="mini"
      icon="el-icon-document"
      @click="chooseExportTimeRange()"
    >
      导出Excel文件
    </el-button>

    <!--选择导出时间段的对话框-->
    <el-dialog
      :visible.sync="exportDialogVisible"
      width="30em"
      append-to-body
    >
      <h3>请选择需要导出的时间段</h3>
      <div class="block">
        <el-date-picker
          v-model="exportTimeRange"
          type="daterange"
          size="mini"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions"
        >
        </el-date-picker>

        <!--取消与确认按钮-->
        <div style="margin-top:1em;text-align:right;">
          <el-button
            size="mini"
            @click="Cancel()"
          >取消</el-button>
          <el-button
            size="mini"
            type="success"
            @click="ConfirmExport()"
          >确认</el-button>
        </div>
      </div>
    </el-dialog>
  </span>
</template>

<script>
export default {
  name: 'ExportButton',
  props: ['taskList'],
  data () {
    return {
      //选择导出时间段的对话框
      exportDialogVisible: false,

      //导出时间段
      exportTimeRange: [],

      //导出的列表
      exportList: [],

      //加载转圈
      downloadLoading: false,

      //日历快捷选项
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick (picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick (picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick (picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
    }
  },
  methods: {
    handleDownload () {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['标题', '分组', '优先级', '个性标签', '开始时间', '结束时间']
        const filterVal = ['name', 'group', 'priority', 'tag', 'startTime', 'endTime']
        const list = this.exportList;
        const data = this.formatJson(filterVal, list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: this.filename,
          autoWidth: this.autoWidth,
          bookType: this.bookType
        })
        this.downloadLoading = false
        this.$message({
          type: 'success',
          message: '导出事项成功！'
        })
      })
    },
    //打开对话框
    chooseExportTimeRange () {
      this.exportDialogVisible = true;
    },
    //取消按钮
    Cancel () {
      this.exportDialogVisible = false;
      this.$message({
        type: 'info',
        message: '取消导出事项'
      })
    },
    //确认按钮
    ConfirmExport () {
      this.exportList = []
      //如果用户没有选择时间，默认全部导出
      if (this.exportTimeRange.length == 0) {
        //确认信息
        this.$confirm('您未选择时间段，确定导出所有事项？', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
          .then(() => {
            this.exportList = this.taskList;
            //如果筛选出的列表为空，提醒用户
            if (this.exportList.length == 0) {
              this.$message({
                type: 'warning',
                message: '该时间段内没有事项，请重新选择时间段！'
              })
              return;
            }
            else {
              this.handleDownload();
            }
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '取消导出事项'
            })
          })
      }
      //如果用户有选时间
      else {
        //开始筛选最终导出列表
        this.taskList.forEach(item => {
          let taskStartTime = new Date(item.startTime);
          let taskEndTime = new Date(item.endTime);
          if ((taskStartTime > this.exportTimeRange[0] && taskStartTime < this.exportTimeRange[1]) ||
            (taskEndTime > this.exportTimeRange[0] && taskEndTime < this.exportTimeRange[1]))
            this.exportList.push(item);
        });

        //如果筛选出的列表为空，提醒用户
        if (this.exportList.length == 0) {
          this.$message({
            type: 'warning',
            message: '该时间段内没有事项，请重新选择时间段！'
          })
          return;
        }
        else {
          this.handleDownload();
        }
      }
    },
    /**
 * 下面是导出为Excel的函数
 */
    formatJson (filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
  }
}
</script>

<style lang="less" scoped>
</style>
