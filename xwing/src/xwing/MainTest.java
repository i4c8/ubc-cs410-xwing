package xwing;


/**
 * Created by Alex on 2014-10-24.
 */
public class MainTest {
    public static void main(String[] args) {
        CallgraphParser test = new CallgraphParser();
     //   test.RemoveJava("result1.txt");
      //  test.RemoveDuplicates("result1.txt");
       // test.SplitClassesMethods("result1.txt");
      //  test.Added("result1.txt", "result2.txt");
     //   test.Removed("result1.txt", "result2.txt");

        String[] result1 = test.SplitClassesMethods(test.RemoveDuplicates(test.RemoveJava("result1.txt")));
        String method1 = result1[0];

        String[] result2 = test.SplitClassesMethods(test.RemoveDuplicates(test.RemoveJava("result2.txt")));
        String method2 = result2[0];

        test.Added(method1, method2);
        test.Removed(method1, method2);
    }
}