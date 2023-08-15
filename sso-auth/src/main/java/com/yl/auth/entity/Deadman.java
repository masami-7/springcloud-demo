package com.yl.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName deadman
 */
@TableName(value = "deadman")
@Data
public class Deadman implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "uid")
    private String uid;

    /**
     * 身份证号
     */
    @TableField(value = "idCard")
    private String idcard;

    /**
     * 死者姓名
     */
    @TableField(value = "userName")
    private String username;

    /**
     * 死者性别
     */
    @TableField(value = "sex")
    private String sex;

    /**
     * 死者年龄
     */
    @TableField(value = "age")
    private String age;

    /**
     * 死因
     */
    @TableField(value = "reason")
    private String reason;

    /**
     * 安排地狱层数
     */
    @TableField(value = "house")
    private String house;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}