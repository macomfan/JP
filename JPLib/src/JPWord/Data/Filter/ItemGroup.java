/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data.Filter;

import JPWord.Data.Filter.IItemFilter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author u0151316
 */
public class ItemGroup {

    private List<Object> items_ = new LinkedList<>();
    private List<ItemGroup> groups_ = new LinkedList<>();

    private boolean sorted_ = false;
    private int itemIndex_ = 0;
    private int groupIndex_ = 0;

    public <T> ItemGroup(List<T> items) {
        items_.addAll(items);
    }

    private ItemGroup() {

    }

    public int getCount() {

        int count = items_.size();
        for (ItemGroup g : groups_) {
            count += g.getCount();
        }
        return count;

    }

    private List<Object> getItems() {
        List<Object> temp = new LinkedList<>();
        for (Object obj : items_) {
            Object t = (Object) obj;
            if (t != null) {
                temp.add(t);
            }
        }
        return temp;
    }

    public void shuffle() {
        groupIndex_ = 0;
        itemIndex_ = 0;
        shuffleItems();
        shuffleItems();
        for (ItemGroup group : groups_) {
            group.shuffle();
        }
    }

    private void shuffleItems() {
        if (items_.isEmpty()) {
            return;
        }
        Random random = new Random();
        List<Object> newList = new LinkedList<>();
        for (Object item : items_) {
            int size = newList.size();
            newList.add(random.nextInt(size + 1), item);
        }
        items_ = newList;
    }

    public void sort(List<IItemFilter> filters) {
        if (filters.isEmpty()) {
            return;
        }
        if (sorted_) {
            return;
            //throw new Exception("The RandomGroup has been sorted");
        }
        sortCurrent(filters.get(0));
        ArrayList<IItemFilter> tempList = new ArrayList<>();
        for (int i = 1; i < filters.size(); i++) {
            tempList.add(filters.get(i));
        }
        IItemFilter[] subSofters = new IItemFilter[tempList.size()];
        for (ItemGroup group : groups_) {
            group.sort(tempList.toArray(subSofters));
        }
        items_.clear();
        sorted_ = true;
    }

    public void sort(IItemFilter... filters) {
        List<IItemFilter> tempList = new LinkedList<>();
        for (int i = 0; i < filters.length; i++) {
            tempList.add(filters[i]);
        }
        ItemGroup.this.sort(tempList);
    }

    private void sortCurrent(IItemFilter filter) {
        int groupNumber = filter.buildChildGroup(getItems());
        for (int i = 0; i < groupNumber; i++) {
            groups_.add(new ItemGroup());
        }
        for (Object item : items_) {
            List<Integer> childGroupsIndexList = filter.distributeItem(item);
            for (Integer i : childGroupsIndexList) {
                groups_.get((int) i).items_.add(item);
            }
        }
    }
    
    public Object next() {
        if (items_.isEmpty()) {
            if (groups_.isEmpty()) {
                return null;
            } else if (groupIndex_ >= groups_.size()) {
                return null;
            } else {
                Object obj = groups_.get(groupIndex_).next();
                if (obj == null) {
                    groupIndex_++;
                    return next();
                } else {
                    return obj;
                }
            }
        } else if (itemIndex_ >= items_.size()) {
            return null;
        } else {
            return items_.get(itemIndex_++);
        }
    }

}
