import request from '@/utils/request'

export function friendList(id) {
  return request({
    url: '/api/friend',
    method: 'get',
    params: { id }
  })
}
export function friendManage(data) {
  return request({
    url: '/api/friend/manage',
    method: 'post',
    data
  })
}
export function AddFriend(userid, email) {
  return request({
    url: '/api/friend/sendRequest',
    method: 'patch',
    params: {
      userid: userid,
      friendEmail: email
    }
  })
}

export function InviteAct(friendId_list, activity_id, user_id) {
  return request({
    url: '/api/friend/inviteAct',
    method: 'post',
    params: {
      friendId_list: friendId_list.join(","),
      activity_id: activity_id,
      user_id: user_id,
    }
  })
}

