import request from '@/utils/request'

export function getTaskById (taskId) {
  return request({
    url: '/api/task/findOneTaskAndRelative/' + taskId,
    method: 'get',
  })
}

// export function getTaskListByMonth (year, month) {
//   return request({
//     url: '/api/task/findmonth',
//     method: 'get',
//     params: {
//       year: year,
//       month: month
//     }
//   })
// }

export function getAllTaskAndRelative (userId) {
  return request({
    url: '/api/task/findAllTaskAndRelative/' + userId,
    method: 'get',
  })
}

export function postOneNewTask (obj) {
  return request({
    url: '/api/task/addOneNewTask',
    method: 'post',
    data: obj
  })
}

export function patchOneTask (obj) {
  return request({
    url: '/api/task/patchOneTask',
    method: 'patch',
    data: obj
  })
}

export function postOneSonTask(taskId, sonTaskObj){
  return request({
    url:'/api/task/addOneSonTask',
    method:'post',
    params:{
      taskId: taskId
    },
    data: sonTaskObj
  })
}
