package com.example.backend.service;

import com.example.backend.entity.Address;
import org.springframework.stereotype.Service;

/**
 * @Author: 杨严
 * @Date: 2022/11/30/1:03 PM
 * @Description:
 */
@Service
public interface AddressService {
    Integer InsertAddress(Address address);
}
