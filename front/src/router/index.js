import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/login/register.vue'),
    hidden: true
  },
  {
    path: '/forget',
    component: () => import('@/views/login/forget.vue'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },




]
export const asyncRoutes = [
  {
    path: '/',
    component: Layout,
    // redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard', roles: ['client'] }
    }],
    hidden: false
  },
  {
    path: '/calendarView',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'calendarView',
        component: () => import('@/views/calendarView/index'),
        meta: { title: '日程', icon: 'schedule', roles: ['client'] }
      }
    ],
  },

  {
    path: '/viewList',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'viewList',
        component: () => import('@/views/viewList/index'),
        meta: { title: '视图', icon: 'viewlist', roles: ['client'] }
      }
    ],
  },


  {
    path: '/dataReport',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'DataReport',
        component: () => import('@/views/dataReport/index'),
        meta: { title: '数据分析', icon: 'dataanalysis', roles: ['client'] }
      }
    ],
  },
  {
    path: '/dustbin',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Dustbin',
        component: () => import('@/views/dustbin/index'),
        meta: { title: '回收站', icon: 'trashbin', roles: ['client'] }
      }
    ],
  },
  {
    path: '/activityIndex',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'ActivityIndex',
        component: () => import('@/views/activity/activityIndex'),
        meta: { title: '活动广场', icon: 'activityindex', roles: ['client'] }
      }
    ],
  },

  {
    path: '/mineActivity',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'MineActivity',
        component: () => import('@/views/activity/mineActivity'),
        meta: { title: '我的活动', icon: 'activity', roles: ['client'] }
      }
    ],
  },
  {
    path: '/activityPage',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'ActivityPage',
        component: () => import('@/views/activity/activityPage'),
        meta: { title: '活动页面', icon: 'activity', roles: ['client'] }
      }
    ],
    hidden: true,
  },
  {
    path: '/message',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'message',
        component: () => import('@/views/message/index'),
        meta: { title: '消息', icon: 'message', roles: ['client'] }
      }
    ],
  },


  {
    path: '/user',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'User',
        component: () => import('@/views/user/index'),
        meta: { title: '个人信息', icon: 'user', roles: ['client', 'admin'] }
      }
    ],
    // hidden: true
  },


  {
    path: '/friend',
    component: Layout,
    children: [{
      path: 'index',
      name: 'Friend',
      component: () => import('@/views/friend/index'),
      meta: { title: '好友', icon: 'user', roles: ['client'] }
    }
    ],
    hidden: true
  },

  {
    path: '/adminConsole',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'AdminConsole',
        component: () => import('@/views/adminConsole/index'),
        meta: { title: '管理员控制台', icon: 'user', roles: ['admin'] }
      }
    ],
  },
  {
    path: '/adminExamineUser',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'AdminExmaineUser',
        component: () => import('@/views/adminExamine/ExamineUser'),
        meta: { title: '审核用户', icon: 'user', roles: ['admin'] }
      }
    ],
    // hidden: true
  },
  {
    path: '/adminExamineActivity',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'AdminExamineActivity',
        component: () => import('@/views/adminExamine/ExamineActivity'),
        meta: { title: '审核活动', icon: 'user', roles: ['admin'] }
      }
    ],
    // hidden: true
  },
  {
    path: '/detailExamineUser',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'DetailExamineUser',
        component: () => import('@/views/adminExamine/ExamineUser/DetailUserExamine'),
        meta: { title: '详细审核用户', icon: 'user', roles: ['admin'] }
      }
    ],
    hidden: true
  },
  {
    path: '/detailExamineActivity',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'DetailExamineActivity',
        component: () => import('@/views/adminExamine/ExamineActivity/DetailActivityExamine'),
        meta: { title: '详细审核活动', icon: 'user', roles: ['admin'] }
      }
    ],
    hidden: true
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }

]
const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
