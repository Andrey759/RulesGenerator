package Util;

import enums.RT;
import enums.TO;

import java.util.Collection;
import java.util.LinkedHashMap;

public class MistakeMap extends LinkedHashMap<String, Mistake> {

    public void putAllToAdd(Collection<String> mistakes, RT rt) {
        for(String mistake : mistakes) {
            if(super.containsKey(mistake)) {
                super.get(mistake).markToAdd(rt);
            } else {
                super.put(mistake, new Mistake(rt, TO.add));
            }
        }
    }

    public void putAllToRemove(Collection<String> mistakes, RT rt) {
        for(String mistake : mistakes) {

            if(super.containsKey(mistake)) {
                super.get(mistake).markToRemove(rt);
            } else {
                super.put(mistake, new Mistake(rt, TO.remove));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(String mistake : keySet()) {
            result
                    .append(mistake)
                    .append(":\t")
                    .append(get(mistake))
                    .append("\n");
        }
        return result.toString();
    }
}
