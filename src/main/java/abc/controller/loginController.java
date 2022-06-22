package abc.controller;


import abc.entity.Manager;
import abc.service.ManagerService;
import abc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;


@Slf4j
@Controller
public class loginController {


    @Autowired
    ManagerService managerService;


    //去登录页index页面
    @GetMapping(value = {"/", "/login"})
    public String loginPage() {
        return "login";
    }


    @PostMapping("/login")
    public String Login(Manager manager, HttpSession session, Model model) {


        //开始判断登录的管理员的账号是否正确
        Manager currentManager = managerService.getOneByManagerName(manager.getManagerName());
        log.info("当前登录管理员{}，查询出来的管理员的信息是：{}", manager, currentManager);
        if (currentManager != null && currentManager.getManagerPassword().equals(manager.getManagerPassword())) {
            //登陆成功，保存登录的用户
            session.setAttribute("manager", manager);
            //登录成功，重定向到toIndex方法。防止页面重新提交
            return "redirect:/toIndex.html";
        }
        model.addAttribute("msg", "登陆失败,请检查账号密码是否正确");
        return "login";


    }


    @GetMapping("/toIndex.html")
    public String toLogin(HttpSession session, Model model) {
        //判断是否登录成功，判断session里是否有manager对象信息
        if (session.getAttribute("manager") != null) {
            //登录成功，重定向到toIndex页面。
            return "index";
        } else {
            //登陆失败，返回登陆页面
            model.addAttribute("msg", "登陆失败");
            return "login";
        }
    }


}
