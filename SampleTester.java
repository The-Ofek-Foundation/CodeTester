public class SampleTester extends CodeTester {

	public void _testPassTest() {
		assert 1 == 1 : "This error shouldn't be happening";
		assertEqual(1, 1, true);
		assertEqual("hello", "goodbye", false);
	}

	@SkipTest
	public void _testSkipTest() {
		assert 1 == 2 : "This test should have been skipped!";
	}

	public void _testStringAssertion() {
		assertEqual("hello", "goodbye", true);
	}

	public void _testNullPointerError() {
		String str = null;
		str.equals("five");
	}

	public static void main(String... pumpkins) {
		SampleTester st = new SampleTester();
		st.runTests();
	}
}