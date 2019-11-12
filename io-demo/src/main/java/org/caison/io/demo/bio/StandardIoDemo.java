package org.caison.io.demo.bio;

import java.io.*;

/**
 * @author ChenCaihua
 * @date 2019年10月25日
 */
public class StandardIoDemo {

    public static void main(String[] args) {
        try {
            StringBuilder str = new StringBuilder();
            char[] buf = new char[1024];
            FileReader f = new FileReader("file.txt");
            while (f.read(buf) > 0) {
                str.append(buf);
            }
            System.out.println(str.toString());
        } catch (IOException e) {
        }

    }

}
