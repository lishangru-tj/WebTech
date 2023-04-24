import request from '@/utils/request'

export function getAnalyzeData (userId, year, month) {
  return request({
    url: '/api/dataAnalyze/findAnalysisData',
    method: 'get',
    params: {
      userId: userId,
      year: year,
      month: month
    }
  })
}