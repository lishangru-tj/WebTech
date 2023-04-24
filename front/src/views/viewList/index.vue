<template>
  <div class="mpv_viewlist_view">
    <div class="mpvv_tabs">
      <el-tabs v-model="activeName" type="card">
        <el-tab-pane label="表格" name="table" class="mpvv_T_">
          <tablelist></tablelist>
        </el-tab-pane>
        <el-tab-pane label="分组" name="group" class="mpvv_G_">
          <!-- 最终分组展示 -->
          <div class="mpvv_G_content">
            <grouplist></grouplist>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
import draggable from "vuedraggable";

import grouplist from "./components/grouplist.vue";
import tablelist from "./components/tablelist.vue";
export default {
  name: "ProjectViewList",
  components: { draggable, grouplist, tablelist },

  data() {
    return {
      activeName: "table",
      /**
       * Group数据
       */

      /*
    Table 数据
    */
    };
  },
  methods: {
    /**
     * Group 函数
     *
     * */
    /*
     *Table 函数
     *
     */
  },
  mounted() {},
  beforeDestroy() {},
};
</script>
<style lang="less" scoped>
.mpv_viewlist_view {
  .mpvv_tabs {
    .mpvv_G_ {
      .mpvv_G_content {
        display: flex;
        flex-direction: row;
        width: 100%;
        height: 100%;
        overflow: scroll;
      }
    }
    .mpvv_T_ {
    }
  }
}
</style>
