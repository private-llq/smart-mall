package com.jsy.config;

import com.jsy.dto.SelectUserOrderGoodsDto;
import com.jsy.dto.SelectUserOrderMenuDto;
import com.jsy.dto.SelectUserOrderMenuGoodsDto;
import com.jsy.dto.SelectUserOrderServiceDto;
//import com.jsy.vo.SelectRefundAndOrderVo;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @Author Created by yejunjie
 * @Description TODO
 * @Date 2019/5/10
 */
public class DaoTest {
    private static Pattern humpPattern = Pattern.compile("[A-Z]");


    public static void main(String[] args) {
        System.out.println(getResultMap(SelectUserOrderMenuGoodsDto.class));

    }
	public static String getUpdateSelective(Class<?> clazz){

        Object obj = null;
        try{
            obj = clazz.newInstance();
        }catch (Exception e){
            return "#Exception.反射生成实体异常#";
        }

        String clazzName = clazz.getSimpleName();
        String resultMapId = Character.toLowerCase(clazzName.charAt(0))+clazzName.substring(1)+"Map";
        String pkgName = clazz.getName();

        StringBuilder resultMap = new StringBuilder();
        resultMap.append("<update id=\"updateSelective\" parameterType=\"");
        resultMap.append(pkgName);
        resultMap.append("\">\n");
        resultMap.append("UPDATE X_TABLE");
        resultMap.append("\n<set>\n");

        Field[] fields = clazz.getDeclaredFields();
        for(Field f : fields){
            String property = f.getName();
            String javaType = f.getType().getName();
            if("serialVersionUID".equals(property)){
                continue;//忽略掉这个属性
            }
            resultMap.append("    <if test=\"");
            resultMap.append(property+"!=null\">");
            resultMap.append(property2Column(property).toUpperCase());
            resultMap.append("=#{"+property+", jdbcType="+javaType2jdbcType(javaType.toLowerCase())+"},</if>\n");
        }
        resultMap.append("</set>\n");
        resultMap.append("where id = #{id,jdbcType=VARCHAR}\n");
        resultMap.append("</update>");
        return resultMap.toString();
    }
	public static void getInsert(Class<?> clazz){
        String text = getUpdateSelective(clazz);
        Pattern p = Pattern.compile("#\\{[^}]+}");
        Matcher m = p.matcher(text);

        while (m.find()){
            System.out.println(m.group()+",");
        }

    }

    /**
     * 获取表的字段
     * @param clazz
     */
    public void getColumns(Class<?> clazz){
        String text = getResultMap(clazz);
        Pattern p = Pattern.compile("column=[\"\\w\"]+");
        Matcher m = p.matcher(text);
        while (m.find()) {
            Pattern p1 = Pattern.compile("[\"\\w\"]+$");
            Matcher m1 = p1.matcher(m.group());

            while (m1.find()) {
                Pattern p2 = Pattern.compile("[\\w]+");
                Matcher m2 = p2.matcher(m1.group());
                while (m2.find()) {
                    System.err.println(m2.group().toUpperCase());
                }
            }
        }
    }
    /**
          * 获取ResultMap
          * @param clazz 实体类的Class
          * @return String
          */
    public static String getResultMap(Class<?> clazz){

        Object obj = null;
        try{
            obj = clazz.newInstance();
        }catch (Exception e){
            return "#Exception.反射生成实体异常#";
        }

        String clazzName = clazz.getSimpleName();
        String resultMapId = Character.toLowerCase(clazzName.charAt(0))+clazzName.substring(1)+"Map";
        String pkgName = clazz.getName();

        StringBuilder resultMap = new StringBuilder();
        resultMap.append("<resultMap id=\"");
        resultMap.append(resultMapId);
        resultMap.append("\" type=\"");
        resultMap.append(pkgName);
        resultMap.append("\">\n");

        Field[] fields = clazz.getDeclaredFields();
        for(Field f : fields){
            String property = f.getName();
            String javaType = f.getType().getName();
            if("serialVersionUID".equals(property)){
                continue;//忽略掉这个属性
            }
            resultMap.append("    <result column=\"");
            resultMap.append(property2Column(property).toUpperCase());
            resultMap.append("\" jdbcType=\"");
            resultMap.append(javaType2jdbcType(javaType.toLowerCase()));
            resultMap.append("\" property=\"");
            resultMap.append(property);
            resultMap.append("\" />\n");
        }
        resultMap.append("</resultMap>");
        return resultMap.toString();
    }

    private static String property2Column(String property){
        Matcher matcher = humpPattern.matcher(property);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String javaType2jdbcType(String javaType){
        if(javaType.contains("string")){
            return "VARCHAR";
        }else if(javaType.contains("boolean")){
            return "BIT";
        }else if(javaType.contains("byte")){
            return "TINYINT";
        }else if(javaType.contains("short")){
            return "SMALLINT";
        }else if(javaType.contains("int")){
            return "INTEGER";
        }else if(javaType.contains("long")){
            return "BIGINT";
        }else if(javaType.contains("double")){
            return "DOUBLE";
        }else if(javaType.contains("float")){
            return "REAL";
        }else if(javaType.contains("date")){
            return "DATE";
        }else if(javaType.contains("timestamp")){
            return "TIMESTAMP";
        }else if(javaType.contains("time")){
            return "TIME";
        }else if(javaType.contains("bigdecimal")){
            return "DECIMAL";
        }else{
            return "未知类型";
        }
    }


    

}