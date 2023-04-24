package com.example.backend.Tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.backend.dto.ForecastWeatherDto;
import com.example.backend.dto.LiveWeatherDto;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author hym
 * @Date $ $
 * @MethodName $
 * @Description $调用高德API，用于获取天气情况
 * @Return $
 * @Throw $
 */
public class GaodeUtil {
    private static String myKey = "c229aae26508a59b9c5745332ffeff46";

    //对url发GET请求，得到返回的JSON对象
    private static JSONObject getResJSON(String uri) throws IOException {
        URL url = new URL(uri);
        byte[] recBuf = new byte[1024];
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        if (urlConnection.getResponseCode() == 200){
            InputStream is = urlConnection.getInputStream();
            int length = is.read(recBuf);
            String res = new String(recBuf,0,length);
            JSONObject jsonObject = JSONObject.parseObject(res);
            return jsonObject;
        }
        else{
            return null;
        }
    }

    //获取当前主机IP下的城市编码
    private static String getYourAdCode() throws IOException {
        String uri = "https://restapi.amap.com/v3/ip?key=" + myKey;

        JSONObject res = getResJSON(uri);
        if(res != null){
            String adcode = res.getString("adcode");
            return adcode;
        }
        else{
            return "-1";
        }
    }

    //根据城市的adcode，获取该城市的预测天气
    private static JSONObject getForecaseWeatherByAdcode(String adcode) throws IOException {
        String uri = "https://restapi.amap.com/v3/weather/weatherInfo?city=" + adcode + "&key=" + myKey +
                "&extensions=all";
        JSONObject res = getResJSON(uri);

        if(res != null){
            JSONArray weatherArray = res.getJSONArray("forecasts");
            return weatherArray.getJSONObject(0).getJSONArray("casts").getJSONObject(0);
        }
        else{
            return null;
        }
    }

    //根据城市的adcode，获取该城市的实时天气
    private static JSONObject getLiveWeatherByAdcode(String adcode) throws IOException{
        String uri = "https://restapi.amap.com/v3/weather/weatherInfo?city=" + adcode + "&key=" + myKey +
                "&extensions=base";

        JSONObject res = getResJSON(uri);
        if(res != null){
            JSONArray weatherArray = res.getJSONArray("lives");
            return weatherArray.getJSONObject(0);
        }
        else{
            return null;
        }
    }

    private static LiveWeatherDto getLiveWeatherDto(JSONObject jsonObject){
        String province = jsonObject.getString("province");
        String city = jsonObject.getString("city");
        String adcode = jsonObject.getString("adcode");
        String windpower = jsonObject.getString("windpower");
        String weather = jsonObject.getString("weather");
        String temperature = jsonObject.getString("temperature");
        String humidity = jsonObject.getString("humidity");
        String reporttime = jsonObject.getString("reporttime");
        String winddirection= jsonObject.getString("winddirection");

        return new LiveWeatherDto(province, city, adcode, windpower, weather, temperature, humidity, reporttime, winddirection);
    }

    private static ForecastWeatherDto getForecastWeatherDto(JSONObject jsonObject){
        String date = jsonObject.getString("date");
        String dayweather = jsonObject.getString("dayweather");
        String daywind = jsonObject.getString("daywind");
        String week = jsonObject.getString("week");
        String daypower = jsonObject.getString("daypower");
        String daytemp = jsonObject.getString("daytemp");
        String nightwind = jsonObject.getString("nightwind");
        String nighttemp = jsonObject.getString("nighttemp");
        String nightweather = jsonObject.getString("nightweather");
        String nightpower = jsonObject.getString("nightpower");

        return new ForecastWeatherDto(date,dayweather,daywind,week,daypower,daytemp,nightwind,nighttemp,nightweather,nightpower);
    }

    /*
     * @description:对外暴露的接口
     * @author: hym
     * @date: 2022/12/12 21:13
     * @param:
     * @return:
     * @return: com.example.backend.dto.LiveWeatherDto
     **/
    //获取实时天气
    public static LiveWeatherDto getLiveWeather() throws IOException {
        //获取当前城市
        String adcode = GaodeUtil.getYourAdCode();

        //获取当前城市当天的实时天气
        JSONObject weather = GaodeUtil.getLiveWeatherByAdcode(adcode);
        return getLiveWeatherDto(weather);
    }

    //获取预测天气
    public static ForecastWeatherDto getForecastWeather() throws IOException{
        //获取当前城市
        String adcode = GaodeUtil.getYourAdCode();

        //获取当前城市的当天预测天气
        JSONObject weather = GaodeUtil.getForecaseWeatherByAdcode(adcode);
        return getForecastWeatherDto(weather);
    }

    //获取实时天气语音播报
    public static String getLiveWeatherText() throws IOException {
        LiveWeatherDto weatherObj = GaodeUtil.getLiveWeather();
        String weatherStr = "实时天气：" + weatherObj.getWeather() + "，" +
                "实时温度：" + weatherObj.getTemperature();

        /*1.判断温度*/
        int temperature = Integer.parseInt(weatherObj.getTemperature());
        if(temperature > 25)
            weatherStr += "，气温较热，建议短袖出门哦。";
        else if(temperature >= 15)
            weatherStr += "，气温适宜~";
        else if(temperature >= 5)
            weatherStr += "，气温较冷，注意添衣哦。";
        else
            weatherStr += "，气温寒冷，注意保暖哦。";

        /*2.判断天气*/
        //如果天气不好
        if(weatherObj.getWeather().indexOf("雨") >= 0 || weatherObj.getWeather().indexOf("雪") >= 0){
            weatherStr += "出门要记得带伞哦~";
        }

        return weatherStr;
    }

    //获取预测天气语音播报
    public static String getForecastWeatherText() throws IOException{
        ForecastWeatherDto forecastWeatherDto = GaodeUtil.getForecastWeather();
        String text = "今天是" + forecastWeatherDto.getDate() +
                "，气温" + forecastWeatherDto.getNighttemp() + "℃-" + forecastWeatherDto.getDaytemp() + "℃";

        //看看会不会下雨
        String dw = forecastWeatherDto.getDayweather();
        String nw = forecastWeatherDto.getNightpower();
        if((dw.indexOf("雨")>=0 || dw.indexOf("雪")>=0) &&
                (nw.indexOf("雨")>=0 || nw.indexOf("雪")>=0)) {
            text += "，且全天有降水，注意出门带伞哦~";
        }
        else if((dw.indexOf("雨")>=0 || dw.indexOf("雪")>=0) &&
                !(nw.indexOf("雨")>=0 || nw.indexOf("雪")>=0)) {
            text += "，且白天有降水，注意白天出门要带伞哦~";
        }
        else if(!(dw.indexOf("雨")>=0 || dw.indexOf("雪")>=0) &&
        (nw.indexOf("雨")>=0 || nw.indexOf("雪")>=0)){
            text += "，且晚上有降水，注意晚上出门要带伞哦~";
        }
        else{
            text += "，且今天不会下雨，请放心出门哦~";
        }
        return text;
    }
}
