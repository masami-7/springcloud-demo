package com.yl.auth;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.yl.auth.entity.Deadman;
import com.yl.auth.mapper.DeadmanMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@SpringBootTest
public class ImportController {

    @Autowired
    DeadmanMapper deadmanMapper;

    // 8992
    @Test
    void load() {
        long before = System.currentTimeMillis();
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file("C:\\Users\\62597\\Desktop\\deadManList.xlsx"));
        List<Map<String, Object>> maps = reader.readAll();
        Deadman deadMan = null;
        List<Deadman> list = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            deadMan = new Deadman();
            deadMan.setUid(UUID.randomUUID().toString());
            deadMan.setUsername(map.get("姓名").toString());
            deadMan.setSex(map.get("性别").toString());
            deadMan.setAge(map.get("年龄").toString());
            deadMan.setIdcard(map.get("身份证").toString());
            deadMan.setReason(map.get("死因").toString());
            deadMan.setHouse(map.get("关入地狱（层）").toString());
            list.add(deadMan);
        }
        long after = System.currentTimeMillis();
        System.out.println(after - before);
    }

    // 13530
    @Test
    void load1() {
        List<Deadman> list1 = new ArrayList<>();
        ExcelUtil.readBySax(FileUtil.file("C:\\Users\\62597\\Desktop\\deadManList.xlsx"), -1, new RowHandler() {

            @Override
            public void handle(int i, long l, List<Object> list) {
                if (l == 0) {
                    return;
                }
                Deadman deadMan = new Deadman();
                deadMan.setUid(UUID.randomUUID().toString());
                deadMan.setUsername(list.get(1).toString());
                deadMan.setAge(list.get(2).toString());
                deadMan.setSex(list.get(3).toString());
                deadMan.setIdcard(list.get(0).toString());
                deadMan.setReason(list.get(4).toString());
                deadMan.setHouse(list.get(5).toString());
                list1.add(deadMan);
            }

            @Override
            public void doAfterAllAnalysed() {
                System.out.println("解析结束");
                long before = System.currentTimeMillis();
                ExecutorService pool = new ThreadPoolExecutor(5,
                        10,
                        1,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(100));

                int doImportCount = 1000;
                int threadSize = (list1.size() / doImportCount) + 1;
                CountDownLatch countDownLatch = new CountDownLatch(threadSize);

                int start = 0;
                int end = 0;
                for (int i = 0; i < threadSize; i++) {
                    if (i == threadSize - 1) {
                        start = i * doImportCount;
                        end = list1.size();
                    } else {
                        start = i * doImportCount;
                        end = (i + 1) * doImportCount;
                    }
                    pool.execute(new DeadManThread(countDownLatch, deadmanMapper, list1, start, end));
                }

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("插入数据库完成");
                long after = System.currentTimeMillis();
                System.out.println(after - before);
                pool.shutdown();
            }
        });
    }

    @AllArgsConstructor
    class DeadManThread implements Runnable {

        private CountDownLatch countDownLatch;
        private DeadmanMapper deadmanMapper;
        private List<Deadman> list;
        private int start;
        private int end;

        @Override
        public void run() {
            try {
                deadmanMapper.insertBatchSomeColumn(list.subList(start, end));
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                countDownLatch.countDown();
            }
        }
    }

}
