package nurzhan.Project.room.Services.impl;

import nurzhan.Project.room.Services.RoomService;
import nurzhan.Project.room.entities.Rooms;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.util.*;


public class RoomServiceImplTest {
    @Autowired
    private RoomService roomService;
    @BeforeTest
    public void  BeforeTest(){
        System.out.println("before test method was started");
    }
    @AfterTest
    public void AfterTest(){
        System.out.println("after test method was started");
    }

}