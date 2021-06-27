package datastructures.data;

import datastructures.Membership;
import datastructures.MembershipTest;
import datastructures.vEBBitmapMinTree;
import datastructures.vEBBitmapTree;

public class vEBBitmapMinTreeTest extends MembershipTest {
    @Override
    protected Membership getInstance() {
        return new vEBBitmapMinTree(32);
    }
}
