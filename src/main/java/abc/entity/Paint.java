package abc.entity;


import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Paint {


    @TableId
    private Long paintId;
    private Long painterId;
    private String painterName;
    private String paintName;//画作名称
    private String paintImageName;//画作照片名称
    private String paintScore;//画作得分
    private String paintIntroduction;
    private Date paintCreateTime;

}
