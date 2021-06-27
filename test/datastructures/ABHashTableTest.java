package datastructures;

public class ABHashTableTest extends MembershipTest {
    @Override
    protected Membership getInstance() {
        return new ABHashTable(1000);
    }
}
