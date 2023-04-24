<script>
import py from "pinyin";
import {
  getActivityAllPaticipant,
  getActivityAllMessage,
  sendMessage,
} from "@/api/chat";
export default {
  name: "QqImui",
  components: {},
  data() {
    return {
      Participants: null,
      currentParticipants: null,
      activityId: null,
      userData: {
        id: null,
        avatar: null,
        displayName: null,
      },
      messages: [],
      keyword: "",
      timer: null,
      len: false,
    };
  },
  render() {
    return (
      <div class="contain">
        <lemon-imui
          width="100%"
          class="lemon-slot"
          theme="blue"
          hideMenu
          user={this.userData}
          avatar-cricle
          ref="IMUI"
          on={{
            "pull-messages": this.handlePullMessages,
            send: this.handleSend,
          }}
          scopedSlots={{
            "message-title": (contact) => {
              return (
                <div>
                  <div style="display:flex;justify-content:center">
                    <span style="font-size:22px;">{contact.displayName}</span>
                  </div>
                </div>
              );
            },
            "sidebar-contact-fixedtop": (contact) => {
              return (
                <div class="slot-contact-fixedtop">
                  <input class="slot-search" placeholder="搜索通讯录" />
                </div>
              );
            },
            "message-side": (contact) => {
              if (contact.isGroup) {
                return (
                  <div class="slot-group">
                    <div class="slot-group-title" style="font-size:15px">
                      群成员
                    </div>
                    <div class="slot-group-panel">
                      <input
                        class="slot-search"
                        placeholder="搜索群成员"
                        v-model={this.keyword}
                        on={{ input: this.searchMember }}
                      />
                      {(this.currentParticipants || []).map((item) => (
                        <div>{item.name}</div>
                      ))}
                    </div>
                  </div>
                );
              }
            },
          }}
        />
      </div>
    );
  },
  computed: {
    /**
     * 用户个人信息
     */
    userId: {
      get: function () {
        return this.$store.getters.id;
      },
    },
    user_name: {
      get: function () {
        return this.$store.getters.name;
      },
    },
    user_avatar: {
      get: function () {
        return this.$store.getters.avatar;
      },
    },
  },
  watch: {},
  created() {},
  async mounted() {
    // 获取当前活动ID
    this.activityId = this.$route.query.id;
    console.log("BigChatBox:this.activityId:", this.activityId);

    // 初始化聊天插件
    const IMUI = this.$refs.IMUI;
    // IMUI.initEmoji(EmojiData);
    IMUI.changeContact(this.activityId);
    IMUI.initContacts([
      {
        id: this.activityId,
        displayName: "活动群",
        isGroup: true,
      },
    ]);

    // 创建WebSocket连接
    var websocket = null;
    if ("WebSocket" in window) {
      websocket = new WebSocket(
        // "ws://localhost:8081/api/webSocket?activityId=" + this.activityId
        // "ws://42.192.45.95:8081/api/webSocket?activityId=" + this.activityId
        "ws://"+process.env.VUE_APP_BASE_IP_PORT+"/api/webSocket?activityId="+this.activityId
      );
    } else {
      alert("该浏览器不支持websocket！");
    }

    websocket.onopen = function (event) {
      console.log("建立连接");
    };
    websocket.onclose = function (event) {
      console.log("连接关闭");
    };
    websocket.onmessage = (event) => {
      console.log("收到消息");
      //将字符串转为JSON对象
      var message = JSON.parse(event.data);
      if (message.fromUser.id != this.userId) {
        IMUI.appendMessage(message, true);
      }
    };
    websocket.onerror = function (event) {
      console.log("websocket通信发生错误");
    };

    window.onbeforeunload = function () {
      websocket.close();
    };

    // 格式化本用户数据
    this.userData.id = this.userId;
    this.userData.avatar = this.user_avatar;
    this.userData.displayName = this.user_name;
    console.log("user_info:", this.userData.displayName);

    // 获取该活动的所有参与者
    await getActivityAllPaticipant(this.activityId)
      .then((res) => {
        this.Participants = res.data;
        this.currentParticipants = this.Participants;
        console.log("该活动的所有参与者为：", this.Participants);
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    // 计时器
    clearTimer() {
      if (this.timer) {
        clearTimeout(this.timer);
      }
    },
    // 查找群成员
    searchMember() {
      this.clearTimer();
      if (this.keyword && this.keyword.length > 0) {
        this.len = true;
        this.timer = setTimeout(() => {
          this.currentParticipants = this.filterMember();
        }, 500);
      } else {
        if (this.len) {
          this.currentParticipants = this.filterMember();
        }
        if (this.keyword === "") {
          this.len = false;
          return;
        }
      }
    },
    // 筛选群成员
    filterMember() {
      // 如果关键词为空，则返回所有数据
      if (this.keyword === "") {
        return this.Participants;
      }
      let results = [];
      // 将查询字符分解并转为正则表达式，用于模糊搜索
      let queryStringArr = this.keyword.split("");
      let str = "(.*?)";
      let regStr = str + queryStringArr.join(str) + str;
      let reg = RegExp(regStr, "i"); // 以mh为例生成的正则表达式为/(.*?)m(.*?)h(.*?)/i

      console.log(this.Participants);
      this.Participants.some((element) => {
        let name = element.name;
        console.log("名字为", name);
        // 获取汉字的拼音，并进行扁平化
        let pyArr = py(name, {
          style: py.STYLE_NORMAL, // 设置拼音风格设置为普通风格（不带声调），
        }).flat();
        let pyStr = pyArr.join("");

        // 拼音符合正则表达式 || 文字符合正则表达式
        if (reg.test(pyStr) || reg.test(name)) {
          // 创建符合组件要求的对象（一定要有value值）
          results.push(element);
        }
      });
      return results;
    },
    // 拉取聊天室中的消息
    async handlePullMessages(contact, next) {
      const IMUI = this.$refs.IMUI;
      await getActivityAllMessage(this.activityId)
        .then((res) => {
          this.messages = res.data;
          // 将发送时间变成Number类型
          this.messages.forEach((element) => {
            element.sendTime = Number(element.sendTime);
          });
        })
        .catch((err) => {
          console.log(err);
        });
      console.log("联系人：", contact);
      console.log("所有的聊天信息为", this.messages);
      await next(this.messages, true);
    },
    // 发送消息
    handleSend(message, next, file) {
      console.log("要发送的信息是:", message);
      console.log("文件为", file);
      if (file == null) {
        // 表明是文本类型

        sendMessage(
          this.activityId,
          this.userId,
          1,
          0,
          new Date().getTime(),
          message.content
        )
          .then((res) => {
            console.log(res);
            next();
          })
          .catch((err) => {
            console.log(err);
            next({ status: "failed" });
          });
      } else {
      }
    },
  },
};
</script>
<style lang="less">
.slot-group {
  width: 170px;
  border-left: 1px solid #ddd;
  height: 100%;
  box-sizing: border-box;
  padding: 10px;
}
.slot-group .slot-search {
  margin: 5px 0;
}
.slot-group-notice {
  color: #999;
  padding: 6px 0;
  font-size: 12px;
}
.slot-group-title {
  font-size: 12px;
}
.slot-group-member {
  font-size: 12px;
  line-height: 18px;
}
.slot-group-menu span {
  display: inline-block;
  cursor: pointer;
  color: #888;
  margin: 4px 10px 0 0;
  border-bottom: 2px solid transparent;
}
.slot-group-menu span:hover {
  color: #000;
  border-color: #333;
}
.slot-contact-fixedtop {
  padding: 10px;
  border-bottom: 1px solid #ddd;
}
.slot-search {
  width: 100%;
  box-sizing: border-box;
  font-size: 14px;
  border: 1px solid #bbb;
  padding: 5px 10px;
}
// 隐藏侧边联系人列表
.lemon-sidebar {
  display: none;
}
.lemon-editor__tool-item{
  display: none;
}
</style>
