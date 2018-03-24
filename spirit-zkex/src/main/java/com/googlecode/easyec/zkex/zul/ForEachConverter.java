package com.googlecode.easyec.zkex.zul;

import org.zkoss.util.Maps;
import org.zkoss.zul.ListModelArray;

import java.io.Serializable;
import java.util.*;

public class ForEachConverter implements Serializable {

    private static final long serialVersionUID = -8351220490289705916L;

    public Object coerceToUi(Object val) {
        if ((val == null) || ((val instanceof List))) {
            return val;
        }

        Collection<Object> data = null;
        if ((val instanceof Collection)) {
            data = new ArrayList<>(((Collection) val).size());
            data.addAll((Collection) val);
        } else if ((val instanceof Map)) {
            data = new ArrayList<>(Maps.transferToSerializableEntrySet(((Map<?, ?>) val).entrySet()));
        } else if ((val instanceof ListModelArray)) {
            data = Arrays.asList(((ListModelArray) val).getInnerArray());
        } else if ((val instanceof Object[])) {
            data = Arrays.asList((Object[]) val);
        } else if (((val instanceof Class)) && (Enum.class.isAssignableFrom((Class) val))) {
            data = Arrays.asList(((Class) val).getEnumConstants());
        }

        return data;
    }
}
