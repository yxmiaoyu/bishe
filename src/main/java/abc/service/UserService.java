package abc.service;

import abc.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


//泛型时要查询的数据类型
public interface UserService extends IService<User> {

    int[] getNewUserInWeek();

    double[] getNewUserSpeed();


    List<User> fun1();



}
