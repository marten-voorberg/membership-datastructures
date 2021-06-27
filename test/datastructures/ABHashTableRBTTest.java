package datastructures;

public class ABHashTableRBTTest extends MembershipTest {
    @Override
    protected Membership getInstance() {
        return new ABHashTableRBT(1000);
    }
}
