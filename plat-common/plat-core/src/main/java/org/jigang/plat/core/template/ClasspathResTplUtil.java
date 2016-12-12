package org.jigang.plat.core.template;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jigang.plat.core.lang.MapUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by wujigang on 2016/8/15.
 */
public class ClasspathResTplUtil {
    /**
     * 初始化路径
     */
    static {
        //采用classPath方式读取路径
        Velocity.addProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.addProperty("input.encoding", "UTF-8");
        Velocity.addProperty("output.encoding", "UTF-8");
        Velocity.init();
    }

    /**
     * 获取模板内容
     *
     * @param parameters
     * @param path
     * @return
     */
    public static String getTemplateContent(Map<String, Object> parameters, String path) throws RuntimeException {
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        String result = null;
        try {
            if (!MapUtil.isEmpty(parameters)) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    context.put(entry.getKey(), entry.getValue());
                }
            }
            Template template = Velocity.getTemplate(path);
            template.merge(context, writer);
            result = writer.toString();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
