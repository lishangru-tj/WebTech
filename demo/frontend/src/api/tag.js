import request from '@/utils/request'

// 获取所有tag
export function getAllTag() {
    return request({
        url: '/api/tag/getAllTags/',
        method: 'get',
        params: {}
    })
}
export function postActTags(activity_id, tag_list, activity) {
    return request({
        url: '/api/tag/setActTags/' + activity_id,
        method: 'post',
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: {
            tag_list:tag_list,
            activity:activity
        }
    })
}
