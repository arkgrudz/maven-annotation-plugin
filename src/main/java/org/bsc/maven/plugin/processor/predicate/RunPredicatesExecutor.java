package org.bsc.maven.plugin.processor.predicate;

import java.util.List;

public class RunPredicatesExecutor {
    private final RunPredicatesProvider runPredicatesProvider;

    public RunPredicatesExecutor(RunPredicatesProvider runPredicatesProvider) {
        this.runPredicatesProvider = runPredicatesProvider;

    }

    public boolean shouldRunProcessorPlugin(List<FileInfo> currentFilesInfo, List<FileInfo> processedFilesInfo) {
        return false;
    }
}
