package abc.service.impl;

import abc.entity.User;
import abc.mapper.UserMapper;
import abc.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

//操作那个mapper，那个对象
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    UserMapper userMapper;


    @Override
    public int[] getNewUserInWeek() {


        List<User> users = new ArrayList<>();

        //查询所哟user对象
        users = userMapper.selectList(null);

        Calendar nowCalendar = new GregorianCalendar();
        Calendar oldCalendar = new GregorianCalendar();
        nowCalendar.setTime(new Date());
        int[] end = new int[8];
        for (User user : users) {
            oldCalendar.setTime(user.getUserCreateTime());
            int difference = (int) (nowCalendar.getTimeInMillis() - oldCalendar.getTimeInMillis()) / (24 * 3600 * 1000);
            if (difference > 6) {
                difference = 7;
            }
            if (difference <0) {
                difference = 0;
            }
            end[difference]++;//---
        }


        return end;
    }


    @Override
    public double[] getNewUserSpeed() {

        //一周新增用户量
        int[] newUser = getNewUserInWeek();
        //总用户量
        int userNumber = Integer.parseInt(userMapper.selectCount(null) + "");
        //返回值
        double[] end = new double[9];
        end[7]=userNumber;
        for(int i=0;i<7;++i){
            if(userNumber!=0){
                end[i]=newUser[i]*1.0/userNumber;
            }else{
                end[i]=0;
            }
            userNumber=userNumber-newUser[i];
        }


        return end;
    }

@Override
    public List<User> fun1(){



        return userMapper.fun1();

}




}
