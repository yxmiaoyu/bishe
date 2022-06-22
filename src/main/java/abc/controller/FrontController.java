package abc.controller;


import abc.entity.Paint;
import abc.entity.Show;
import abc.entity.User;
import abc.service.PaintService;
import abc.service.ShowService;
import abc.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/app")
public class FrontController {


    @Autowired
    UserService userService;
    @Autowired
    PaintService paintService;
    @Autowired
    ShowService showService;


    /**
     * 登录
     *
     * @param request
     * @param response
     * @param session
     * @throws IOException
     */

    //登录：登录直接根据userID和密码请求，你给我返回结果（boolean）
    @PostMapping("/login")
    public void login(HttpServletRequest request,
                      HttpServletResponse response,
                      HttpSession session) throws IOException {

        log.info("开始处理客户端登录请求");
        String userNameStr = (String) request.getParameter("userStr");
        String passwordStr = (String) request.getParameter("passwordStr");
        String returnMsg = "这是返回String信息";
        Boolean judge = false;
        if (userNameStr.equals("") || userNameStr == null || passwordStr.equals("") || passwordStr == null) {
            returnMsg = "输入信息有误，登录失败";
        } else {
            //连接数据库，开始登录验证
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("user_name", userNameStr);
            User currentUser = userService.getOne(wrapper);
            if (currentUser.getUserPassword().equals(passwordStr)) {
                //信息正确
                returnMsg = "用户" + currentUser.getUserName() + "登录成功";
                judge = true;
            } else {
                returnMsg = "用户" + currentUser.getUserName() + "登录失败，请检查密码重新登录";
            }
            PrintWriter writer = null;
            writer = response.getWriter();
            session.setAttribute("user", currentUser);
            session.setAttribute("judge", judge);
            session.setAttribute("returnMsg", returnMsg);
            writer.write(returnMsg);


        }
    }


    /**
     * 登出
     *
     * @param session
     * @param model
     */
    @GetMapping("/logout")
    public void logout(HttpSession session, Model model) {
        //  清理session，跳转登录页面


        Math.sqrt(16);


        session.removeAttribute("user");
        log.info("用户{}登出", session.getAttribute("user"));
    }


    /**
     * 根据作品民下载作品
     *
     * @param request
     * @param response
     * @param paintName
     * @throws Exception
     */
    @PostMapping(value = "download/{paintName}")
    public void download(HttpServletRequest request,
                         HttpServletResponse response,
                         @PathVariable("paintName") String paintName) throws Exception {


        QueryWrapper<Paint> wrapper = new QueryWrapper<>();
        wrapper.eq("paint_name", paintName);
        Paint paint = paintService.getOne(wrapper);


        String realPath = ResourceUtils.getURL("classpath:").getPath() + "static";
        File file = new File(realPath + "/images/paintImage/" + paint.getPaintImageName());
        log.info("用户下载的作品名称为：{}", realPath + "/images/paintImage/" + paint.getPaintImageName());


        response.setHeader("Content-Disposition", "attachment;filename=a.mp4");
        long total = file.length();
        response.setContentLengthLong(total);
        ServletOutputStream outputStream = response.getOutputStream();
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[1024 * 8];
        int len;
        while ((len = fis.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        fis.close();


    }


    /**
     * @param painterName
     * @param paintName
     * @param image
     * @param paintScore
     * @param paintIntroduction
     * @param model
     * @return
     * @throws IOException
     * @deprecated 上传作品
     */
    @PostMapping("/upload1")
    public String upload1(@RequestParam("painterName") String painterName,
                          @RequestParam("paintName") String paintName,
                          @RequestPart("image") MultipartFile image,
                          @RequestParam(value = "paintScore", defaultValue = "A") String paintScore,
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
                    Paint paint = new Paint();
                    paint.setPainterId(user.getUserId());
                    paint.setPaintCreateTime(new Date());
                    paint.setPainterName(painterName);
                    paint.setPaintName(paintName);
                    paint.setPaintScore(paintScore);
                    paint.setPaintImageName(paintName + "." + imageType);
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


    /**
     * 查询用户作品历史
     *
     * @return
     */
    @PostMapping("upload2")
    public String upload2(@RequestParam("userId") long userId,//用户id
                          @RequestParam("showMeessage") String showMeessage,//用户id,
                          @RequestParam("showName") String showName,//用户id,
                          @RequestPart("files") MultipartFile[] files,
                          @RequestParam("worksId ") long worksId) throws IOException {


        Show show = new Show();
        show.setShowId(worksId);
        show.setShowMessage(showMeessage);
        show.setUserId(userId);
        showService.save(show);


        // 保存作品信息
        if (files.length > 0) {
            //遍历文件
            int num = 1;
            for (MultipartFile image : files) {
                if (!image.isEmpty()) {
                    //保存每个图片文件
//                    String originalFilename = multipartFile.getOriginalFilename();
//                    multipartFile.transferTo(new File("D:\\" + originalFilename));


                    //获取原始作品类型
                    String imageType = image.getOriginalFilename().split("\\.")[1];
                    //// 获取static目录 获取static就可以完成上传功能了
                    String realPath = ResourceUtils.getURL("classpath:").getPath() + "static";
                    image.transferTo(new File(realPath + "/images/paintImage/" + showName + num++ + "." + imageType));
                    //判断图片是否上传完成
                    File file = new File(realPath + "/images/paintImage/" + showName + (num - 1) + "." + imageType);
                    if (file.exists()) {
                        //图片上传成功，开始数据库新增数据


                        QueryWrapper<User> wrapper = new QueryWrapper<>();
                        wrapper.eq("user_Id", userId);
                        User user = userService.getOne(wrapper);


                        Paint paint = new Paint();
                        paint.setPainterId(user.getUserId());
                        paint.setPaintCreateTime(new Date());
                        paint.setPainterName(user.getUserName());
                        paint.setPaintName(showName + (num - 1));
                        paint.setPaintScore("A");
                        paint.setPaintImageName(showName + (num - 1) + "." + imageType);
                        paint.setPaintIntroduction(showMeessage);
                        log.info("paint对象上传数据库前打印下日志看看对不对{}", paint);
                        //开始插入数据
                        boolean end = paintService.save(paint);
                    }
                }
            }


            return null;
        }

        return null;
    }


    //查询用户作品历史

    /**
     * 查询用户作品历史
     *
     * @return
     */
    @PostMapping("hostory1")
    public List<Paint> history1(@RequestParam("userName") long name,
                                HttpServletRequest request,
                                HttpSession session,
                                HttpServletResponse response) throws IOException {

        QueryWrapper<Paint> wrapper = new QueryWrapper<>();
        wrapper.eq("painter_name", name);

        List<Paint> paints = paintService.list(wrapper);


        PrintWriter writer = null;
        writer = response.getWriter();
        session.setAttribute("paints", paints);
        return paints;
    }


    /**
     * 查看作品展历史
     *
     * @param userId
     * @return
     */
    @PostMapping("hostory2")
    public List<Show> history2(@RequestParam("userId") long userId,
                               HttpServletRequest request,
                               HttpServletResponse response) {




        QueryWrapper<Show> wrapper = new QueryWrapper<>();
        wrapper.eq("user_Id", userId);
        List<Show> shows = showService.list(wrapper);



        return shows;
    }


    /**
     * 注册
     *
     * @param request
     * @param response
     * @param session
     * @throws IOException
     */

    //注册：我post上传userID和密码
    @PostMapping("/register ")
    public void register(HttpServletRequest request,
                         HttpServletResponse response,
                         HttpSession session) throws IOException {

        log.info("开始处理客户端注册请求");
        String userNameStr = (String) request.getParameter("userStr");
        String passwordStr = (String) request.getParameter("passwordStr");
        String returnMsg = "这是返回String信息";
        Boolean judge = false;
        if (userNameStr.equals("") || userNameStr == null || passwordStr.equals("") || passwordStr == null) {
            returnMsg = "输入信息有误，注册失败";
        } else {
            //连接数据库，开始验证注册信息
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("user_name", userNameStr);
            User currentUser = userService.getOne(wrapper);
            if (currentUser != null) {
                returnMsg = "用户已存在，无法注册";
            } else {
                //开始注册
                User user = new User();
                user.setUserName(userNameStr);
                user.setUserPassword(passwordStr);
                boolean end = userService.save(user);
                if (end) {
                    returnMsg = "用户创建成功";
                    judge = true;
                } else {
                    returnMsg = "用户创建失败";
                    judge = false;
                }
            }


            PrintWriter writer = null;
            writer = response.getWriter();
            session.setAttribute("judge", judge);
            session.setAttribute("returnMsg", returnMsg);
            writer.write(returnMsg);


        }
    }


}