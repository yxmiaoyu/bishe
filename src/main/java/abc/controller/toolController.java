package abc.controller;

import abc.entity.Manager;
import abc.entity.Paint;
import abc.entity.User;
import abc.service.ManagerService;
import abc.service.PaintService;
import abc.service.UserService;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/tool")
public class toolController {


    @Autowired
    UserService userService;
    @Autowired
    PaintService paintService;
    @Autowired
    ManagerService managerService;


    @GetMapping("/userExcel")
    public void downloadUserExcel(HttpServletResponse response) throws IOException {
        List<User> users = userService.list();
        log.info("开始写excel");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=demo.xlsx");
        EasyExcel.write(response.getOutputStream(),User.class).sheet("模板").doWrite(users);
    }


    @GetMapping("/paintExcel")
    public void downloadPaintExcel(HttpServletResponse response) throws IOException {
        List<Paint> paints = paintService.list();
        log.info("开始写excel");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=demo.xlsx");
        EasyExcel.write(response.getOutputStream(),Paint.class).sheet("模板").doWrite(paints);
    }




    @GetMapping("/managerExcel")
    public void downloadManagerExcel(HttpServletResponse response) throws IOException {
        List<Manager> managers = managerService.list();
        log.info("开始写excel");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=demo.xlsx");
        EasyExcel.write(response.getOutputStream(),Manager.class).sheet("模板").doWrite(managers);
    }

}
