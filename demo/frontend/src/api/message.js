import request from '@/utils/request'

export function getSystemInfo(userId) {
    return request({
        url: '/api/notice/getSystemNoticeByUserID',
        method: 'get',
        params: {
            userId: userId
        }
    })
}

export function getActivityInfo(userId) {
    return request({
        url: '/api/notice/getActivityNoticeByUserID',
        method: 'get',
        params: {
            userId: userId
        }
    })
}

export function getItemInfo(userId) {
    return request({
        url: '/api/notice/getItemNoticeByUserID',
        method: 'get',
        params: {
            userId: userId
        }
    })
}
//好友通知
export function getFriendRequest(userId) {
    return request({
        url: '/api/notice/getFriendRequest',
        method: 'get',
        params: {
            userId: userId
        }
    })
}

export function modifyNoticeStatus(userId, noticeId) {
    return request({
        url: '/api/notice/modifyNoticeStatus',
        method: 'patch',
        params: {
            userId: userId,
            noticeId: noticeId
        }
    })
}

export function modifyItemNoticeStatus(itemNoticeId) {
    return request({
        url: '/api/notice/modifyItemNoticeStatus',
        method: 'patch',
        params: {
            itemNoticeId: itemNoticeId
        }
    })
}
export function rejectFriendRequest(userid, friendid) {
    return request({
        url: '/api/notice/rejectFriendRequest',
        method: 'patch',
        params: {
            userid: userid,
            friendid: friendid
        }
    })
}
export function acceptFriendRequest(userid, friendid) {
    return request({
        url: "/api/notice/acceptFriendRequest",
        method: 'patch',
        params: {
            userid: userid,
            friendid: friendid
        }
    })
}

// 删除user notice
export function deleteUserNotice(user_id, notice_id) {
    return request({
        url: "/api/notice/deleteUserNotice",
        method: 'delete',
        params: {
            user_id: user_id,
            notice_id: notice_id
        }
    })
}

// 删除item notice
export function deleteItemrNotice(item_notice_id) {
    return request({
        url: "/api/notice/deleteItemNotice",
        method: 'delete',
        params: {
            item_notice_id: item_notice_id,
        }
    })
}

// 删除friend request
export function deleteFriendRequest(friend_request_id) {
    return request({
        url: "/api/notice/deleteFriendRequest",
        method: 'delete',
        params: {
            friend_request_id: friend_request_id,
        }
    })
}


