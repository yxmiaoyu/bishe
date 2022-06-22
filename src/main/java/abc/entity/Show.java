package abc.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Show {
    @TableId(value = "show_id")
    private long showId;

    private long userId;

    private String showMessage;

    @TableField(typeHandler = JacksonTypeHandler.class, value = "false")
    private List<Paint> paints;
}
