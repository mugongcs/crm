package com.javastudy.crm.web.listener;

import com.javastudy.crm.settings.domain.DicValue;
import com.javastudy.crm.settings.service.DicService;
import com.javastudy.crm.settings.service.impl.DicServiceImpl;
import com.javastudy.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


    }
}
