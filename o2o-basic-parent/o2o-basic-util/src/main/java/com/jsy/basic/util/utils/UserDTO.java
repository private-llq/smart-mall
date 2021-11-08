package com.jsy.basic.util.utils;
 
import lombok.Data;
import lombok.experimental.Accessors;
 
@Data
@Accessors(chain = true)
public class UserDTO {

    private Long id;
    private String name;
    private Integer age;
    private String sex;

}