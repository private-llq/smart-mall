package com.jsy.vo;

import lombok.Data;

import java.util.List;


@Data
public class SortVo2 {
    private String shopUuid;
    private List<SortVo> sortVo;
}
