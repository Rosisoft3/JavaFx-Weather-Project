package com.rosisoft.weatherproject.app.controller;


import com.rosisoft.openweathermap.classes.Unit;
import com.rosisoft.openweathermap.entities.CurrentWeather;
import com.rosisoft.openweathermap.entities.WeatherForecastDaily;
import com.rosisoft.openweathermap.exceptions.WeatherDataServiceException;
import com.rosisoft.openweathermap.interfaces.IWeatherDataService;
import com.rosisoft.openweathermap.services.DataServiceFactory;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;



public class MainController implements Initializable{
 IWeatherDataService dataService ;

    @FXML
    private Label   text_Date,text_Time,
                    text_SunRise,text_SunSet,
                    text_Temperature,text_WeatherDescription,text_City,
                    text_Wind,text_Precipitation,text_Humidity;

    @FXML
    private ImageView img_Weather;
    @FXML
    private ToggleButton toggleButton_Conversion;

    @FXML
    void onActionConversion(ActionEvent event) {
        ToggleButton jtb = (ToggleButton) event.getSource();
        if(jtb.isSelected()){
           dataService.setUnit(Unit.Imperial);
           jtb.setText("C°");
        }
        else{
           dataService.setUnit(Unit.Metric);
            jtb.setText("F°");
        }
     Platform.runLater(new Runnable() {
         @Override
         public void run() {
             initialize(null, null);
         }
     });
    }

    @FXML
    void onMousePressedClose(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resources)  {
          
             
    
        String timeFormatter12H="hh:mm a";
        String timeFormatter24H="HH:mm";
        String dateFormatter="EEE, dd MMM";
    
        Date ldt = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatter,new Locale("EN"));
        text_Date.setText(simpleDateFormat.format(ldt));
        
        DateTimeFormatter timeformatter ;
        timeformatter= DateTimeFormatter.ofPattern(timeFormatter12H);
       
       new Thread() {
       public void run() {

                       while (true) {
                        try {
                            Thread.sleep(1000);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    text_Time.setText(LocalTime.now().format(timeformatter));
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
            }
        }.start();
        
        try {
            
           String apiKey="*****";
           this.dataService = DataServiceFactory.getWeatherService(DataServiceFactory.service.OPEN_WEATHER_MAP,apiKey);
           CurrentWeather data = dataService.GET_CURRENT_WEATHER_DATA_BY_CITY_NAME("Constantine");
         
         
            text_City.setText(data.getName()+", "+data.getSys().getCountry());
            text_WeatherDescription.setText(data.getWeather()[0].getDescription());
            text_Temperature.setText((int)Double.parseDouble(data.getMain().getTemp())+"°");

                 if (dataService.getUnit().equals(Unit.Metric))
                     text_Wind.setText(data.getWind().getSpeed() + "m/s") ;
                 else text_Wind.setText(data.getWind().getSpeed() + "m/h");

            text_Humidity.setText(data.getMain().getHumidity()+"%");
            text_Precipitation.setText(data.getMain().getPressure()+" hPa");
            
            long unixTimestampSunrise =Long.parseLong(data.getSys().getSunrise());
            long javaTimestamp = unixTimestampSunrise * 1000L;
            Date date = new Date(javaTimestamp);
         
            String sunrise = new SimpleDateFormat(timeFormatter12H).format(date);
            text_SunRise.setText(sunrise);

            long unixTimestampSunset =Long.parseLong(data.getSys().getSunset());
            javaTimestamp = unixTimestampSunset * 1000L;
            date = new Date(javaTimestamp);
            String sunset = new SimpleDateFormat(timeFormatter12H).format(date);
            text_SunSet.setText(sunset);
            
          
            URL iconUrl = getClass().getResource("/com/rosisoft/weatherproject/app/resources/images/"+data.getWeather()[0].getIcon()+".png");
            img_Weather.setImage(new Image(String.valueOf(iconUrl)));
         
           //img_Weather.setImage(new Image("http://openweathermap.org/img/wn/"+data.getWeather()[0].getIcon()+".png"));
            
            
        } catch (WeatherDataServiceException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
   
}
