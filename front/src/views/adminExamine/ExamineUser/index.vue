
<template>
  <div class="views">

    <div style="float:left">
      <!-- tab -->
      <el-card>
        <el-tabs
          type="card"
          class="tabs"
          @tab-click="HandleTabsClick"
        >
          <el-tab-pane label="待办审核"></el-tab-pane>
          <el-tab-pane label="已完成审核"></el-tab-pane>

        </el-tabs>

        <!-- table -->
        <el-table
          :data="show_table"
          class="order_table"
          height="500"
          width="100%"
          v-loading="loading"
          :default-sort="{prop: 'REPORTTIME', order: 'descending'}"
          stripe
        >
          <el-table-column
            prop="reportId"
            label="举报单编号"
            width="180"
            sortable
          ></el-table-column>
          <el-table-column
            prop="type"
            label="举报分类"
            width="250"
          >
          </el-table-column>

          <el-table-column
            prop="reportTime"
            label="举报时间"
            width="280"
            sortable
          ></el-table-column>

          <el-table-column
            prop="state"
            label="审核状态"
            width="380"
          >
            <template slot-scope="scope">
              <el-tag
                v-if="scope.row.state==0"
                type='warning'
                disable-transitions
              >待审核</el-tag>
              <el-tag
                v-else
                type='success'
                disable-transitions
              >已审核</el-tag>
            </template>
          </el-table-column>

          <el-table-column
            fixed="right"
            label="操作"
            width="120"
          >
            <template slot-scope="scope">
              <el-button
                @click.native.prevent="viewDetailButtonClick(scope.row)"
                type="text"
                size="small"
              >
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="query.pageNum"
          :page-sizes="[5, 10, 20, 30]"
          :page-size="query.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="query.total"
        ></el-pagination>
      </el-card>

      <!-- 弹窗对话框 -->
      <el-dialog
        class="serviceDialog"
        title="举报单卡片"
        :visible.sync="dialogVisible"
      >

        <div class="dialog-content">

          <!-- :column="2" 一行有2个 -->
          <el-descriptions
            class="margin-top"
            :column="2"
            border
          >

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-user"></i>
                举报单编号
              </template>
              {{report_detail.reportId}}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-user"></i>
                举报类型
              </template>
              {{report_detail.type}}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-user"></i>
                举报人ID
              </template>
              {{report_detail.informerId}}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-s-custom"></i>
                被举报人ID
              </template>
              {{report_detail.userId}}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-mobile-phone"></i>
                举报是否属实
              </template>
              {{report_detail.real}}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-mobile-phone"></i>
                惩罚类型
              </template>
              封号{{report_detail.punishType}}天
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-mobile-phone"></i>
                举报时间
              </template>
              {{report_detail.reportTime}}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-mobile-phone"></i>
                审核时间
              </template>
              {{report_detail.handleTime}}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-mobile-phone"></i>
                审核状态
              </template>
              <span v-if="report_detail.state==0">待审核</span>
              <span v-else>已审核</span>
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-mobile-phone"></i>
                举报详述
              </template>
              {{report_detail.detail}}
            </el-descriptions-item>

          </el-descriptions>
        </div>
        <!-- 底部的slot插槽 -->
        <span
          slot="footer"
          class="dialogFooter"
        >
          <el-button
            type="primary"
            @click="dialogVisible = false"
          >确认</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </span>
      </el-dialog>
    </div>

  </div>
</template>

<script>
import { getAllUserReportList, getOneReport } from '@/api/admin.js'
export default {
  name: "AdminExamineUser",
  components: {},
  data () {
    return {
      flag: true,
      order_table: [
        [], []
      ],
      show_table: [],
      dialogVisible: false,
      selected_index: 0,
      tab_index: 0,
      report_detail: {},

      //分页数据
      query: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      loading: true,
    }
  },
  methods: {
    viewDetailButtonClick (row) {
      if (this.tab_index == 0)//待办审核
      {
        //从后端拿举报单数据
        getOneReport(row.reportId)
          .then((res) => {
            this.dialogVisible = false;
            this.$router.push({ path: '/detailExamineUser/index', query: { res: res } })
          })
          .catch((err) => {
            console.log(err)
          });

      }
      else if (this.tab_index == 1)//已完成审核
      {
        //从后端拿举报单数据
        getOneReport(row.reportId)
          .then((res) => {
            this.report_detail = res.data.report;
            this.dialogVisible = true;
          })
          .catch((err) => {
            console.log(err)
          });

        this.dialogVisible = true;
        //console.log(this.report_detail.report_text);
      }
    },
    toConsole () {
      this.dialogVisible = false;
      this.$router.push(
        { path: '/detailExamineUser/index', query: { res: res } }
      )
    },
    HandleTabsClick (tab) {
      this.tab_index = parseInt(tab.index);
      //parseInt(),string->int
    },
    getPageData () {//获取页面数据的函数
      const DataAll = this.order_table[this.tab_index].slice(0);//深拷贝
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
      let j = 0;
      for (let i = strlength - 1; i < endlength && j < DataAll.length; i++, j++) {
        this.show_table.push(DataAll[i]);
      }
      //数据的总条数
      this.query.total = DataAll.length;
    },
    //切换当前页显示的数据条数，执行方法
    handleSizeChange (val) {
      //console.log(`每页 ${val} 条`);
      this.query.pageSize = val;
      this.getPageData();
    },
    //切换页数，执行方法
    handleCurrentChange (val) {
      //console.log(`当前页: ${val}`);
      this.query.pageNum = val;
      this.getPageData();
    },

  },
  mounted: function () {
    //从后端拿举报单数据
    getAllUserReportList()
      .then((res) => {

        let receive_table = res.data.reports;
        for (let i = 0; i < receive_table.length; i++) {
          //选择加到哪一个列表里，待审核列表还是已审核列表
          let choice = receive_table[i].state == 0 ? 0 : 1;
          this.order_table[choice].push(receive_table[i]);
        }
        console.group("获取后端数据成功！")
        console.log(this.order_table)
        console.groupEnd("获取后端数据成功！")

        this.show_table = this.order_table[this.tab_index].slice(0, 10);//深拷贝
        this.query.total = this.order_table[this.tab_index].length;//初始化条目总数
        this.loading = false;
      })
      .catch((err) => {
        console.log(err)
      });
  },
  watch: {
    tab_index: {
      handler (newVal, oldVal) {
        this.query.total = this.order_table[this.tab_index].length;//更改页面总条数
        this.show_table = this.order_table[this.tab_index].slice(0);//更改显示的条目集合
        this.getPageData();//换页后重新获取页的数据
        console.log(newVal);
        console.log(oldVal);
      }
    }
  }

};
</script>

// lang选择less语法，scoped限制该样式只在本文件使用，不影响其他组件
<style lang="less" scoped>
.head {
  float: left;
  margin-right: 800px;
}
.backButton {
  float: right; // 靠右
}
</style>>
