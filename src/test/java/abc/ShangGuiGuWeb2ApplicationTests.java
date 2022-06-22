package abc;

import abc.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
class ShangGuiGuWeb2ApplicationTests {





    @Autowired
    JdbcTemplate jdbcTemplate;





    @Test
    void contextLoads() {

//        List<Map<String, Object>> user=jdbcTemplate.queryForList("select * from user ");
//        System.out.println("--------------");
//        System.out.println(user);


    }

}
