package com.example.backend.controller;

import com.example.backend.Tools.FileUtils;
import com.example.backend.common.Result;
import com.example.backend.service.ActivityService;
import com.example.backend.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * @Author: 杨严
 * @Date: 2022/12/03/10:32 AM
 * @Description:
 */
@Api(tags = {"File"})
@RestController
@RequestMapping("file")
public class FileController {
    @Autowired
    ActivityService activityService;

    @Autowired
    ReportService reportService;

//    public static String BASE_FOLDER = "D:/Upload";
    public static String BASE_FOLDER = "/file";

    @ApiOperation("上传图片集")
    @PostMapping("/postImgList")
    public Result<Map<String, Object>> postImgList(
            @ApiParam(name = "uploadFile", value = "uploadFile", required = true)
            @RequestParam("uploadFile") MultipartFile[] uploadFile,
            @ApiParam(name = "folderPath", value = "folderPath", required = true)
            @RequestParam("folderPath") String folderPath,
            @ApiParam(name = "entity", value = "entity", required = false)
            @RequestParam("entity") String entity,
            @ApiParam(name = "id", value = "id", required = false)
            @RequestParam("id") Long id
    ) {

        folderPath = BASE_FOLDER + folderPath;
//        folderPath = folderPath;
        Map<String, Object> result_map = new HashMap<>();
        List<Map<String, Object>> file_list = new ArrayList<>();

        try{
            //for every file
            for (MultipartFile multipartFile : uploadFile) {
                //map for one file
                String file_name = id.toString()+"-"+multipartFile.getOriginalFilename();
                Map<String, Object> file_map = new HashMap<>();
                file_map.put("name", file_name);

                if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
                    file_map.put("isEmpty", true);
                    file_map.put("upLoadSuccess", false);
                } else {
                    //判断非空
                    file_map.put("isEmpty", false);

                    try {
                        byte[] bytes = multipartFile.getBytes();
                        //要存入本地的地址放到path里面

                        Path path = Paths.get(folderPath);
                        //如果没有files文件夹，则创建
                        if (!Files.isWritable(path)) {
                            Files.createDirectories(path);
                        }
                        String extension = FileUtils.getFileExtension(multipartFile);  //获取文件后缀
                        FileUtils.getFileByBytes(bytes, folderPath, file_name);
                        file_map.put("extension", extension);

                        file_map.put("upLoadSuccess", true);

                    } catch (Exception e) {
                        file_map.put("upLoadSuccess", false);
                        e.printStackTrace();
                    }
                }

                file_list.add(file_map);


            }

            result_map.put("file_list:", file_list);
            System.out.println("result map:" + result_map);

            /**活动图片的绑定*/
            if (entity!=null && entity.equals("activity") && id!=null){
                String images="";
                /**图片路径字符串*/
                for (Map<String, Object> file_map:file_list){
                    String name=(String) file_map.get("name");

                    String one_imge="/activity/illus/"+name;
                    images=images+":"+one_imge;
                }
                activityService.PatchActImages(images,id);
            }

            /** 举报单图片的绑定 */
            if (entity!=null && entity.equals("report") && id!=null){
                String images="";
                /**图片路径字符串*/
                for (Map<String, Object> file_map:file_list){
                    String name=(String) file_map.get("name");

                    String one_imge="/report/illus/"+name;
                    images=images+":"+one_imge;
                }
                reportService.PatchReportImages(images,id);

            }

            return Result.success(result_map);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "postImgList FAILED");

        }

    }


    @ApiOperation("上传一个文件")
    @PostMapping("/postOneFile")
    public Result<Map<String, Object>> PostOneFile(
            @ApiParam(name = "uploadFile", value = "uploadFile", required = true)
            @RequestParam("uploadFile") MultipartFile uploadFile,
            @ApiParam(name = "folderPath", value = "folderPath", required = true)
            @RequestParam("folderPath") String folderPath,
            @ApiParam(name = "entity", value = "entity", required = false)
            @RequestParam("entity") String entity,
            @ApiParam(name = "id", value = "id", required = false)
            @RequestParam("id") Long id
    ) {
        folderPath = BASE_FOLDER + folderPath;
        Map<String, Object> result_map = new HashMap<>();

        //map for one file
        String file_name = uploadFile.getOriginalFilename();
        Map<String, Object> file_map = new HashMap<>();
        file_map.put("name", file_name);

        if (Objects.isNull(uploadFile) || uploadFile.isEmpty()) {
            file_map.put("isEmpty", true);
            file_map.put("upLoadSuccess", false);
        } else {
            //判断非空
            file_map.put("isEmpty", false);

            try {
                byte[] bytes = uploadFile.getBytes();
                //要存入本地的地址放到path里面

                Path path = Paths.get(folderPath);
                //如果没有files文件夹，则创建
                if (!Files.isWritable(path)) {
                    Files.createDirectories(path);
                }
                String extension = FileUtils.getFileExtension(uploadFile);  //获取文件后缀
                FileUtils.getFileByBytes(bytes, folderPath, file_name);
                file_map.put("extension", extension);

                file_map.put("upLoadSuccess", true);

            } catch (Exception e) {
                file_map.put("upLoadSuccess", false);
                e.printStackTrace();
            }
        }



        result_map.put("file_map:", file_map);
        System.out.println("result map:" + result_map);


        return Result.success(result_map);
    }


    @ApiOperation("获取对应路径的文件")
    @GetMapping(value = "/getFile")
    public Result<Map<String, Object>> GetImg(
            @ApiParam(name = "path", value = "path", required = true)
            @RequestParam("path") String path
    ) throws IOException {

        path = BASE_FOLDER + path;
        Map<String, Object> result_map = new HashMap<>();
        result_map.put("path", path);
        try {
            byte[] bytes = FileUtils.getBytesByFile(path);
            System.out.println("result_map:" + result_map);
            System.out.println(bytes);
            result_map.put("bytes", bytes);
        } catch (Exception e) {
            return Result.fail(HttpStatus.EXPECTATION_FAILED.value(), "GetImgList FAILED");

        }
        return Result.success(result_map);

    }


}
