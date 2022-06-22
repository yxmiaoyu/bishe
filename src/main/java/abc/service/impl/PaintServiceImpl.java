package abc.service.impl;


import abc.entity.Paint;
import abc.mapper.PaintMapper;
import abc.mapper.UserMapper;
import abc.service.PaintService;
import abc.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PaintServiceImpl extends ServiceImpl<PaintMapper, Paint> implements PaintService {


    @Autowired
    PaintMapper paintMapper;


    @Override
    public double[] getNewPaintSpeed() {


        int paintNumber = Integer.parseInt(paintMapper.selectCount(null) + "");
        int[] paintInWeek = getNewPaintInWeek();
        double[] end = new double[8];
        end[7] = paintNumber;
        for (int i = 0; i < 7; ++i) {
            if (paintNumber != 0) {
                end[i] = paintInWeek[i] * 1.0 / paintNumber;
            } else {
                end[i] = 0;
            }
            paintNumber = paintNumber - paintInWeek[i];
        }


        return end;
    }


    @Override
    public int[] getNewPaintInWeek() {


        List<Paint> paints = new ArrayList<>();
        Calendar nowCalendar = new GregorianCalendar();
        Calendar oldCalendar = new GregorianCalendar();
        nowCalendar.setTime(new Date());
        //获取paints列表
        paints = paintMapper.selectList(null);
        log.info("---------------{}", paints);
        int[] end = new int[8];
        end[7] = paints.size();
        int paintCount = paints.size();
        for (Paint paint : paints) {
            oldCalendar.setTime(paint.getPaintCreateTime());
            int difference = (int) (nowCalendar.getTimeInMillis() - oldCalendar.getTimeInMillis()) / (24 * 3600 * 1000);
            if (difference > 6) {
                difference = 7;
            }
            if (difference <0) {
                difference = 0;
            }
            end[difference]++;
        }


        return end;
    }

    @Override
    public int[] getPaintScoreScale() {

        List<Paint> paints = new ArrayList<>();
        paints = paintMapper.selectList(null);
        int[] end = new int[5];// A B C D E
        for (Paint paint : paints) {
            int index=paint.getPaintScore().charAt(0) - 'A';
            if(index>5 || index<0){
                index=0;
            }
            end[index]++;
        }
        return end;
    }
}