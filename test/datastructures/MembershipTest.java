package datastructures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class MembershipTest {
    protected abstract Membership getInstance();
    private Membership data;
    public static final String INSERT_FILEPATH = "test/datastructures/data/test-insert.data";
    public static final String NON_MEMBER_FILEPATH = "test/datastructures/data/test-is-member.data";


    private static final List<Integer> ELEMENTS = new ArrayList<>(1000);

    @BeforeEach
    public void setUp() {
        for (int i = 1; i < 1000; i++) {
            ELEMENTS.add(i * 200);
        }

        data = getInstance();
        for (int element : ELEMENTS) {
            data.insert(element);
        }
    }

    @Test
    public void allElementsContained() {
        for (int element : ELEMENTS) {
            Assertions.assertTrue(data.isMember(element), String.format("%d should be contained", element));
        }
    }

    @Test
    public void elementsNotContained() {
        final int MAX = 10000;
        for (int element = 1; element < MAX; element += 1) {
            try {
                Assertions.assertEquals(data.isMember(element), ELEMENTS.contains(element));
            } catch (ArrayIndexOutOfBoundsException e) {
                fail(String.format("%d", element));
            }
        }
    }

    protected List<Integer> getInsertNumbers(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)))) {
            return reader.lines()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
            return new ArrayList<>();
        }
    }

    private Membership getFileFilledDataStructure() {
        Membership ds = getInstance();

        for (Integer number : getInsertNumbers(INSERT_FILEPATH)) {
            ds.insert(number);
        }
//        System.out.println(Arrays.toString(((DynamicHashTableBitmap) ds).partitionSizes));
        return ds;
    }

    @Test
    public void testAllInsertedElementsArePresent() {
       Membership ds = getFileFilledDataStructure();

       for (Integer number : getInsertNumbers(INSERT_FILEPATH)) {
           assertTrue(ds.isMember(number),
            String.format("%d should be in the data structure", number));
       }
    }

    @Test
    public void testAllNonInsertedElementsAreNotPresent() {
        Membership ds = getFileFilledDataStructure();

        for (Integer number : getInsertNumbers(NON_MEMBER_FILEPATH)) {
            assertFalse(ds.isMember(number),
                String.format("%d should NOT be in the data structure", number));
        }
    }

    @Test
    public void testElementsAreDeleted() {
        Membership ds = getFileFilledDataStructure();
        for (Integer number : getInsertNumbers(INSERT_FILEPATH)) {
            assertTrue(ds.isMember(number), String.format("%d should still be a member!", number));
            ds.delete(number);
            assertFalse(ds.isMember(number), String.format("%d should be deleted!", number));
        }
    }
}
