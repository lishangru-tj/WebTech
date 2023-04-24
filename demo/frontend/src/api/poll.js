import request from '@/utils/request'

// 获取投票
export function getPoll(acitivity_id) {
  return request({
    url: '/api/poll/getPoll/' + acitivity_id.toString(),
    method: 'get',
    params: {}
  })
}
// 删除投票
export function deletePoll(poll_id) {
  return request({
    url: '/api/poll/deletePoll/' + poll_id.toString(),
    method: 'delete',
    params: {}
  })
}
export function getVote(userID) {
  return request({
    url: '/api/vote/getvote/' + userID.toString(),
    method: 'get',
    params: {}
  })
}
export function postPoll(param) {
  return request({
    url: '/api/poll/postPoll',
    method: 'post',
    data: param,
  })
}
export  function postVote(userId,option_id){
  return request({
    url: '/api/vote/postvote',
    method: 'post',
    params: {
      userId: userId,
      option_id: option_id
  },
  })
}

