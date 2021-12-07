package com.jsy.query;


import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;
import java.io.Serializable;

@Data
public class NearTheServiceQuery extends BaseQuery implements Serializable {

    /**
     * 经度
     */
   private String longitude;
    /**
     * 维度
     */
   private String latitude;

}
