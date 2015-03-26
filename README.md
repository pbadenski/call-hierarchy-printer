CHP (Call Hierarchy Printer) prints selected method call hierarchy in text format for easy processing (eg. grep). It is equivalent
to "Call Hierarchy" function in any advanced IDE.

Installation
------------

	gradle jar

Usage
-----

1. Before running the code you need classpath for the analyzed project.

	For Maven run:

		mvn dependency:build-classpath | grep -v "^\[.*\].*" | tr '\n' ':' > this.classpath

	For Gradle add this to build.gradle:

		task showClasspath << {
    		it.println sourceSets.main.runtimeClasspath.collect { it.absolutePath }.join(':')
    	}

	and run

 		gradle -q showClasspath > this.classpath

2. To print method call hierarchy run:

		java -jar build/libs/org.chp-VERSION.jar -m (--method-name) METHOD_NAME -s (--source-folder) SOURCE_FOLDERS --classpath-file CLASSPATH_FILE
    
		--classpath-file CLASSPATH_FILE     : file containing the classpath for the analyzed project
		-c (--classpath) CLASSPATH          : classpath for the analyzed project
		-m (--method-name) METHOD_NAME      : method name to print call hierarchy
		-s (--source-folder) SOURCE_FOLDERS : source folder(s) for the analyzed project
    
Example
-------

Execute following from this project root directory:

	$ gradle -q showClasspath > this.classpath
	$ java -jar build/libs//org.chp-*.jar -s src/main/java -m org.chp.ShowMethodCallHierarchy.main --classpath-file this.classpath

Output:

	Method call hierarchy callees of
	org.chp.ShowMethodCallHierarchy.main(java.lang.String[])
	  org.chp.ShowMethodCallHierarchy.doMain
	    spoon.Launcher.setArgs(java.lang.String[])
	    spoon.Launcher.setArgs(java.lang.String[])
	    org.apache.commons.lang3.StringUtils.strip(java.lang.String, java.lang.String)
	    org.apache.commons.io.FileUtils.readFileToString(java.io.File)
	    spoon.Launcher.addInputResource(spoon.compiler.SpoonResource)
	    spoon.Launcher.run
	    org.chp.ShowMethodCallHierarchy.printCallHierarchy(spoon.Launcher, java.io.PrintStream)
	      spoon.Launcher.getFactory
	      org.chp.ClassHierarchyProcessor.executeSpoon(spoon.support.QueueProcessingManager)
	        spoon.support.QueueProcessingManager.addProcessor(spoon.processing.Processor<?>)
	        spoon.support.QueueProcessingManager.process
	        org.slf4j.Logger.debug(java.lang.String)
	      org.chp.MethodExecutionProcessor.executeSpoon(spoon.support.QueueProcessingManager)
	        spoon.support.QueueProcessingManager.addProcessor(spoon.processing.Processor<?>)
	        spoon.support.QueueProcessingManager.process
	        org.slf4j.Logger.debug(java.lang.String)
	      org.chp.MethodCallHierarchyBuilder.forMethodName(java.lang.String, java.util.Map<spoon.reflect.reference.CtExecutableReference, java.util.List<spoon.reflect.reference.CtExecutableReference>>, java.util.Map<spoon.reflect.reference.CtTypeReference, java.util.Set<spoon.reflect.reference.CtTypeReference>>)
	        org.chp.MethodCallHierarchyBuilder.findExecutableForMethodName(java.lang.String, java.util.Map<spoon.reflect.reference.CtExecutableReference, java.util.List<spoon.reflect.reference.CtExecutableReference>>)
	          java.util.Map<spoon.reflect.reference.CtExecutableReference, java.util.List<spoon.reflect.reference.CtExecutableReference>>.keySet
	          spoon.reflect.reference.CtTypeReference.getQualifiedName
	          spoon.reflect.reference.CtExecutableReference.getDeclaringType
	          spoon.reflect.reference.CtReference.getSimpleName
	          java.lang.String.equals(java.lang.Object)
	      org.chp.MethodCallHierarchyBuilder.printCallHierarchy(java.io.PrintStream)
	        java.io.PrintStream.println(java.lang.String)
	        org.chp.MethodCallHierarchyBuilder.printCallHierarchy(java.io.PrintStream, spoon.reflect.reference.CtExecutableReference, java.lang.String, java.util.Set<spoon.reflect.reference.CtExecutableReference>)
	          java.util.Set<spoon.reflect.reference.CtExecutableReference>.contains(java.lang.Object)
	          java.util.Set<spoon.reflect.reference.CtExecutableReference>.add(spoon.reflect.reference.CtExecutableReference)
	          java.util.Map<spoon.reflect.reference.CtExecutableReference, java.util.List<spoon.reflect.reference.CtExecutableReference>>.get(java.lang.Object)
	          java.io.PrintStream.println(java.lang.String)
	          java.lang.Object.toString
	          org.chp.MethodCallHierarchyBuilder.printCallHierarchy(java.io.PrintStream, spoon.reflect.reference.CtExecutableReference, java.lang.String, java.util.Set<spoon.reflect.reference.CtExecutableReference>)
	          java.lang.String.concat(java.lang.String)
	          java.util.Map<spoon.reflect.reference.CtTypeReference, java.util.Set<spoon.reflect.reference.CtTypeReference>>.get(java.lang.Object)
	          spoon.reflect.reference.CtExecutableReference.getDeclaringType
	          spoon.reflect.reference.CtExecutableReference.getOverridingExecutable(spoon.reflect.reference.CtTypeReference)
	          java.io.PrintStream.println(java.lang.String)
	          java.lang.Object.toString
	          org.chp.MethodCallHierarchyBuilder.printCallHierarchy(java.io.PrintStream, spoon.reflect.reference.CtExecutableReference, java.lang.String, java.util.Set<spoon.reflect.reference.CtExecutableReference>)
	          java.lang.String.concat(java.lang.String)
	  org.chp.ShowMethodCallHierarchy.parse(java.lang.String[])
	    org.kohsuke.args4j.CmdLineParser.parseArgument(java.lang.String[])
	    java.io.PrintStream.println(java.lang.String)
	    java.lang.Throwable.getMessage
	    java.io.PrintStream.print(java.lang.String)
	    org.kohsuke.args4j.CmdLineParser.printExample(org.kohsuke.args4j.OptionHandlerFilter)
	    java.io.PrintStream.println
	    java.io.PrintStream.println
	    java.io.PrintStream.println(java.lang.String)
	    org.kohsuke.args4j.CmdLineParser.printUsage(java.io.OutputStream)
	    java.lang.System.exit(int)
