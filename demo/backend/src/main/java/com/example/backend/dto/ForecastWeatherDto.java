package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author hym
 * @Date $ $
 * @MethodName $
 * @Description $
 * @Return $
 * @Throw $
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForecastWeatherDto {
    String date;
    String dayweather;
    String daywind;
    String week;
    String daypower;
    String daytemp;
    String nightwind;
    String nighttemp;
    String nightweather;
    String nightpower;
}
