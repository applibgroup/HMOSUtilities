package com.matthewtamlin.java_utilities.checkers;

import java.util.Objects;

public class NullChecker {
    public static Object checkNotNull(Object objects,String s){
        if(objects!=null){
            return objects;
        }
        return false;
    }
}
