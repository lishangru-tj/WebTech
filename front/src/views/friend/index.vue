<template>
    <div class="friend-container">
      <h1>好友列表</h1><el-popover
    placement="top-start"
    title="添加好友"
    width="200"
    trigger="click"
    style="margin-left: 60%;height: 50%;align-self: center;">
    <div>
    <el-input v-model="friendToAddEmail" placeholder="输入对方邮箱"></el-input>
    <el-button type="primary" @click="addFriend(friendToAddEmail)">确定</el-button>
  </div>
    <el-button type="primary" slot="reference">添加好友</el-button>
  </el-popover>
      <el-button type="primary" style="height: 50%;align-self: center;margin-left: 1%;" @click="(dialogVisible=true)">好友管理</el-button>
      <hr width="100%"></hr>
      <div class="friend-groupbox">
        <el-menu default-active="1"
        @open="handleOpen"
        @close="handleClose" style="height: 70vh; overflow-y:scroll"
        @select="handleSelect">
        <template v-for="item in friendgroups">
        <el-menu-item :index="item.groupid">
          <span style="font-size: large;">{{item.name}}</span>
        </el-menu-item>
        <hr></hr>
        </template>
      </el-menu>

      </div>
      <div class="friend-line"></div>
      <div class="friend-box">
        <template v-for="item in friendgroup">
         <div style="margin-bottom: 20px;display: flex;font-size: large;align-items: center;justify-content: flex-start;"> 
          <el-avatar :size="60" :src="item.avatar"></el-avatar>
          <span v-if="item.name">{{item.name}}</span>
          <span v-else>{{item.rootname}}</span>
          <span style="margin-left:100px">{{item.email}}</span>
          <span v-if="item.age" style="margin-left: 200px;">{{item.age}}岁</span>
          <span v-else style="margin-left: 200px;">年龄保密</span>
          <span v-if="item.introduce" style="margin-left:200px">{{item.introduce}}</span>
          <span v-else style="margin-left:200px">他很神秘，没有简介</span>

         </div>
        </template>
      </div>
      <el-dialog
  title="好友管理"
  :visible.sync="dialogVisible"
  :close-on-click-modal="false"  
  :close-on-press-escape="false" 
  width="80%"
  @close="handleDialogClose()"
  >
  <div style="">
  <el-button
    size=""
    @click="addGroup()">
    添加分组
  </el-button>
  <el-popover
    placement="top-start"
    title="修改组名"
    width="200"
    trigger="click">
    <el-input v-model="friendgroups[selectGroup-1].name"></el-input>
    <el-button slot="reference">修改当前组名</el-button>
  </el-popover>
</div>
  <el-tabs v-model="selectGroup" closable @tab-remove="deleteGroup" type="card">
  <el-tab-pane
    v-for="item in friendgroups"
    :label="item.name"
    :name="item.groupid">
  </el-tab-pane>
  </el-tabs>
  <template v-for="item in friendManagerGroup">
    <div style="margin-bottom: 20px;display: flex;font-size: large;align-items: center;justify-content: flex-start;"> 
          <el-avatar :size="60" :src="item.avatar"></el-avatar>
          <el-input v-model="friends[friends.indexOf(item)].name" style="width:10% ;"></el-input>
          <span>原名:{{item.rootname}}</span>
          <span style="margin-left:100px">{{item.email}}</span>
  <el-button type="text" style="margin-left: 500px;margin-right: 20px;" @click="handleDeleteFriend(item)">删除</el-button>
  <el-dropdown @command="handleCommand">
  <span class="el-dropdown-link">
    移动至<i class="el-icon-arrow-down el-icon--right"></i>
  </span>
  <el-dropdown-menu slot="dropdown">
    <template v-for="group in friendgroups">
      <el-dropdown-item :command="{item,group}">{{group.name}}</el-dropdown-item>
    </template>
  </el-dropdown-menu>
  </el-dropdown>
  <el-dropdown @command="handleCopyCommand">
  <span class="el-dropdown-link">
    复制到<i class="el-icon-arrow-down el-icon--right"></i>
  </span>
  <el-dropdown-menu slot="dropdown">
    <template v-for="group in friendgroups">
      <el-dropdown-item :command="{item,group}">{{group.name}}</el-dropdown-item>
    </template>
  </el-dropdown-menu>
  </el-dropdown>
    </div>
  </template>
  <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取消</el-button>
    <el-button type="primary" @click="updateData">确定</el-button>
  </span>
</el-dialog>
    </div>


</template>
<script>
import { friendList, friendManage, AddFriend } from "@/api/friend";
export default {
  name: "CalenderView",
  data() {
    return {
      friends: [],
      friendgroups: [],
      groupindex: "1",
      dialogVisible: false,
      selectGroup: "1",
      reloading: true,
      temp: null,
      friendToAddEmail: null,
    };
  },
  computed: {
    friendgroup: function () {
      return this.friends.filter((friend) => {
        console.log("过滤列表");
        return friend.groupid === this.groupindex;
      });
    },
    friendManagerGroup: function () {
      return this.friends.filter((friend) => {
        console.log("过滤列表");
        return friend.groupid === this.selectGroup;
      });
    },
  },
  methods: {
    updateData() {
      this.reloading = false;
      this.dialogVisible = false;
      let friendlist = [];
      let formData = new FormData();

      this.friends.forEach((element) => {
        friendlist.push({
          userid: this.$store.getters.id,
          friendid: element.id,
          groupid: element.groupid,
          name: element.name,
        });
      });
      formData.append("firendList", friendlist);
      formData.append("friendGroupList", this.friendgroups);
      console.log(friendlist);
      console.log(this.friendgroups);
      friendManage({
        id: this.$store.getters.id,
        friendList: friendlist,
        friendGroup: this.friendgroups,
      }).then((response) => {
        this.$message({
          type: "success",
          message: "已上传服务器!",
        });
      });
    },
    handleDialogClose() {
      if (this.reloading) {
        location.reload();
      }
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    handleSelect(index, indexPath) {
      console.log(index, indexPath);
      this.groupindex = index;
      console.log(this.friendgroup);
    },
    handleCommand(command) {
      if (command.item.groupid == 1) {
        this.$message.error("不能移出默认分组里的好友!");
        return;
      }
      let re = true;
      this.friends.forEach((element) => {
        console.log(
          element.id,
          command.item.id,
          element.groupid,
          command.group.groupid
        );
        if (
          element.id === command.item.id &&
          element.groupid === command.group.groupid
        ) {
          console.log("相等");
          re = false;
        }
      });
      console.log(command);
      if (re) {
        this.friends[this.friends.indexOf(command.item)].groupid =
          command.group.groupid;
      } else {
        this.$message.error("目标组里已经存在当前好友");
      }
    },
    handleCopyCommand(command) {
      let re = true;
      this.friends.forEach((element) => {
        console.log(
          element.id,
          command.item.id,
          element.groupid,
          command.group.groupid
        );
        if (
          element.id === command.item.id &&
          element.groupid === command.group.groupid
        ) {
          console.log("相等");
          re = false;
        }
      });
      console.log(command);
      if (re) {
        this.friends.push({
          avatar: command.item.avatar,
          email: command.item.email,
          groupid: command.group.groupid,
          id: command.item.id,
          introduce: command.item.introduce,
          name: command.item.name,
          rootname: command.item.rootname,
        });
      } else {
        this.$message.error("目标组里已经存在当前好友");
      }
    },
    handleDeleteFriend(friend) {
      if (friend.groupid === 1) {
        this.$confirm("这将彻底删除该好友! 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            this.temp = friend.id;
            this.friends = this.friends.filter((friend1) => {
              return friend1.id !== this.temp;
            });
            this.$message({
              type: "success",
              message: "删除成功!",
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      } else {
        this.friends.splice(this.friends.indexOf(friend), 1);
      }
    },
    addGroup() {
      let newgroupID = (this.friendgroups.length + 1).toString();
      this.friendgroups.push({
        belongid: this.$store.getters.id,
        groupid: newgroupID,
        name: "新建分组",
      });
      this.selectGroup = newgroupID;
    },
    deleteGroup(targetId) {
      console.log("目标id" + targetId);
      if (targetId === "1") {
        this.$message.error("默认分组不可删除!");
        return;
      }
      console.log(this.friendgroups);
      if (
        this.friends.filter((friend) => {
          return friend.groupid === targetId;
        }).length > 0
      ) {
        this.$message.error("还有好友的组不可删除!");
        return;
      }
      let groups = this.friendgroups;
      this.selectGroup = "1";
      this.friendgroups = groups.filter((tab) => tab.groupid !== targetId);
      this.friendgroups.forEach((group) => {
        if (group.groupid > targetId) {
          console.log("遍历好友");
          this.friends.forEach((friend) => {
            if (friend.groupid === group.groupid) {
              friend.groupid -= 1;
            }
          });
          group.groupid -= 1;
        }
      });
      console.log("群组" + this.friendgroups);
    },
    addFriend(friendEmail) {
      AddFriend(this.$store.getters.id, friendEmail)
        .then(
          this.$message({
            type: "success",
            message: "好友申请发送成功!",
          })
        )
        .catch(() => {
          this.$message({
            type: "error",
            message: "已经发送过或目标不存在",
          });
        });
    },
  },
  async mounted() {
    await friendList(this.$store.getters.id).then((response) => {
      console.log(response);
      this.friends = response.data.friends;
      this.friendgroups = response.data.groups;
    });
  },
};
</script>
<style lang="scss" scoped>
.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}
.friend {
  &-container {
    margin: 30px;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
  }
  &-groupbox {
    padding: 20px;
    font-size: 50px;
  }
  &-line {
    width: 0;
    height: 76vh;
    border-left: 2px solid black;
  }
  &-box {
    margin-left: 50px;
    margin-top: 20px;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
  }
}
</style>