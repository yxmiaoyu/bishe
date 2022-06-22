package abc.controller;


import abc.entity.Paint;
import abc.entity.User;
import abc.service.PaintService;
import abc.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/paint")
public class paintController {


    @Autowired
    PaintService paintService;
    @Autowired
    UserService userService;


    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "painterName", defaultValue = "nobody") String painterName,
                       @RequestParam(value = "nowPage", defaultValue = "1") Integer nowPage) {
//        分页查询
//        参数1：当前页，参数2：每页几个数据
        Page<Paint> paintPage = new Page<>(nowPage, 5);
        //参数1：分页的page对象，参数2：查询条件
        Page<Paint> page = new Page<>();
        if (painterName.equals("nobody")) {
            //无条件查询
            page = paintService.page(paintPage, null);
        } else {
            //有条件查询
            log.info("开始有条件查询{}", painterName);
            QueryWrapper<Paint> wrapper = new QueryWrapper<>();
            page = paintService.page(paintPage, wrapper.eq("painter_name", painterName));
        }
        //page对象里存在所有的分页信息，所以直接返回page对象就行了
        model.addAttribute("page", page);
        log.info("{}", page.getRecords());


        return "paint/paint-list";
    }


    @GetMapping("/add")
    public String add(Model model) {
        //处理post方式上传请求

        log.info("开始跳转paint-add页面");
        return "paint/paint-add";

    }


    //自动封装上传的文件
    @PostMapping("/toAdd")
    @ResponseBody
    public String toAdd(@RequestParam("painterName") String painterName,
                        @RequestParam("paintName") String paintName,
                        @RequestPart("image") MultipartFile image,
                        @RequestPart("paintScore") String paintScore,
                        @RequestPart("paintIntroduction") String paintIntroduction,
                        Model model
    ) throws IOException {


        String returnMessage = null;
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", painterName);
        User user = userService.getOne(wrapper);
        log.info("新增用户，根据输入的用户名查到作者用户信息为{}", user);
        if (user == null) {
            returnMessage = "用户查询失败，请检查用户名是否正确";
        } else {
            if (!image.isEmpty()) {
                //获取原始作品类型
                String imageType = image.getOriginalFilename().split("\\.")[1];
                //// 获取static目录 获取static就可以完成上传功能了
                String realPath = ResourceUtils.getURL("classpath:").getPath() + "static";
                image.transferTo(new File(realPath + "/images/paintImage/" + paintName + "." + imageType));
                log.info("打印上传的用户作品的保存地址{}", realPath + "/images/paintImage/" + paintName + "." + imageType);
                //判断图片是否上传完成
                File file = new File(realPath + "/images/paintImage/" + paintName + "." + imageType);
                if (file.exists()) {
                    //图片上传成功，开始数据库新增数据
                    Paint paint=new Paint();
                    paint.setPainterId(user.getUserId());
                    paint.setPaintCreateTime(new Date());
                    paint.setPainterName(painterName);
                    paint.setPaintName(paintName);
                    paint.setPaintScore(paintScore);
                    paint.setPaintImageName(paintName+"."+imageType);
                    paint.setPaintIntroduction(paintIntroduction);
                    log.info("paint对象上传数据库前打印下日志看看对不对{}", paint);
                    //开始插入数据
                    boolean end = paintService.save(paint);
                    if (end) {
                        returnMessage = "作品上传成功";
                    } else {
                        returnMessage = "作品上传失败，请重试";
                    }


                } else {
                    //图片上传失败，请重新新建用户
                    returnMessage = "作品上传失败，请重新添加作品";
                }
            }
        }
        return returnMessage;

    }







    @GetMapping("/delete")
    public String delete(
            @RequestParam(value = "paintId", defaultValue = "0") Long paintId) {
        boolean end = paintService.removeById(paintId);
        log.info("删除作品id={}，删除结果为{}", paintId, end);
        //重定向，回到第二页
        return "redirect:/paint/list";
    }


}
