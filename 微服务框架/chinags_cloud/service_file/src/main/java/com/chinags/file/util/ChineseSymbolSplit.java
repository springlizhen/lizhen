package com.chinags.file.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Mark_Wang
 * @Date : 2020/1/10
 **/
public class ChineseSymbolSplit {
    public static List<Character> chSymSplits;

    static {
        chSymSplits = new ArrayList<Character>();
        chSymSplits.add('，');
        chSymSplits.add('。');
        chSymSplits.add('！');
        chSymSplits.add('；');
        chSymSplits.add('？');
    }

    public static boolean isIncludeChar(int srcChar) {
        for (int i = 0; i < chSymSplits.size(); i++) {
            if (chSymSplits.get(i) == srcChar) {
                return true;
            }
        }
        return false;
    }
}
