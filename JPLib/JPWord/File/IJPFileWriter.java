/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.File;

/**
 *
 * @author u0151316
 */
public interface IJPFileWriter {

    IJPFileWriter clone(String filename);
    
    void open() throws Exception;

    void writeline(String value) throws Exception;

    void close() throws Exception;
}
