package com.chinags.dbra;

import com.chinags.dbra.service.DbDatabaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Mark_Wang
 * @date 2019/6/27
 * @time 13:28
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApp {
    @Autowired
    private DbDatabaseService dbDatabaseService;

    @Test
    public void findAll() {
        List<String> names = dbDatabaseService.findAllTableNameById("11");
        System.out.println(names.toString());
    }
}
