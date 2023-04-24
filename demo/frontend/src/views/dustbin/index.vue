<template>
  <div>
    <div id="searchBox">
      <el-button
        @click="clearDustbin()"
        size="mini"
        type="danger"
      >清空回收站</el-button>
    </div>
    <el-table
      ref="table"
      :data="show_table.filter(data => !search || data.taskTitle.toLowerCase().includes(search.toLowerCase()))"
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column
        :key="currentInfoKey"
        label="删除日期"
        prop="isInDustbin"
      >
      </el-table-column>
      <el-table-column
        :key="currentInfoKey+1"
        label="事项名称"
        prop="taskTitle"
      >
      </el-table-column>
      <el-table-column
        :key="currentInfoKey+2"
        label="分组"
        prop="classificationTitle"
      >
      </el-table-column>
      <el-table-column
        :key="currentInfoKey+3"
        label="原定开始时间"
        prop="startTime"
      >
      </el-table-column>
      <el-table-column
        :key="currentInfoKey+4"
        label="原定结束时间"
        prop="endTime"
      >
      </el-table-column>

      <el-table-column align="right">
        <template
          slot="header"
          slot-scope="scope"
        >
          <el-input
            v-model="search"
            size="mini"
            placeholder="输入关键字搜索"
          ></el-input>

        </template>

        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            @click="handleRestore(scope.$index, scope.row)"
          >恢复</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleSmash(scope.$index, scope.row)"
          >彻底删除</el-button>
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
  </div>
</template>

<script>
import { getAllRubbish, smashOneRubbish, smashAllRubbish, restoreOneRubbish } from "@/api/dustbin.js"
export default {
  name: 'Dustbin',
  data () {
    return {
      userId: this.$store.getters.id,
      search: '',
      currentInfoKey: 1,
      show_table: [],//分页实际展示的table
      //分页数据
      query: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      loading: true,
      //需要一开始由后端赋值
      tableData: [{
        task_id: 100,
        date: '2016-05-02 12:00:00',
        name: '背单词',
        classification: '学习',
        timeRange: 'null'
      }, {
        task_id: 101,
        date: '2016-05-04 12:00:00',
        name: '玩原神',
        classification: '游戏',
        timeRange: 'null'
      }, {
        task_id: 102,
        date: '2016-05-01 12:00:00',
        name: '扫地',
        classification: '家务',
        timeRange: 'null'
      }, {
        task_id: 103,
        date: '2016-05-03 12:00:00',
        name: '上课',
        classification: '学习',
        timeRange: 'null'
      }],
    }
  },
  methods: {
    removeOneFromDustbin (task_id) {
      // let p1 = () => {
      //   return new Promise((res, rej) => {
      //     this.tableData = this.tableData.filter(item => {
      //       return item.task_id != task_id;
      //     })
      //   })
      // }
      // async function q1 () {
      //   let res = await p1();
      //   console.log("await结束！", res);
      //   this.currentInfoKey++;
      // }
      // q1();

      let newTableData = []
      for (let i = 0; i < this.tableData.length; i++) {
        if (this.tableData[i].id != task_id) {
          newTableData.push(this.tableData[i])
        }
      }
      this.tableData = newTableData;

      this.currentInfoKey += 1;


    },
    handleRestore (index, row) {
      console.log(index, row);
      console.log("row.id=", row.id)
      //请求后端恢复事项
      restoreOneRubbish(row.id)
        .then((res) => {
          console.log(res);
          //从前端回收站删除这一条数据
          this.removeOneFromDustbin(row.id);
          this.$message({
            type: 'success',
            message: '事项恢复成功！'
          });
        })
        .catch((err) => {
          console.log(err);
          this.$message({
            type: 'danger',
            message: '事项恢复失败'
          });
        })


    },
    handleSmash (index, row) {
      console.log(index, row);
      this.$confirm('您确定要永久删除事项吗？删除的事项不可恢复！', '确认信息', {
        distinguishCancelAndClose: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
        .then(() => {
          //向后端请求删除...
          smashOneRubbish(row.id)
            .then((res) => {
              console.log(res);
              //从前端回收站删除这一条数据
              this.removeOneFromDustbin(row.id);
              this.$message({
                type: 'success',
                message: '事项永久删除成功！'
              });
            })
            .catch((err) => {
              console.log(err);
              this.$message({
                type: 'danger',
                message: '事项删除失败'
              });
            })


        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消永久删除'
          })
        });

    },
    //清空回收站
    clearDustbin () {
      //确认信息
      this.$confirm('您确定要清空回收站吗？删除的事项不可恢复！', '确认信息', {
        distinguishCancelAndClose: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
        .then(() => {
          //向后端请求删除...
          smashAllRubbish(this.userId)
            .then((res) => {
              console.log(res);
              //清空前端回收站
              this.tableData = [];
              this.$message({
                type: 'success',
                message: '回收站清空成功！'
              });
            })
            .catch((err) => {
              console.log(err);
              this.$message({
                type: 'danger',
                message: '回收站清空失败'
              });
            })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消清空回收站'
          })
        });
    },
    getFrontendTaskList () {
      console.log("开始拉取后端数据")
      //从后端拉取数据
      getAllRubbish(this.userId)
        .then((res) => {
          let taskList = []
          //然后要把后端拉回来的task对象转换成前端要用的对象
          if (res.data != null || res.data != []) {
            console.log("res", res)
            let backendDataList = res.data;
            if (backendDataList == null)
              return;
            backendDataList.forEach((value) => {
              if (value.isInDustbin != '0') {
                taskList.push({
                  id: value.taskId,
                  taskTitle: value.taskTitle,
                  classificationTitle: value.classificationTitle,
                  isInDustbin: value.isInDusbin,
                  startTime: value.startTime,
                  endTime: value.endTime,
                  isInDustbin: this.timeTrans(value.isInDustbin)
                })
              }

            })
          }//end of if

          this.tableData = taskList;

          this.initPage();
        })//end of then
        .catch((err) => {
          console.log(err);
        })
    },//end of method

    timeTrans (time, type) {
      if (time == null || time == '')
        return null;
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
    //得到符合条件的信息
    postKeyAndFetch () {
      this.show_table = this.searchResource();
    },
    searchResource () {
      this.loading = true;
      // //函数返回值为筛选后的列表
      let search = this.input;
      let result = this.tableData.slice(0);//深拷贝
      if (search)//有内容才执行关键字筛选
      {
        result = result.filter(data => {
          return Object.keys(data).some(key => {
            return String(data[key]).toLowerCase().indexOf(search) > -1
          })
        })

      }
      //分页参数的更改
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
      this.postKeyAndFetch();
      const DataAll = this.show_table.slice(0);//深拷贝
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
      //数据的总条数
      this.query.total = DataAll.length;
    },
    /**
     * 分页所用函数end
     */


  },
  mounted: function () {
    //在这个函数里的then执行了页数初始化
    this.getFrontendTaskList();
  },
  watch: {
    tableData: {
      deep: true,
      immediate: true,
      handler () {
        this.initPage();
      }
    }
  }
}

</script>

<style lang="less" scoped>
div {
  div#searchBox {
    width: 20em;
    margin-top: 1em;
  }
}
</style> 