package com.example.linquas.myapplication;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;




/**
 * Created by Linquas on 2015/10/29.
 * Used to parse the weather data from Central Weather Bureau
 */
public class XMLparser {


    public static List<Location> readXML(String input) throws XmlPullParserException {

        XmlPullParserFactory factory = XmlPullParserFactory
                .newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        List<Location> locations = null;


        try {
            parser.setInput(new StringReader(input)); // 設置數據源編碼
            int eventType = parser.getEventType();// 獲取事件類型
            Location currentLocation = null;

            while (eventType != XmlResourceParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlResourceParser.START_DOCUMENT:// 文檔開始事件,可以進行數據初始化處理
                        locations = new ArrayList<>();// 實例化集合類
                        break;
                    case XmlResourceParser.START_TAG://開始讀取某個標簽
                        //通過getName判斷讀到哪個標簽，然後通過nextText()獲取文本節點值，或通過getAttributeValue(i)獲取屬性節點值
                        String name = parser.getName();
                        if(name.equalsIgnoreCase("location") && currentLocation == null){
                            currentLocation = new Location();
                        }
                        if(name.equalsIgnoreCase("lat") && currentLocation != null){
                            currentLocation.setLattitude(Float.valueOf(parser.nextText()));
                        }else if(name.equalsIgnoreCase("lon")){
                            currentLocation.setLontitude(Float.valueOf(parser.nextText()));
                        }else if(name.equalsIgnoreCase("locationName")){
                            currentLocation.setCityName(parser.nextText());
                        }else if (name.equalsIgnoreCase("weatherElement")){
                            parser.nextTag();
                            String elementName = parser.nextText();
                            if(elementName.equalsIgnoreCase("ELEV")){
                                parser.nextTag();//elementValue
                                parser.nextTag();
                                String t = parser.getName();
                                currentLocation.setElevation(Float.valueOf(parser.nextText()));
                            }else if(elementName.equalsIgnoreCase("TEMP")){
                                parser.nextTag();
                                parser.nextTag();
                                currentLocation.setTemp(Float.valueOf(parser.nextText()));
                            }else if(elementName.equalsIgnoreCase("HUMD")){
                                parser.nextTag();
                                parser.nextTag();
                                currentLocation.setHumid(Float.valueOf(parser.nextText()));
                            }
                        }else if (name.equalsIgnoreCase("parameter")){
                            parser.nextTag();
                            String parameterName = parser.nextText();
                            if(parameterName.equalsIgnoreCase("CITY")){
                                parser.nextTag();//parameterName
                                currentLocation.setCityName(parser.nextText());
                            }else if(parameterName.equalsIgnoreCase("TOWN")){
                                parser.nextTag();//parameterName
                                currentLocation.setTownName(parser.nextText());
                            }
                        }
                        break;
                    case XmlResourceParser.END_TAG:// 結束元素事件
                        //讀完一個Person，可以將其添加到集合類中
                        String end_tag = parser.getName();
                        if (end_tag.equalsIgnoreCase("location")&& currentLocation != null) {
                            locations.add(currentLocation);
                            currentLocation = null;
                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locations;
    }

}
