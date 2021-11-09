package com.jsy.basic.gen;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CodeGenerator {
    public static void main(String[] args) {
            final ResourceBundle resource = ResourceBundle.getBundle("generator-user");
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        final String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + resource.getString("outputDir"));
        gc.setAuthor(resource.getString("author"));
        gc.setOpen(false);
        gc.setFileOverride(true);
        gc.setBaseResultMap(true);
        gc.setIdType(IdType.AUTO);
        gc.setSwagger2(true);//添加swagger2
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(resource.getString("url"));
        dsc.setDriverName(resource.getString("driverClassName"));
        dsc.setUsername(resource.getString("username"));
        dsc.setPassword(resource.getString("password"));
        mpg.setDataSource(dsc);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(resource.getString("parent.package"));
        pc.setEntity("domain");
        mpg.setPackageInfo(pc);
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to DO nothing
            }
        };
        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + resource.getString("mapperDir")
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        templatePath = "/templates/query.java.vm";
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + resource.getString("queryDir")
                        + tableInfo.getEntityName() + "Query" + StringPool.DOT_JAVA;
            }
        });
        templatePath = "/templates/entity.java.vm";
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + resource.getString("domainDir")
                        + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setEntity(null);
        templateConfig.setController("/templates/controller.java.vm");
        mpg.setTemplate(templateConfig);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        //生成的表
        strategy.setInclude(resource.getString("tables").replaceAll(" ","").split(","));
        strategy.setTablePrefix("w_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }

}