package org.jigang.plat.generator.mybatis;

import org.jigang.plat.core.template.ClasspathResTplUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wujigang on 16/7/12.
 */
public class MybatisGenerator {
    private static final Map<String, String[]> typeMap = new HashMap<String, String[]>();
    private static final Map<String, String> javaTypeMap = new HashMap<String, String>();
    private static final String DAO_TPL_NAME = "/tpl/mybatis/dao.vm";
    private static final String ENTITY_TPL_NAME = "/tpl/mybatis/entity.vm";
    private static final String MAPPER_TPL_NAME = "/tpl/mybatis/mapper.vm";
    private static final String SERVICE_TPL_NAME = "/tpl/mybatis/serviceInterface.vm";
    private static final String SERVICE_IMPL_TPL_NAME = "/tpl/mybatis/serviceImpl.vm";

    private String schemaName;
    private String tableName;
    private String packageName;

    private String generatedTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

    static {
        typeMap.put("Integer", new String[]{"int", "tinyint"});
        typeMap.put("BigDecimal", new String[]{"decimal"});
        typeMap.put("Date", new String[]{"date", "datetime", "timestamp"});
        typeMap.put("String", new String[]{"varchar", "char"});
        typeMap.put("Long", new String[]{"bigint"});

        javaTypeMap.put("Date", "java.util.Date");
        javaTypeMap.put("BigDecimal", "java.math.BigDecimal");
        javaTypeMap.put("Long", "java.lang.Long");

    }
    public void generator() throws IOException {
        packageName = PropertiesHelper.getProps("generator.package");
        String generatorTableStr = PropertiesHelper.getProps("generator.table");
        if (null == generatorTableStr || "".equals(generatorTableStr)) {
            System.out.println("没有要生成的对象");
            return;
        }
        String[] tables = generatorTableStr.split(";");
        if (null == tables || tables.length <= 0) {
            System.out.println("没有要生成的对象");
            return;
        }
        for (String table : tables) {
            if (table != null && !"".equals(table)) {
                String[] str = table.split("\\.");
                schemaName = str[0];
                tableName = str[1];
                //生成表
                generateTable();
            }
        }
    }

    private void generateTable() throws IOException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>开始生成 " + schemaName + "." + tableName + " <<<<<<<<<<<<<<<<<<<<<<<<");
        List<ColumnInfo> columns = DBHelper.getAllColumns(schemaName.toUpperCase(), tableName.toUpperCase());

        if (null != columns && columns.size() > 0) {
            //设置字段属性
            for (ColumnInfo column : columns) {
                column.setJavaName(transferJavaName(column.getName(), false)); //字段名
                column.setJavaType(getFieldType(column.getType())); //字段类型
            }

            //生成实体类
            String entityName = generateEntity(columns);

            //生成dao
            String daoName = generateDao(entityName);

            //生成mapper
            generateMapper(entityName, daoName, columns);

            //生成service
            generateService(entityName, daoName);
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>生成 " + schemaName + "." + tableName + " 结束<<<<<<<<<<<<<<<<<<<<<<<<");

    }

    /**
     * 生成实体对象
     * @param columns
     * @return
     */
    private String generateEntity(List<ColumnInfo> columns) throws IOException {
        String entityName = transferJavaName(tableName, true);

        String projectPath = getProjectPath();

        String entityFilePath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/entity/";
        String entityFile = entityFilePath + entityName + ".java";

        File pathDir = new File(entityFilePath);
        if (!pathDir.exists()) {
            pathDir.mkdirs();
        }

        String content = generateEntityContent(entityName, columns);

        System.out.println(">>>>>>>>>>>>>>>>>>>>Generator " + entityName + " to " + entityFile);

        //写入java文件
        writeFile(entityFile, content, "UTF-8");

        return entityName;
    }

    /**
     * 根据模板生成entity内容
     * @param entityName
     * @param columns
     * @return
     */
    private String generateEntityContent(String entityName, List<ColumnInfo> columns) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("package", packageName);
        map.put("generatedTime", generatedTime);

        map.put("entityName", entityName);
        map.put("entityColumns", columns);

        //导包import部分
        List<String> imports = new ArrayList<String>();
        for (ColumnInfo column : columns) {
            String javaType = column.getJavaType();
            String javaTypeImport = javaTypeMap.containsKey(javaType) ? javaTypeMap.get(javaType) : null;
            if (null != javaTypeImport && !javaTypeImport.startsWith("java.lang") && !imports.contains(javaTypeImport)) {
                imports.add(javaTypeImport);
            }
        }
        if (map.size() > 0) {
            map.put("imports", imports);
        }
        return ClasspathResTplUtil.getTemplateContent(map, ENTITY_TPL_NAME);
    }

    /**
     * 获取java字段类型
     * @param type
     * @return
     */
    private String getFieldType(String type) {
        for (Map.Entry<String, String[]> entry : typeMap.entrySet()) {
            if (isContain(entry.getValue(), type)) {
                return entry.getKey();
            }
        }
        return "String";
    }

    private boolean isContain(String[] strArr, String str) {
        for (String s : strArr) {
            if (s.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private void writeFile(String entityFile, String content, String charset) throws IOException {
        File javaFile = new File(entityFile);
        if (!javaFile.exists()) {
            javaFile.createNewFile();
        }

        //写入内容
        BufferedOutputStream br = null;

        try {
            br = new BufferedOutputStream(new FileOutputStream(javaFile));
            br.write(content.getBytes(charset));
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    /**
     * 以"_"字符分隔单词,生成驼峰的java命名
     * @param tableName
     * @return
     */
    private String transferJavaName(String tableName, boolean firstUpper) {
        String[] words = tableName.split("_");

        String name = null;

        for (String word : words) {
            if (name == null) {
                name = firstUpper ? word.substring(0, 1).toUpperCase() + word.substring(1). toLowerCase() : word.toLowerCase();
            } else {
                name = name + word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            }
        }

        return name;
    }

    /**
     * 生成dao
     * @param entityName
     * @return
     */
    private String generateDao(String entityName) throws IOException {
        String daoName = "I" + entityName + "Dao";

        String projectPath = getProjectPath();

        String daoFilePath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/dao/";
        String daoFile = daoFilePath + daoName + ".java";

        File pathDir = new File(daoFilePath);
        if (!pathDir.exists()) {
            pathDir.mkdirs();
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>Generator " + daoName + " to " + daoFile);

        String content = getDaoContent(entityName);

        //写入java文件
        writeFile(daoFile, content, "UTF-8");

        return daoName;
    }

    /**
     * 根据模板获取dao内容
     * @param entityName
     * @return
     */
    private String getDaoContent(String entityName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("package", packageName);
        map.put("generatedTime", generatedTime);

        map.put("entityName", entityName);
        map.put("entityVar", entityName.substring(0, 1).toLowerCase() + entityName.substring(1));

        return ClasspathResTplUtil.getTemplateContent(map, DAO_TPL_NAME);
    }

    /**
     * 获取工程路径
     * @return
     */
    private String getProjectPath() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        String path = url.getPath();

        int fromIndex = path.length() - "/test-classes/".length() - 1;
        String projectPath = null;
        //windows
        if (path.indexOf(":") != -1) {
            projectPath = path.substring(1, path.lastIndexOf("/", fromIndex));
        }
        //mac osx等
        else {
            projectPath = path.substring(0, path.lastIndexOf("/", fromIndex));
        }

        return projectPath;
    }

    /**
     * 生成mapper文件
     *
     * @param entityName
     * @param daoName
     * @param columns
     */
    private void generateMapper(String entityName, String daoName, List<ColumnInfo> columns) throws IOException {
        String mapperName = entityName + "Mapper";

        String projectPath = getProjectPath();

        String mapperFilePath = projectPath + "/src/main/resources/sql/";
        String mapperFile = mapperFilePath + mapperName + ".xml";

        File pathDir = new File(mapperFilePath);
        if (!pathDir.exists()) {
            pathDir.mkdirs();
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>Generator " + mapperName + " to " + mapperFile);

        String content = generateMapperContent(entityName, columns);

        //写入java文件
        writeFile(mapperFile, content, "UTF-8");

    }

    /**
     * 根据模板生成mapper内容
     * @param entityName
     * @param columns
     * @return
     */
    private String generateMapperContent(String entityName, List<ColumnInfo> columns) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("package", packageName);
        map.put("generatedTime", generatedTime);

        map.put("entityName", entityName);
        map.put("entityColumns", columns);

        map.put("schemaName", schemaName);
        map.put("tableName", tableName);

        return ClasspathResTplUtil.getTemplateContent(map, MAPPER_TPL_NAME);
    }

    private String generateService(String entityName, String daoName) throws IOException {
        String serviceName = "I" + entityName + "Service";
        String serviceImplName = entityName + "ServiceImpl";

        String projectPath = getProjectPath();
        String serviceFilePath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/service/";
        String serviceImplFilePath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/service/impl/";
        String serviceFile = serviceFilePath + serviceName + ".java";
        String serviceImplFile = serviceImplFilePath + serviceImplName + ".java";

        mkdir(serviceFilePath, serviceImplFilePath);

        System.out.println(">>>>>>>>>>>>>>>>>>>>Generator " + serviceName + " to " + serviceFile);

        String serviceContent = getServiceContent(daoName, entityName, serviceName);
        String serviceImplContent = getServiceImplContent(daoName, entityName, serviceName, serviceImplName);

        //写入java文件
        writeFile(serviceFile, serviceContent, "UTF-8");
        writeFile(serviceImplFile, serviceImplContent, "UTF-8");

        return serviceName;
    }

    private String getServiceContent(String daoName, String entityName, String serviceName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("package", packageName + ".service");
        map.put("generatedTime", generatedTime);

        List<String> imports = new ArrayList<String>();
        imports.add(packageName + ".entity." + entityName);
        map.put("imports", imports);

        map.put("serviceInterfaceName", serviceName);
        map.put("entityName", entityName);
        map.put("entityVar", entityName.substring(0, 1).toLowerCase() + entityName.substring(1));

        return ClasspathResTplUtil.getTemplateContent(map, SERVICE_TPL_NAME);
    }

    private String getServiceImplContent(String daoName, String entityName, String serviceName, String serviceImplName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("package", packageName + ".service.impl");
        map.put("generatedTime", generatedTime);

        List<String> imports = new ArrayList<String>();
        imports.add(packageName + ".dao." + daoName);
        imports.add(packageName + ".entity." + entityName);
        imports.add(packageName + ".service." + serviceName);
        imports.add("org.springframework.beans.factory.annotation.Autowired");
        imports.add("org.springframework.stereotype.Service");
        Collections.sort(imports);
        map.put("imports", imports);

        map.put("serviceImplName", serviceImplName);
        map.put("serviceInterfaceName", serviceName);
        map.put("daoName", daoName);
        map.put("daoVar", daoName.substring(1, 2).toLowerCase() + daoName.substring(2));
        map.put("entityName", entityName);
        map.put("entityVar", entityName.substring(0, 1).toLowerCase() + entityName.substring(1));

        return ClasspathResTplUtil.getTemplateContent(map, SERVICE_IMPL_TPL_NAME);
    }

    private void mkdir(String... paths) {
        for (String path : paths) {
            File pathDir = new File(path);
            if (!pathDir.exists()) {
                pathDir.mkdirs();
            }
        }
    }

}
