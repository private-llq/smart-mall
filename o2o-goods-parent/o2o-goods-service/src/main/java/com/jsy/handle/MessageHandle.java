package com.jsy.handle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageHandle implements Serializable {

   private boolean flag;

   private String userUuid;

   private String goodsUuid;

   private String shopUuid;


}