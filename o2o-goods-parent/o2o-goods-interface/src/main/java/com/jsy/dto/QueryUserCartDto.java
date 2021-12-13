package com.jsy.dto;


import com.jsy.domain.Goods;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class QueryUserCartDto implements Serializable {
   private List<NewShopInfo> newShopDtoList=new ArrayList<>();
   private List<Goods> goodsList=new ArrayList<>();
 }
