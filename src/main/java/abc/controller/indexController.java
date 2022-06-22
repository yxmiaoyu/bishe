package abc.controller;


import abc.entity.Manager;
import abc.entity.Paint;
import abc.entity.User;
import abc.service.ManagerService;
import abc.service.PaintService;
import abc.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/index")
public class indexController {


    @Autowired
    UserService userService;
    @Autowired
    ManagerService managerService;
    @Autowired
    PaintService paintService;


    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {

        //  清理session，跳转登录页面
        session.removeAttribute("manager");
        log.info("用户{}登出", session.getAttribute("manager"));
        //登录成功，重定向到toIndex方法。防止页面重新提交
        return "redirect:/login";
    }


    //        QueryWrapper<Manager> wrapper=new QueryWrapper<>();
    //        wrapper.eq("manager_name",name);
    //        return managerMapper.selectOne(wrapper);


    @GetMapping("/welcome")
    public String welcome(Model model) {

        //开始查询数据库准备数据进行展示
        List<User> users = userService.list();
        List<Manager> managers = managerService.list();
        List<Paint> paints = paintService.list();
        model.addAttribute("userNumber", users.size());
        model.addAttribute("paintNumber", paints.size());
        model.addAttribute("managerNumber", managers.size());
        model.addAttribute("avgPaintNumber", paints.size() * 1.0 / users.size());
        Date nowDate = new Date();
        Calendar nowCalendar = new GregorianCalendar();
        Calendar oldUserCalendar = new GregorianCalendar();
        Calendar oldPaintCalendar = new GregorianCalendar();
        nowCalendar.setTime(nowDate);
        int countUser=0;
        int countPaint=0;
        for(User user:users){
            oldUserCalendar.setTime(user.getUserCreateTime());
            if((nowCalendar.getTimeInMillis()-oldUserCalendar.getTimeInMillis())/(1000*3600*24)<=1){
                countUser++;
            }
        }
        for(Paint paint:paints){
            oldPaintCalendar.setTime(paint.getPaintCreateTime());
            if((nowCalendar.getTimeInMillis()-oldPaintCalendar.getTimeInMillis())/(1000*3600*24)<=1){
                countPaint++;
            }
        }

        model.addAttribute("newPaintNumber",countPaint);
        model.addAttribute("newUserNumber",countUser);

        return "welcome";
    }


}
