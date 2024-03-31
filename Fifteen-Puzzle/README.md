<h1 align="center" id="title">Fifteen Puzzle Solver</h1>

<p id="description">This is a Java-based 15 puzzle game solver. It uses different algorithms (Breadth-First Search, Depth-First Search, A*) to solve the puzzle and writes the solution to a file.
</p>    

<h2>🧐 Features</h2>

Here're some of the project's best features:

* Supporting multiple solving algorithms: BFS, DFS, and A*
* Reading puzzle board configuration from a file
* Writing the solution steps and additional information to separate files
* Handling errors and providing meaningful error messages

<h2>💻 Built with</h2>

Technologies used in the project:

* Java 17
* IntelliJ IDEA

<h2>🛠️ Installation Steps</h2>

To get started with this project, clone the repository.
Open the project in IntelliJ IDEA.
Ensure that you have Java 17 installed and set as the project SDK.
Build the project using IntelliJ IDEA's build tools.

<h2>🚀 Running the project</h2>

Run the ```Main``` class in the ```PietnastkaJava/src/Main.java``` file. The main method accepts command-line arguments as follows:

```
java Main <solver> <parameters> <board_file> <solution_file> <extra_info_file>
```

Where:
* ```<solver>``` is one of bfs, dfs, or astr to select the solving algorithm.
* ```<parameters>``` are the parameters for the selected solver.
* ```<board_file>``` is the path to the file containing the initial board configuration.
* ```<solution_file>``` is the path to the file where the solution steps will be written.
* ```<extra_info_file>``` is the path to the file where additional information about the solution will be written.

For example:
```
java Main bfs LRUD board.txt solution.txt extra.txt
```
This will solve the puzzle using the BFS algorithm with 'L', 'R', 'U', 'D' as the order of moves, read the initial board configuration from board.txt, write the solution steps to solution.txt, and write extra information to extra.txt.
