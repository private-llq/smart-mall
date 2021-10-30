package com.jsy.mapper;

import java.util.List;

/**
 * dto和实体类互转
 *
 * @param <D>
 * @param <E>
 */
public interface BaseMapper<D,E> {
    /**
     * 实体类转为Dto
     * @param entity
     * @return
     */
   D toDto(E entity);

    /**
     * Dto转为实体类
     * @param dto
     * @return
     */
   E toEntity(D dto);

    /**
     * listDto转为listEntity
     * @param list
     * @return
     */
   List<E> toListEntity(List<D> list);

    /**
     * listEntity转为listDto
     * @param list
     * @return
     */
   List<D> toListDto(List<E> list);
}
