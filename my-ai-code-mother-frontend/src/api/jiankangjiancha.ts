// @ts-ignore
/* eslint-disable */
import request from '@/utils/request'

/** 健康检查 健康检查 GET /health/ */
export async function healthCheck(options?: { [key: string]: any }) {
  return request<API.BaseResponseString>('/health/', {
    method: 'GET',
    ...(options || {}),
  })
}
