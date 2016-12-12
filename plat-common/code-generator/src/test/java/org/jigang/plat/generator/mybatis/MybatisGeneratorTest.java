package org.jigang.plat.generator.mybatis;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by wjg on 2016/12/12.
 */
public class MybatisGeneratorTest {
    @Test
    public void generate() throws IOException {
        new MybatisGenerator().generator();
    }
}
