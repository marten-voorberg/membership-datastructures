package datastructures;

public class OpenAddressingHashTableTest extends MembershipTest {
    @Override
    protected Membership getInstance() {
        return new OpenAddressingHashTable(4096);
    }
}
