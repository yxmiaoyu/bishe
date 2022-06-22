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
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/user")
public class userController {

    //    @Autowired
//    UserMapper userMapper;
    @Autowired//自动注入
            UserService userService;

    @Autowired
    PaintService paintService;


    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "userName", defaultValue = "nobody") String userName,
                       @RequestParam(value = "nowPage", defaultValue = "1") Integer nowPage) {
//        //表格展示user用户的信息,可用但是不如下面的page
//        List<User> users = new ArrayList<User>();
//        users = userService.list();
//        log.info("查询出来的users={}",users);

        //        QueryWrapper<Manager> wrapper=new QueryWrapper<>();
        //        wrapper.eq("manager_name",name);
        //        return managerMapper.selectOne(wrapper);


        //分页查询
        //参数1：当前页，参数2：每页几个数据
        Page<User> userPage = new Page<>(nowPage, 5);
        //参数1：分页的page对象，参数2：查询条件
        Page<User> page = new Page<>();
        if (userName.equals("nobody")) {
            //无条件查询
            page = userService.page(userPage, null);
        } else {
            //有条件查询
            log.info("开始有条件查询{}", userName);
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            page = userService.page(userPage, wrapper.eq("user_name", userName));
        }
        //page对象里存在所有的分页信息，所以直接返回page对象就行了
        model.addAttribute("page", page);


        return "user/user-list";
    }


    @GetMapping("/add")
    public String add(Model model) {
        //处理post方式上传请求

        log.info("开始跳转user-add页面");
        return "user/user-add";

    }


    //自动封装上传的文件
    @PostMapping("/toAdd")
    @ResponseBody
    public String toAdd(@RequestParam("username") String userName,
                        @RequestParam("pass") String password,
                        @RequestPart("image") MultipartFile image,
                        @RequestPart("introduction") String introduction,
                        Model model
    ) throws IOException {
        String returnMessage = null;
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", userName);
        User user = userService.getOne(wrapper);
        log.info("新增用户，根据输入的用户名查到的旧的用户信息为{}", user);
        if (user != null && user.getUserName().equals(userName)) {
            returnMessage = "用户名已存在，请更换用户名";
        } else {
            //开新增用户，先存图片后存用户
            //处理post方式上传请求
            //开始保存上传的文件
            if (!image.isEmpty()) {
                //获取原始作品类型
                String imageType = image.getOriginalFilename().split("\\.")[1];
                //// 获取static目录 获取static就可以完成上传功能了
                String realPath = ResourceUtils.getURL("classpath:").getPath() + "static";
                image.transferTo(new File(realPath + "/images/userImage/" + userName + "." + imageType));
                log.info("打印新增用户是上传的用户头像的保存地址{}", realPath + "/images/userImage/" + userName + "." + imageType);
                //判断图片是否上传完成
                File file = new File(realPath + "/images/userImage/" + userName + "." + imageType);
                if (file.exists()) {
                    //图片上传成功，开始数据库新增数据
                    User insertUser = new User();
                    insertUser.setUserCreateTime(new Date());
                    insertUser.setUserName(userName);
                    insertUser.setUserIntroduction(introduction);
                    insertUser.setUserPassword(password);
                    insertUser.setUserImage(userName + "." + imageType);
                    log.info("user对象上传数据库前打印下日志看看对不对{}", insertUser);
                    //开始插入数据
                    boolean end = userService.save(insertUser);
                    if (end) {
                        returnMessage = "用户 " + insertUser.getUserName() + " 创建成功";
                    } else {
                        returnMessage = "用户 " + insertUser.getUserName() + " 创建失败，请重试";
                    }
                } else {
                    //图片上传失败，请重新新建用户
                    returnMessage = "图片上传失败，请重新创建用户";
                }
            }
        }
        return returnMessage;
    }


    @GetMapping("/delete")
    public String delete(
//            @PathVariable("userId") Long userId
            @RequestParam(value = "userId", defaultValue = "0") Long userId) {
        boolean end = userService.removeById(userId);
        log.info("删除用户id={}，删除结果为{}", userId, end);
        //开始删除用户作品
        QueryWrapper<Paint> wrapper = new QueryWrapper<>();
        wrapper.eq("painter_id", userId);
        paintService.remove(wrapper);
        //重定向，回到第二页
        return "redirect:/user/list";
    }


    //------------------------------------------------------------------------------------------------------------------

    @GetMapping("/edit")
    public String edit(Model model) {
        //处理post方式上传请求

        log.info("开始跳转user-edit页面");
        return "user/user-edit";

    }

    //自动封装上传的文件
    @PostMapping("/toEdit")
    @ResponseBody
    public String toEdit(@RequestParam("username") String userName,
                         @RequestParam("pass") String password,
                         @RequestPart("image") MultipartFile image,
                         @RequestPart("introduction") String introduction,
                         Model model
    ) throws IOException {
        String returnMessage = null;
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", userName);
        User oldUser = userService.getOne(wrapper);

        if (!image.isEmpty()) {
            //获取原始作品类型
            String imageType = image.getOriginalFilename().split("\\.")[1];
            //// 获取static目录 获取static就可以完成上传功能了
            String realPath = ResourceUtils.getURL("classpath:").getPath() + "static";
            image.transferTo(new File(realPath + "/images/userImage/" + userName + "." + imageType));
            log.info("打印修改用户是上传的用户头像的保存地址{}", realPath + "/images/userImage/" + userName + "." + imageType);
            //判断图片是否上传完成
            File file = new File(realPath + "/images/userImage/" + userName + "." + imageType);
            if (file.exists()) {
                //图片上传成功，开始数据库新增数据
                User insertUser = new User();
                insertUser.setUserCreateTime(oldUser.getUserCreateTime());
                insertUser.setUserId(oldUser.getUserId());
                insertUser.setUserName(userName);
                insertUser.setUserIntroduction(introduction);
                insertUser.setUserPassword(password);
                insertUser.setUserImage(userName + "." + imageType);
                log.info("user对象上传数据库前打印下日志看看对不对{}", insertUser);
                //开始插入数据
                boolean end = userService.updateById(insertUser);
                if (end) {
                    returnMessage = "用户 " + insertUser.getUserName() + " 创建成功";
                } else {
                    returnMessage = "用户 " + insertUser.getUserName() + " 创建失败，请重试";
                }
            } else {
                //图片上传失败，请重新新建用户
                returnMessage = "图片上传失败，请重新修改用户";
            }
        }
        return returnMessage;

    }


    @GetMapping("/data")
    public String data(Model model) {
        //处理post方式上传请求
        log.info("开始跳转用户数据页面请求");


        //一周用户新增
        int[] nums1 = userService.getNewUserInWeek();
        log.info("打印nums1的数据{}", nums1);
        model.addAttribute("userWeek", nums1);

        double[] newUserSpeed = userService.getNewUserSpeed();
        model.addAttribute("newUserSpeed", newUserSpeed);

        double[] newPaintSpeed = paintService.getNewPaintSpeed();
        model.addAttribute("newPaintSpeed", newPaintSpeed);

        int[] paintScoreScale = paintService.getPaintScoreScale();
        model.addAttribute("paintScoreScale", paintScoreScale);


        model.addAttribute("disk", 60);


        log.info("开始跳转user-data");
        return "user/user-data";

    }


    @GetMapping("/echarts")
    public String echarts(Model model) {
        //处理post方式上传请求
        log.info("开始跳转用户数据页面请求");


        //一周用户新增
        int[] nums1 = userService.getNewUserInWeek();
        log.info("打印nums1的数据{}", nums1);
        model.addAttribute("userWeek", nums1);

        //用户新增率
        double[] newUserSpeed = userService.getNewUserSpeed();
        model.addAttribute("newUserSpeed", newUserSpeed);


        log.info("开始跳转user-data");
        return "user/user-echarts";

    }


    //用户排行表单
    @GetMapping("/ranking")
    public String ranking(Model model) {


        //User集合
        List<User> users = new ArrayList<>();
        users = userService.list();
        Map<Integer, List<User>> end = new HashMap<>();
        Set<Integer> index = new HashSet<>();
        int max = 0;
        //遍历User对象，计算每个用户得分
        for (User user : users) {
            List<Paint> paints = new ArrayList<>();
            Map<String, Object> map1 = new HashMap<>();
            map1.put("painter_name", user.getUserName());
            paints = paintService.listByMap(map1);
            //遍历每个user的List<Paint>属性
            int num = 0;
            for (Paint paint : paints) {
                num += paint.getPaintScore().charAt(0) - 'A' + 1;
            }
            //保存计算数据
            if (end.containsKey(num) == true) {
                List<User> endUser = new ArrayList<>();
                endUser = end.get(num);
                endUser.add(user);
                end.put(num, endUser);
            } else {
                List<User> endUser = new ArrayList<>();
                endUser.add(user);
                end.put(num, endUser);
            }
            if (index.contains(num) == false) {
                index.add(num);
            }
            if (num > max) {
                max = num;
            }
        }
        //index转换降序
        int[] indexNum = new int[index.size()];
        int[] returnNum = new int[users.size()];
        int n = 0;
        for (Integer num : index) {
            indexNum[n++] = num;
        }
        Arrays.sort(indexNum);
        n = 0;
        for (int i = indexNum.length - 1; i > 0; --i) {
            returnNum[n++] = indexNum[i];
        }
        //遍历endUser
        List<User> returnUser = new ArrayList<>();
        int i = 0;
        for (Integer num : returnNum) {
            users = end.get(num);
            if (users.size() != 0) {
                for (User user : users) {
                    returnUser.add(user);
                }
            }
        }

        model.addAttribute("user", returnUser);
        model.addAttribute("num", returnNum);
        return "user/user-ranking";

    }


}
