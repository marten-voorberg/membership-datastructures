package datastructures;

import datastructures.Membership;
import datastructures.MembershipTest;
import datastructures.vEBBitmapTree;

public class vEBBitmapTreeTest extends MembershipTest {
    @Override
    protected Membership getInstance() {
        return new vEBBitmapTree(32);
    }
}
