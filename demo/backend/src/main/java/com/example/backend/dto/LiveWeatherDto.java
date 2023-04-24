package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author HUAWEI
 * @Date $ $
 * @MethodName $
 * @Description $
 * @Return $
 * @Throw $
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiveWeatherDto {
    String province;
    String city;
    String adcode;
    String windpower;
    String weather;
    String temperature;
    String humidity;
    String reporttime;
    String winddirection;
}
