package com.example.backend.service.impl;

import com.example.backend.entity.Address;
import com.example.backend.mapper.AddressMapper;
import com.example.backend.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 杨严
 * @Date: 2022/11/30/1:03 PM
 * @Description:
 */
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressMapper addressMapper;
    @Override
    public Integer InsertAddress(Address address) {
        Integer res=0;
        res=addressMapper.insert(address);
        return res;
    }
}
