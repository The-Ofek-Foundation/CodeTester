# CodeTester
Make easy automated tests for your Java projects.

### Usage
You can get your code tests running in less than five minutes. When making your testing class, simply do the following three things:

1. Extend CodeTester
```java
public class SampleTester extends CodeTester {
```
<ol start="2">
	<li>Create your public test methods with descriptive method names starting with an underscore ("_"). Make sure they have no parameters.</li>
</ol>

```java
public void _testFileDeletion() {
	File testFile = new File("file_to_delete.txt");
	assert testFile.exists() : "File doesn't exist!";
	assert testFile.delete() : "Error deleting file!";
}
```
<ol start="3">
	<li>Call the runTests method. The program automatically finds and calls your tests.</li>
</ol>
```java
public static void main(String[] args) {
	SampleTester st = new SampleTester();
	st.runTests();
	// that's it!
}
```

#### What if I want to skip one of my tests?
No problem, just add the @SkipTest annotation!
```java
@SkipTest
public void _testToSkip() { ... }
```

#### What if I want to reuse code in my tests?
You can always create helper methods for your tests, just make sure not to make their names start with an underscore ("_").
```java
private void testFileDeletionHelper(String fileName) {
	File testFile = new File(fileName);
	assert testFile.exists() : "File doesn't exist!";
	assert testFile.delete() : "Error deleting file!";
	assert testFile.exists() == false : "File not deleted!";
}

public void _testFileDeletion() {
	testFileDeletionHelper("testfile.txt");
}

public void _testDirectoryDeletion() {
	testFileDeletionHelper("testdir/");
}
```

### And that's it!
It's really as simple as that, everything is handled automatically!

We even provide several helper methods of testing, including assertEqual, which checks if two strings/ints/objects are equal.

#### Sample output:
Sample output for java -ea SampleTester
```batch
Running tests:

Test skip test                                     --> skipped
Test pass test                                     --> ok
Test string assertion with identical strings       --> ok
Test string assertion with different strings       --> FAIL
Test null pointer error                            --> FAIL

!!!! 2 tests failed out of 4 tests total in 0.012415 seconds.

Test string assertion with different strings -> FAIL

CodeError: java.lang.AssertionError: "hello" != "goodbye"
	at SampleTester.stringAssertionHelperMethod(SampleTester.java:15)
	at SampleTester._testStringAssertionWithDifferentStrings(SampleTester.java:23)
	at SampleTester.main(SampleTester.java:33)

Test null pointer error -> FAIL

CodeError: java.lang.NullPointerException
	at SampleTester._testNullPointerError(SampleTester.java:28)
	at SampleTester.main(SampleTester.java:33)

```
