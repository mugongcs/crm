package com.javastudy.crm.web.listener;

import com.javastudy.crm.settings.domain.DicValue;
import com.javastudy.crm.settings.service.DicService;
import com.javastudy.crm.settings.service.impl.DicServiceImpl;
import com.javastudy.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("初始化全局作用域，缓存数据字典开始");

        ServletContext application = event.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        Map<String, List<DicValue>> map = ds.getAll();
        Set<String> set = map.keySet();
        for(String key : set){
            application.setAttribute(key, map.get(key));
        }
        System.out.println("缓存数据字典结束");


        //-----------------------------------------------------------------
        //数据处理完毕之后，处理Stage2Possibility.properties文件
        //-----------------------------------------------------------------
        Map<String,String> pMap = new HashMap<String, String>();
        //解析properties文件
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){
            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key, value);
        }
        application.setAttribute("pMap", pMap);

    }
}
