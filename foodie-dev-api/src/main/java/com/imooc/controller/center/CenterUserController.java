package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "用户信息", tags = {"用户信息相关接口"})
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {
    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(@RequestParam String userId, @RequestBody @Valid CenterUserBO centerUserBO, BindingResult result
            , HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            Map<String, String> errorsMap = getErrors(result);
            return IMOOCJSONResult.errorMap(errorsMap);
        }

        Users user = centerUserService.updateUserInfo(userId, centerUserBO);
        Users newUser = setPropertiesNull(user);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(newUser), true);
        // TODO 后续会增加TOKEN,整合进分布式会话
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(
            @RequestParam @ApiParam(name = "userId", value = "用户Id") String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        //定义头像保存的地址
//        String fileSpace = IMAGE_USER_FACE_PATH;
        String fileSpace = fileUpload.getImageUserFaceLocation();
        //在路径下为每个用户增加一个userId，区分不同用户上传
        String uploadPathPrefix = File.separator + userId;
        //开始文件上传
        if (file == null) {
            return IMOOCJSONResult.errorMsg("文件不能为空！");
        }
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            return IMOOCJSONResult.errorMsg("文件名错误！");
        }
        String[] fileNameArr = filename.split("\\.");
        //获取文件后缀名
        String suffix = fileNameArr[fileNameArr.length - 1];

        if (!suffix.equalsIgnoreCase("png") && !suffix.equalsIgnoreCase("jpg") && !suffix.equalsIgnoreCase("jpeg")) {
            return IMOOCJSONResult.errorMsg("图片格式不正确！");
        }
        //文件名重组，格式：face-{userId}.png
        //覆盖式上传
//            String newFileName="face-"+userId+"."+suffix;
        //增量式
        String newFileName = "face-" + userId + "." + suffix;
        //上传的头像最终保存的位置
        String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
        File outFile = new File(finalFacePath);
//        if (!outFile.getParentFile().exists()) {
//            outFile.getParentFile().mkdirs();
//        }

        //文件输出保存到目录
        try (
                FileOutputStream fos = new FileOutputStream(outFile);
                InputStream fis = file.getInputStream()
        ) {
            IOUtils.copy(fis, fos);
            fos.flush();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return IMOOCJSONResult.errorMsg("文件未找得到！");
        } catch (IOException e2) {
            e2.printStackTrace();
            return IMOOCJSONResult.errorMsg("上传错误！");
        }
        //由于浏览器可能存在缓存，所以需要加上时间戳保证更新后的图片可以及时刷新
        String userFaceUrl = fileUpload.getImageServerUrl() + userId + "/" + newFileName + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
//        String userFaceUrl = fileUpload.getImageServerUrl() + userId + "/" + newFileName;
        Users updateFaceUser = centerUserService.updateUserFace(userId, userFaceUrl);
        Users user = setPropertiesNull(updateFaceUser);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);
        return IMOOCJSONResult.ok();
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError error : fieldErrors) {
            //发生验证错误所对应的属性
            String errorField = error.getField();
            //发生验证错误的信息
            String message = error.getDefaultMessage();
            map.put(errorField, message);
        }
        return map;
    }

    /**
     * 隐藏重要信息
     *
     * @param result 未隐藏信息的Users对象
     * @return 隐藏信息后的结果
     */
    public Users setPropertiesNull(Users result) {
        result.setPassword(null);
        result.setMobile(null);
        result.setEmail(null);
        result.setCreatedTime(null);
        result.setRealname(null);
        return result;
    }
}
