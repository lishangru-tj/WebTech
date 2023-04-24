package com.example.backend.service;

import com.example.backend.entity.Report;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Author: 杨严
 * @Date: 2022/12/13/9:02 AM
 * @Description:
 */
@Service
public interface ReportService {

    /**依据条件筛选，获取举报单列表 */
    Map<String,Object> getReportList(String state, String target_type,String sortByTime);

    /** 新增一条举报单*/
    Map<String,Object> addReport(Report report);

    /** 获取一个举报单详情*/
    Map<String,Object> getReport(Long report_id);


    /** 修改举报单状态 */
    Map<String,Object> changeReportState(String state,String handle_operation,Long admin_id,Long report_id,Date handle_time);

    /**获取具体状态的举报单数量 */
    Map<String,Object> getReportListNumByState(String state);


    /**
     * 根据举报单信息 进行处理
     * */
    Map<String,Object> handleReport(Report report);



    /**举报单的images，覆盖*/

    String PatchReportImages(String images,Long report_id);


}
