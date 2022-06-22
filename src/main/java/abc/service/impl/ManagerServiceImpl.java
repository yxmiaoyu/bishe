package abc.service.impl;


import abc.entity.Manager;
import abc.entity.User;
import abc.mapper.ManagerMapper;
import abc.mapper.UserMapper;
import abc.service.ManagerService;
import abc.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager> implements ManagerService {

    @Autowired
    ManagerMapper managerMapper;


    @Override
    public Manager getOneByManagerName(String name) {


        QueryWrapper<Manager> wrapper=new QueryWrapper<>();
        wrapper.eq("manager_name",name);
        return managerMapper.selectOne(wrapper);
    }
}
