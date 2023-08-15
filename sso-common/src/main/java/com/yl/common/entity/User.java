package com.yl.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName tb_users
 */
@Data
public class User implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String status;

    private static final long serialVersionUID = 1L;
}