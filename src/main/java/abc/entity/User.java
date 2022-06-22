package abc.entity;


import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
//@TableName("user")如果数据库中表名与实体类名不一致时候加的注解
public class User {
    @TableId(value = "user_id")
    @ExcelProperty("用户ID")
    private long userId;
    @ExcelProperty("用户名")
    private String userName;
    @ExcelProperty("用户密码")
    private String userPassword;
    @ExcelProperty("用户介绍")
    private String userIntroduction;
    @ExcelProperty("用户头像名称")
    private String userImage;
    @ExcelProperty("用户创建时间")
    private Date userCreateTime;
    //一对多
    @TableField(typeHandler = JacksonTypeHandler.class,value = "false")
    private List<Paint> userPaints;

}