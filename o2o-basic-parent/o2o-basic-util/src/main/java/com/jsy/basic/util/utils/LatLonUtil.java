package com.jsy.basic.util.utils;

import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Data
public class LatLonUtil {
        private static double PI = 3.14159265;
        private static double EARTH_RADIUS = 6378137;
        private static double RAD = Math.PI / 180.0;
        public static int ONEKM = 3; //一公里骑行时间
        public static int BASIC_TIME = 25;

        /// <summary>
        /// 根据提供的经度和纬度、以及半径，取得此半径内的最大最小经纬度
        /// </summary>
        /// <param name="lat">纬度</param>
        /// <param name="lon">经度</param>
        /// <param name="raidus">半径(米)</param>
        /// <returns></returns>
        public static Map<String, Double> GetAround(double lat, double lon, int raidus) {

            Double latitude = lat;
            Double longitude = lon;

            Double degree = (24901 * 1609) / 360.0;
            double raidusMile = raidus;

            Double dpmLat = 1 / degree;
            Double radiusLat = dpmLat * raidusMile;
            Double minLat = latitude - radiusLat;
            Double maxLat = latitude + radiusLat;

            Double mpdLng = degree * Math.cos(latitude * (PI / 180));
            Double dpmLng = 1 / mpdLng;
            Double radiusLng = dpmLng * raidusMile;
            Double minLng = longitude - radiusLng;
            Double maxLng = longitude + radiusLng;
            HashMap<String, Double> map = new HashMap<>();
            map.put("minLat",minLat);
            map.put("minLng",minLng);
            map.put("maxLat",maxLat);
            map.put("maxLng",maxLng);
            return map;
        }


        /**
         * 根据两个经纬度 算距离
         * @param longitude1
         * @param latitude1
         * @param longitude2
         * @param latitude2
         * @return 返回的距离单位为米
         */
        public static double GetDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
            //            // 纬度
            double lat1 = Math.toRadians(latitude1);
            double lat2 = Math.toRadians(latitude2);
            // 经度
            double lng1 = Math.toRadians(longitude1);
            double lng2 = Math.toRadians(longitude2);
            // 纬度之差
            double a = lat1 - lat2;
            // 经度之差
            double b = lng1 - lng2;
            // 计算两点距离的公式
            double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                    Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
            // 弧长乘地球半径, 返回单位: 米
            s =  s * EARTH_RADIUS;
            BigDecimal bg = new BigDecimal(s).setScale(1, RoundingMode.HALF_UP);
            return bg.doubleValue();
        }

        //根据距离算配送大概时间
        public static int getMin(double distance) {
            if (distance <= 2000) {
                return BASIC_TIME;
            }
            double time = BASIC_TIME + (distance - 2000) / 1000 * ONEKM;
            return (int)Math.round(time);
        }
    }