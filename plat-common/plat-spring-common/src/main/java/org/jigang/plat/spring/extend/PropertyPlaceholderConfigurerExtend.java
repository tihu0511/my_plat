package org.jigang.plat.spring.extend;

import org.jigang.plat.spring.property.PropertiesConfigur;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * spring配置扩展类
 * 将初始化的props同步到PropertiesConfigur中
 *
 * Created by wujigang on 16/7/26.
 */
public class PropertyPlaceholderConfigurerExtend extends PropertyPlaceholderConfigurer {
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory,
                                     Properties props)throws BeansException {

        super.processProperties(beanFactory, props);

        //将配置信息同步到PropertiesConfigur对象中
        PropertiesConfigur.init(props);
    }
}
