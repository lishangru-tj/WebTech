<template>
  <el-container style=" border: 1px solid #eee">

    <el-container>
      <el-header style="text-align: right; font-size: 12px; padding-right: 20px; padding-top:10px">
        <el-input ref="searchInput" v-model="search" style="width: 300px; padding-right: 20px;" placeholder="请输入内容" prefix-icon="el-icon-search" />
        <el-button style="margin-right: 20px;" @click="submitFun();open()">搜索</el-button>
      </el-header>

      <el-main>
        <el-card v-if="searchData.length>0" class="box-card">
          <div v-for="(list,index) in searchData" :key="index" style="height: 50px">
            {{ list.name }}于{{ list.date }}完成操作：{{ list.operation }}
          </div>
        </el-card>
        <el-card v-else class="box-card">
          <div v-for="item in tableData" :key="item" class="text item" style="height: 50px">
            {{ item.name }}于{{ item.date }}完成操作：{{ item.operation }}
          </div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>
</template>
<style>
.el-header {
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
</style>
<script src="./vue.js"></script>
<script>
export default {
  data() {
    //应从数据库获取一组items，使用v-for遍历并展示在前端，此处简化，只定义一个project填充一个20条数据的数组。
    const item = {
      date: '2022-10-22 11:59',
      name: 'user1',
      operation: '创建新事项',
    }
    return {
      tableData: Array(20).fill(item),
      search: '',
      searchData: '',
      user: { username: 'user1' },
    }
  },
  created: function () {
    this.inintData()
  },
  methods: {
    open() {
      if (this.searchData.length == 0) {
        this.$alert('未找到相关日历数据！返回日历列表。', '查询结果', {
          confirmButtonText: '确定',
          callback: (action) => {
            this.$message({
              type: 'info',
              message: `action: ${action}`,
            })
          },
        })
      } else {
        this.$alert('共找到' + this.searchData.length + '条结果', '查询结果', {
          confirmButtonText: '确定',
        })
      }
    },
    inintData() {
      this.searchData = this.tableData
    },
    submitFun() {
      let search = this.search
      this.searchData = this.tableData.filter(function (tableData) {
        console.log('过滤', tableData)
        let searchField = {
          name: tableData.name,
          operation: tableData.operation,
        }
        return Object.keys(searchField).some(function (key) {
          console.log('key值', key)
          return String(tableData[key]).toLowerCase().indexOf(search) > -1
        })
      })
    },
  },
}
</script>
