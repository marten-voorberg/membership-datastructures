package datastructures;

public class SplayTreeTest extends MembershipTest {
    @Override
    protected Membership getInstance() {
        return new SplayTree();
    }
}
