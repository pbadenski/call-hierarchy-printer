package org.chp;

import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

import java.io.PrintStream;
import java.util.*;

public class MethodCallHierarchyBuilder {
    private final CtExecutableReference executableReference;
    private final Map<CtExecutableReference, List<CtExecutableReference>> callList;
    private final Map<CtTypeReference, Set<CtTypeReference>> classHierarchy;

    private MethodCallHierarchyBuilder(CtExecutableReference executableReference,
                                       Map<CtExecutableReference, List<CtExecutableReference>> callList,
                                       Map<CtTypeReference, Set<CtTypeReference>> classHierarchy) {
        this.executableReference = executableReference;
        this.callList = callList;
        this.classHierarchy = classHierarchy;
    }

    public static List<MethodCallHierarchyBuilder> forMethodName(String methodName,
                                                           Map<CtExecutableReference, List<CtExecutableReference>> callList,
                                                           Map<CtTypeReference, Set<CtTypeReference>> classHierarchy) {
        ArrayList<MethodCallHierarchyBuilder> result = new ArrayList<>();
        for (CtExecutableReference executableReference : findExecutablesForMethodName(methodName, callList)) {
            result.add(new MethodCallHierarchyBuilder(executableReference, callList, classHierarchy));
        }
        return result;
    }

    static List<CtExecutableReference> findExecutablesForMethodName(String methodName, Map<CtExecutableReference, List<CtExecutableReference>> callList) {
        ArrayList<CtExecutableReference> result = new ArrayList<>();
        for (CtExecutableReference executableReference : callList.keySet()) {
            String executableReferenceMethodName = executableReference.getDeclaringType().getQualifiedName() + "." + executableReference.getSimpleName();
            if (executableReferenceMethodName.equals(methodName)
                    || executableReference.toString().contains(methodName)) {
                result.add(executableReference);
            }
        }
        return result;
    }

    public void printCallHierarchy(PrintStream printStream) {
        printStream.println("Method call hierarchy callees of " + executableReference + "");
        printCallHierarchy(printStream, executableReference, "\t", new HashSet<CtExecutableReference>());
    }

    private void printCallHierarchy(PrintStream printStream, CtExecutableReference method, String indents, Set<CtExecutableReference> alreadyVisited) {
        if (alreadyVisited.contains(method)) {
            return;
        }
        alreadyVisited.add(method);
        List<CtExecutableReference> callListForMethod = callList.get(method);
        if (callListForMethod == null) {
            return;
        }
        for (CtExecutableReference eachReference : callListForMethod) {
            printStream.println(indents + eachReference.toString());

            printCallHierarchy(printStream, eachReference, indents.concat("\t"), alreadyVisited);
            Set<CtTypeReference> subclasses = classHierarchy.get(eachReference.getDeclaringType());
            if (subclasses != null) {
                for (CtTypeReference subclass : subclasses) {
                    CtExecutableReference reference = eachReference.getOverridingExecutable(subclass);
                    if (reference != null) {
                        printStream.println(indents + "* " + reference.toString());
                        printCallHierarchy(printStream, reference, indents.concat("\t"), alreadyVisited);
                    }
                }
            }
        }
    }
}
