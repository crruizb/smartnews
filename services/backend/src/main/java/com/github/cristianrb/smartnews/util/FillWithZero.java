package com.github.cristianrb.smartnews.util;

public class FillWithZero {

    public static String fillWithZero(int number) {
        if (number < 10) return "0" + number;
        return String.valueOf(number);
    }
}
