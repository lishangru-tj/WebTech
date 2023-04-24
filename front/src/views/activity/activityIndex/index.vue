<template>
  <el-container style="border: 1px solid #eee">
    <el-tabs
      style="width: 100%"
      tab-position="right"
      v-model="index_tab"
      @tab-click="(tab) => IndexTabsClick(tab.name)"
    >
      <el-tab-pane label="首页" name="index">
        <el-container>
          <el-main>
            <div style="display: inline-block">
              <el-col style="width: 700px; padding-left: 20px">
                <el-card class="box-card" style="width: 100%">
                  <div
                    slot="header"
                    class="clearfix"
                    style="text-align: center"
                  >
                    <span>全部活动</span>

                    <el-input
                      ref="searchInput"
                      v-model="search"
                      style="width: 200px; padding-right: 20px"
                      placeholder="请输入内容"
                      prefix-icon="el-icon-search"
                    />
                    <el-button
                      style="margin-right: 20px"
                      @click="
                        submitFun();
                        open();
                      "
                      >搜索</el-button
                    >

                    <el-button type="text" @click="dialogFormVisible = true"
                      >筛选</el-button
                    >

                    <el-dialog title="筛选" :visible.sync="dialogFormVisible">
                      <el-form :model="form" style="text-align: left">
                        <el-form-item
                          label="活动标签"
                          :label-width="formLabelWidth"
                        >
                          <el-checkbox-group v-model="checkboxGroup1">
                            <el-checkbox-button
                              v-for="tag in tags"
                              :key="tag.tagID"
                              :label="tag"
                              >{{ tag.name }}</el-checkbox-button
                            >
                          </el-checkbox-group>
                        </el-form-item>
                      </el-form>
                      <div style="padding-left: 50px">
                        已选择：
                        <el-tag
                          v-for="tag in checkboxGroup1"
                          :key="tag.tagID"
                          :label="tag"
                          >{{ tag.name }}</el-tag
                        >
                      </div>
                      <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogFormVisible = false"
                          >取 消</el-button
                        >
                        <el-button
                          type="primary"
                          @click="
                            dialogFormVisible = false;
                            submitTagFun();
                          "
                          >确 定</el-button
                        >
                      </div>
                    </el-dialog>
                  </div>

                  <div
                    v-for="(list, index) in showData.slice(
                      (currentPage1 - 1) * pagesize1,
                      currentPage1 * pagesize1
                    )"
                    :key="index"
                  >
                    <div style="display: inline-block; width: 800px">
                      <!--el-image v-if="list.showimage.length" style="width: 100px; height: 100px; float: right;margin-right: 100px;" :src="list.showimage" fit="cover" /-->
                      <div
                        style="
                          display: inline-block;
                          width: 100%;
                          border-bottom: solid 1px #ebeef5;
                          padding: 0.5em 0 0.5em 0;
                        "
                      >
                        <p>
                          活动名称：{{ list.name }}
                          <el-button
                            style="padding: 0 0 0 3em"
                            type="text"
                            @click="ClickActDetail(list.activity_id)"
                            >点击查看</el-button
                          >
                        </p>
                        <!-- 活动配图展示 -->
                        <div class="images">
                          <el-image
                            v-if="list.images"
                            style="margin-right: 1em; height: 10em"
                            :src="GetIllusImgUrl(list.images)"
                          >
                            <div slot="error" class="image-slot">
                              <i class="el-icon-picture-outline"></i>
                            </div>
                          </el-image>
                        </div>
                        <p>活动开始时间： {{ list.start_time }}</p>
                        <div
                          v-for="(tag, indexOfTag) in list.tags"
                          :key="indexOfTag"
                          style="display: inline-block"
                        >
                          <el-tag style="margin-left: 5px; margin-right: 5px">{{
                            tag.tag_name
                          }}</el-tag>
                        </div>
                      </div>
                    </div>
                  </div>
                  <el-pagination
                    @size-change="handleSizeChange1"
                    @current-change="handleCurrentChange1"
                    :current-page="currentPage1"
                    :page-sizes="[5, 10, 15, 20]"
                    :page-size="pagesize1"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="showData.length"
                  >
                  </el-pagination>
                </el-card>
              </el-col>
              <el-col style="width: 400px; padding-left: 50px">
                <el-card class="box-card" style="width: 100%">
                  <div
                    slot="header"
                    class="clearfix"
                    style="text-align: center"
                  >
                    <span>热门活动</span>
                  </div>

                  <el-table
                    :data="
                      tableData.slice(
                        (currentPage - 1) * pagesize,
                        currentPage * pagesize
                      )
                    "
                    style="width: 100%"
                    :default-sort="{ prop: 'heat', order: 'descending' }"
                  >
                    <el-table-column prop="name" label="名称" />
                    <el-table-column label="标签">
                      <template slot-scope="scope">
                        <div
                          v-for="(tag, indexOfTag) in scope.row.tags"
                          :key="indexOfTag"
                          style="display: inline-block"
                        >
                          <el-tag style="margin-left: 5px; margin-right: 5px">{{
                            tag.tag_name
                          }}</el-tag>
                        </div>
                      </template>
                    </el-table-column>
                    <el-table-column prop="heat" label="热度" />
                    <el-table-column label="操作">
                      <template slot-scope="scope">
                        <el-button
                          type="text"
                          size="small"
                          @click="ClickActDetail(scope.row.activity_id)"
                          >查看</el-button
                        >
                      </template>
                    </el-table-column>
                  </el-table>
                  <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[5, 10, 15, 20]"
                    :page-size="pagesize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="tableData.length"
                  >
                  </el-pagination>
                </el-card>
              </el-col>
            </div>
          </el-main>
        </el-container>
      </el-tab-pane>
      <el-tab-pane label="附近" name="around">
        <ActivityAround></ActivityAround>
      </el-tab-pane>
    </el-tabs>
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
import { getAvailableActs, findActTags, getActByTag } from "@/api/activity";
import ActivityAround from "./components/activityAround";
import { getAllTag } from "@/api/tag";
export default {
  name: "ActivityIndex",
  components: { ActivityAround },
  data() {
    return {
      currentPage: 1, // 初始页
      pagesize: 5, //    每页的数据
      currentPage1: 1, // 初始页
      pagesize1: 5, //    每页的数据
      form: {},
      tableData: [],
      showData: "",
      search: "请输入活动名称",
      searchData: "",
      searchResult: true,
      dialogFormVisible: false,

      index_tab: "index",

      tags: [],
      checkboxGroup1: [],
      region: "",
      date: "",
      formLabelWidth: "120px",
    };
  },
  created: function () {
    this.initData();
  },
  mounted() {
    console.log("gettingAvailableActs");
    this.getAvailableActs();
    console.log("gotAvailableActs");
    this.GetAllTags();
  },
  methods: {
    handleSizeChange(size) {
      console.log(size, "size");
      this.pagesize = size;
      console.log(this.pagesize); // 每页下拉显示数据
    },
    handleCurrentChange(currentPage) {
      console.log(currentPage, "currentPage");
      this.currentPage = currentPage;
      console.log(this.currentPage); // 点击第几页
    },
    handleSizeChange1(size) {
      console.log(size, "size");
      this.pagesize1 = size;
      console.log(this.pagesize1); // 每页下拉显示数据
    },
    handleCurrentChange1(currentPage) {
      console.log(currentPage, "currentPage");
      this.currentPage1 = currentPage;
      console.log(this.currentPage1); // 点击第几页
    },
    /** activity.images 得出第一张配图的url */
    GetIllusImgUrl(images) {
      console.log("image_url iiii", images);

      /* 分析images */
      let image_url;
      const image_paths = images.split(":");
      for (let i = 0; i < image_paths.length; i++) {
        const image_path = image_paths[i];
        /** 直接通过url访问图片 */
        if (image_path.length > 0) {
          image_url = process.env.VUE_APP_BASE_API + "/api" + image_path;

          console.log("image_url iiii", image_url);
          break;
        }
      }
      return image_url;
    },

    /** get all tags */
    GetAllTags() {
      // get all tags
      getAllTag()
        .then((res) => {
          this.tags = res.data.map((x) => {
            return {
              tagID: x.tag_id,
              name: x.tag_name,
            };
          });
          console.log("this.tags:res:", this.tags);
        })
        .catch((err) => {
          console.log("getAllTag:err:", err);
        });
      setTimeout(() => {
        this.getLngLatLocation();
      }, 100);
    },

    IndexTabsClick(val) {},

    async getAvailableActs() {
      // api
      // 获取所有可参与的活动
      await getAvailableActs()
        .then(async (res) => {
          const acts = [];
          console.log("可参与的活动：", res);
          res.data.forEach((ele) => {
            const tags = [];
            findActTags(ele.activity_id).then(async (res) => {
              if (Object.keys(res.data).length > 0) {
                res.data.forEach((ele) => {
                  tags.push({
                    tag_name: ele.tag_name,
                    tag_id: ele.tag_id,
                  });
                });
              }
            });
            acts.push({
              activity_id: ele.activity_id,
              name: ele.title_name,
              start_time: ele.start_time,
              heat: ele.hit_num,
              tags: tags,
              images: "images" in ele ? ele.images : -1,
            });
          });
          console.log("获取活动：", acts);
          this.tableData = acts;
          this.showData = acts;
          console.log("展示信息：", this.showData);
        })
        .catch((err) => {
          console.log(err);
        })
        .finally(() => {});
    },
    ClickActDetail(act_id) {
      // console.log(act);
      // 跳转路由
      const query_data = {
        id: act_id,
      };
      this.$router.push({
        name: "ActivityPage",
        query: query_data,
      });
    },
    open() {
      if (this.searchData.length == 0) {
        this.$alert("未找到相关活动数据！", "查询结果", {
          confirmButtonText: "确定",
          callback: (action) => {
            this.$message({
              type: "info",
              message: `action: ${action}`,
            });
          },
        });
      } else {
        this.showData = this.searchData;
        this.$alert(
          "共找到" + this.searchData.length + "条结果：",
          "查询结果",
          {
            confirmButtonText: "点击查看",
          }
        );
      }
    },

    initData() {
      this.showData = this.tableData;
    },
    submitFun() {
      const search = this.search;
      this.searchData = this.tableData.filter(function (tableData) {
        console.log("过滤", tableData);
        const searchField = {
          name: tableData.name,
        };

        return Object.keys(searchField).some(function (key) {
          console.log("key值", key);
          return String(tableData[key]).toLowerCase().indexOf(search) > -1;
        });
      });
    },
    submitTagFun() {
      if (this.checkboxGroup1.length == 0) this.showData = this.tableData;
      else {
        const tagIDs = [];
        let i = 0;
        for (i; i < this.checkboxGroup1.length; i++) {
          tagIDs.push(this.checkboxGroup1[i].tagID);
        }
        getActByTag(tagIDs).then((res) => {
          const acts = [];
          res.data.forEach((ele) => {
            const tags = [];
            findActTags(ele.activity_id).then(async (res) => {
              if (Object.keys(res.data).length > 0) {
                res.data.forEach((ele) => {
                  tags.push({
                    tag_name: ele.tag_name,
                    tag_id: ele.tag_id,
                  });
                });
              }
            });
            acts.push({
              activity_id: ele.activity_id,
              name: ele.title_name,
              start_time: ele.start_time,
              heat: ele.hit_num,
              tags: tags,
            });
          });
          this.showData = acts;
          if (this.showData.length == 0) {
            this.$alert("未找到相关活动数据！", "查询结果", {
              confirmButtonText: "确定",
              callback: (action) => {
                this.$message({
                  type: "info",
                  message: `action: ${action}`,
                });
              },
            });
          } else {
            this.$alert(
              "共找到" + this.showData.length + "条结果：",
              "查询结果",
              {
                confirmButtonText: "点击查看",
              }
            );
          }
        });
      }
    },
  },
};
</script>
