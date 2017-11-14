/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getContent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FORSAKEN MYSTERY
 */
public class GetContent {
    private static GetContent get=new GetContent();
    public static GetContent get(){
        return get;
    }
    public String GetContent(String filename) {
        try {
            return this.readFile(filename);
        } catch (IOException ex) {
            Logger.getLogger(GetContent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private String readFile(String filename) throws IOException {
        String content = null;
        File file = new File(filename); //for ex foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return content;
    }
}
