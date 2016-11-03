import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.List;

/**
 * CodeTester.java
 * Very easy tool to create your own Java code tester. Simply extend this class,
 * make your test methods that start with an underscore and are public void with
 * no parameters: public void _myTestMethod() {}, and then call this class's
 * runTests() function!
 *
 * For some simper and easier to follow information, simply read this project's
 * README.md :)
 *
 * @author Ofek Gila
 * @since  10/28/16
 */
public abstract class CodeTester {

	/**
	 * Prevent this class from running with assertions disabled.
	 *
	 * If assertions are disabled, then a RuntimeException will be
	 * thrown upon class loading.
	 */
	static {
		boolean hasAssertEnabled = false;
		assert hasAssertEnabled = true; // rare - an intentional side effect!
		if (!hasAssertEnabled) {
			System.out.println();
			throw new RuntimeException("Asserts must be enabled to test "
				+ "code. Please run with -ea, ex: java -ea <class name>");
		}
	}

	/**
	 * All test methods should start with the test method prefix and then a
	 * capital letter in order to be registered by this class.
	 */
	public static final String TEST_METHOD_PREFIX = "_";
	private int testsOk, testsFail, testsSkipped, testsTotal;
	private List<CodeError> codeErrors;

	public CodeTester() {
		testsOk = testsFail = testsSkipped = testsTotal = 0;
		codeErrors = new ArrayList<CodeError>();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD})
	@Inherited
	@interface SkipTest {}

	/**
	 * Runs a single test given a {@link Method}. Skips methods that should be
	 * skipped, and adds {@link CodeError}s to the error list.
	 * @param testMethod the testing {@link Method}
	 */
	private void runTest(Method testMethod) {
		String methodName = parseMethodName(testMethod.getName());
		System.out.printf("%-50s --> ", methodName);

		if (skipTest(testMethod)) {
			System.out.println("skipped");
			testsSkipped++;
			return;
		}

		try {
			testMethod.invoke(this);
			System.out.println("ok");
			testsOk++;
		} catch (InvocationTargetException | IllegalAccessException ie) {
			System.out.println("FAIL");
			codeErrors.add(new CodeError(ie.getCause(), methodName));
			testsFail++;
		} catch (AssertionError | Exception e) {
			System.out.println("FAIL");
			codeErrors.add(new CodeError(e, methodName));
			testsFail++;
		}
		testsTotal++;
	}

	/**
	 * Returns whether or not a test should be skipped.
	 * @param  testMethod the {@link Method} of the test
	 * @return            true if skip, false otherwise
	 */
	private boolean skipTest(Method testMethod) {
		return testMethod.getAnnotation(SkipTest.class) != null;
	}

	/**
	 * Prints the test summary, iterating through {@link CodeError}s.
	 * @param elapsedTime the time in seconds it took to run the tests
	 */
	private void printSummary(double elapsedTime) {
		System.out.println();

		if (testsFail == 0)
			System.out.printf("All tests passed, in %f seconds!\n",
				elapsedTime);
		else {
			System.out.printf("!!!! %d test%s failed out of %d tests total "
				+ "in %f seconds.\n", testsFail, testsFail == 1 ? "":"s",
				testsTotal, elapsedTime);

			for (CodeError codeError : codeErrors) {
				System.out.printf("\n%s -> FAIL\n\n", codeError.getMethodName());
				codeError.printStackTrace();
			}
		}
		System.out.println();
	}

	/**
	 * Finds tests for this class, runs them, and prints the test summary.
	 */
	public void runTests() {
		System.out.printf("\nRunning tests:\n\n");
		Class c = getClass();
		double startTime = System.nanoTime();
		for (Method method : c.getDeclaredMethods())
			if (isTestMethod(method))
				runTest(method);
		printSummary((System.nanoTime() - startTime) / 1E9);
	}

	/**
	 * Checks whether or not a given {@link Method} is a test method.
	 * @param  method the {@link Method} to test
	 * @return        true if valid test method, false otherwise
	 */
	private boolean isTestMethod(Method method) {
		String methodName = method.getName();
		int prefixLength = TEST_METHOD_PREFIX.length();

		return methodName.length() > prefixLength + 1 &&
			methodName.substring(0, prefixLength).equals(TEST_METHOD_PREFIX) &&
			Modifier.isPublic(method.getModifiers()) &&
			method.getParameterCount() == 0;
	}

	/**
	 * Converts java method name into a descriptive name.
	 * @param  methodName the name of the java method
	 * @return            the descriptive name (spaces separated)
	 */
	private String parseMethodName(String methodName) {
		String testName = "";
		char tempChar;
		boolean firstChar = true;

		for (int i = TEST_METHOD_PREFIX.length(); i < methodName.length(); i++) {
			tempChar = methodName.charAt(i);
			if (firstChar) {
				testName += Character.toUpperCase(tempChar);
				firstChar = false;
				continue;
			}
			if (Character.isUpperCase(tempChar))
				testName += " ";
			testName += Character.toLowerCase(tempChar);
		}

		return testName;
	}

	public void assertEqual(String s1, String s2, boolean equal) {
		assert s1.equals(s2) == equal :
			String.format("\"%s\" %s \"%s\"", s1, equal ? "!=":"=", s2);
	}

	public void assertEqual(int i1, int i2, boolean equal) {
		assert (i1 == i2) == equal :
			String.format("%d %s %d!", i1, equal ? "!=":"=", i2);
	}

	public void assertNull(Object obj, boolean nll) {
		assert (obj == null) == nll :
			String.format("%s %s null.", obj.toString(), nll ? "!=":"=");
	}

	public void assertEqual(Object obj1, Object obj2, boolean equal) {
		assert obj1.equals(obj2) == equal :
			String.format("%s %s %s", obj1.toString(), equal ? "!=":"=",
				obj2.toString());
	}
}
