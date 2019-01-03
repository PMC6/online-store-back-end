/**
 * @author： fanzhonghao
 * @date: 19-1-2 09 57
 * @version: 1.0
 * @description:
 */
package com.fenlan.spring.shop.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBService {
    public boolean backup() throws Exception{
        Process process = null;
        List<String> processList = new ArrayList<String>();
        try {
            process = Runtime.getRuntime().exec("sh ~/Downloads/backup.sh");
//            process = Runtime.getRuntime().exec("sh /home/fan/Desktop/backup.sh");
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                processList.add(line);
            }
            input.close();
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
        return true;
    }
}
