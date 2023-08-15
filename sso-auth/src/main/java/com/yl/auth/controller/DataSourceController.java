package com.yl.auth.controller;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.yl.auth.mapper.DeadmanMapper;
import com.yl.common.annotation.Limit;
import com.yl.common.config.LimitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.Set;

@RestController
@RequestMapping("/user/datasource")
public class DataSourceController {

    private final static Logger logger = LoggerFactory.getLogger(DataSourceController .class);

    @Autowired
    DataSource dataSource;
    @Autowired
    DefaultDataSourceCreator dataSourceCreator;
    @Autowired
    DeadmanMapper deadmanMapper;

    @Limit(type = LimitType.DEFAULT, time = 10, count = 2)
    @GetMapping("/list")
    public Set<String> list() {
        logger.info("1111111111111");
        logger.debug("22222222222222");
        logger.warn("333333333333333");
        logger.error("444444444444444");
        DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
        return dynamicRoutingDataSource.getDataSources().keySet();
    }

    @PostMapping("/add")
    public Set<String> add(@RequestBody DataSourceProperty dto) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        BeanUtils.copyProperties(dto, dataSourceProperty);
        DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
        DataSource ds = dataSourceCreator.createDataSource(dataSourceProperty);
        dynamicRoutingDataSource.addDataSource(dto.getPoolName(), ds);
        return dynamicRoutingDataSource.getDataSources().keySet();
    }

    @PostMapping("/remove")
    public void remove(@RequestBody DataSourceProperty dto) {
        DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
        dynamicRoutingDataSource.removeDataSource(dto.getPoolName());
    }

    @GetMapping("/test")
    @DS("#header#test")
    public void test() {
        deadmanMapper.selectById("1");
    }

}
