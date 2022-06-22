package abc.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.util.Date;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Manager {
    @TableId(value="manager_id")
    private long managerId;
    private String managerName;
    private String managerPassword;
    private Date managerCreateTime;
}
