package abc.mapper;

import abc.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//想操作什么类型的数据就使用什么泛型
@Mapper
public interface UserMapper extends BaseMapper<User> {


    List<User> fun1();



}
