/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data.Filter;

import java.util.List;

/**
 *
 * @author u0151316
 */
public interface IItemFilter {

    int buildChildGroup(List<Object> items);

    List<Integer> distributeItem(Object item);
}
