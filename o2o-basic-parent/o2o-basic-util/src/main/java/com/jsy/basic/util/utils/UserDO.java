package com.jsy.basic.util.utils;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
 
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class UserDO {

    private String id;
    private String name;
    private Integer age;
    private String sex;

}