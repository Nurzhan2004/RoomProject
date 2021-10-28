package nurzhan.Project.room.JUnitTest;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    @BeforeTest
    public void beforeTest(){
        System.out.println("before test method was started");
    }
    @AfterTest
    public void afterTest(){
        System.out.println("after test method was started");
    }
}
