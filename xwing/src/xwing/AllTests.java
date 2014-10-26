package xwing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CallTreeTest.class, CallGraphParserTest.class })
public class AllTests {

}
