import Util.Mistake;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MistakeTest {

    @Test
    public void test() {
        assertEquals("добавить в правила по Вторичке; ",                            new Mistake(true, null, null).toString());
        assertEquals("добавить в правила по Новостройкам; ",                        new Mistake(null, true, null).toString());
        assertEquals("добавить в правила по Загородной; ",                          new Mistake(null, null, true).toString());
        assertEquals("добавить в правила по Вторичке; Новостройкам; ",              new Mistake(true, true, null).toString());
        assertEquals("добавить в правила по Вторичке; Загородной; ",                new Mistake(true, null, true).toString());
        assertEquals("добавить в правила по Новостройкам; Загородной; ",            new Mistake(null, true, true).toString());
        assertEquals("добавить в правила по Новостройкам; Загородной; ",            new Mistake(null, true, true).toString());
        assertEquals("добавить в правила по Вторичке; Новостройкам; Загородной; ",  new Mistake(true, true, true).toString());

        assertEquals("не распарсилось во Вторичке; ",                               new Mistake(false, null, null).toString());
        assertEquals("не распарсилось в Новостройках; ",                            new Mistake(null, false, null).toString());
        assertEquals("не распарсилось в Загородной; ",                              new Mistake(null, null, false).toString());
        assertEquals("не распарсилось во Вторичке; Новостройках; ",                 new Mistake(false, false, null).toString());
        assertEquals("не распарсилось во Вторичке; Загородной; ",                   new Mistake(false, null, false).toString());
        assertEquals("не распарсилось в Новостройках; Загородной; ",                new Mistake(null, false, false).toString());
        assertEquals("не распарсилось в Новостройках; Загородной; ",                new Mistake(null, false, false).toString());
        assertEquals("не распарсилось во Вторичке; Новостройках; Загородной; ",     new Mistake(false, false, false).toString());
    }

}
