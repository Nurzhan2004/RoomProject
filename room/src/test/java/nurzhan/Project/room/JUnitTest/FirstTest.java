package nurzhan.Project.room.JUnitTest;


import org.testng.annotations.Test;

public class FirstTest extends BaseTest{
    @Test
    public void hello(){
        System.out.println("Hello, Nurzhan");
    }
    @Test(description = "Проверить что-это?")
    public void checkSome(){
        System.out.println("I am a the test method");
    }
}
