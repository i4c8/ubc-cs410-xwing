package xwing;
import com.google.gson.*;


import java.io.*;
import java.util.HashSet;




public class CallgraphParser implements Parser<Object> {

    private BufferedReader br;
    private BufferedWriter bw;

    public CallgraphParser(){
    }

    public String CallgraphToJSON(String file){
        String append = ".json";
        String fileName = file.concat(append);

        String line;
        String[] sourceAndTarget;
        String[] sourceClassMethod;
        String[] targetClassMethod;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CallTree tree = new CallTree();

        try {
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(fileName));
            while ((line = br.readLine()) != null){
                //Splits each line into Source and Target
                //Then splits each of those into Class and Method
                sourceAndTarget = line.split("\\s");
                sourceClassMethod = sourceAndTarget[0].split(":");
                targetClassMethod = sourceAndTarget[1].split(":");

                //Builds up the tree used by GSON to create the JSON
                tree.addMethod(sourceClassMethod[2]);
                tree.addMethod(targetClassMethod[1]);
                tree.addClass(sourceClassMethod[1]);
                tree.addClass(targetClassMethod[0].split("\\)")[1]);
                tree.addMethodToClass(sourceClassMethod[1], sourceClassMethod[2]);
                tree.addMethodToClass(targetClassMethod[0].split("\\)")[1], targetClassMethod[1]);
                tree.addConnection(sourceClassMethod[2], targetClassMethod[1]);
            }
            String jsonOutput = gson.toJson(tree);
            bw.write(jsonOutput);
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }



    //Creates a new file removing any default java classes and methods from the result
    //@return the filename of the result
    public String RemoveJava(String file){
        String prepend = "noJava-";
        String fileName = prepend.concat(file);
        String line;
        try {
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(fileName));
            while ((line = br.readLine()) != null){
                if (!line.contains("java")){
                    bw.write(line+"\n");
                }
            }
            bw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    //Creates two files, one containing only methods and the other only classes
    //@return String[0] = fileName of the classes
    //        String[1] = fileName of the methods
    public String[] SplitClassesMethods(String file){
        String prepend = "methods-";
        String prepend2 = "classes-";
        String[] fileNames = new String[2];
        fileNames[0] = prepend.concat(file);
        fileNames[1] = prepend2.concat(file);
        String line;
        try {
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(fileNames[0]));
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(fileNames[1]));
            while ((line = br.readLine()) != null){
                if (line.charAt(0)=='M'){
                    bw.write(line+"\n");
                }
                else {
                    bw2.write(line + "\n");
                }
            }
            bw.close();
            bw2.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    //Creates a new file with all duplicates removed
    //@return String = name of file with no duplicates
    public String RemoveDuplicates(String file){
        String prepend = "noDups-";
        String fileName = prepend.concat(file);
        String line;
        HashSet<String> set = new HashSet<String>();
        try {
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(fileName));
            while ((line = br.readLine()) != null){
                if (!set.contains(line)){
                    set.add(line);
                    bw.write(line+"\n");
                }
            }
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    // Creates a new file showing all new connections added in file2 compared to file1
    // @return String = name of new file containing additions
    public String Added(String file1, String file2){
        String mid = "-added-";
        String fileName = file1.concat(mid).concat(file2);
        String line;
        HashSet<String> set = new HashSet<String>();
        try {
            br = new BufferedReader(new FileReader(file1));
            bw = new BufferedWriter(new FileWriter(fileName));
            while ((line = br.readLine()) != null){
                if (!set.contains(line))
                    set.add(line);
            }
            br.close();
            br = new BufferedReader(new FileReader(file2));
            while ((line = br.readLine()) != null){
                if (!set.contains(line)){
                    bw.write(line+"\n");
                }
            }
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    // Creates a new file showing all connections removed in file2 compared to file1
    // @return String = name of new file containing subtractions
    public String Removed(String file1, String file2) {
        String mid = "-removed-";
        String fileName = file1.concat(mid).concat(file2);
        String line;
        HashSet<String> set = new HashSet<String>();
        try {
            br = new BufferedReader(new FileReader(file2));
            bw = new BufferedWriter(new FileWriter(fileName));
            while ((line = br.readLine()) != null){
                if (!set.contains(line))
                    set.add(line);
            }
            br.close();
            br = new BufferedReader(new FileReader(file1));
            while ((line = br.readLine()) != null){
                if (!set.contains(line)){
                    bw.write(line+"\n");
                }
            }
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }



	@Override
	public Object parse(File jcOutput) {
		// TODO Auto-generated method stub
		return null;
	}

}
