import request from '@/utils/request'

export function getAllSortedTask (userId) {
  return request({
    url: '/api/classification/getOneUserAllSortedTask',
    method: 'get',
    params: { userId: userId }
  })
}

export function postOneNewClassification (userId, classificationTitle) {
  return request({
    url: '/api/classification/postOneClassification',
    method: 'post',
    params: {
      userId: userId,
      classificationTitle: classificationTitle
    }
  })
}



export function getAllClassificationTitle (userId) {
  return request({
    url: '/api/classification/getOneUserAllClassificationTitle',
    method: 'get',
    params: { userId: userId }
  })
}

export function deleteOneClassification (userId, classificationTitle) {
  return request({
    url: '/api/classification/deleteOneClassification',
    method: 'delete',
    params: {
      userId: userId,
      classificationTitle: classificationTitle
    }
  })
}