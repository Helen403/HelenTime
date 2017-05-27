package bean;

/**
 * Created by Helen on 2017/5/24.
 */
public class WeatherBean {


    /**
     * city : 中山
     * weatherStatus : 暴雨
     * maxTemperature : 27
     * minTemperature : 22
     */

    private String city;
    private String weatherStatus;
    private String maxTemperature;
    private String minTemperature;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(String weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }
}
