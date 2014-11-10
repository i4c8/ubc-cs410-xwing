package calltree;
import com.google.gson.*;

import java.io.*;
import java.util.HashSet;




public class CallgraphParser implements Parser<Object> {

    private BufferedReader br;
    private BufferedWriter bw;
    private CallTree baseTree;
    private CallTree newTree;
    
    public CallgraphParser(){
    }

    //Takes a list of callGraphs to parse, returns the full JSON of each input
    //@return String[i] = fileName of JSON of links added in i'th step
    public String[] parseList(String[] toParse){
        String[] result = new String[toParse.length];
        for (int i=0; i<toParse.length; i++){
            if (i==0){
            	baseTree = this.buildTree(this.splitClassesMethods(this.removeDuplicates(this.removeJava(toParse[i], i), i), i)[0]);
                result[i] = this.callgraphToJSON(this.splitClassesMethods(this.removeDuplicates(this.removeJava(toParse[i], i), i), i)[0], i);
                this.cleanUp(i);
            }
            else {
                String file1 = this.splitClassesMethods(this.removeDuplicates(this.removeJava(toParse[i], i), i), i)[0];
                String file2 = this.splitClassesMethods(this.removeDuplicates(this.removeJava(toParse[i-1], i-1), i-1), i-1)[0];
                newTree = this.treeUpdate(this.added(file2, file1));
                result[i] = this.callTreeToJSON(newTree, i);
                this.cleanUp(i, i-1);
            }
        }
        return result;
    }

    public void cleanUp(int i, int j){
        cleanUp(i);
        cleanUp(j);
        File toDelete = new File("temp"+j+".methods-added-temp"+i+".methods");
        toDelete.delete();
    }

    public void cleanUp(int i){
        File toDelete = new File("temp"+i);
        toDelete.delete();
        toDelete = new File("temp"+i+".classes");
        toDelete.delete();
        toDelete = new File("temp"+i+".methods");
        toDelete.delete();
    }
    
    public CallTree treeUpdate(String file){
    	
        String line;
        String[] sourceAndTarget;
        String[] sourceClassMethod;
        String[] targetClassMethod;
      	
        try {
			br = new BufferedReader(new FileReader(file));        	
			while ((line = br.readLine()) != null){
				//Splits each line into Source and Target
				//Then splits each of those into Class and Method
				sourceAndTarget = line.split("\\s");
				sourceClassMethod = sourceAndTarget[0].split(":");
				targetClassMethod = sourceAndTarget[1].split(":");

				//Builds up the tree used by GSON to create the JSON
				baseTree.addMethod(sourceClassMethod[1].concat(":").concat(sourceClassMethod[2]));
				baseTree.addMethod(targetClassMethod[0].split("\\)")[1].concat(":").concat(targetClassMethod[1]));
				baseTree.addClass(sourceClassMethod[1]);
				baseTree.addClass(targetClassMethod[0].split("\\)")[1]);
				baseTree.addMethodToClass(sourceClassMethod[1] ,sourceClassMethod[1].concat(":").concat(sourceClassMethod[2]));
				baseTree.addMethodToClass(targetClassMethod[0].split("\\)")[1] ,targetClassMethod[0].split("\\)")[1].concat(":").concat(targetClassMethod[1]));
				baseTree.addConnection(sourceClassMethod[1].concat(":").concat(sourceClassMethod[2]), targetClassMethod[0].split("\\)")[1].concat(":").concat(targetClassMethod[1]));
            	
			}
			br.close();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return baseTree;    
    }

    public String callgraphToJSON(String file, int callNumber){

        String fileName = "result"+callNumber+".json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CallTree tree = new CallTree();

        try {
            tree = this.buildTree(file);
            bw = new BufferedWriter(new FileWriter(fileName));
            String jsonOutput = gson.toJson(tree);
            bw.write(jsonOutput);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
    
    public String callTreeToJSON(CallTree tree, int callNumber){
    	String fileName = "result"+callNumber+".json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(tree);
        
        try {
			bw = new BufferedWriter(new FileWriter(fileName));
			bw.write(jsonOutput);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

        
        return fileName;
    }
    
    public CallTree buildTree(String file){
    	
        String line;
        String[] sourceAndTarget;
        String[] sourceClassMethod;
        String[] targetClassMethod;
      	CallTree tree = new CallTree();
      	
        try {
			br = new BufferedReader(new FileReader(file));        	
			while ((line = br.readLine()) != null){
				//Splits each line into Source and Target
				//Then splits each of those into Class and Method
				sourceAndTarget = line.split("\\s");
				sourceClassMethod = sourceAndTarget[0].split(":");
				targetClassMethod = sourceAndTarget[1].split(":");

				//Builds up the tree used by GSON to create the JSON
				tree.addMethod(sourceClassMethod[1].concat(":").concat(sourceClassMethod[2]));
				tree.addMethod(targetClassMethod[0].split("\\)")[1].concat(":").concat(targetClassMethod[1]));
				tree.addClass(sourceClassMethod[1]);
				tree.addClass(targetClassMethod[0].split("\\)")[1]);
				tree.addMethodToClass(sourceClassMethod[1] ,sourceClassMethod[1].concat(":").concat(sourceClassMethod[2]));
				tree.addMethodToClass(targetClassMethod[0].split("\\)")[1] ,targetClassMethod[0].split("\\)")[1].concat(":").concat(targetClassMethod[1]));
				tree.addConnection(sourceClassMethod[1].concat(":").concat(sourceClassMethod[2]), targetClassMethod[0].split("\\)")[1].concat(":").concat(targetClassMethod[1]));
            	
			}
			br.close();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return tree;            
    }



    //Creates a new tempfile removing any default java classes and methods from the result
    //@return the filename of the tempfile
    public String removeJava(String file, int callNumber){
        String fileName = "temp"+callNumber;
        String tmpFileName = "deleteMe";

        String line;
        try {
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(tmpFileName));
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
        File oldFile = new File(fileName);
        oldFile.delete();

        File newFile = new File(tmpFileName);
        newFile.renameTo(oldFile);
        return fileName;
    }

    //Creates two files, one containing only methods and the other only classes
    //@return String[0] = fileName of the classes
    //        String[1] = fileName of the methods
    public String[] splitClassesMethods(String file, int callNumber){
        String[] fileNames = new String[2];
        fileNames[0] = "temp"+callNumber+".methods";
        fileNames[1] = "temp"+callNumber+".classes";
        String line;
        try {
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(fileNames[0]));
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(fileNames[1]));
            while ((line = br.readLine()) != null){
                if (line.charAt(0)=='M'){
                    bw.write(line + "\n");
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

    //Creates a new tempfile with all duplicates removed
    //@return String = name of tempfile with no duplicates
    public String removeDuplicates(String file, int callNumber){
        String fileName = "temp"+callNumber;
        String tmpFileName = "deleteMe";

        String line;
        HashSet<String> set = new HashSet<String>();
        try {
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(tmpFileName));
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
        File oldFile = new File(fileName);
        oldFile.delete();

        File newFile = new File(tmpFileName);
        newFile.renameTo(oldFile);
        return fileName;
    }

    // Creates a new file showing all new connections added in file2 compared to file1
    // @return String = name of new file containing additions
    public String added(String file1, String file2){
        String fileName = file1+"-added-"+file2;
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
    public String removed(String file1, String file2) {
        String fileName = file1+"-subtracted-"+file2;
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
