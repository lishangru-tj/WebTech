<template>
  <div>
    <el-dialog
      v-bind="$attrs"
      v-on="$listeners"
      @open="onOpen"
      @close="onClose"
      title="创建新活动"
      :visible.sync="isShow_"
    >
      <el-row :gutter="15">
        <el-form
          ref="create_activity"
          :model="newact_form"
          :rules="newact_rules"
          size="medium"
          label-width="100px"
          label-position="top"
        >
          <el-col :span="24">
            <el-form-item label="活动标题" prop="title_name">
              <el-input
                v-model="newact_form.title_name"
                placeholder="请输入活动标题"
                clearable
                :style="{ width: '100%' }"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="活动概要" prop="summary">
              <el-input
                v-model="newact_form.summary"
                placeholder="一句话介绍该活动(30字)"
                :maxlength="30"
                clearable
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="开始时间" prop="start_time">
              <el-date-picker
                type="datetime"
                v-model="newact_form.start_time"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                :style="{ width: '100%' }"
                placeholder="请选择开始时间"
                clearable
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="结束时间" prop="end_time">
              <el-date-picker
                type="datetime"
                v-model="newact_form.end_time"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                :style="{ width: '100%' }"
                placeholder="请选择结束时间"
                clearable
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="活动地点" prop="address_name">
              <el-input
                v-model="newact_form.address_name"
                placeholder="请输入活动地点"
                clearable
                prefix-icon="el-icon-location-information"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>

            <el-form-item label="详细地址" prop="address_formatted">
              <el-input
                v-model="newact_form.address_formatted"
                placeholder="可以根据地图选择详细地址"
                clearable
                prefix-icon="el-icon-location-information"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="innerVisible = !innerVisible"
                >选择地图地址</el-button
              >
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- <el-form-item label="" prop="limit_capacity">
              <el-radio-group
                v-model="newact_form.limit_capacity"
                size="medium"
              >
                <el-radio
                  v-for="(item, index) in limit_capacityOptions"
                  :key="index"
                  :label="item.value"
                  :disabled="item.disabled"
                  >{{ item.label }}</el-radio
                >
              </el-radio-group>
            </el-form-item> -->
          </el-col>
          <el-col :span="12">
            <el-form-item
              label-width="-3px"
              label="参与人数上限"
              prop="capacity"
            >
              <el-input-number
                :disabled="newact_form.limit_capacity != '1'"
                v-model="newact_form.capacity"
                :step="1"
                :min="2"
              ></el-input-number>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="标签" required>
              <el-checkbox-group
                v-model="newact_form.tags"
                @change="TagGroupChange"
              >
                <span
                  v-for="(tag, tag_index) in optional_tags"
                  :key="tag_index"
                >
                  <el-checkbox border :label="tag.tag_id">{{
                    tag.tag_name
                  }}</el-checkbox>
                </span>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="" prop="repeat" required>
              <el-switch
                v-model="newact_form.repeat"
                active-text="自动重复"
                inactive-text="活动只进行一次"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重复的间隔天数" prop="repeat_interval">
              <el-input-number
                :disabled="!newact_form.repeat"
                v-model="newact_form.repeat_interval"
                placeholder="重复的间隔天数"
                :step="1"
              >
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="" prop="is_private">
              <el-radio-group v-model="newact_form.is_private" size="medium">
                <el-radio-button
                  v-for="(item, index) in privateOptions"
                  :key="index"
                  :label="item.value"
                  :disabled="item.disabled"
                  >{{ item.label }}</el-radio-button
                >
              </el-radio-group>
            </el-form-item>
          </el-col> -->
          <el-col :span="24">
            <el-form-item label="详细说明" prop="intro_text">
              <el-input
                v-model="newact_form.intro_text"
                type="textarea"
                placeholder="请输入活动的详细说明"
                :autosize="{ minRows: 4, maxRows: 10 }"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="上传相关图片">
              <UploadImg
                @FileChange="HandleUploadImgInput"
                :limit="3"
              ></UploadImg>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div slot="footer">
        <el-button @click="cancel_CreateAct">取消</el-button>
        <el-button type="primary" @click="confirm_CreateAct">确定</el-button>
        <!-- <el-button @click="TestTagAPI">Test Tag API</el-button> -->
      </div>

      <span v-if="primitiveData != []">
        <MapChoose
          :dialogShow.sync="innerVisible"
          @locationSure="locationSure"
          :primitiveData_comp="primitiveData"
        ></MapChoose>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { postAct } from "@/api/activity";
import { postFile } from "@/api/file";
import { parseTime } from "@/utils";
import MapChoose from "@/components/MapChoose";
import UploadImg from "@/components/UploadImg";
import { getAllTag, postActTags } from "@/api/tag";

export default {
  inheritAttrs: false,
  name: "CreateActivity",
  components: { MapChoose, UploadImg },
  props: {
    isShow: {
      type: Boolean,
      required: false,
      default: false,
    },
  },
  data() {
    return {
      //map:null,
      primitiveData: [],
      innerVisible: false,
      isShow_: true,
      newact_form: {
        title_name: undefined,
        summary: undefined,
        start_time: "2022-11-17 00:00:00",
        end_time: undefined,
        address_name: undefined,
        address_formatted: undefined,
        longitude: undefined,
        latitude: undefined,
        limit_capacity: 1,
        capacity: 2,
        repeat: false,
        repeat_interval: undefined,
        intro_text: undefined,
        file_formData: "",
        images: "",

        tags: [],
      },
      newact_rules: {
        title_name: [
          {
            required: true,
            message: "请输入活动标题",
            trigger: "blur",
          },
        ],
        summary: [
          {
            required: true,
            message: "一句话介绍该活动(30字)",
            trigger: "blur",
          },
        ],
        start_time: [
          {
            required: true,
            message: "请选择开始时间",
            trigger: "change",
          },
        ],
        end_time: [],
        address_name: [
          {
            required: true,
            message: "请输入活动地点",
            trigger: "blur",
          },
        ],
        address_formatted: [
          {
            required: false,
            message: "",
            trigger: "blur",
          },
        ],
        limit_capacity: [
          {
            required: true,
            message: "不能为空",
            trigger: "change",
          },
        ],
        capacity: [],
        repeat_interval: [],

        intro_text: [],
      },

      limit_capacityOptions: [
        {
          label: "限制人数",
          value: 1,
        },
        {
          label: "不限人数",
          value: 2,
        },
      ],

      //可选标签
      optional_tags: [],
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
  watch: {
    isShow: {
      handler(newValue, oldValue) {
        this.isShow_ = newValue;
      },
      deep: true,
      immediate: true,
    },
  },
  created() {},
  mounted() {
    this.isShow_ = this.isShow;
    this.GetAllTags();
  },
  methods: {
    /**Tag */
    GetAllTags() {
      //get all tags
      getAllTag()
        .then((res) => {
          this.optional_tags = res.data;
          console.log("getAllTag:res:", res);
        })
        .catch((err) => {
          console.log("getAllTag:err:", err);
        });
      setTimeout(() => {
        this.getLngLatLocation();
      }, 100);
    },
    //check group change event
    TagGroupChange(val) {
      // console.log("newact_form.tags:", this.newact_form.tags);
      console.log("TagGroupChange:", val);
    },
    //测试Tag API
    TestTagAPI() {
      let tag_list = [
        {
          tag_id: 1,
          name: "tag_1",
        },
        {
          tag_id: 2,
          name: "tag_2",
        },
      ];

      let activity = {
        act: 1,
        id: 2,
      };
      postActTags(1, tag_list, activity)
        .then((res) => {
          console.log("postActTags:res:" + res);
        })
        .catch((err) => {
          console.log("postActTags:err:" + err);
        });
    },

    /**图片上传 */

    //接收组件传来的formData
    HandleUploadImgInput(FormData) {
      this.newact_form.file_formData = FormData;
    },

    //接收地图组件返回值，设置form值
    locationSure(val) {
      console.log("val", val);
      this.newact_form.address_formatted = val.address_formatted;
      this.newact_form.longitude = val.value[0];
      this.newact_form.latitude = val.value[1];

      this.primitiveData = val.value;
      // console.log("primitiveData:", this.primitiveData);
    },
    //浏览器IP定位
    getLngLatLocation() {
      let that = this;
      AMap.plugin("AMap.CitySearch", function () {
        var citySearch = new AMap.CitySearch();
        citySearch.getLocalCity(function (status, result) {
          if (status === "complete" && result.info === "OK") {
            // 查询成功，result即为当前所在城市信息
            console.log("通过ip获取当前城市：", result);
            that.primitiveData = [
              (result.bounds.northeast.lng + result.bounds.southwest.lng) / 2,
              (result.bounds.northeast.lat + result.bounds.southwest.lat) / 2,
            ];
            console.log("primitiveData", that.primitiveData);
            //逆向地理编码
            AMap.plugin("AMap.Geocoder", function () {
              var geocoder = new AMap.Geocoder({
                // city 指定进行编码查询的城市，支持传入城市名、adcode 和 citycode
                city: result.adcode,
              });

              var lnglat = result.rectangle.split(";")[0].split(",");

              geocoder.getAddress(lnglat, function (status, data) {
                if (status === "complete" && data.info === "OK") {
                  // result为对应的地理位置详细信息
                  console.log(data);
                }
              });
            });
          }
        });
      });
    },
    onOpen() {},
    onClose() {
      //   this.$refs["create_activity"].resetFields();
      this.$emit("CreateActivityClose", this.isShow_);
    },
    cancel_CreateAct() {
      //   this.$emit("update:visible", false);
    },
    confirm_CreateAct() {
      this.$refs["create_activity"].validate(async (valid) => {
        if (!valid) {
          return;
        } else {
          //弹窗确认
          const h = this.$createElement;
          this.$msgbox({
            title: "确定发布活动？",
            // message:
            // h("p", null, [
            //   h("span", null, "将把管理员 "),
            //   h("i", { style: "color: teal" }, mem.name),
            //   h("span", null, " 降级为一般成员"),
            // ]),
            // dangerouslyUseHTMLString: true,
            showCancelButton: true,
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            beforeClose: async (action, instance, done) => {
              //点击了 确认
              if (action === "confirm") {
                instance.confirmButtonLoading = true;
                instance.confirmButtonText = "执行中...";
                //向后端接口请求

                //api
                //请求新增活动
                //组装请求数据包
                console.log(
                  "this.newact_form.start_time",
                  this.newact_form.start_time
                );
                //后端需要格式字符串
                //2022-11-17 00:00:00
                let now = new Date();
                let create_time = now
                  .toLocaleString("zh-CN")
                  .replace(/[/]/, "-");
                create_time = create_time.replace(/[/]/, "-");

                // console.log("create_time", create_time);
                // let create_time=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDay()+" "+
                console.log();
                let activity = {
                  // activity_id: undefined,
                  title_name: this.newact_form.title_name,
                  detail_text: this.newact_form.intro_text,
                  summary: this.newact_form.summary,
                  start_time: this.newact_form.start_time,
                  end_time: this.newact_form.end_time,
                  create_time: create_time,

                  hit_num: 0,
                  //limit_capacity 1限制人数
                  limit_capacity: this.newact_form.limit_capacity,
                  capacity: this.newact_form.capacity,

                  state: 0,
                  participant_num: 1,
                  applicant_num: 0,
                  //地址
                  address_name: this.newact_form.address_name,
                  address_formatted: this.newact_form.address_formatted,
                  longitude: this.newact_form.longitude,
                  latitude: this.newact_form.latitude,

                  //前端repeat_interval undefined，后端null空，返回实体不含前端repeat_interval
                  repeat_interval: this.newact_form.repeat
                    ? this.newact_form.repeat_interval
                    : undefined,
                  creator_id: this.user_id,
                  vote_id: undefined,

                  //图片
                  images: "",
                };
                let tag_list = [1, 2, 3];
                console.log("post:activity", activity);
                await postAct(activity, this.newact_form.tags)
                  .then(async (res) => {
                    console.log("postAct,res", res);
                    this.$message({
                      type: "success",
                      message: "创建活动成功!",
                    });

                    //api，post文件列表formData
                    if (this.newact_form.file_formData) {
                      this.newact_form.file_formData.append(
                        "folderPath",
                        "/activity/illus"
                      );
                      // res.data.activity_id + "-"

                      this.newact_form.file_formData.append(
                        "entity",
                        "activity"
                      );
                      this.newact_form.file_formData.append(
                        "id",
                        res.data.activity_id
                      );

                      //上传文件,并额外传递参数，使得这些文件与活动绑定
                      await postFile(this.newact_form.file_formData)
                        .then((res) => {
                          console.log("postFile:res:", res);
                        })
                        .catch((err) => {
                          console.log("postFile:err:", err);
                        });
                    }

                    this.isShow_ = false;
                    this.onClose();
                  })
                  .catch((err) => {
                    console.log(err);
                  })
                  .finally(() => {
                    instance.confirmButtonLoading = false;
                    done();
                  });
              } else {
                done();
              }
            },
          }).then((action) => {
            // this.$message({
            //   type: "info",
            //   message: "action: " + action,
            // });
          });
        }
      });
    },
  },
};
</script>
<style>
.el-upload__tip {
  line-height: 1.2;
}
</style>
