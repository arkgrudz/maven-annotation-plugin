package org.bsc.maven.plugin.processor.predicate;

import org.codehaus.plexus.PlexusContainer;

import java.io.File;
import java.util.List;

public class ProcessorPluginRunOracle {

    private final RunPredicatesProvider runPredicatesProvider;
    private final ProcessedFilesProvider processedFilesProvider;

    public ProcessorPluginRunOracle(String projectBuildDirectory, PlexusContainer plexusContainer) {
        processedFilesProvider = new ProcessedFilesProvider(projectBuildDirectory);
        runPredicatesProvider = new RunPredicatesProvider(plexusContainer);
    }

    public boolean shouldRunProcessorPlugin(List<File> currentSourceFiles) {
        RunPredicatesExecutor executor = new RunPredicatesExecutor(runPredicatesProvider);

        List<FileInfo> processedFilesInfo = processedFilesProvider.readFilesInfo();
        List<FileInfo> currentFilesInfo = new CurrentFilesProvider(currentSourceFiles).getFilesInfo();

        return executor.shouldRunProcessorPlugin( currentFilesInfo, processedFilesInfo );
    }
}
