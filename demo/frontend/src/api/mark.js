import request from '@/utils/request'

//新建一个用户间评价
export function postUserMark(param) {
    return request({
        url: '/api/usermark/postUserMark',
        method: 'post',
        data: param,
        headers: {
            TokenValue: '',
        }
    })
}

//新建一个用户对活动的评价
export function postActMark(param) {
    return request({
        url: '/api/activitymark/postActMark',
        method: 'post',
        data: param,
        headers: {
            TokenValue: '',
        }
    })
}
