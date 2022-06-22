package abc.service;

import abc.entity.Paint;
import abc.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PaintService extends IService<Paint> {


    double[] getNewPaintSpeed();

    int[] getNewPaintInWeek();

    int[] getPaintScoreScale();

}
