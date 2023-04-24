import request from '@/utils/request'

export function getAllRubbish (userId) {
  return request({
    url: '/api/dustbin/findAllRubbish',
    method: 'get',
    params:{
      userId: userId
    }
  })
}

export function smashOneRubbish (id) {
  return request({
    url: '/api/dustbin/smashOneRubbish',
    method: 'delete',
    params: { id: id }
  })
}

export function smashAllRubbish (userId) {
  return request({
    url: '/api/dustbin/clearDustbin',
    method: 'delete',
    params:{
      userId: userId
    }
  })
}

export function restoreOneRubbish (id) {
  return request({
    url: '/api/dustbin/restoreOneRubbish',
    method: 'patch',
    params: { id: id }
  })
}