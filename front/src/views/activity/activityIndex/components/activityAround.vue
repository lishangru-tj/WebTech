<template>
  <div class="aa_view">
    <div class="aa_searchBox">
      <div class="aa_searchTop">
        <el-input
          v-model="inputValue"
          placeholder="搜索地名或者地址"
          id="tipinput"
          prefix-icon="el-icon-search"
          class="searchInput"
          @keyup.native.enter="initMapByInput"
        ></el-input>
        <el-button
          class="searchButton"
          type="primary"
          size="small"
          @click="initMapByInput"
          >查询</el-button
        >
      </div>
      <div
        class="searchContainer"
        v-if="markers.length > 0 && searchContainerShow"
      >
        <div
          class="searchItem"
          v-for="(item, index) in markers"
          :key="item.id"
          :style="activeIndex === index && 'background: #ecf5ff'"
          @click="itemChange(item, index)"
        >
          <div>{{ item.name }}</div>
          <div
            style="color: #999; font-size: 12px; margin-left: 10px"
            class="textOverflow"
          >
            {{ item.address }}
          </div>
        </div>
      </div>
    </div>

    <el-amap
      ref="map"
      vid="amapDemo"
      :center="center"
      :zoom="zoom"
      :plugin="plugin"
      class="aa_amap"
      :events="events"
    >
      <!-- <el-amap-marker :position="center" key="100"></el-amap-marker> -->
      <!-- 周围活动 -->
      <el-amap-marker
        v-for="(act_marker, index) in act_markers"
        :key="index"
        :events="act_marker.events"
        :position="act_marker.position"
      />

      <!-- 活动的信息窗口 -->
      <el-amap-info-window
        v-for="window in act_marker_windows"
        :key="window.position[0]"
        :position="window.position"
        :visible="window.visible"
        :content="window.content"
        :offset="window.offset"
        :close-when-click-map="true"
        :is-custom="true"
      >
        <el-card id="info-window">
          <div>
            <p>{{ window.title_name }}</p>
            <el-button type="text" @click="ClickActDetail(window.activity_id)"
              >查看详情</el-button
            >
          </div>
        </el-card>
      </el-amap-info-window>
    </el-amap>
    <div class="aa_bottom">
      <div style="font-weight: bold">
        所选位置:
        <span> {{ currentValue }}</span>
      </div>

      <div>
        <!-- <el-button type="primary" @click="save">确 定</el-button> -->
        <!-- <el-button @click="GetActMarker" type="primary">GetActMarker</el-button> -->
      </div>
    </div>
  </div>
</template>
<script>
// 引入获取实例
import { AMapManager } from "vue-amap";
import { getMapAct } from "@/api/activity";
let amapManager = new AMapManager();
let Geocoder;
export default {
  name: "ActivityAround",
  components: {},

  data() {
    const that = this;
    return {
      primitiveData: {},

      //经度
      lng: {},
      //维度
      lat: {},
      tabIndex: "",
      currentValue: "",
      searchContainerShow: false,
      markers: [],
      /**activity markers */
      act_markers: [],
      act_data: [],
      act_marker_windows: [],
      act_marker_window: "",
      map_scope: {
        longitudes: [],
        latitudes: [],
      },
      /*** */
      activeIndex: "",
      zoom: 12,
      center: [121.231227, 39.909187],
      searchOption: {
        citylimit: false,
      },
      resultValue: {},
      inputValue: "",
      searchResult: {
        address: "",
        latitude: "",
        longitude: "",
        name: "",
        type: "",
        country: "",
        province: "",
        city: "",
        area: "",
        township: "",
        street: "",
        neighborhood: "",
        locationName: "",
      },
      map_obj: undefined,
      events: {
        // 地图上的点击事件
        click: (e) => {
          this.$nextTick(() => {
            let that = this;
            that.position = [e.lnglat.lng, e.lnglat.lat];
            that.center = [e.lnglat.lng, e.lnglat.lat]; //打点

            this.GetActMarker();
            let geocoder = new AMap.Geocoder({});
            geocoder.getAddress(that.position, function (status, result) {
              if (status === "complete" && result.info === "OK") {
                that.resultValue = result.regeocode.addressComponent;
                let obj = result.regeocode.addressComponent;
                let value =
                  obj.province +
                  obj.city +
                  obj.district +
                  obj.township +
                  obj.street +
                  obj.streetNumber +
                  (obj.building || "");
                that.currentValue = value;
              }
            });
          });
          this.searchContainerShow = false;
        },
        moveend: (e) => {
          this.GetActMarker();
          console.log(e);
        },
        zoomchange: (e) => {
          this.GetActMarker();
          console.log(e);
        },
      },

      plugin: [
        {
          enableHighAccuracy: true, //是否使用高精度定位，默认true
          timeout: 100, //超过10秒后停止定位，默认：无穷大
          convert: true, //自动偏移后的坐标为高德坐标，默认：true
          showButton: true, //显示定位按钮，默认：true
          buttonPosition: "RB", //定位按钮停靠位置，默认'LB'，左下角
          showMarker: true, //定位成功后在定位到的位置显示标记，默认：true
          showCircle: false, //定位成功后用圆圈表示定位精度范围，默认：true
          panToLocation: true, //定位成功后将定位到的位置作为地图中心点，默认true
          zoomToAccuracy: true, //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
          extensions: "all",
          pName: "Geolocation", //AMap-Geolocation 定位插件
          events: {
            init(o) {
              // o 是高德地图定位插件实例
              o.getCurrentPosition((status, result) => {
                if (result && result.position) {
                  let obj = result.addressComponent;
                  that.resultValue = result.addressComponent;
                  let value =
                    obj.province ||
                    "" + obj.city ||
                    "" + obj.district ||
                    "" + obj.township ||
                    "" + obj.street ||
                    "" + obj.streetNumber + (obj.building || "");
                  that.currentValue = value;
                  that.lng = result.position.lng; //设置经度
                  that.lat = result.position.lat; //设置纬度
                  that.center = [that.lng, that.lat]; //设置坐标
                  that.markers.push(that.center); //获取当前定位并存入标记中显示标记点
                  that.loaded = true; //load
                  that.zoom = 14;
                  that.$nextTick(); //页面渲染好后
                }
              });
            },
          },
        },
        //Geocoder编码：根据地理名称来获得地点的经纬度
        {
          pName: "Geocoder",
          events: {
            init: (o) => {
              Geocoder = o; // o 则是AMap.Geocoder的实例 对外部的Geocoder变量进行赋值，在任何地方就都可以使用
              //data里的events中使用了Geocoder
              o.getAddress(that.center, function (status, result) {
                //根据坐标获取位置
                if (status === "complete" && result.info === "OK") {
                  let obj = result.regeocode.addressComponent;
                  that.resultValue = result.regeocode.addressComponent;

                  let value =
                    obj.province +
                    obj.city +
                    obj.district +
                    obj.township +
                    obj.street +
                    obj.streetNumber +
                    (obj.building || "");
                  that.currentValue = value;
                }
              });
            },
          },
        },
      ],
    };
  },
  computed: {},
  watch: {
    dialogShow(val) {
      if (val) {
        if (this.lng && this.lat) {
          this.center[0] = this.lng;
          this.center[1] = this.lat;
        }
        this.inputValue = this.primitiveData.name;
        this.markers = [];
      }
    },
    inputValue(value) {
      if (value) {
        // 监听输入内容
        // this.initMapByInput()
      }
    },
  },
  mounted() {
    setTimeout(() => {
      this.getLngLatLocation();
    }, 200);
    // this.GetAroundActData();
    // this.CreateMarker();
  },
  methods: {
    //获取周围活动信息
    async GetAroundActData() {
      this.GetMapScope();
      //输入地图范围，请求后端api，获取范围内活动
      await getMapAct(this.map_scope.latitudes, this.map_scope.longitudes)
        .then((res) => {
          console.log("getMapAct:res", res);
          this.act_data = res.data.activityList.map((x) => {
            return {
              position: [x.longitude, x.latitude],
              address: x.address_name,
              activity_id: x.activity_id,
              title_name: x.title_name,
            };
          });
        })
        .catch((err) => {
          console.log("getMapAct:err", err);
        });

      //   let indexs = [1, 2, 3, 4];
      //   this.act_data = indexs.map((x) => {
      //     return {
      //       position: [this.center[0] + x * 0.1, this.center[1] + x * 0.1],
      //       address: "address_name_" + x.toString(),
      //     };
      //   });

      console.log("this.act_data", this.act_data);
    },
    //根据活动，新建marker
    CreateMarker() {
      console.log("!!!!!!!!!");
      const markers = [];
      const windows = [];
      const that = this;
      this.act_data.forEach((item, index) => {
        console.log("center:", this.center);
        console.log(item.position);
        markers.push({
          position: item.position,
          // icon:item.url, //不设置默认蓝色水滴
          events: {
            mouseover() {},
            click() {
              // 方法：鼠标click点标记，显示相应窗体
              that.act_marker_windows.forEach((window) => {
                window.visible = false; // 关闭窗体
              });
              that.act_marker_window = that.act_marker_windows[index];
              that.$nextTick(() => {
                that.act_marker_window.visible = true;
              });
            },
          },
        });
        windows.push({
          position: item.position,
          isCustom: true,
          offset: [115, 55], // 窗体偏移
          showShadow: false,
          visible: false, // 初始是否显示
          address: item.address,
          title_name: item.title_name,
          activity_id: item.activity_id,
        });
      });
      //  加点
      this.act_markers = markers;
      // 加弹窗
      this.act_marker_windows = windows;
      console.log("this.act_marker_windows", this.act_markers);
    },

    GetActMarker() {
      this.GetAroundActData();
      this.CreateMarker();
    },
    //获取当前地图的范围经纬度
    GetMapScope() {
      if (this.map_obj == undefined) {
        this.map_obj = this.$refs["map"];
      }
      let bounds = this.map_obj.$$getInstance().getBounds();

      this.map_scope.longitudes = [bounds.southwest.lng, bounds.northeast.lng];
      this.map_scope.latitudes = [bounds.southwest.lat, bounds.northeast.lat];
      console.log({
        longitudes: this.map_scope.longitudes,
        latitudes: this.map_scope.latitudes,
      });
      console.log(this.map_scope);

      return this.map_scope;
    },

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
            that.center = that.primitiveData;
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

    //跳转活动详情页面
    ClickActDetail(activity_id) {
      //跳转路由
      let query_data = {
        id: activity_id,
      };
      this.$router.push({
        name: "ActivityPage",
        query: query_data,
      });
    },

    //***/ */
    itemChange(n, index) {
      const that = this;
      that.activeIndex = index;
      // 输入后搜索出来的，反地理编码出来的参数有所不同
      let lng = n.lng || n.longitude || n.location.lng;
      let lat = n.lat || n.latitude || n.location.lat;
      let name = n.name || "";
      that.inputValue = name;
      that.zoom = 13;
      that.position = [lng, lat];
      // that.searchResult.address = n.address
      // that.searchResult.latitude = lat
      // that.searchResult.longitude = lng
      // that.searchResult.name = name
      let geocoder = new AMap.Geocoder({});
      geocoder.getAddress(that.position, function (status, result) {
        if (status === "complete" && result.info === "OK") {
          let obj = result.regeocode.addressComponent;
          that.resultValue = result.regeocode.addressComponent;

          // that.searchResult.locationName = obj.province + obj.city + obj.district + obj.township + obj.street +
          //   obj.streetNumber + (n.address || obj.building || '')
          that.currentValue ==
            obj.province +
              obj.city +
              obj.district +
              obj.township +
              obj.street +
              obj.streetNumber +
              (n.address || obj.building || "");
        }
      });
      this.searchContainerShow = false;
    },
    initMapByInput() {
      console.log("initMapByInput");
      let that = this;
      // 自动搜索插件
      AMap.plugin("AMap.Autocomplete", () => {
        let autoOptions = {
          city: "全国",
        };
        let autoComplete = new AMap.Autocomplete(autoOptions);
        autoComplete.search(that.inputValue, function (status, e) {
          if (status === "complete" && e.info === "OK") {
            if (e.tips && e.tips.length > 0) {
              let tips = e.tips.filter((i) => {
                return i.location && i.address.length > 0;
              });
              let tip = tips[0];
              that.center = [tip.location.lng, tip.location.lat];
              that.zoom = 13;
              that.position = [tip.location.lng, tip.location.lat];
              that.markers = tips;
              // that.searchResult.address = tip.address
              // that.searchResult.latitude = tip.location.lat
              // that.searchResult.longitude = tip.location.lng
              // that.searchResult.name = tip.name
              let geocoder = new AMap.Geocoder({});
              geocoder.getAddress(that.position, function (status, result) {
                if (status === "complete" && result.info === "OK") {
                  let obj = result.regeocode.addressComponent;
                  that.resultValue = result.regeocode.addressComponent;

                  // that.searchResult.locationName = obj.province + obj.city + obj.district + obj.township +
                  //   obj.street + tip.address
                  that.currentValue =
                    obj.province +
                    obj.city +
                    obj.district +
                    obj.township +
                    obj.street +
                    tip.address;
                }
              });
              that.searchContainerShow = true;
            } else {
              that.markers = [];
              that.searchResult = [];
            }
          }
        });
      });
    },
    // 保存提交
    save() {
      if (this.center.length == 0 || !this.currentValue) {
        return this.$message.warning("选择的地点有误，请重新选择");
      }
      this.$emit("locationSure", {
        name: this.currentValue,
        value: this.center,
        address: this.resultValue,
      });
      this.$emit("update:primitiveData", this.searchResult);
      this.hide();
    },
  },
};
</script>

<style lang='less' scoped>
.aa_view {
  .aa_searchBox {
    background: #fff;
    font-size: 1.3em;
    color: #333333;

    .aa_searchTop {
      display: flex;
      flex-direction: row;
      width: 100%;

      .searchInput {
        width: calc(80% - 5em);
      }
      .searchButton {
        font-size: 0.8em;
        margin-left: 1em;
        width: 5em;
      }
    }

    .searchContainer {
      height: 180px;
      margin-top: 10px;
      padding: 0 10px;
    }

    .searchItem {
      color: #333;
      font-size: 13px;
      text-align: left;
      padding: 7px 10px;
      display: flex;
      border-bottom: 1px solid #e6e6e6;
      cursor: pointer;
    }
  }

  .aa_amap {
    width: 100%;
    height: 600px;
  }

  .aa_bottom {
  }
}
</style>