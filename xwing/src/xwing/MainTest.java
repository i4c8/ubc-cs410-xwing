package xwing;


/**
 * Created by Alex on 2014-10-24.
 */
public class MainTest {
    public static void main(String[] args) {
        CallgraphParser test = new CallgraphParser();
        String[] toCompare = {"result1.txt","result2.txt"};
        test.parseList(toCompare);
    }
}