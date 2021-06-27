package datastructures;

public class vEBTreeTest extends MembershipTest {
    @Override
    protected Membership getInstance() {
        return new vEBTree(32);
    }
}
