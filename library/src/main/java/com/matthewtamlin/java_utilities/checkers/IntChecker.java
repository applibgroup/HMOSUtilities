package com.matthewtamlin.java_utilities.checkers;

public class IntChecker {

    public static boolean checkGreaterThanOrEqualTo(int des,int num,String s){
        return des <= num;
    }

    public static boolean checkLessThan(int offset,int dataLength,String s){
        return offset < dataLength;
    }
    public static  boolean checkLessThanOrEqualTo(int length,int dataLength,String s){
        return length > dataLength;
    }
}
