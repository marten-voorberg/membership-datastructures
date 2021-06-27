package datastructures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicHashTableBitmapTest extends MembershipTest {

    @Override
    protected Membership getInstance() {
        return new DynamicHashTableBitmap(10);
    }

    @Test
    public void testInsertOnPeakData() {
        DynamicHashTableBitmap htb = new DynamicHashTableBitmap(20);
        List<Integer> insertNumbers = getInsertNumbers(HashTableBitmapTest.INSERT_FILEPATH);
        insertNumbers.add(Integer.MIN_VALUE);
        for (int number: insertNumbers) {
            htb.insert(number);
        }

        for (int number: insertNumbers) {
            assertTrue(htb.isMember(number), String.format("%d should be a member", number));
        }
    }

    @Test
    public void testDeleteOnPeakData() {
        DynamicHashTableBitmap htb = new DynamicHashTableBitmap(20);
        List<Integer> allNumbers = getInsertNumbers(HashTableBitmapTest.INSERT_FILEPATH);
        Set<Integer> insertNumbers = new HashSet<>(allNumbers);

        for (int number: insertNumbers) {
            htb.insert(number);
        }

        for (int number: insertNumbers) {
            assertTrue(htb.isMember(number), String.format("%d should be a member", number));
            htb.delete(number);
            assertFalse(htb.isMember(number), String.format("%d not should be a member", number));
        }
    }
}
