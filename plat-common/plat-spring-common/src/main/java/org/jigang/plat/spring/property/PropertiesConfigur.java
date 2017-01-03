package org.jigang.plat.spring.property;

import org.jigang.plat.core.properties.PropertyResovleHelper;

import java.util.Properties;

/**
 * property配置类
 * 在spring加载配置时同步配置到此类中,方便其它地方以编码方式获取
 *
 * Created by wujigang on 16/7/26.
 */
public class PropertiesConfigur {
    private static Properties properties;

    private static PropertyResovleHelper propertyPlaceholderHelper;

    /**
     * 初始化properties
     *
     * @param props
     */
    public static void init(Properties props) {
        properties = props;
        propertyPlaceholderHelper = new PropertyResovleHelper("${", "}", ":", true);
    }

    /**
     * 获取property
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        return propertyPlaceholderHelper.replacePlaceholders(value, properties);
    }

    /**
     * 获取property
     * 如果获取不到则取默认值defaultValue
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return null == value ? defaultValue : value;
    }

}
