package org.chb;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.Launcher;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.QueueProcessingManager;
import spoon.support.compiler.FileSystemFolder;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShowMethodCallHierarchy {
    private static Logger logger = LoggerFactory.getLogger(ShowMethodCallHierarchy.class);

    @Option(name="-s", aliases = "--source-folder", metaVar = "SOURCE_FOLDERS",
            usage="source folder(s) for the analyzed project",
            handler = StringArrayOptionHandler.class,
            required = true)
    private List<String> sourceFolders;

    @Option(name="-m", aliases = "--method-name", metaVar = "METHOD_NAME",
            usage="method name to print call hierarchy",
            required = true)
    private String methodName;

    @Option(name="-c", aliases = "--classpath",  metaVar = "CLASSPATH",
            usage="classpath for the analyzed project")
    private String classpath;

    @Option(name="--classpath-file", metaVar = "CLASSPATH_FILE", usage="file containing the classpath for the analyzed project",
            forbids = "--classpath")
    private File classpathFile;

    public static void main(String[] args) throws Exception {
        new ShowMethodCallHierarchy().doMain(args);
    }

    private void doMain(String[] args) throws Exception {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.print("Usage: java -jar <CHB_JAR_PATH>" + parser.printExample(OptionHandlerFilter.REQUIRED));
            System.err.println();
            System.err.println();
            System.err.println("Options:");
            parser.printUsage(System.err);
            return;
        }
        Launcher launcher = new Launcher();
        if (classpath != null) {
            launcher.setArgs(new String[] { "--source-classpath", classpath});
        }
        if (classpathFile != null) {
            launcher.setArgs(new String[] { "--source-classpath", StringUtils.strip(FileUtils.readFileToString(classpathFile), "\n\r\t ")});
        }
        for (String sourceFolder : sourceFolders) {
            launcher.addInputResource(new FileSystemFolder(new File(sourceFolder)));
        }
        launcher.run();

        QueueProcessingManager queueProcessingManager = new QueueProcessingManager(launcher.getFactory());
        Map<CtTypeReference, Set<CtTypeReference>> classHierarchy =
                new ClassHierarchyProcessor().executeSpoon(queueProcessingManager);
        Map<CtExecutableReference, List<CtExecutableReference>> callList =
                new MethodExecutionProcessor().executeSpoon(queueProcessingManager);

        MethodCallHierarchyBuilder methodCallHierarchyBuilder = MethodCallHierarchyBuilder.forMethodName(methodName, callList, classHierarchy);
        methodCallHierarchyBuilder.printCallHierarchy();
    }
}
