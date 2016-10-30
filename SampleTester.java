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

	private void stringAssertionHelperMethod(String s1, String s2) {
		assertEqual(s1, s2, true);
	}

	public void _testStringAssertionWithIdenticalStrings() {
		stringAssertionHelperMethod("same str", "same str");
	}

	public void _testStringAssertionWithDifferentStrings() {
		stringAssertionHelperMethod("hello", "goodbye");
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