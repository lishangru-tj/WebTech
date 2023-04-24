<template>
  <div :loading="edit_confirm_loading">
    <el-card class="intro-card-component">
      <!-- header -->
      <div class="icc_header">
        <span class="header_title">
          {{ title_name }}
        </span>
      </div>
      <div class="hint">创建于{{ act_time_created_label }}</div>

      <!-- tags -->
      <div>
        <el-tag v-for="(tag, i) in tags" :key="i" style="margin-right: 1em">
          {{ tag.tag_name }}
        </el-tag>
      </div>
      <!-- 活动状态 -->
      <div>
        <span class="font-bold">活动状态:</span>
        <span>{{ state_label }}</span>
      </div>
      <div>
        <span class="font-bold">活动概要:</span>
        <span>{{ summary }}</span>
      </div>
      <div class="detail_text">
        <div style="margin-bottom: 1em; font-weight: bold">活动的详细说明:</div>
        <span>
          {{ act_introText }}
        </span>
      </div>
      <!-- 重复 -->
      <!-- <div>
        <span style="margin-bottom: 1em; font-weight: bold">重复的间隔天数:</span>
        <span>{{ repeat_interval }}</span>
      </div> -->
      <!-- 活动时间展示 -->
      <div>
        <div>
          <span class="font-bold">开始时间:</span>
          <span>{{ start_time.label }}</span>
        </div>
        <div>
          <span class="font-bold">结束时间:</span>
          <span>{{ end_time.label }}</span>
        </div>

        <!-- 活动地点 -->
        <div>
          <span class="font-bold">活动地点:</span>
          <span>{{ address_name }}</span>
        </div>

        <!-- 详细地址 -->
        <div>
          <span class="font-bold">详细地址: </span>
          <span>{{ address_formatted }}</span>
        </div>
      </div>

      <!-- 活动配图展示 -->
      <div class="images">
        <el-image
          style="margin-right: 1em; height: 20em"
          v-for="(img, img_i) in image_urlList"
          :key="img_i"
          :src="img"
        >
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
      </div>

      <!-- 修改按钮，只有creator可按 -->

      <el-button
        v-if="UserIdentity == 'creator'"
        class="el-icon-edit edit_icon"
        @click="ClickEditActInfo"
      >
        修改
      </el-button>

      <el-divider style="margin-top: 1em"></el-divider>
      <!-- 参与人 -->
      <div class="projmembers_box">
        <!-- header -->
        <div class="pmb_header">
          <span class="hint" style="font-size: 1.2em">参与人</span>
          <span>{{ participant_num }}/{{ participate_limit }}</span>
          <span
            style="float: right; font-weight: bold"
            @click="isShow_dialog_adminAllMembers = true"
            >更多</span
          >
        </div>
        <!-- 成员展示 -->
        <div class="pmb_members">
          <!-- 点击邀请好友 -->
          <i
            v-if="UserIdentity == 'creator'"
            class="el-icon-circle-plus-outline"
            style="margin-top: 1em; font-size: 1.8em"
            @click="ClickShowInviteFriend"
          ></i>
          <div
            class="pmb_member_item"
            v-for="(member, index) in participant_list"
            :key="index"
          >
            <el-popover
              placement="bottom"
              :title="member.name"
              width="200"
              trigger="click"
            >
              <span class="avator_button_box" style="display: block">
                <el-button
                  @click="CheckUserDetail(member)"
                  icon="el-icon-search"
                  circle
                ></el-button>
              </span>
              <el-avatar
                slot="reference"
                :src="member.avatar"
                style="margin: 20px"
              >
                {{ member.name }}
              </el-avatar>
            </el-popover>
          </div>
        </div>
      </div>

      <!-- 活动参与者的全部展示 -->
      <el-dialog
        class="icc_adminMember_dialog"
        :v-if="isShow_dialog_adminAllMembers"
        :visible.sync="isShow_dialog_adminAllMembers"
      >
        <span class="icc_adminMember_dashBoard">
          <div
            style="
              margin-top: -1em;
              font-size: 2em;
              color: black;
              font-weight: bold;
            "
          >
            参与人员
          </div>
          <div class="hint">活动创建者</div>
          <div class="icca_creator_main">
            <el-avatar
              @click.native="CheckUserDetail(creator_data)"
              class="icca_creator_avatar"
              :src="creator_data.avatar"
            >
            </el-avatar>
            <span class="icca_proj_info">
              <span style="font-size: 1.2em; color: black">{{
                creator_data.name
              }}</span>
              <span class="hint">创建于{{ act_time_created_label }}</span>
            </span>
            <el-button
              style="margin-left: auto"
              type="primary"
              @click="CheckUserDetail(creator_data)"
            >
              查看</el-button
            >
          </div>

          <el-divider></el-divider>
          <div
            class="icca_memberline"
            v-for="(mem, index) in participant_list"
            :key="index"
          >
            <el-avatar
              @click.native="CheckUserDetail(mem)"
              class="icca_memerline_avatar"
              :src="creator_data.avatar"
            >
            </el-avatar>
            <span class="icca_memberline_info">
              <span style="font-size: 1.2em; color: black">{{ mem.name }}</span>
            </span>
            <el-button
              style="margin-left: auto"
              type="primary"
              @click="CheckUserDetail(mem)"
            >
              查看</el-button
            >
          </div>
        </span>
      </el-dialog>

      <!-- 报名状态下 -->
      <span v-if="state_val == 0">
        <!-- 报名人数展示-->
        <div>
          <h2>报名人数:{{ applicant_num }}</h2>
        </div>
        <!-- 报名与取消报名的按钮 -->
        <div v-if="!is_creator && !is_member">
          <!-- if 没有报名 -->
          <span v-if="!is_applicant">
            <el-button type="primary" @click="ClickapplyActivity"
              >报名</el-button
            >
          </span>
          <!-- else 已经报名 -->
          <span v-else>
            <h2>您已经报名该活动!</h2>
            <el-button type="primary" @click="ClickCancelApplyActivity"
              >取消报名</el-button
            >
          </span>
        </div>
        <!-- 创建人 才能 结束报名 -->
        <div v-if="is_creator">
          <span>
            <el-button type="text" @click="ClickCreatorCloseApply">
              停止报名
            </el-button>
          </span>
        </div>
      </span>

      <span v-if="state_val == 1">
        <h2>已结束报名！活动正在进行中</h2>
        <el-button
          v-if="UserIdentity == 'creator'"
          type="text"
          @click="ClickCreatorFinishAct"
          >结束活动</el-button
        >
      </span>

      <span v-if="state_val == 2">
        <h2>活动已经结束</h2>
      </span>

      <el-button
        v-if="UserIdentity == 'creator'"
        style="float: right; margin-top: -3em"
        class="el-icon-delete"
        @click="ClickDeleteAct"
        type="text"
      >
        删除活动
      </el-button>

      <span v-if="state_val == 2">
        <ReportBox
          selection="test"
          targetType="0"
          :activityId="activity_id"
          :name="title_name"
        />
      </span>

      <!-- 某个用户的详情查看 -->
      <el-dialog
        class="mac_user_detail_dialog"
        :v-if="isShow_dialog_user"
        :visible.sync="isShow_dialog_user"
        title="其他用户信息"
      >
        <div>
          <img :src="other_user_detail_data.avatar" style="height: 200px" />
        </div>
        <div>
          <span>用户名：{{ other_user_detail_data.name }}</span>
        </div>
        <div>
          <span>用户介绍：{{ other_user_detail_data.introduce }}</span>
        </div>
        <div>
          <span>联系邮箱：{{ other_user_detail_data.email }}</span>
        </div>


        <span v-if="other_user_detail_data.id != user_id">
          <el-button
            type="primary"
            @click="ClickAddFriend(other_user_detail_data.email)"
            style="margin-bottom: 1em; margin-top: 1em"
            >添加好友</el-button
          >

          <ReportBox
            selection="test"
            targetType="1"
            :userId="other_user_detail_data.id"
            :name="other_user_detail_data.name"
          />
        </span>
      </el-dialog>
    </el-card>

    <!-- 评价的卡片 -->
    <ActivityMarkCard
      style="margin-left: 1em"
      v-if="rate_val != -1 && state_val == 2"
      :activity_id="activity_id"
      :user_id="user_id"
      :hadrate="rated"
      :showVal="rate_val"
      @MarkActivity="ComponentsMarkActivity"
    ></ActivityMarkCard>
    <!-- 活动投票卡片 -->
    <el-col :span="12" v-if="is_member">
      <!-- 目前活动有投票-->
      <!--span v-if="hasVote"-->
      <!-- 展示投票 -->
      <el-button
        @click="
          updateTime();
          getPoll();
          calculatePercentage();
        "
        >查看本活动投票</el-button
      >
      <el-card>
        <span>待投票</span>
        <div v-for="(poll, index) in Polls" :key="index">
          <div v-if="poll.notVoted * (Date.parse(poll.deadline) - nowTime)">
            <vue-poll :key="index" v-bind="poll.option" @addvote="addVote" />
            <span v-if="poll.notCommitted">
              <el-button
                type="primary"
                @click="
                  poll.notCommitted = 0;
                  confirmVote();
                "
                >确认</el-button
              >
              <el-button @click="cancelVote">取消</el-button>
              <el-button
                style="margin-left: 50px"
                v-if="is_creator"
                @click="
                  pollToDelete = poll.poll_id;
                  deleteVote();
                "
                >删除投票</el-button
              >
            </span>
          </div>
        </div>
      </el-card>
      <el-card>
        <span>已投票</span>
        <div v-for="(poll, index) in Polls" :key="index">
          <div v-if="!poll.notVoted" style="margin-bottom: 30px">
            <h3>{{ poll.option[0].question }}</h3>
            <div
              v-for="(option, optindex) in poll.option[0].answers"
              :key="optindex"
            >
              <div
                style="
                  background-color: #e1e8ed;
                  margin-bottom: 10px;
                  height: 30px;
                "
                :style="{
                  width:
                    Math.round((option.votes / poll.totalVotes) * 100) + '%',
                }"
              >
                <span
                  v-text="
                    Math.round((option.votes / poll.totalVotes) * 100) + '%'
                  "
                  style="
                    padding-left: 10px;
                    font-weight: bold;
                    padding-right: 50px;
                    height: 30px;
                  "
                ></span>
                <span
                  style="line-height: 30px"
                  v-if="option.value == poll.selected"
                  v-html="option.text + ' ✔'"
                ></span>
                <span
                  style="line-height: 30px"
                  v-else
                  v-html="option.text"
                ></span>
              </div>
            </div>
            <el-button
              style="margin-left: 50px"
              v-if="is_creator"
              @click="
                pollToDelete = poll.poll_id;
                deleteVote();
              "
              >删除投票</el-button
            >
          </div>
        </div>
      </el-card>
      <el-card>
        <span>已结束投票</span>
        <div v-for="(poll, index) in Polls" :key="index">
          <div
            v-if="Date.parse(poll.deadline) - nowTime <= 0"
            style="margin-bottom: 30px"
          >
            <h3>{{ poll.option[0].question }}</h3>
            <div
              v-for="(option, optindex) in poll.option[0].answers"
              :key="optindex"
            >
              <div
                style="
                  background-color: #e1e8ed;
                  margin-bottom: 10px;
                  height: 30px;
                "
                :style="{
                  width:
                    Math.round((option.votes / poll.totalVotes) * 100) + '%',
                }"
              >
                <span
                  v-text="
                    Math.round((option.votes / poll.totalVotes) * 100) + '%'
                  "
                  style="
                    padding-left: 10px;
                    font-weight: bold;
                    padding-right: 50px;
                    height: 30px;
                  "
                ></span>
                <span
                  style="line-height: 30px"
                  v-if="option.value == poll.selected"
                  v-html="option.text + ' ✔'"
                ></span>
                <span
                  style="line-height: 30px"
                  v-else
                  v-html="option.text"
                ></span>
              </div>
            </div>
            <el-button
              style="margin-left: 50px"
              v-if="is_creator"
              @click="
                pollToDelete = poll.poll_id;
                deleteVote();
              "
              >删除投票</el-button
            >
          </div>
        </div>
      </el-card>
      <!--/span-->

      <span>
        <el-card v-if="is_creator">
          <!-- 创建人发起投票 -->
          <el-button @click="clickCreateVote" v-if="true" type="primary"
            >新建投票</el-button
          >
        </el-card>
      </span>

      <div></div>
    </el-col>

    <!-- 新建投票的dialog -->

    <el-dialog
      title="新建投票"
      :visible.sync="isShow_dialog_createVote"
      width="50%"
      :before-close="handleClose_creatVote"
      v-bind="$attrs"
      v-on="$listeners"
    >
      <el-form
        ref="newVote"
        :model="newVoteFrom"
        :rules="rules"
        size="medium"
        label-width="100px"
      >
        <el-form-item label="投票主题" prop="topic_text">
          <el-input
            v-model="newVoteFrom.topic_text"
            placeholder="请输入投票主题"
            clearable
            :style="{ width: '100%' }"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="截止时间" prop="deadline_time">
          <el-date-picker
            type="datetime"
            v-model="newVoteFrom.deadline_time"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            :style="{ width: '100%' }"
            placeholder="请选择截止时间"
            clearable
          >
          </el-date-picker>
        </el-form-item>
        <el-row>
          <el-form-item
            :label="'选项' + (option_i + 1)"
            v-for="(option, option_i) in newVoteFrom.option"
            :key="option_i"
            :prop="'option.' + option_i + '.option'"
            :rules="[
              {
                required: true,
                message: '请输入选项内容',
                trigger: 'blur',
              },
            ]"
          >
            <el-input
              v-model="newVoteFrom.option[option_i].option"
              :placeholder="'请输入选项' + (option_i + 1)"
              clearable
              :style="{ width: '80%' }"
            >
            </el-input>

            <el-popconfirm
              title="这是一段内容确定删除吗？"
              @onConfirm="createVote_removeOption(option_i)"
            >
              <el-button slot="reference" icon="el-icon-close">删除</el-button>
            </el-popconfirm>
          </el-form-item>

          <el-button @click="createVote_addOption">新增选项</el-button>
        </el-row>
        <el-form-item label-width="0" prop="multiple_choice">
          <el-radio-group v-model="newVoteFrom.multiple_choice" size="medium">
            <el-radio-button
              v-for="(item, index) in multiple_choiceOptions"
              :key="index"
              :label="item.value"
              :disabled="item.disabled"
              >{{ item.label }}</el-radio-button
            >
          </el-radio-group>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="confirm_createVote" type="primary"
          >确认创建</el-button
        >
        <el-button @click="cancel_createVote">取消</el-button>
      </span>
    </el-dialog>

    <!-- 修改活动信息dialog -->
    <el-dialog
      v-bind="$attrs"
      v-on="$listeners"
      title="修改活动信息"
      :before-close="handleClose_edit"
      :visible.sync="isShow_dialog_edit"
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
            <el-form-item label="详细说明" prop="detail_text">
              <el-input
                v-model="newact_form.detail_text"
                type="textarea"
                placeholder="请输入活动的详细说明"
                :autosize="{ minRows: 4, maxRows: 10 }"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="false">
            <el-form-item label="上传相关图片" prop="file_value">
              <el-upload
                ref="file_value"
                :file-list="file_valuefileList"
                :action="file_valueAction"
                multiple
                :before-upload="file_valueBeforeUpload"
                list-type="picture-card"
                accept="image/*"
              >
                <i class="el-icon-plus"></i>
                <div slot="tip" class="el-upload__tip">
                  只能上传不超过 2MB 的image/*文件
                </div>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div slot="footer">
        <el-button @click="CancelEdit">取消</el-button>
        <el-button type="primary" @click="ConfirmEdit">确定</el-button>
      </div>
      <span v-if="primitiveData != -1">
        <MapChoose
          :dialogShow.sync="innerVisible"
          @locationSure="locationSure"
          :primitiveData_comp="primitiveData"
        ></MapChoose>
      </span>
    </el-dialog>

    <!-- 邀请好友的dialog -->
    <el-dialog
      :v-if="showDialog_inviteFriend"
      :visible.sync="showDialog_inviteFriend"
      title="邀请好友参与活动"
    >
      <span>
        <el-tree
          :data="friend_tree_data"
          show-checkbox
          default-expand-all
          node-key="id"
          ref="tree"
          highlight-current
          props="{
          children: 'children',
          label: 'label'
        }"
        >
        </el-tree>
        <el-button @click="resetChecked">清空选中</el-button>
        <el-button type="primary" @click="InviteFriends"
          >确认邀请好友</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {
  deleteActivity,
  deleteActivityApply,
  getActApplicantList,
  getActDetail,
  getIsApplicant,
  getIsCreator,
  getIsMember,
  patchAct,
  patchActFinish,
  patchActStopApply,
  postActivityApply,
} from "@/api/activity";
import { friendList, InviteAct, AddFriend } from "@/api/friend";

import MapChoose from "@/components/MapChoose";
import ReportBox from "@/components/ReportBox";

import { getAllTag } from "@/api/tag";
import { getInfo } from "@/api/user";

import { getImg } from "@/api/file";

import { deletePoll, getPoll, getVote, postPoll, postVote } from "@/api/poll";
import { getoptions, putTotal } from "@/api/vote_option";
import { CodeToText } from "element-china-area-data";
import VuePoll from "vue-poll";
import ActivityMarkCard from "./markcard.vue";

const getTime = () => new Date().getTime();
export default {
  name: "ActivityPageOverView",
  components: { VuePoll, ActivityMarkCard, MapChoose, ReportBox },
  data() {
    return {
      /**
       * 用户个人信息
       */
      // creator,member
      UserIdentity: "",
      is_creator: false,
      is_applicant: false,
      is_member: false,

      // 用户对这个活动的评价
      rated: false, // 是否评价过
      rate_val: -1, // 评价分数

      /**
       * 活动信息
       */
      // 从api获取的活动详情，直接备份，后续修改时方便传递给后端
      api_activity_detail_bak: {},
      activity_id: "",
      title_name: "",
      address_name: "",
      address_formatted: "",
      // 重复
      repeat_interval: "",
      /**
       * 文本介绍
       */
      act_introText: "",
      // 一句话介绍
      summary: "",
      // tags
      tags: [],
      // 配图
      act_imgs: [],

      // 是否限制人数
      limit_capacity: true,
      // 活动创建时间
      act_time_created_label: "2022.1.1",
      // 活动的状态
      state_val: 0,

      // 创建人data
      creator_data: {},
      creator_id: "",

      /**
       * 参与人data
       */
      participant_list: [],
      // 参与人限制
      participate_limit: "",
      participant_num: "",
      // 当前报名人数
      applicant_num: 0,
      // 当前参与人数
      now_participant_num: 0,

      // 管理所有成员的dialog
      isShow_dialog_adminAllMembers: false,

      // 查看某个用户的dialog
      isShow_dialog_user: false,
      // 查看的这个用户的detail
      other_user_detail_data: {},

      /* 日期展示 */
      start_time: {},
      end_time: {},
      create_time_date: "",
      // date picker
      start_date_picker: "",
      end_date_picker: "",
      /*
      投票
      */
      // 要删除的投票
      pollToDelete: "",
      // 这个活动目前是否有投票,
      hasVote: "",
      // 选中的选项
      selectedOption: "",
      // 这个投票的选项与数据
      Polls: [],
      // 我是否能进行投票
      canIVote: "",
      // :key for update
      VotePollKey: 1,
      // 新建投票的isShow
      isShow_dialog_createVote: false,

      // 当前时间
      nowTime: "",
      /* 新建投票的表单 */
      newVoteFrom: {
        topic_text: undefined,
        deadline_time: null,
        option: [{ option: "" }, { option: "" }],
        multiple_choice: 1,
        activity_id: this.activity_id,
      },

      rules: {
        topic_text: [
          {
            required: true,
            message: "请输入投票主题",
            trigger: "blur",
          },
        ],
        deadline_time: [
          {
            required: true,
            message: "请选择截止时间",
            trigger: "change",
          },
        ],
        optionsGroupRules: {
          option: [
            {
              required: true,
              message: "请输入选项内容",
              trigger: "blur",
            },
          ],
        },

        multiple_choice: [
          {
            required: true,
            message: "不能为空",
            trigger: "change",
          },
        ],
      },
      multiple_choiceOptions: [
        {
          label: "单选",
          value: 1,
        },
        {
          label: "多选",
          value: 0,
        },
      ],

      // 创建者 修改信息的flag
      isShow_dialog_edit: false,
      edit_confirm_loading: false,

      // show地图
      innerVisible: false,

      // map:null,
      primitiveData: -1,
      /* 修改活动信息 */
      isShow_: true,
      newact_form: {
        title_name: undefined,
        summary: undefined,
        start_time: undefined,
        end_time: undefined,
        address_name: undefined,
        address_formatted: undefined,
        longitude: undefined,
        latitude: undefined,
        limit_capacity: undefined,
        capacity: undefined,
        repeat_interval: undefined,
        detail_text: undefined,
        file_value: null,
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

        detail_text: [],
      },
      // 可选tag
      optional_tags: [],
      file_valueAction: "https://jsonplaceholder.typicode.com/posts/",
      file_valuefileList: [],
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
      /** *图片 */
      image_baseList: [],
      image_urlList: [],
      images: "", // 字段值
      file_valueAction: "https://jsonplaceholder.typicode.com/posts/",
      file_valuefileList: [],
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

      /**
       * 邀请好友
       *
       */
      // 好友列表数据
      friends_data: [],
      friend_tree_data: [],
      showDialog_inviteFriend: false,
    };
  },
  created: function () {},
  methods: {
    updateTime() {
      this.nowTime = getTime();
    },
    /**添加好友 */
    async ClickAddFriend(friend_mail) {
      await AddFriend(this.$store.getters.id, friend_mail)
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
    /** 投票*/
    // api
    // 删除投票
    deleteVote() {
      const h = this.$createElement;
      this.$msgbox({
        title: "确定删除投票",
        showCancelButton: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        beforeClose: (action, instance, done) => {
          // 点击了 确认
          if (action === "confirm") {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = "执行中...";
            // 向后端接口请求
            console.log(this.pollToDelete);
            deletePoll(this.pollToDelete)
              .then()
              .catch((err) => {
                console.log("deletePoll:err", err);
              });
            // 静态测试，3s后完成操作
            setTimeout(() => {
              done();
              this.canIVote = false;
              setTimeout(() => {
                instance.confirmButtonLoading = false;
              }, 300);
            }, 500);
          } else {
            done();
          }
        },
      }).then((action) => {
        this.$message({
          type: "info",
          message: "action: " + action,
        });
      });
    },
    // api
    // 获取此时的投票信息
    async getPoll() {
      const polls = [];
      await getPoll(this.activity_id)
        .then((res) => {
          this.canIVote = true;
          res.data.forEach((ele) => {
            const answers = [];
            const optionOfPoll = [];
            let totalVotes = 0;
            console.log("ele.pollID", ele.pollID);
            getoptions(ele.pollID).then((res) => {
              res.data.forEach((ele) => {
                totalVotes = totalVotes + ele.voteNum;
                answers.push({
                  value: ele.optionID,
                  text: ele.optionName,
                  votes: ele.voteNum,
                });
                optionOfPoll.push(ele.optionID);
              });
              let notVoted = 1;
              getVote(this.user_id).then((res) => {
                let selected = 0;
                res.data.forEach((ele) => {
                  if (optionOfPoll.indexOf(ele) != -1) {
                    notVoted = 0;
                    selected = ele;
                  }
                });
                const option = [];
                option.push({ question: ele.topicText, answers: answers });

                polls.push({
                  poll_id: ele.pollID,
                  deadline: ele.deadline,
                  multiple_choice: ele.multipleChoice,
                  option: option,
                  activity_id: this.activity_id,
                  notCommitted: 1,
                  notVoted: notVoted,
                  selected: selected,
                  totalVotes: totalVotes,
                });
              });
            });
          });
          this.Polls = polls;
          console.log(polls);
        })
        .catch((err) => {
          console.log("geterr:" + err);
        });
    },
    // 暂时投票
    addVote(obj) {
      console.log(obj);
      this.selectedOption = obj.value;
    },
    // 确定投票，api
    confirmVote() {
      // 弹窗确认
      const h = this.$createElement;
      this.$msgbox({
        title: "确定本次投票",
        showCancelButton: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        beforeClose: (action, instance, done) => {
          // 点击了 确认
          if (action === "confirm") {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = "执行中...";
            // 向后端接口请求
            putTotal(this.selectedOption)
              .then()
              .catch((err) => {
                console.log("putTotal:err", err);
              });
            postVote(this.user_id, this.selectedOption)
              .then()
              .catch((err) => {
                console.log("postVote:err", err);
              });
            // 静态测试，3s后完成操作
            setTimeout(() => {
              done();
              this.canIVote = false;
              setTimeout(() => {
                instance.confirmButtonLoading = false;
              }, 300);
            }, 500);
          } else {
            done();
          }
        },
      }).then((action) => {
        this.$message({
          type: "info",
          message: "action: " + action,
        });
      });
    },

    // 撤回临时投票
    cancelVote() {
      this.$nextTick(() => {
        // 重新请求api，获取当前的投票情况

        this.VoteOptions = this.getPoll(this.poll_id);
        this.VotePollKey += 1;
        // this.$forceUpdate();
      });
    },

    // 新建投票
    clickCreateVote() {
      this.isShow_dialog_createVote = true;
    },
    // 新建投票diaglog 关闭前
    handleClose_creatVote() {
      this.edit_confirm_loading = false;
      this.isShow_dialog_createVote = false;
    },
    // 新增选项
    createVote_addOption() {
      this.newVoteFrom.option.push({ option: "" });
    },
    // 删除选项
    createVote_removeOption(option_i) {
      this.newVoteFrom.option.splice(option_i, 1);
    },

    // 确认新建投票
    confirm_createVote() {
      // api
      // 创建新的投票
      this.newVoteFrom.activity_id = this.activity_id;
      const post_data = this.newVoteFrom;
      console.log("post_data:", post_data);
      postPoll(post_data)
        .then((res) => {
          console.log("postPoll:res:", res),
            this.$message({
              type: "success",
              message: "投票创建成功",
            });
        })
        .catch((err) => {
          console.log("postPoll:err:", err);
        });

      this.edit_confirm_loading = true;
      // 全局loading模板
      const thisContent = this;
      const edit_loading = thisContent.$loading({
        lock: true,
        text: "创建新投票，请稍候...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.5)",
      });
      setTimeout(() => {
        this.isShow_dialog_createVote = false;
        this.edit_confirm_loading = false;
        edit_loading.close();
      }, 1000);
    },
    // 取消新建投票
    cancel_createVote() {
      this.isShow_dialog_createVote = false;
    },
    /**
     * 活动信息
     */
    // api
    // 获取活动详细综合信息
    async getActDetail(activity_id) {
      // api
      // 根据活动id，获取详情
      await getActDetail(activity_id, this.user_id)
        .then(async (res) => {
          console.log("api请求活动详情：", res);
          const act_detail = res.data.activity_detail;
          const activity_mark = res.data.activity_mark;
          // 直接备份
          this.api_activity_detail_bak = act_detail;
          // 活动修改表格的data
          this.newact_form.title_name = act_detail.title_name;
          this.newact_form.summary = act_detail.summary;
          this.newact_form.start_time = act_detail.start_time;
          this.newact_form.end_time = act_detail.end_time;
          this.newact_form.address_name = act_detail.address_name;
          this.newact_form.address_formatted = act_detail.address_formatted;
          this.newact_form.longitude = act_detail.longitude;
          this.newact_form.latitude = act_detail.latitude;

          this.newact_form.limit_capacity = act_detail.limit_capacity;
          this.newact_form.capacity = act_detail.capacity;
          this.newact_form.repeat_interval = act_detail.repeat_interval;

          this.newact_form.detail_text = act_detail.detail_text;
          this.newact_form.tags = res.data.activity_tags.map((x) => {
            // return parseInt(x.tag_id);
            return x.tag_id;
          });
          // 展示卡片的data，赋值
          this.address_name = act_detail.address_name;
          this.address_formatted = act_detail.address_formatted;

          this.participate_limit = act_detail.capacity;

          this.act_introText = act_detail.detail_text;
          this.title_name = act_detail.title_name;
          this.summary = act_detail.summary;
          this.creator_id = act_detail.creator_id;
          this.limit_capacity = act_detail.limit_capacity;
          this.state_val = act_detail.state;
          this.create_time_date = act_detail.create_time;
          this.act_time_created_label = this.create_time_date;
          this.tags = res.data.activity_tags;
          const start_time_date = new Date(act_detail.start_time);

          const end_time_date = new Date(act_detail.end_time);
          this.repeat_interval = act_detail.repeat_interval
            ? act_detail.repeat_interval
            : "不重复";

          // 评价
          // 是否评价过
          this.rated = activity_mark.length != 0;
          // 图片
          this.images = act_detail.images;
          console.log("this.images", act_detail.images);
          // this.GetImgByteUrl()
          this.GetImgUrl();

          if (this.rated) {
            this.rate_val = activity_mark.mark[0].mark;
          } else {
            this.rate_val = 0;
          }

          // 报名人数
          this.applicant_num = act_detail.applicant_num;
          // 参与人数
          this.participant_num = act_detail.participant_num;
          // 活动日期date

          this.start_time = {
            label:
              start_time_date.getFullYear() +
              "年" +
              (start_time_date.getMonth() + 1) +
              "月" +
              start_time_date.getDate() +
              "日 " +
              start_time_date.getHours() +
              "时" +
              start_time_date.getMinutes() +
              "分" +
              start_time_date.getSeconds() +
              "秒",
            year: start_time_date.getFullYear(),
            month: start_time_date.getMonth() + 1,
            day: start_time_date.getDate(),
            hour: start_time_date.getHours(),
            minute: start_time_date.getMinutes(),
            second: start_time_date.getSeconds(),
            exist: true,
          };
          // console.log("start_time", start_time);
          // console.log("start_time", this.start_time);
          // end time not exists
          if ("end_time" in act_detail) {
            this.end_time = {
              label:
                end_time_date.getFullYear() +
                "年" +
                (end_time_date.getMonth() + 1) +
                "月" +
                end_time_date.getDate() +
                "日 " +
                end_time_date.getHours() +
                "时" +
                end_time_date.getMinutes() +
                "分" +
                end_time_date.getSeconds() +
                "秒",
              year: end_time_date.getFullYear(),
              month: end_time_date.getMonth() + 1,
              day: end_time_date.getDate(),
              hour: end_time_date.getHours(),
              minute: end_time_date.getMinutes(),
              second: end_time_date.getSeconds(),
              exist: true,
            };
          } else {
            this.end_time = {
              label: "未定",
              year: "",
              month: "",
              day: "",
              hour: "",
              minute: "",
              second: "",
              exist: false,
            };
          }
          this.act_imgs = [
            "https://tse4-mm.cn.bing.net/th/id/OIP-C.5ABMjlXUlPCUHSj6xFdEZAHaE8?pid=ImgDet&rs=1",
            "https://tse4-mm.cn.bing.net/th/id/OIP-C.5ABMjlXUlPCUHSj6xFdEZAHaE8?pid=ImgDet&rs=1",
          ];

          // 创建者具体信息
          await getInfo(this.creator_id)
            .then((res) => {
              console.log("creator_info:res:", res);
              this.creator_data = res.data;
            })
            .catch((err) => {
              console.log("creator_info:err:", err);
            });
          // this.creator_data = {
          //   id: "p1_creator_ID",
          //   name: "p1_creator_NAME",
          //   avatar: "https://s1.ax1x.com/2022/11/13/zF8nrd.jpg",
          //   role: "creator",
          // };
          // 所有参与人员信息
          await getActApplicantList(this.activity_id)
            .then((res) => {
              console.log("getActApplicantList:res:", res);

              this.participant_list = res.data;
            })
            .catch((err) => {
              console.log("getActApplicantList:err:", err);
            });
        })
        .catch((err) => {
          console.log(err);
        })
        .finally(() => {});
    },

    // 点击修改,弹窗
    ClickEditActInfo() {
      // 跳转路由
      // let query_data = {
      //   id: this.activity_id,
      // };
      // this.$router.push({
      //   name: "ActivityEdit",
      //   query: query_data,
      // });
      this.GetAllTags();
      setTimeout(() => {
        this.getLngLatLocation();
      }, 100);
      this.isShow_dialog_edit = true;
    },
    // 点击删除
    ClickDeleteAct() {
      // 弹窗确认
      const h = this.$createElement;
      this.$msgbox({
        title: "彻底删除活动！",
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
          // 点击了 确认
          if (action === "confirm") {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = "执行中...";
            // 向后端接口请求
            await deleteActivity(this.user_id, this.activity_id)
              .then((res) => {
                console.log("deleteActivity:res:", res);
                this.$message({
                  type: "success",
                  message: "删除活动！",
                });
                // route
                this.$router.push({
                  name: "MineActivity",
                  // query: query_data,
                });
              })
              .catch((err) => {
                console.log("deleteActivity:err:", err);
                this.$message({
                  type: "error",
                  message: "删除失败！",
                });
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
    },

    /* 管理员降级ButtonClick*/
    ClickDemotemember(mem) {
      // 弹窗确认
      const h = this.$createElement;
      this.$msgbox({
        title: "取消管理员权限",
        message: h("p", null, [
          h("span", null, "将把管理员 "),
          h("i", { style: "color: teal" }, mem.name),
          h("span", null, " 降级为一般成员"),
        ]),
        // dangerouslyUseHTMLString: true,
        showCancelButton: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        beforeClose: (action, instance, done) => {
          // 点击了 确认
          if (action === "confirm") {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = "执行中...";
            // 向后端接口请求，修改管理员权限
            // 静态测试，3s后完成操作
            setTimeout(() => {
              done();
              setTimeout(() => {
                instance.confirmButtonLoading = false;
              }, 300);
            }, 3000);
          } else {
            done();
          }
        },
      }).then((action) => {
        this.$message({
          type: "info",
          message: "action: " + action,
        });
      });
    },

    // 查看某个user的 detail
    CheckUserDetail(user) {
      this.isShow_dialog_user = true;

      this.other_user_detail_data = {};
      this.other_user_detail_data = {
        id: user.id,
        name: user.name,
        avatar: user.avatar,
        email: user.email,
        introduce: user.introduce,
      };
      console.log("other_user_detail_data:", other_user_detail_data);
      this.other_user_detail_data = mem;
    },

    /* 修改活动信息 */
    // cancel
    CancelEdit() {
      this.isShow_dialog_edit = false;
    },
    // dialog 关闭事件
    handleClose_edit() {
      this.edit_confirm_loading = false;
      this.isShow_dialog_edit = false;
    },
    // confirm修改
    ConfirmEdit() {
      this.$refs["create_activity"].validate(async (valid) => {
        if (!valid) {
          return;
        } else {
          // api
          // 提交新的活动信息
          this.edit_confirm_loading = true;

          // 全局loading模板
          const thisContent = this;
          const edit_loading = thisContent.$loading({
            lock: true,
            text: "修改中，请稍候...",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.5)",
          });

          // 中间进行一系列的操作

          // 上传成功后关闭loading， 并显示成功
          // 转化得到需要的更新的data
          const patch_data = {
            activity_id: this.activity_id,
            title_name: this.newact_form.title_name,
            summary: this.newact_form.summary,
            start_time: this.newact_form.start_time,
            end_time: this.newact_form.end_time,
            address_name: this.newact_form.address_name,
            address_formatted: this.newact_form.address_formatted,
            longitude: this.newact_form.longitude,
            latitude: this.newact_form.latitude,

            limit_capacity: this.newact_form.limit_capacity,
            capacity: this.newact_form.capacity,
            // repeat: this.newact_form.repeat,
            repeat_interval: this.newact_form.repeat_interval,

            detail_text: this.newact_form.detail_text,
            // file_value: "",
          };
          console.log("patch_data,", patch_data);
          await patchAct(patch_data, this.newact_form.tags)
            .then((res) => {
              console.log("patchAct:res:", res);
              // 关闭修改页面，停止loading
              this.isShow_dialog_edit = false;
              this.edit_confirm_loading = false;
              edit_loading.close();
              // 重新请求活动detail以刷新
              this.getActDetail(this.activity_id);

              this.$message({
                type: "success",
                message: "信息修改成功",
              });
            })
            .catch((err) => {
              console.log("patchAct:err", err);
              this.$message({
                type: "error",
                message: "修改失败",
              });
            });
        }
      });
    },

    // 地区选择的change
    region_edit_change(val) {
      this.getCodeToText(null, this.newact_form.region);
    },
    // region data代码值转为名字
    getCodeToText(codeStr, codeArray) {
      if (codeStr === null && codeArray === null) {
        return null;
      } else if (codeArray === null) {
        codeArray = codeStr.split(",");
      }
      let area = "";
      switch (codeArray.length) {
        case 1:
          area += CodeToText[codeArray[0]];
          break;
        case 2:
          area += CodeToText[codeArray[0]] + "/" + CodeToText[codeArray[1]];
          break;
        case 3:
          area +=
            CodeToText[codeArray[0]] +
            "/" +
            CodeToText[codeArray[1]] +
            "/" +
            CodeToText[codeArray[2]];
          break;
        default:
          break;
      }
      this.newact_form.region_label = area;
      console.log(
        "this.newact_form.region_label",
        this.newact_form.region_label
      );
      console.log("this.newact_form.region", this.newact_form.region);
      return area;
    },
    file_valueBeforeUpload(file) {
      const isRightSize = file.size / 1024 / 1024 < 2;
      if (!isRightSize) {
        this.$message.error("文件大小超过 2MB");
      }
      const isAccept = new RegExp("image/*").test(file.type);
      if (!isAccept) {
        this.$message.error("应该选择image/*类型的文件");
      }
      return isRightSize && isAccept;
    },

    /** Tag */
    GetAllTags() {
      // get all tags
      getAllTag()
        .then((res) => {
          this.optional_tags = res.data;
          console.log("getAllTag:res:", res);
        })
        .catch((err) => {
          console.log("getAllTag:err:", err);
        });
    },
    TagGroupChange() {
      console.log("this.newact_form.tags", this.newact_form.tags);
    },
    // 用户是不是这个活动的创建者
    async Get_isCreator() {
      console.log("this.activity_id:", this.activity_id);
      await getIsCreator(this.user_id, this.activity_id)
        .then((res) => {
          console.log("IsCreator:res:", res);

          if (res.data.is_creator) {
            this.UserIdentity = "creator";
          }
          this.is_creator = res.data.is_creator;
        })
        .catch((err) => {
          console.log("IsCreator:err:", err);
        });
    },

    // 用户是不是这个活动的报名者
    async Get_isApplicant() {
      // console.log("this.activity_id:", this.activity_id);
      await getIsApplicant(this.user_id, this.activity_id)
        .then((res) => {
          console.log("Get_isApplicant:res:", res);
          this.is_applicant = res.data.is_applicant;
        })
        .catch((err) => {
          console.log("Get_isApplicant:err:", err);
        });
    },
    // 用户是不是这个活动的参与者
    async Get_isMember() {
      console.log("this.activity_id:", this.activity_id);
      await getIsMember(this.user_id, this.activity_id)
        .then((res) => {
          console.log("IsMember:res:", res);
          this.is_member = res.data.is_member;
        })
        .catch((err) => {
          console.log("IsMember:err:", err);
        });
    },

    // 用户点击报名活动
    ClickapplyActivity() {
      // 弹窗确认
      const h = this.$createElement;
      this.$msgbox({
        title: "确定报名",
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
          // 点击了 确认
          if (action === "confirm") {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = "执行中...";
            // 向后端接口请求
            // 拼凑post data
            const now = new Date();
            let apply_time = now.toLocaleString("zh-CN").replace(/[/]/, "-");
            apply_time = apply_time.replace(/[/]/, "-");
            const post_data = {
              user_id: this.user_id,
              activity_id: this.activity_id,
              apply_time: apply_time,
            };
            await postActivityApply(post_data)
              .then((res) => {
                console.log("postActivityApply:res:", res);
                this.$message({
                  type: "success",
                  message: "活动报名成功",
                });
                this.Get_isApplicant();
                this.applicant_num += 1;
              })
              .catch((err) => {
                console.log("postActivityApply:err:", err);
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
    },

    // 用户取消报名活动
    ClickCancelApplyActivity() {
      // 弹窗确认
      const h = this.$createElement;
      this.$msgbox({
        title: "您确定要取消报名？",
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
          // 点击了 确认
          if (action === "confirm") {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = "执行中...";
            // 向后端接口请求
            // 拼凑delete data
            const now = new Date();
            let apply_time = now.toLocaleString("zh-CN").replace(/[/]/, "-");
            apply_time = apply_time.replace(/[/]/, "-");
            const delete_data = {
              user_id: this.user_id,
              activity_id: this.activity_id,
            };
            await deleteActivityApply(
              delete_data.user_id,
              delete_data.activity_id
            )
              .then((res) => {
                console.log("deleteActivityApply:res:", res);
                this.$message({
                  type: "success",
                  message: "取消报名!",
                });
                this.$nextTick(() => {
                  this.Get_isApplicant();
                  this.applicant_num -= 1;
                });
              })
              .catch((err) => {
                console.log("deleteActivityApply:err:", err);
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
    },

    // 创建人 关闭活动报名,活动状态改变
    async ClickCreatorCloseApply() {
      // 弹窗确认
      const h = this.$createElement;
      this.$msgbox({
        title: "停止活动的报名通道",
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
          // 点击了 确认
          if (action === "confirm") {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = "执行中...";
            // 向后端接口请求
            // 拼凑data

            const patch_data = {
              activity_id: this.activity_id,
            };
            await patchActStopApply(patch_data)
              .then((res) => {
                console.log("creatorStopApply:res:", res);
                this.$message({
                  type: "success",
                  message: "活动报名停止",
                });
                this.$nextTick(() => {
                  this.getActDetail(this.activity_id);
                });
              })
              .catch((err) => {
                console.log("creatorStopApply:err:", err);
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
    },

    // 创建人 结束活动,活动状态改变
    async ClickCreatorFinishAct() {
      // 弹窗确认
      const h = this.$createElement;
      this.$msgbox({
        title: "确定结束活动？",
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
          // 点击了 确认
          if (action === "confirm") {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = "执行中...";
            // 向后端接口请求
            // 拼凑data

            const patch_data = {
              activity_id: this.activity_id,
            };
            await patchActFinish(patch_data)
              .then((res) => {
                console.log("creatorFinishAct:res:", res);
                this.$message({
                  type: "success",
                  message: "活动报名停止",
                });
                this.$nextTick(() => {
                  this.getActDetail(this.activity_id);
                });
              })
              .catch((err) => {
                console.log("creatorFinishAct:err:", err);
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
    },

    // 评分组件，confirm评分
    ComponentsMarkActivity(val) {
      // this.rated = false;
      this.getActDetail(this.activity_id);
    },

    /* 地图 */
    // 地图
    locationSure(val) {
      console.log("val", val);
      this.newact_form.address_formatted = val.address_formatted;
      this.newact_form.longitude = val.value[0];
      this.newact_form.latitude = val.value[1];

      this.primitiveData = val.value;
      // console.log("primitiveData:", this.primitiveData);
    },
    // 浏览器IP定位
    getLngLatLocation() {
      const that = this;
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
            // 逆向地理编码
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

    /** 图片 */
    // 根据this.images，请求后端获取到对应的图片bytes
    async GetImgByteUrl() {
      const images = this.images;
      const image_baseList = [];
      const image_urlList = [];
      if (images != "") {
        const image_paths = images.split(":");
        for (let i = 0; i < image_paths.length; i++) {
          const image_path = image_paths[i];
          if (image_path.length > 0) {
            let image_bytes;
            let image_url;

            await getImg(image_path)
              .then((res) => {
                image_bytes = res.data.bytes;
                // console.log("image_bytes", image_bytes);
                image_url = "data:image/png;base64," + image_bytes;
                image_baseList.push(image_bytes);
                image_urlList.push(image_url);
              })
              .catch((err) => {
                console.log("TestGetImg:err", err);
              });

            console.log("image_url", image_url);
            // console.log("image_bytes", image_bytes);
          }
        }
      }
      this.image_baseList = image_baseList;
      this.image_urlList = image_urlList;
    },

    // 切割images字符串，直接通过url访问图片
    GetImgUrl() {
      const images = this.images;
      const image_urlList = [];
      if (images != "") {
        const image_paths = images.split(":");

        for (let i = 0; i < image_paths.length; i++) {
          /** 直接通过url访问图片 */
          let image_url;
          const image_path = image_paths[i];

          if (image_path.length > 0) {
            image_url = process.env.VUE_APP_BASE_API + "/api" + image_path;

            console.log("image_url", image_url);
            image_urlList.push(image_url);
          }
        }
      }
      this.image_urlList = image_urlList;
    },

    /**
     * 邀请好友
     */
    // 邀请好友的弹窗显示
    ClickShowInviteFriend() {
      this.showDialog_inviteFriend = true;
      this.getUserFriends();
    },

    // 获取当前用户的好友
    async getUserFriends() {
      await friendList(this.user_id)
        .then((res) => {
          console.log("friendList:res", res);
          this.friends_data = res.data;
          const group_data = res.data.groups;
          const friend_data = res.data.friends;
          /**
           * 拼装tree数据
           */
          const friend_tree_data = [];
          group_data.forEach((group) => {
            const one_group_children = [];
            friend_data.forEach((f) => {
              if (f.groupid == group.groupid) {
                one_group_children.push({
                  id: f.id,
                  label: f.email,
                });
              }
            });
            friend_tree_data.push({
              id: group.groupid,
              label: group.name,
              children: one_group_children,
            });
          });

          this.friend_tree_data = friend_tree_data;
        })
        .catch((err) => {
          console.log("friendList:err", err);
        });
    },

    // 清空选中好友
    resetChecked() {
      this.$refs.tree.setCheckedKeys([]);
    },

    // 选中若干好友后，给所有好友发送参与活动的邀请
    async InviteFriends() {
      const chosen_nodes = this.$refs.tree.getCheckedNodes(true);
      // 邀请的好友个数不能超过当前空余的参与人数
      const friend_num = chosen_nodes.length;
      const participate_left = this.participate_limit - this.participant_num;
      if (friend_num == 0) {
        this.$alert("请至少选择一名好友发送邀请", "", {
          confirmButtonText: "确定",
        });
        return;
      }

      if (friend_num > participate_left) {
        this.$alert(
          "邀请的好友人数 " +
            friend_num +
            " 不能超过剩余可参与人数 " +
            participate_left,
          "人数超出活动限制",
          {
            confirmButtonText: "确定",
            callback: (action) => {
              this.$message({
                type: "info",
                message: `邀请好友失败`,
              });
            },
          }
        );
        return;
      }

      const friednId_list = chosen_nodes.map((x) => {
        return x.id;
      });
      // console.log("InviteAct:friednId_list:", friednId_list);

      await InviteAct(friednId_list, this.activity_id, this.user_id)
        .then((res) => {
          console.log("InviteAct:res:", res);
          this.$alert(
            "已向选中好友发送活动参与邀请，好友同意后可直接参与该活动",
            "邀请成功发送",
            {
              confirmButtonText: "确定",
              callback: (action) => {
                this.$message({
                  type: "success",
                  message: `成功发送活动参与邀请`,
                });
              },
            }
          );
          this.showDialog_inviteFriend = false;
          return;
        })
        .catch((err) => {
          console.log("InviteAct:err:", err);
        });
    },
  },
  computed: {
    state_label: {
      get() {
        switch (this.state_val) {
          case 0:
            return "正在报名";
          case 1:
            return "结束报名，正在进行中";
        }
        // return "123";
      },
    },
    /**
     * 用户个人信息
     */
    user_id: {
      get: function () {
        return this.$store.getters.id;
      },
    },
    user_name: {
      get: function () {
        return this.$store.getters.name;
      },
    },
  },
  mounted() {
    this.activity_id = this.$route.query.id;
    this.nowTime = getTime();
    // 根据传入的id，获取活动的详情
    this.getActDetail(this.activity_id);

    // 判断是否为这个活动的创建者
    this.Get_isCreator();

    // 判断是否为报名者
    this.Get_isApplicant();

    this.Get_isMember();
  },
};
</script>
<style lang="less" scoped>
.intro-card-component {
  margin: 1em;
  border-radius: 10px;
  .icc_header {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;

    .header_title {
      margin-right: auto;
    }
    .title_input {
      width: auto;
      margin-right: auto;
    }

    .set_project_time {
      margin-left: auto;
    }
    margin-bottom: 1em;
  }

  .detail_text {
    margin-bottom: 0.8em;
    margin-top: 0.8em;
    min-height: 3em;
  }
  .edit_icon {
    float: right;
    font-size: 1.5em;
    margin-top: -1.5em;
  }
  .projmembers_box {
    .pmb_header {
    }
    .pmb_members {
      display: flex;
    }
  }
  .icc_adminMember_dashBoard {
    .icca_creator_main {
      display: flex;
      align-items: center;
      margin-bottom: 1em;
      .icca_creator_avatar {
      }
      .icca_proj_info {
        display: flex;
        flex-direction: column;
        margin-left: 1em;
      }
    }
    .icca_memberline {
      display: flex;
      flex-direction: row;
      margin-bottom: 1em;
      .icca_memerline_avatar {
      }
      .icca_memberline_info {
        display: flex;
        flex-direction: column;
        margin-left: 1em;
      }
    }
  }
}
.hint {
  margin-bottom: 0.5em;
  margin-top: 0.5em;
  color: #8f8f8f;
  font-size: 0.8em;
}
.font-bold {
  font-weight: bold;
}
</style>


