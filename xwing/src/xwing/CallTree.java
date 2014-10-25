package xwing;

import java.util.ArrayList;

/**
 * Created by Alex on 2014-10-24.
 */
public class CallTree {
    private ArrayList nodes;
    private ArrayList links;
    private ArrayList classes;

    public CallTree() {
        nodes = new ArrayList();
        links = new ArrayList();
        classes = new ArrayList();
    }


    public void addMethod(String method) {
        methodNode toAdd = new methodNode(method);
        boolean add = true;
        for (Object method1 : nodes) {
            methodNode toTest = (methodNode) method1;
            if (toAdd.equals(toTest)) {
                add = false;
            }
        }
        if (add) {
            nodes.add(toAdd);
        }
    }

    public void addClass(String className) {
        classNode toAdd = new classNode(className);
        boolean add = true;
        for (Object aClass : classes) {
            classNode toTest = (classNode) aClass;
            if (toAdd.equals(toTest)) {
                add = false;
            }
        }
        if (add) {
            classes.add(toAdd);
        }
    }

    public void addConnection(String method1, String method2){
        int index1 = nodes.indexOf(method1);
        int index2 = nodes.indexOf(method2);
        for (int i=0; i<nodes.size(); i++){
            methodNode toTest = (methodNode) nodes.get(i);
            if (toTest.getName().equals(method1)) {
                index1 = i;
            }
            if (toTest.getName().equals(method2)) {
                index2 = i;
            }
        }
        links.add(new connectionNode(index1, index2));
    }

    public void addMethodToClass(String className, String methodName) {

        int methodIndex = -1;
        int classIndex = -1;
        for (int i=0; i<nodes.size(); i++){
            methodNode toTest = (methodNode) nodes.get(i);
            if (toTest.getName().equals(methodName)) {
                methodIndex = i;
                break;
            }
        }

        for (int i=0; i<classes.size(); i++){
            classNode toTest = (classNode) classes.get(i);
            if (toTest.getName().equals(className)) {
                classIndex = i;
                break;
            }
        }

        if ((classIndex >= 0)&&(methodIndex >= 0)){
            classNode toEdit = (classNode) classes.get(classIndex);
            toEdit.addMethod(methodIndex);
        }

    }

    private class methodNode {
        String name;

        public methodNode(String methodName){
            name = methodName;
        }

        public boolean equals(methodNode toCompare){
            return (toCompare.getName().equals(name));
        }

        public String getName(){
            return name;
        }
    }

    private class connectionNode {
        int source;
        int target;

        public connectionNode(int source, int target){
            this.source = source;
            this.target = target;
        }
    }

    private class classNode {
        String name;
        ArrayList<Integer> contains;

        public classNode(String className) {
            name = className;
            contains = new ArrayList<Integer>();
        }

        public void addMethod(int methodLocation) {
            if (!contains.contains(methodLocation)) {
                contains.add(methodLocation);
            }
        }

        public String getName() {
            return name;
        }

        public boolean equals(classNode toCompare){
            return(toCompare.getName().equals(name));
        }
    }
}
