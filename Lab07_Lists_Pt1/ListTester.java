public class ListTester {
    public static void main(String[] args) {
        ListTester tester = new ListTester();
        tester.runTest();
    }

    private void runTest() {
        test_A_addToRearB_AB();
        test_A_add0B_BA();
    }

    private void test_A_addToRearB_AB() {
        try {
            System.out.println("Scenario 7: [A] -> addToRear(B) -> [A,B]");
            
        } catch (Exception e) {
            System.out.println("Unable to Complete tests");
            e.printStackTrace();
        }
    }

    private void test_A_add0B_BA() {
        try {
            System.out.println("Scenario 10: [A] -> add(0,B) -> [B,A]");
        } catch (Exception e) {
            System.out.println("Unable to Complete tests");
            e.printStackTrace();
        }
    }

}
