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

    boolean open();

    void writeline(String value);

    void close();
}
