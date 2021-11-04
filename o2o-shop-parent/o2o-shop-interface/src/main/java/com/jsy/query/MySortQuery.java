package com.jsy.query;


import com.jsy.domain.Tree;
import lombok.Data;

import java.util.List;


@Data
public class MySortQuery {

    /**
     * 根据父id分开排序
     */
    private Integer parentId;
    private List<Tree> trees;
}
