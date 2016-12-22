package org.jigang.plat.file;

import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * 文件操作工具类
 *
 * Created by wujigang on 16/8/17.
 */
public class FileUtil {
    private static final int BUFFER_SIZE = 2048;

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 获取硬盘文件的字符串内容
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getStringContent(String file) throws IOException {
        return getStringContent(file, DEFAULT_CHARSET);
    }

    /**
     * 获取硬盘文件的字符串内容
     *
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    public static String getStringContent(String file, String charset) throws IOException {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return getStringContentByIS(fis, charset);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }
    /**
     * 获取classpath下文件的字符串内容
     *
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    public static String getStringContentByClassPath(String file, String charset) throws IOException {
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
            return getStringContentByIS(is,charset);
        }finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 获取文件的字符串内容
     *
     * @param is
     * @param charset
     * @return
     * @throws IOException
     */
    public static String getStringContentByIS(InputStream is, String charset) throws IOException {

        if(is == null){
            throw new IOException("输入流为空");
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null;

        try {
            bis = new BufferedInputStream(is);
            baos = new ByteArrayOutputStream();
            bos = new BufferedOutputStream(baos);

            byte[] buf = new byte[BUFFER_SIZE];
            int len = -1;
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bos.flush();

            return new String(baos.toByteArray(), charset);
        } finally {
            IOUtils.closeQuietly(baos);
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(bis);
        }
    }

    /**
     * 删除文件
     * @param files
     */
    public static void deletes(String... files) {
        if (null == files || files.length <= 0) {
            return ;
        }

        for (String file : files) {
            File f = new File(file);
            if (!f.exists()) {
                continue;
            }

            if (f.isFile()) {
                f.delete();
            } else  {
                File[] subFiles = f.listFiles();
                deletes(subFiles);
                f.delete();
            }
        }
    }

    /**
     * 删除文件
     * @param files
     */
    public static void deletes(File[] files) {
        if (null == files || files.length <= 0) {
            return ;
        }

        for (File file : files) {
            if (!file.exists()) {
                continue;
            }

            if (file.isFile()) {
                file.delete();
            } else  {
                File[] subFiles = file.listFiles();
                deletes(subFiles);
            }
        }
    }

    /**
     * 创建多级文件夹
     * @param directory
     * @return
     */
    public static boolean mkdirs(String directory) {
        File dirFile = new File(directory);
        return dirFile.mkdirs();
    }

    /**
     * 创建多级文件夹
     * @param directories
     * @return
     */
    public static boolean mkdirs(String... directories) {
        if (null == directories || directories.length <=0) {
            return true;
        }
        for (String directory : directories) {
            if (!mkdirs(directory)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 写文件
     * @param file
     * @param content
     * @throws IOException
     */
    public static void writeFile(String file, String content) throws IOException {
        writeFile(file, content, DEFAULT_CHARSET);
    }

    /**
     * 写文件
     * @param file
     * @param content
     * @param charset
     * @throws IOException
     */
    public static void writeFile(String file, String content, String charset) throws IOException {
        File javaFile = new File(file);
        if (!javaFile.exists()) {
            javaFile.createNewFile();
        }

        //写入内容
        BufferedOutputStream br = null;
        try {
            br = new BufferedOutputStream(new FileOutputStream(javaFile));
            br.write(content.getBytes(charset));
        } finally {
            IOUtils.closeQuietly(br);
        }
    }
}
