ubc-cs410-xwing
===============
CS410 Project Part 3 - Team XWing
Adam Lloyd - 55259113, Norris Ng - 36888121, Jimmy Shi - 30053110  , Alex Sommerey - 72597099
What We Made: 
Our program produces an animated graph of all of the method calls in a java program obtained from the compiled JAR. Our program crawls through a git repository (using JGit) and obtain a compiled JAR from each commit. Using the “java-callgraph” component, we obtain the call graph data from the current JAR; a list of methods and their class, and the methods called by each method and their classes. We combine this with author data and logs to determine who updated a method, and how “recently” it was changed or added. Our program builds a JSON object tree for each commit which is read by our visualizer and produces a graph of nodes (methods) and their connections (method calls). The nodes are color coded by author, with the intensity of the color representing when the method was last added or changed. The nodes are further separated (by “force”; like a charged particle) into sections where one can infer their class. The visualizer queues up the JSON files from each version and can walk through them chronologically.

Code Repository: 
https://github.com/i4c8/ubc-cs410-xwing (finalworking branch)

How to Run Our Code: 
Our code is an eclipse project and as such, the simplest way to run is by importing into eclipse, setting the arguments and running DataService.java.
The files needed to run the project are:
A Windows installation, preferably with Eclipse.
The repositories from Git for java-callgraph and nostra13 Android-Universal-Loader, in C:\Users\%USERNAME%\git
A properly installed and configured Apache Maven (this is a pain, but is a needed component to build JARs), with the path in Crawler.java updated to your Maven installation (put it in C:\Program Files\apache-maven-3.2.3\ for best results…) Tutorial for installation here: http://www.mkyong.com/maven/how-to-install-maven-in-windows/ 
All external java libraries used are included in the lib folder.

In the launch arguments, input gousiosg java-callgraph, to access the first repository then simply launch the program. After computation (lengthy!) the program will attempt to open our visualizer in a web browser. Some browsers have compatibility issues, however - Chrome is fastest but needs to be given permission to open our HTML/javascript file, while Firefox may work without issue.

As this is not very portable, you may prefer to run our visualization in your browser from this DropBox link: (TBD-See PDF). This is what our project outputs after the computations and compilations have run.

Architecture:
Front-end/DataService: Runs the program and responsible for passing most of the elements to different components. Currently takes in as an argument the name of a github project and its author.
Crawler/Maven/JGit: Crawls the github repository (visits each commit in order via JGit library calls) and obtains the code as it was at that point in time. Then compiles that code to a JAR using Maven, stores commit names and author names then continues to iterate.
Java-callgraph: As each commit is visited and compiled, the JAR is passed to java-callgraph which obtains the textual call-graph representation of the program and stores it in a text file. Program doesn’t progress past here until crawler is finished.
Parser/Interpreter: Takes in a file containing output of java-callgraph. Parses the output into meaningful objects then writes them to a JSON tree readable by our visualizer.
Visualizer: Reads the JSON files and produces our output; a force-directed graph with nodes and labeled method names, where each link between nodes represents their calls. Each JSON is queued and loaded in order, then matched to a color representing the author of the commit from where the java-callgraph output came.

Difficulty:
Front-end/DataService: Wasn’t terribly difficult to implement. Designing the architecture beforehand made the task simpler. It was just a matter of passing data around between components and making sure everyone agreed on what their components needed as inputs and what they produced as outputs.
Crawler/Maven/JGit: The most difficult aspect of our project to implement. Very time consuming and a big learning experience. This component, in the final build, ended up needing another sub-component that was not planned - Maven was crucial to being able to compile the github repositories we used for our outputs. Because of the many sub-components and large amount of input files, there was some trial and error on finding the best way to implement this component.
Java-callgraph: The simplest component to implement. The library is built for java and returned the output we expected from it without having to fight with any method calls.
Parser/Interpreter: Fairly difficult to implement. Probably one of the largest components in our code, as it was almost entirely written by members of our team with little reliance on external libraries. This component crucially determined what the outputs to our visualizer would be and thus it needed to be tested and maintained more than the other components.
Visualizer: One of the most difficult components to implement. No member on our team had prior experience with javascript, which is what D3 uses. This lead to a lot of experimentation and iteration to achieve the final output that we desired. The visualizer still had to do some computation; JSON object files weren’t enough to achieve our desired output.
