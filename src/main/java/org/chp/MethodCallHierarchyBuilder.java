package org.chp;

import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static MethodCallHierarchyBuilder forMethodName(String methodName,
                                                           Map<CtExecutableReference, List<CtExecutableReference>> callList,
                                                           Map<CtTypeReference, Set<CtTypeReference>> classHierarchy) {
        return new MethodCallHierarchyBuilder(findExecutableForMethodName(methodName, callList), callList, classHierarchy);
    }

    static CtExecutableReference findExecutableForMethodName(String methodName, Map<CtExecutableReference, List<CtExecutableReference>> callList) {
        for (CtExecutableReference executableReference : callList.keySet()) {
            String executableReferenceMethodName = executableReference.getDeclaringType().getQualifiedName() + "." + executableReference.getSimpleName();
            if (executableReferenceMethodName.equals(methodName)) {
                return executableReference;
            }
        }
        throw new RuntimeException("`" + methodName + "` not found");
    }

    public void printCallHierarchy() {
        System.out.println("Call hierarchy [" + executableReference + "]");
        printCallHierarchy(executableReference, "\t", new HashSet<CtExecutableReference>());
    }

    private void printCallHierarchy(CtExecutableReference method, String indents, Set<CtExecutableReference> alreadyVisited) {
        if (alreadyVisited.contains(method)) {
            return;
        }
        alreadyVisited.add(method);
        List<CtExecutableReference> callListForMethod = callList.get(method);
        if (callListForMethod == null) {
            return;
        }
        for (CtExecutableReference eachReference : callListForMethod) {
            System.out.println(indents + eachReference.toString());

            printCallHierarchy(eachReference, indents.concat("\t"), alreadyVisited);
            Set<CtTypeReference> subclasses = classHierarchy.get(eachReference.getDeclaringType());
            if (subclasses != null) {
                for (CtTypeReference subclass : subclasses) {
                    CtExecutableReference reference = eachReference.getOverridingExecutable(subclass);
                    if (reference != null) {
                        System.out.println(indents + "* " + reference.toString());
                        printCallHierarchy(reference, indents.concat("\t"), alreadyVisited);
                    }
                }
            }
        }
    }
}
