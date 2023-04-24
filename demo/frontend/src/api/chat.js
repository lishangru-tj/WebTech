import request from '@/utils/request'

// 获取活动所有的参与者
export function getActivityAllPaticipant(activityId) {
    return request({
        url: '/api/activity/getActApplicantList',
        method: 'get',
        params: {
            activity_id: activityId
        }
    })
}

// 获取活动所有的聊天信息
export function getActivityAllMessage(activityId) {
    return request({
        url: '/api/chat/getActivityAllMessage',
        method: 'get',
        params: {
            activityId: activityId
        }
    })
}

// 发送聊天信息
export function sendMessage(activityId, senderId, status, type, sendTime, content) {
    return request({
        url: '/api/chat/addMessage',
        method: 'post',
        params: {
            activityId: activityId,
            senderId: senderId,
            status: status,
            type: type,
            sendTime: sendTime,
            content: content
        }
    })
}

// 获取活动所有的留言板信息
export function getActivityAllMessageBoard(activityId) {
    return request({
        url: '/api/chat/getActivityAllMessageBoard',
        method: 'get',
        params: {
            activityId: activityId,
        }
    })
}

// 发送留言板信息
export function sendMessageBoard(activityId, userId, content, createTime) {
    return request({
        url: '/api/chat/addMessageBoard',
        method: 'post',
        params: {
            activityId: activityId,
            userId: userId,
            content:content,
            createTime:createTime
        }
    })
}
// 删除留言板消息
export function deleteMessageBoard(message_board_id) {
    return request({
        url: '/api/chat/deleteMessageBoard',
        method: 'delete',
        params: {
            message_board_id: message_board_id,
        }
    })
}

