package datastructures;

public class SuperImposedBitmapTest extends MembershipTest {
    @Override
    protected Membership getInstance() {
        return new SuperImposedBitmap();
    }
}
