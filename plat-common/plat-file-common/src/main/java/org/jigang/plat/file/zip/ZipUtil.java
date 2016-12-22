package org.jigang.plat.file.zip;

import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 文件工具类
 *
 * Created by BF100271 on 2016/6/23.
 */
public class ZipUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);
    private static final int BUFFER_SIZE = 1024;

    /**
     *
     * @param src 源文件
     * @param destDirectory 目标文件路径(文件夹)
     * @param charset 编码格式
     * @return
     */
    public static List<String> unZip(String src, String destDirectory, String charset) throws IOException {
        logger.info("Start unzip file " + src + " to " + destDirectory + " with charset " + charset);

        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        ZipFile zipFile = null;

        List<String> unzipFiles = new ArrayList<String>();

        try {
            zipFile = new ZipFile(src, charset);

            if (!destDirectory.endsWith(File.separator)) {
                destDirectory += File.separator;
            }
            for (Enumeration entries = zipFile.getEntries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                File destFile = new File(destDirectory + entry.getName());

                if (entry.isDirectory()) {// 是目录，则创建
                    destFile.mkdirs();
                } else {
                    // 是文件
                    unzipFiles.add(destDirectory + entry.getName());
                    // 如果指定文件的父目录不存在,则创建
                    File parent = destFile.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                    bis = new BufferedInputStream(zipFile.getInputStream(entry));
                    bos = new BufferedOutputStream(new FileOutputStream(destFile));
                    int len = 0;
                    byte[] buf = new byte[BUFFER_SIZE];
                    while ((len = bis.read(buf)) != -1) {
                        bos.write(buf, 0, len);
                    }
                    bos.flush();
                }
            }
            logger.info("Unzip file " + src + " to " + destDirectory + " with charset " + charset + " successful.");
            return unzipFiles;
        } finally {
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(bis);
            if (zipFile != null) {
                zipFile.close();
            }
        }
    }

    /**
     * 压缩zip文件目录
     *
     * @param srcDir
     * @param destFile
     */
    public static void zipDirectory(String srcDir, String destFile) {
        File file = new File(srcDir);
        if (!file.exists()) {
            throw new RuntimeException(srcDir + "不存在！");
        }

        File dest = new File(destFile);

        Project prj = new Project();
        Zip zip = new Zip();
        zip.setProject(prj);
        zip.setDestFile(dest);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setDir(file);
        //fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");
        //fileSet.setExcludes(...); 排除哪些文件或文件夹
        zip.addFileset(fileSet);

        zip.execute();
    }

}
