/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.File;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author u0151316
 */
public class DefaultFileWriter implements IJPFileWriter {

    private File file_ = null;
    private BufferedWriter writer_ = null;

    public DefaultFileWriter(String filename) {
        file_ = new File(filename);
    }

    public DefaultFileWriter(File file) {
        file_ = file;
    }

    @Override
    public void open() throws Exception {
        if (!file_.exists()) {
            file_.createNewFile();
        }
    }

    @Override
    public void writeline(String value) throws Exception {
        if (writer_ == null) {
            writer_ = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file_), "utf-8"));
        }
        if (writer_ != null) {
            writer_.write(value);
            writer_.write("\r\n");
        }
    }

    @Override
    public void close() throws Exception {
        if (writer_ != null) {
            writer_.flush();
            writer_.close();
            writer_ = null;
        }
    }

}
