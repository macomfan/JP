/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

/**
 *
 * @author u0151316
 */
public interface IWordCodec {
    byte[] encodeToBytes();
    void decodeFromBytes(byte[] bytes) throws Exception;
}
