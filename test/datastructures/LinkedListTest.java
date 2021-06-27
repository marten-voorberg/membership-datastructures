package datastructures;

public class LinkedListTest extends MembershipTest {
    @Override
    protected Membership getInstance() {
        return new LinkedList();
    }
}
