package com.gmy.AccessCTLManagementSys;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.gmy.AccessCTLManagementSys.utils.ForDevTime.sendHeartBeat;

/**
 * @authon GMY
 * @create 2020-11-03 14:53
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class test {

    @Test
    public void test1(){
        sendHeartBeat("asd", 1);
    }
}
