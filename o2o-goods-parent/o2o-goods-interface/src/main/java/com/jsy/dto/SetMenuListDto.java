package com.jsy.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SetMenuListDto implements Serializable {
    private String title;
    private List<SetMenuGoodsDto> dtoList;
}
