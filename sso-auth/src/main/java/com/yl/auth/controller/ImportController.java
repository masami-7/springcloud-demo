package com.yl.auth.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.yl.auth.entity.Deadman;
import com.yl.auth.mapper.DeadmanMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/import")
public class ImportController {

    @Autowired
    DeadmanMapper deadmanMapper;

    @PostMapping("/deadManList")
    public boolean importFile(@RequestParam("file") MultipartFile file) throws IOException {
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
        return true;
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

    public static void main(String[] args) {
        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("身份证", "123456789");
            map.put("姓名", "张三");
            map.put("年龄", 18);
            map.put("性别", "男");
            map.put("死因", "去二次元");
            map.put("关入地狱（层）", "18");

            rows.add(map);
        }
        // 通过工具类创建writer
        BigExcelWriter writer = ExcelUtil.getBigWriter("C:\\Users\\62597\\Desktop\\deadManList.xlsx");
        // 合并单元格后的标题行，使用默认标题样式
        //writer.merge(row1.size() - 1, "一班成绩单");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        // 关闭writer，释放内存
        writer.close();
    }

}
