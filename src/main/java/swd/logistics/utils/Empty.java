package swd.logistics.utils;

import java.util.List;

public class Empty {

    public static boolean isEmpty(Object object){
        if (object == null){
            return true;
        }
        if (object instanceof List){
           return  ((List) object).size() == 0;
        }
        if (object instanceof String){
            return ((String) object).trim().equals("");
        }
        return false;
    }

    public static boolean isNotEmpty(Object object){
        return !isEmpty(object);
    }
}
