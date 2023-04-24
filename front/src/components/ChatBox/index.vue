<template>
  <div id="app">
    <lemon-imui
      width="350px"
      simple
      :user="user"
      theme="blue"
      avatarCricle="true"
      ref="IMUI"
      :contextmenu="contextmenu"
      @pull-messages="handlePullMessages"
      @message-click="handleMessageClick"
      @send="handleSend"
    >
      <template #cover>
        <div class="cover">
          <i class="el-icon-loading"></i>
        </div>
      </template>
    </lemon-imui>
  </div>
</template>

<script>
import EmojiData from "../../assets/emoji";
const getTime = () => new Date().getTime();
const generateRandId = () => Math.random().toString(36).substr(-8);

export default {
  name: "App",
  components: {},
  data() {
    return {
      user: {
        id: "1",
        displayName: "养乐多和养乐多",
        avatar: "https://pic2.zhimg.com/v2-af0c3030ea926691fb00030b4b727835_b.jpg",
      },
      contextmenu: [
        {
          click: (e, instance, hide) => {
            const { IMUI, message } = instance;
            const data = {
              id: generateRandId(),
              type: "event",
              //使用 jsx 时 click必须使用箭头函数（使上下文停留在vue内）
              content: (
                <div>
                  <span>
                    你撤回了一条消息{" "}
                    <span
                      v-show={message.type == "text"}
                      style="color:#333;cursor:pointer"
                      content={message.content}
                      on-click={e => {
                        IMUI.setEditorValue(e.target.getAttribute("content"));
                      }}
                    >
                      重新编辑
                    </span>
                  </span>
                </div>
              ),

              toContactId: message.toContactId,
              sendTime: getTime(),
            };
            IMUI.removeMessage(message.id);
            IMUI.appendMessage(data, true);
            hide();
          },
          visible: instance => {
            return instance.message.fromUser.id == this.user.id;
          },
          text: "撤回消息",
        },
        {
          visible: instance => {
            return instance.message.fromUser.id != this.user.id;
          },
          text: "举报",
        },
        {
          text: "转发",
        },
        {
          visible: instance => {
            return instance.message.type == "text";
          },
          text: "复制文字",
        },
        {
          visible: instance => {
            return instance.message.type == "image";
          },
          text: "下载图片",
        },
        {
          visible: instance => {
            return instance.message.type == "file";
          },
          text: "下载文件",
        },
        {
          click: (e, instance, hide) => {
            const { IMUI, message } = instance;
            IMUI.removeMessage(message.id);
            hide();
          },
          icon: "lemon-icon-folder",
          color: "red",
          text: "删除",
        },
      ],
    };
  },
  mounted() {
    const { IMUI } = this.$refs;
    IMUI.initContacts([
      {
        id: "2",
        displayName: "优酸乳和优酸乳",
        avatar: "https://pic1.zhimg.com/v2-5b581a44738e3843e535227c42f6554c_r.jpg",
        type: "single",
        index: "T",
        unread: 0,
        lastSendTime: getTime(),
        lastContent: "",
      },
    ]);
    setTimeout(() => {
      IMUI.changeContact("2");
    }, 2000);
    IMUI.initEditorTools([
      {
        name: "emoji",
      },
      {
        name: "uploadFile",
      },
      {
        name: "uploadImage",
      },
      {
        name: "test1",
        click: () => {
          IMUI.$refs.editor.selectFile("application/vnd.ms-excel");
        },
        render: () => {
          return <span>Excel</span>;
        },
      },
      {
        name: "test1",
        click: () => {
          IMUI.initEditorTools([{ name: "uploadFile" }, { name: "emoji" }]);
        },
        render: () => {
          return <span>重制工具栏</span>;
        },
      },
      {
        name: "test2",
        isRight: true,
        title: "上传 Excel",
        click: () => {
          alert("点击了 ··· ");
        },
        render: () => {
          return <b>···</b>;
        },
      },
    ]);
    IMUI.initEmoji(EmojiData);
  },
  methods: {
    handleMessageClick(e, key, message) {
      const { IMUI } = this.$refs;
      if (key == "status" && message.status === "failed") {
        IMUI.updateMessage({
          id:message.id,
          toContactId:message.toContactId,
          status: "going",
          content: "重新发送消息...",
        });
        setTimeout(() => {
          IMUI.updateMessage({
            id:message.id,
            toContactId:message.toContactId,
            status: "failed",
            content: "还是发送失败",
          });
        }, 2000);
      }
    },
    handleSend(message, next, file) {
      console.log(message, next, file);
      setTimeout(() => {
        next({
          status: "succeed",
        });
      }, 1000);
    },
    handlePullMessages(contact, next) {
      const { currentContactId } = this.$refs.IMUI;
      const otherUser = {
        id: contact.id,
        avatar: contact.avatar,
        displayName: contact.displayName,
      };
      const message = (content, fromUser = this.user) => {
        return {
          id: generateRandId(),
          status: "succeed",
          type: "text",
          sendTime: getTime(),
          content,
          toContactId: currentContactId,
          fromUser,
        };
      };

      const messages = [
        message("您今天的任务还没有完成", otherUser),
        message("收到"),
      ];
      next(messages, true);
    },
  },
};
</script>

<style>
</style>