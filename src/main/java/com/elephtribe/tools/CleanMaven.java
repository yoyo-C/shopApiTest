package com.elephtribe.tools;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bytes on 2017/7/18.
 */
public class CleanMaven {
        public void clear() {
            File file = new File("/Users/Bytes/Documents/workspace/yuyu/repos");
            if (file.exists()) {
                clearFile(file);
            }
        }

        public static void main(String[] args) {
            CleanMaven cm = new CleanMaven();
            cm.clear();
        }

        public void clearFile(File file) {
            File[] files = file.listFiles();
            boolean flag = false;
            for (int i = 0; i < files.length; i++) {
                File temp = files[i];
                if (temp.isDirectory()) {
                    clearFile(temp);
                } else {
                    if (temp.getName().endsWith("jar")) {
                        flag = true;
                    }
                }
            }
            Pattern pt = Pattern.compile("^[\\d.]*$");
            Matcher matcher = pt.matcher(file.getName());
            if (!flag && matcher.find()) {
                boolean rs = deleteDir(file);
                if (rs) {
                    System.out.println("删除：" + file.getAbsolutePath());
                }

            }
        }

        public boolean deleteDir(File dir) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        }

    }
