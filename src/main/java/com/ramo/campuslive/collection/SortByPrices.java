package com.ramo.campuslive.collection;

import com.ramo.campuslive.bean.Gift;

import java.util.Comparator;

/**
 * 复写比较 AirRank对象比较 按污染值排序 从大到小
 *
 * @author ramo
 */
public class SortByPrices implements Comparator<Gift> {

    public int compare(Gift arg0, Gift arg1) {
        if (arg0 != null && arg1 != null) {
            if (arg0.getPrice() < arg1.getPrice()) {
                return 1;
            }
        }
        return -1;

    }
}
