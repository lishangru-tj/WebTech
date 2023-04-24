import request from '@/utils/request'

export function getAuditLog(state) {
  return request({
    url: '/api/report/getNumByState',
    method: 'get',
    params: {
      state: state
    }
  })
}

export function getAllActivityReportList() {
  return request({
    url: '/api/report/getReports',
    method: 'get',
    params: {
      sortByTime: 'desc',
      state: 2,
      target_type: 0
    }
  })
}

export function getAllUserReportList() {
  return request({
    url: '/api/report/getReports',
    method: 'get',
    params: {
      sortByTime: 'desc',
      state: 2,
      target_type: 1
    }
  })
}

export function getOneReport(reportId) {
  return request({
    url: '/api/report/getOneReport',
    method: 'get',
    params: {
      report_id: reportId
    }
  })
}

export function postOneReport(reportObj) {
  return request({
    url: '/api/report/postOneReport',
    method: 'post',
    data: reportObj
  })
}

export function patchOneReport(reportObj) {
  return request({
    url: '/api/report/patchOneReport',
    method: 'patch',
    data: reportObj,
    params: reportObj,
  })
}

export function getOneActivity(activityId){
  return request({
    url:'/api/activity/adminGetActivityById',
    method: 'get',
    params:{
      activity_id: activityId
    }
  })
}

export function getOneUser(userId){
  return request({
    url:'/api/user/adminGetUserById',
    method: 'get',
    params:{
      userId: userId
    }
  })
}