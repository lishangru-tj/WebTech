<template>
  <div class="star_view">
    <div
      id="star"
      :ref="star_ref"
      :class="{
        unActive: !isActive_,
        Active: isActive_,
      }"
    ></div>
  </div>
</template>

<script>
export default {
  name: "star",
  data() {
    return {
      starDOM: "",
      isActive_: false,
      ViewListVal: {},
      emitVal: {},
    };
  },
  props: {
    isActive: {
      type: Boolean,
      required: true,
      default: () => {
        return false;
      },
    },
    star_ref: {
      type: String,
      required: true,
      default: () => {
        return "";
      },
    },
    eventName: {
      type: String,
      required: true,
      default: () => {
        return "";
      },
    },
    otherStaff: {
      type: Object,
    },
    valName: {
      type: String,
    },
  },
  mounted() {
    this.isActive_ = this.isActive;
    this.starDOM = this.$refs[`${this.star_ref}`];
    // console.log(starDOM);
    this.starDOM.addEventListener("click", this.ClickStar);
    // console.log(this.otherStaff);
    // this.showIsActive();
  },
  destroyed() {
    //监听事件解绑
    this.starDOM.removeEventListener("click", this.ClickStar);
  },
  methods: {
    //click监听事件
    ClickStar() {
      if (!this.isActive_) {
        let classList = this.starDOM.classList.value;
        this.starDOM.classList.add("fill");
        setTimeout(() => {
          this.starDOM.classList.value = classList;
          this.isActive_ = true;
          this.EmitEvent();
        }, 200);
      } else {
        this.isActive_ = false;
        this.EmitEvent();
      }
    },
    //show
    showIsActive() {
      console.log(this.isActive_);
      console.log(this.star_ref);
    },
    //emit event
    EmitEvent() {
      if (this.valName == "SetViewListVal") {
        this.SetViewListVal();
      }
      this.$emit(this.eventName, this.emitVal);
    },
    //设置ViewList的返回值
    SetViewListVal() {
      this.ViewListVal = {
        isActive: this.isActive_,
        content_index: this.otherStaff.content_index,
        line_index: this.otherStaff.line_index,
      };
      this.emitVal = this.ViewListVal;
    },
  },
};
</script>

<style>
@keyframes banuce {
  0% {
    transform: scale(0.8);
  }
  100% {
    transform: scale(1.35);
  }
}
@keyframes circle {
  0% {
    transform: scale(0.2);
    opacity: 0.8;
  }
  100% {
    transform: scale(1.5);
    opacity: 1;
  }
}
@keyframes show {
  0% {
    opacity: 1;
  }
}
.wrap {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
#star {
  display: inline-block;
  width: 30px;
  height: 30px;
  background-repeat: no-repeat;
  background-size: 100%;
  background-position: center;
}
.unActive {
  background-image: url(../../icons/img/star.png);
}
.Active {
  background-image: url(../../icons/img/star_active.png) !important;
}
.fill {
  position: relative;
  background-image: url(../../icons/img/star_active.png) !important;

  animation: banuce 0.2s ease 0s 2 alternate;
}
.fill::before {
  opacity: 0;
  content: "";
  position: absolute;
  left: 50%;
  top: 50%;
  width: 10%;
  height: 10%;
  transform: translate(-50%, -50%);
  border-radius: 50%;
  background: transparent;
  box-shadow: 15px -15px 0 #fed001, 15px 15px 0 #fed001, -15px -15px 0 #fed001,
    -15px 15px 0 #fed001;
  animation: show 0.2s steps(1, end) 0s 1;
}
.fill::after {
  opacity: 0;
  content: "";
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  animation: circle 0.3s ease 0.02s 1 alternate;
  border-radius: 50%;
  background: rgba(254, 208, 1, 0.1);
}
</style>


