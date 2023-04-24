import request from '@/utils/request'

export function postFile(formData) {
    return request({
        url: '/api/file/postImgList',
        method: 'post',
        headers: {
            "Content-Type": "multipart/form-data; boundary=----WebKitFormBoundaryn8D9asOnAnEU4Js0"
        },
        data: formData


    })
}




//获取文件，返回二进制对象
export function getImg(path) {
    return request({
        url: '/api/file/getFile',
        method: 'get',
        params: {
            path: path,
        }
    })
}