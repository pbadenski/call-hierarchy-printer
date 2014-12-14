CHP (Call Hierarchy Printer) prints selected method call hierarchy in text format for easy processing (eg. grep). It is equivalent
to "Call Hierarchy" function in any advanced IDE.

Installation
------------

	mvn package

Usage
-----

Before running the code you need classpath for the analyzed project.
For maven run:

	mvn dependency:build-classpath
	
To print method call hierarchy run:

    java -jar org.chp-VERSION.jar -m (--method-name) METHOD_NAME -s (--source-folder) SOURCE_FOLDERS
    
    --classpath-file CLASSPATH_FILE     : file containing the classpath for the analyzed project
    -c (--classpath) CLASSPATH          : classpath for the analyzed project
    -m (--method-name) METHOD_NAME      : method name to print call hierarchy
    -s (--source-folder) SOURCE_FOLDERS : source folder(s) for the analyzed project
    
Example
-------

	$ mvn dependency:build-classpath | grep -v INFO > chp.classpath
	$ java -jar target/org.chp-*-SNAPSHOT.jar -s src/main/java -m org.chp.ShowMethodCallHierarchy.main --classpath-file chp.classpath
