package abc.service;

import abc.entity.Manager;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ManagerService extends IService<Manager> {



    Manager getOneByManagerName(String name);



}