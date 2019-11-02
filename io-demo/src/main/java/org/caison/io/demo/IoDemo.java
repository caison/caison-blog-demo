package org.caison.io.demo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author ChenCaihua
 * @date 2019年10月25日
 */
public class IoDemo {

    public static void main(String[] args) throws IOException {
        File file = new File("io-demo/test-file.txt");
        if (!file.exists()){
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("testFile......");
        fileWriter.flush();
        fileWriter.close();

    }

}
