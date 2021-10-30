package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 附件资源日志
 * </p>
 *
 * @author lijin
 * @since 2020-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_file_info")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String uuid;
    /**
     * 相对路径
     */
    private String downloadUrl;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * MD5_Hash值
     */
    private String md5Hash;

    /**
     * 原始文件名
     */
    private String fileName;

    /**
     * 文件类型,字符串
     */
    private String fileType;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 文件后缀
     */
    private String tags;

    /**
     * 数据状态：0启用;1禁用;2软删
     */
    private Integer validStatus;



}
