import request from '@/utils/request'

// 获取投票
export function getoptions(poll_id) {
  return request({
    url: '/api/vote_option/getoptions/' + poll_id.toString(),
    method: 'get',
    params: {}
  })
}
export function putTotal(option_id) {
  return request({
    url: '/api/vote_option/putTotal/' + option_id.toString(),
    method: 'put',
    params: {}
  })
}
