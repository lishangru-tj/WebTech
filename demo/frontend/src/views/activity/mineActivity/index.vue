<template>
  <div class="ma_view">
    <!-- <el-radio-group
      class="ma_view_redios"
      v-model="ActivityOrTemplate"
      @input="radio_input_change"
    >
      <el-radio-button label="activity">活动</el-radio-button>
      <el-radio-button label="template">模板</el-radio-button>
    </el-radio-group> -->

    <div class="ma_activity_page" v-if="ActivityOrTemplate === 'activity'">
      <div class="ma_sort_find_box">
        <el-select
          v-model="state_select_val"
          placeholder="请选择"
          @change="select_state_change"
        >
          <el-option key="all" label="全部" :value="-1"> </el-option>
          <el-option key="applying" label="正在报名" :value="0"> </el-option>
          <el-option key="doing" label="正在进行" :value="1"> </el-option>
          <el-option key="done" label="已结束" :value="2"> </el-option>
        </el-select>
        <el-button type="primary" @click="ClickCreateNewAct"
          >新建活动</el-button
        >
      </div>
      <el-tabs
        v-model="activity_tab"
        @tab-click="(tab) => ActivityTabsClick(tab.name)"
      >
        <el-tab-pane label="所有活动" name="all">
          <el-col
            :span="6"
            v-for="(act, act_i) in activity_data_all"
            :key="act_i"
            class="ma_act_col"
          >
            <el-card @click.native="ClickActDetail(act)">
              <span class="ma_act_page_card">
                <div class="user-select-none">{{ act.name }}</div>
                <el-image class="el-image-ii" :src="act.img">
                  <div slot="error" class="image-slot">
                    <div class="user-select-none hint">暂无配图</div>
                    <i
                      style="font-size: 15em"
                      class="el-icon-picture-outline"
                    ></i>
                  </div>
                </el-image>
                <el-tag :type="ActicityState_TagType(act.state)">{{
                  ActicityState_TagLabel(act.state)
                }}</el-tag>
              </span>
            </el-card>
          </el-col>
        </el-tab-pane>
        <el-tab-pane label="我创建的" name="create">
          <el-col
            :span="6"
            v-for="(act, act_i) in activity_data_create"
            :key="act_i"
            class="ma_act_col"
          >
            <el-card @click.native="ClickActDetail(act)">
              <span class="ma_act_page_card">
                <div class="user-select-none">{{ act.name }}</div>
                <el-image class="el-image-ii" :src="act.img">
                  <div slot="error" class="image-slot">
                    <div class="user-select-none hint">暂无配图</div>
                    <i
                      style="font-size: 15em"
                      class="el-icon-picture-outline"
                    ></i>
                  </div>
                </el-image>
                <el-tag :type="ActicityState_TagType(act.state)">{{
                  ActicityState_TagLabel(act.state)
                }}</el-tag>
              </span>
            </el-card>
          </el-col>
        </el-tab-pane>
        <el-tab-pane label="我报名的" name="apply">
          <el-col
            :span="6"
            v-for="(act, act_i) in activity_data_apply"
            :key="act_i"
            class="ma_act_col"
          >
            <el-card @click.native="ClickActDetail(act)">
              <span class="ma_act_page_card">
                <div class="user-select-none">{{ act.name }}</div>
                <el-image class="el-image-ii" :src="act.img">
                  <div slot="error" class="image-slot">
                    <div class="user-select-none hint">暂无配图</div>
                    <i
                      style="font-size: 15em"
                      class="el-icon-picture-outline"
                    ></i></div
                ></el-image>
                <el-tag :type="ActicityState_TagType(act.state)">{{
                  ActicityState_TagLabel(act.state)
                }}</el-tag>
              </span>
            </el-card>
          </el-col>
        </el-tab-pane>
        <el-tab-pane label="我参与的" name="involve">
          <el-col
            :span="6"
            v-for="(act, act_i) in activity_data_involve"
            :key="act_i"
            class="ma_act_col"
          >
            <el-card @click.native="ClickActDetail(act)">
              <span class="ma_act_page_card">
                <div class="user-select-none">{{ act.name }}</div>
                <el-image class="el-image-ii" :src="act.img">
                  <div slot="error" class="image-slot">
                    <div class="user-select-none hint">暂无配图</div>
                    <i
                      style="font-size: 15em"
                      class="el-icon-picture-outline"
                    ></i></div
                ></el-image>
                <el-tag :type="ActicityState_TagType(act.state)">{{
                  ActicityState_TagLabel(act.state)
                }}</el-tag>
              </span>
            </el-card>
          </el-col>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div class="ma_template_page" v-if="ActivityOrTemplate === 'template'">
      <el-col
        :span="6"
        v-for="(act, act_i) in template_data"
        :key="act_i"
        class="ma_act_col"
      >
        <el-card @click.native="ClickModifyTemplate(act)">
          <span class="ma_act_page_card">
            <div class="user-select-none">{{ act.name }}</div>
            <el-image class="el-image-ii" :src="act.img">
              <div slot="error" class="image-slot">
                <div class="user-select-none hint">暂无配图</div>
                <i
                  style="font-size: 15em"
                  class="el-icon-picture-outline"
                ></i></div
            ></el-image>
          </span>
          <div class="bottom clearfix">
            <el-button type="primary" @click.stop="ClickModifyTemplate(act)"
              >修改</el-button
            >
            <el-button type="danger" @click.stop="ClickDeleteTemplate(act)"
              >删除</el-button
            >
          </div>
        </el-card>
      </el-col>
    </div>

    <CreateActivity
      v-on:CreateActivityClose="CreateActivityClose_Handler"
      v-show="isShow_dialog_createNewAct"
      :isShow="isShow_dialog_createNewAct"
    ></CreateActivity>
  </div>
</template>
<script>
import { getImg } from "@/api/file";
import { HelloID } from "@/api/user";
import {
  getActList_All,
  getActList_Create,
  getActList_Apply,
  getActList_Participate,
} from "@/api/activity";
import CreateActivity from "../components/createAct.vue";
import { mapGetters } from "vuex";

export default {
  name: "MyActivity",
  components: { CreateActivity },
  data() {
    return {
      //tab 值
      ActivityOrTemplate: "activity",
      activity_tab: "create",

      //活动data
      activity_data_all: [],
      activity_data_create: [],
      activity_data_involve: [],
      activity_data_apply: [],

      // act单选框，筛选
      state_select_val: -1,

      //模板data
      template_data: [],

      /*创建新活动 */
      isShow_dialog_createNewAct: false,
    };
  },
  computed: {
    user_id: {
      get: function () {
        return this.$store.getters.id;
      },
      set: function (newVal) {},
    },
    user_name: {
      get: function () {
        return this.$store.getters.name;
      },
      set: function (newVal) {},
    },
  },
  methods: {
    /*
    获取活动列表data
     */

    async GetActivityData_All() {
      let activity_data_all = [];
      // img_url = "";
      await getActList_All(this.user_id, this.state_select_val)
        .then((res) => {
          console.log("user_id:", this.user_id, "getActList_All:", res);

          res.data.forEach(async (ele, i) => {
            /*分析images */
            let image_url;
            if ("images" in ele) {
              let image_paths = ele.images.split(":");
              for (let i = 0; i < image_paths.length; i++) {
                let image_path = image_paths[i];
                /**直接通过url访问图片 */
                if (image_path.length > 0) {
                  image_url = "http://42.192.45.95:8081/api" + image_path;
                  console.log("image_url", image_url);
                  break;
                }
              }
            }
            activity_data_all.push({
              id: ele.activity_id,
              name: ele.title_name,
              img: image_url,
              state: ele.state,
            });
          });
          this.activity_data_all = activity_data_all;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async GetActivityData_Create() {
      let activity_data_create = [];
      let img_url =
        "https://ts1.cn.mm.bing.net/th/id/R-C.f4470ef67e6e8803479dc44bb3c66574?rik=FXJkAJS%2fLCy9vg&riu=http%3a%2f%2fwww.szshequ.org%2fuserfiles%2fmanagers%2fdachong%2fimage%2f20191028%2f20191028193129_387.jpg&ehk=8FRYXacd0vDrnp2VfQlyA2xK1jIEETABsKgITDdCJXs%3d&risl=&pid=ImgRaw&r=0";
      // img_url = "";
      await getActList_Create(this.user_id, this.state_select_val)
        .then((res) => {
          console.log("user_id:", this.user_id, "getActList_Create:", res);
          res.data.forEach(async (ele, i) => {
            /*分析images */
            let image_url;
            if ("images" in ele) {
              let image_paths = ele.images.split(":");
              for (let i = 0; i < image_paths.length; i++) {
                let image_path = image_paths[i];
                /**直接通过url访问图片 */
                if (image_path.length > 0) {
                  // image_url = "http://localhost:8081/api" + image_path;
                  image_url =
                    process.env.VUE_APP_BASE_API + "/api" + image_path;

                  console.log("image_url", image_url);
                  break;
                }
              }
            }
            activity_data_create.push({
              id: ele.activity_id,
              name: ele.title_name,
              img: image_url,
              state: ele.state,
            });
          });
          this.activity_data_create = activity_data_create;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async GetActivityData_Apply() {
      let activity_data_apply = [];
      let img_url =
        "https://ts1.cn.mm.bing.net/th/id/R-C.f4470ef67e6e8803479dc44bb3c66574?rik=FXJkAJS%2fLCy9vg&riu=http%3a%2f%2fwww.szshequ.org%2fuserfiles%2fmanagers%2fdachong%2fimage%2f20191028%2f20191028193129_387.jpg&ehk=8FRYXacd0vDrnp2VfQlyA2xK1jIEETABsKgITDdCJXs%3d&risl=&pid=ImgRaw&r=0";
      // img_url = "";
      await getActList_Apply(this.user_id, this.state_select_val)
        .then((res) => {
          console.log("user_id:", this.user_id, "getActList_Apply:", res);
          res.data.forEach(async (ele, i) => {
            /*分析images */
            let image_url;
            if ("images" in ele) {
              let image_paths = ele.images.split(":");
              for (let i = 0; i < image_paths.length; i++) {
                let image_path = image_paths[i];
                /**直接通过url访问图片 */
                if (image_path.length > 0) {
                  image_url = "http://42.192.45.95:8081/api" + image_path;
                  console.log("image_url", image_url);
                  break;
                }
              }
            }
            activity_data_apply.push({
              id: ele.activity_id,
              name: ele.title_name,
              img: image_url,
              state: ele.state,
            });
          });
          this.activity_data_apply = activity_data_apply;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async GetActivityData_Involve() {
      let activity_data_involve = [];
      let img_url =
        "https://ts1.cn.mm.bing.net/th/id/R-C.f4470ef67e6e8803479dc44bb3c66574?rik=FXJkAJS%2fLCy9vg&riu=http%3a%2f%2fwww.szshequ.org%2fuserfiles%2fmanagers%2fdachong%2fimage%2f20191028%2f20191028193129_387.jpg&ehk=8FRYXacd0vDrnp2VfQlyA2xK1jIEETABsKgITDdCJXs%3d&risl=&pid=ImgRaw&r=0";
      // img_url = "";
      await getActList_Participate(this.user_id, this.state_select_val)
        .then((res) => {
          console.log("user_id:", this.user_id, "getActList_Participate:", res);
          res.data.forEach(async (ele, i) => {
            /*分析images */
            let image_url;
            if ("images" in ele) {
              let image_paths = ele.images.split(":");
              for (let i = 0; i < image_paths.length; i++) {
                let image_path = image_paths[i];
                /**直接通过url访问图片 */
                if (image_path.length > 0) {
                  image_url = "http://42.192.45.95:8081/api" + image_path;
                  console.log("image_url", image_url);
                  break;
                }
              }
            }
            activity_data_involve.push({
              id: ele.activity_id,
              name: ele.title_name,
              img: image_url,
              state: ele.state,
            });
          });
          this.activity_data_involve = activity_data_involve;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //传入活动配图的路径，返回byte[]
    async GetImgBytes(ImgPath) {
      await getImg(ImgPath)
        .then((res) => {
          return res.data.bytes;
        })
        .catch((err) => {
          console.log("TestGetImg:err", err);
        });
      return -1;
    },

    //获取模板data
    GetTemplateData() {
      let prefix = "template";
      let tmp = [];
      let img_url =
        "https://ts1.cn.mm.bing.net/th/id/R-C.f4470ef67e6e8803479dc44bb3c66574?rik=FXJkAJS%2fLCy9vg&riu=http%3a%2f%2fwww.szshequ.org%2fuserfiles%2fmanagers%2fdachong%2fimage%2f20191028%2f20191028193129_387.jpg&ehk=8FRYXacd0vDrnp2VfQlyA2xK1jIEETABsKgITDdCJXs%3d&risl=&pid=ImgRaw&r=0";

      for (let i = 0; i < 6; i++) {
        tmp.push({
          id: prefix + "_" + "id" + "_" + i.toString(),
          name: prefix + "_" + "name" + "_" + i.toString(),
          img: img_url,
        });
      }
      this.template_data = tmp;
    },
    //状态筛选改变
    select_state_change(val) {
      this.ActivityTabsClick(this.activity_tab);
      // console.log(val);
    },
    radio_input_change(val) {
      switch (val) {
        case "activity":
          this.GetActivityData_All();
          break;
        case "template":
          this.GetTemplateData();
          break;
      }
    },
    //Act tabs click
    ActivityTabsClick(name) {
      switch (name) {
        case "all":
          this.GetActivityData_All();
          break;
        case "create":
          this.GetActivityData_Create();
          break;
        case "apply":
          this.GetActivityData_Apply();
          break;
        case "involve":
          this.GetActivityData_Involve();
          break;
      }
    },

    /*活动操作 */
    //点击活动，查看详情
    ClickActDetail(act) {
      // console.log(act);
      //跳转路由
      let query_data = {
        id: act.id,
      };
      this.$router.push({
        name: "ActivityPage",
        query: query_data,
      });
    },

    /*
    模板操作
    */
    //删除模板
    ClickDeleteTemplate(act) {
      console.log(act);
      //弹窗确认
      const h = this.$createElement;
      this.$msgbox({
        title: "删除该模板！",
        // message: h("p", null, [
        //   h("span", null, "将把管理员 "),
        //   h("i", { style: "color: teal" }, mem.name),
        //   h("span", null, " 降级为一般成员"),
        // ]),
        // dangerouslyUseHTMLString: true,
        showCancelButton: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        beforeClose: (action, instance, done) => {
          //点击了 确认
          if (action === "confirm") {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = "执行中...";
            //向后端接口请求，删除
            //静态测试，2s后完成操作
            setTimeout(() => {
              done();
              setTimeout(() => {
                instance.confirmButtonLoading = false;
              }, 300);
            }, 2000);
          } else {
            done();
          }
        },
      }).then((action) => {
        this.$message({
          type: "info",
          message: "模板删除成功",
        });
        //重新获取模板data
        this.GetTemplateData();
      });
    },

    //点击模板，修改
    ClickModifyTemplate(act) {
      //进入模板的修改页面
      console.log(act);
      let query_data = {
        id: act.id,
      };
      this.$router.push({ name: "TemplateView", query: query_data });
    },
    /*创建新活动 */
    ClickCreateNewAct() {
      this.isShow_dialog_createNewAct = true;
    },
    //创建活动的组件，关闭事件
    CreateActivityClose_Handler(val) {
      this.ActivityTabsClick(this.activity_tab);

      this.isShow_dialog_createNewAct = val;
    },
    //活动状态的label
    ActicityState_TagType(state_val) {
      if (state_val == 0) {
        return "";
      } else if (state_val == 1) {
        return "danger";
      } else if (state_val == 2) {
        return "warning";
      }
    },

    ActicityState_TagLabel(state_val) {
      if (state_val == 0) {
        return "正在报名";
      } else if (state_val == 1) {
        return "正在进行";
      } else if (state_val == 2) {
        return "已结束";
      }
    },
  },
  mounted() {
    console.log("this.$store.getters.id;", this.$store.getters.id);
    const query = this.$route.query;
    // console.log(query);

    this.ActivityOrTemplate = "value" in query ? query.value : "activity";
    if (this.ActivityOrTemplate == "activity") {
      this.GetActivityData_All();
      this.GetActivityData_Create();
    } else {
      this.GetTemplateData();
    }
  },
};
</script>
<style lang="less" scoped>
.ma_view {
  .ma_view_redios {
  }
  .ma_activity_page {
    .ma_act_col {
      margin-left: 1em;

      margin-right: 2em;
      margin-bottom: 2em;
    }
  }
  .ma_template_page {
    .ma_act_col {
      margin-left: 1em;

      margin-right: 2em;
      margin-bottom: 2em;
    }
  }
  /deep/.el-image-ii {
    width: 20em !important;
    height: 15em !important;
  }
  .hint {
    font-size: 0.8em;
    color: #8f8f8f;
  }
}
</style>
