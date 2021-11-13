package com.jsy.client.impl;

import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.ServiceCharacteristicsClient;
import com.jsy.domain.ServiceCharacteristics;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class ServiceCharacteristicsClientImpl implements ServiceCharacteristicsClient {


    @RequestMapping(value = "/serviceCharacteristics/{id}", method = RequestMethod.GET)
    @Override
    public ServiceCharacteristics get(Long id) {
        return null;
    }
}
