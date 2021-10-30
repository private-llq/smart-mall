package com.jsy.vo;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2021-05-18
 */
public class OwnerBankcardsVo implements Serializable {


    private Integer id;

    /**
     * uuid
     */
    private String uuid;

    /**
     * bankcard_uuid
     */
    private String bankcardUuid;

    /**
     * 银行卡号
     */
    private String bankcard;

    /**
     * 银行卡绑定状态
     */
    private String stateId;
    /**
     * 银行卡名字
     */
    private String name;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBankcardUuid() {
        return bankcardUuid;
    }

    public void setBankcardUuid(String bankcardUuid) {
        this.bankcardUuid = bankcardUuid;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
