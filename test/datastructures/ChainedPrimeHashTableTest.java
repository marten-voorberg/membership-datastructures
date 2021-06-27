package datastructures;

public class ChainedPrimeHashTableTest extends MembershipTest {
    @Override
    protected Membership getInstance() {
        return new ChainedDivisionHashTable(2 * -214748364);
    }
}
