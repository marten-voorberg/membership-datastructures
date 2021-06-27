package datastructures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class BinaryPTrieTest {
    private List<Integer> getInsertNumbers(String filepath) {
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

    private static final String INSERT_FILEPATH = "test/datastructures/data/test-insert.data";
    private static final String NON_MEMBER_FILEPATH = "test/datastructures/data/test-is-member.data";

    private Membership getFileFilledDataStructure() {
        BinaryTrie2 ds = new BinaryTrie2();

        for (Integer number : getInsertNumbers(INSERT_FILEPATH)) {
            ds.insert(number);
        }

        return new BinaryPTrie(ds);
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
}
