package university.spring_lab3_notifications;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SpyTest {

    @Test
    void testSpyListBehavior() {
        List<String> list = new ArrayList<>();
        List<String> spyList = spy(list);

        spyList.add("Spring");

        verify(spyList, times(1)).add("Spring");

        assertEquals(1, spyList.size());
        assertEquals("Spring", spyList.get(0));
    }
}
