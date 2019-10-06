//package mapper;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(value = {
//        "classpath:spring/spring-dao.xml"
//})
//public class LogMapperTest {
//    @Autowired
//    private LogMapper logMapper;
//
//    @Test
//    public void getLogsByUser() {
//        List logs=logMapper.getLogsByUser(14);
//        logs.forEach(p->{
//            System.out.println(p);
//        });
//    }
//}