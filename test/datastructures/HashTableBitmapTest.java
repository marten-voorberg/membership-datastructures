package datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class HashTableBitmapTest {
    public static final String INSERT_FILEPATH = "test/datastructures/data/test-multiple-normal-insert.data";
    public static final String NON_MEMBER_FILEPATH = "test/datastructures/data/test-multiple-normal-false-is-member.data";

    private List<Integer> getInsertNumbers(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)))) {
            return reader.lines()
                .map(Integer::valueOf)
//                .map(x -> -x)
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
            return new ArrayList<>();
        }
    }

    private Membership getFileFilledDataStructure() {
        return new HashTableBitmap(getInsertNumbers(INSERT_FILEPATH));
    }

    @BeforeEach
    public void setup() {
        HashTableBitmap.SPARSENESS_THRESHOLD = 0.00001;
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

        List<Integer> nonPresent = getInsertNumbers(NON_MEMBER_FILEPATH);
        List<Integer> present = getInsertNumbers(INSERT_FILEPATH);
        nonPresent.removeAll(present);

        for (Integer number : nonPresent) {
            assertFalse(ds.isMember(number),
                String.format("%d should NOT be in the data structure", number));
        }
    }

    @Test
    public void testElementsAreDeleted() {
        Membership ds = getFileFilledDataStructure();
        List<Integer> presentNumbers = getInsertNumbers(INSERT_FILEPATH);
        Set<Integer> uniquePresentNumbers = new HashSet<>(presentNumbers);
        for (Integer number : uniquePresentNumbers) {
            assertTrue(ds.isMember(number), String.format("%d should still be a member!", number));
            ds.delete(number);
            assertFalse(ds.isMember(number), String.format("%d should be deleted!", number));
        }
    }
}
