package Util;

import enums.RT;
import enums.TO;
import org.apache.commons.lang3.BooleanUtils;

public class Mistake {

    Boolean rs, nb, ch;

    public Mistake(Boolean rs, Boolean nb, Boolean ch) {
        this.rs = rs;
        this.nb = nb;
        this.ch = ch;
    }

    public Mistake(RT rt, TO to) {
        if(to == TO.add) {
            markToAdd(rt);
        } else if(to == TO.remove) {
            markToRemove(rt);
        }
    }

    public void markToAdd(RT rt) {
        if(rt == RT.rs)
            rs = true;
        if(rt == RT.nb)
            nb = true;
        if(rt == RT.ch)
            ch = true;
    }

    public void markToRemove(RT rt) {
        if(rt == RT.rs)
            rs = false;
        if(rt == RT.nb)
            nb = false;
        if(rt == RT.ch)
            ch = false;
    }

    public boolean isNeedToAdd(RT rt) {
        if(rt == RT.rs)
            return BooleanUtils.isTrue(rs);
        if(rt == RT.nb)
            return BooleanUtils.isTrue(nb);
        if(rt == RT.ch)
            return BooleanUtils.isTrue(ch);
        throw new IllegalArgumentException("Wrong realtyType " + rt);
    }

    public boolean isNeedToRemove(RT rt) {
        if(rt == RT.rs)
            return BooleanUtils.isFalse(rs);
        if(rt == RT.nb)
            return BooleanUtils.isFalse(nb);
        if(rt == RT.ch)
            return BooleanUtils.isFalse(ch);
        throw new IllegalArgumentException("Wrong realtyType " + rt);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if(BooleanUtils.isTrue(rs))
            result.append("добавить в правила по Вторичке; ");
        else if(BooleanUtils.isFalse(rs))
            result.append("не распарсилось во Вторичке; ");

        if(BooleanUtils.isTrue(nb)) {
            if(!result.toString().contains("добавить в правила"))
                result.append("добавить в правила по ");
            result.append("Новостройкам; ");
        } else if(BooleanUtils.isFalse(nb)) {
            if(!result.toString().contains("не распарсилось"))
                result.append("не распарсилось в ");
            result.append("Новостройках; ");
        }

        if(BooleanUtils.isTrue(ch)) {
            if(!result.toString().contains("добавить в правила") ||
                    BooleanUtils.isFalse(nb))
                result.append("добавить в правила по ");
            result.append("Загородной; ");
        } else if(BooleanUtils.isFalse(ch)) {
            if(!result.toString().contains("не распарсилось") ||
                    BooleanUtils.isTrue(nb))
                result.append("не распарсилось в ");
            result.append("Загородной; ");
        }

        return result.toString();
    }
}
