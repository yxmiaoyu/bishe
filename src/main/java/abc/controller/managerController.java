package abc.controller;


import abc.entity.Manager;
import abc.service.ManagerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/manager")
public class managerController {


    @Autowired
    ManagerService managerService;


    @GetMapping("/add")
    public String add(Model model) {
        //处理post方式上传请求

        log.info("开始跳转manager-add页面");
        return "manager/manager-add";

    }



    @PostMapping("/toAdd")
    @ResponseBody
    public String toAdd(@RequestParam("managerName") String managerName,
                        @RequestParam("pass") String password,
                        Model model
    ) throws IOException {
        String returnMessage = null;
        QueryWrapper<Manager> wrapper = new QueryWrapper<>();
        wrapper.eq("manager_name", managerName);
        Manager manager = managerService.getOne(wrapper);
        if (manager != null) {
            returnMessage = "管理员已存在，请更换名称";
        } else {
            manager=new Manager();
            manager.setManagerName(managerName);
            manager.setManagerCreateTime(new Date());
            manager.setManagerPassword(password);
            boolean end = managerService.save(manager);
            if (end) {
                returnMessage = "管理员创建成功";
            } else {
                returnMessage = "管理员创建失败，请重试";
            }
        }
        return returnMessage;
    }




    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "managerName", defaultValue = "nobody") String managerName,
                       @RequestParam(value = "nowPage", defaultValue = "1") Integer nowPage) {
        //参数1：当前页，参数2：每页几个数据
        Page<Manager> managerPage = new Page<>(nowPage, 5);
        //参数1：分页的page对象，参数2：查询条件
        Page<Manager> page = new Page<>();
        if (managerName.equals("nobody")) {
            //无条件查询
            page = managerService.page(managerPage, null);
        } else {
            //有条件查询
            log.info("开始有条件查询{}", managerName);
            QueryWrapper<Manager> wrapper = new QueryWrapper<>();
            page = managerService.page(managerPage, wrapper.eq("manager_name", managerName));
        }
        //page对象里存在所有的分页信息，所以直接返回page对象就行了
        model.addAttribute("page", page);


        return "manager/manager-list";
    }




    @GetMapping("/delete")
    public String delete(
//            @PathVariable("userId") Long userId
            @RequestParam(value = "managerId", defaultValue = "0") Long managerId) {
        boolean end = managerService.removeById(managerId);
        log.info("删除用户id={}，删除结果为{}", managerId, end);
        //重定向，回到第二页
        return "redirect:/manager/list";
    }





}

