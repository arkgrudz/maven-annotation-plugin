package org.bsc.maven.plugin.processor.predicate;

import java.io.File;
import java.util.List;

public class ProcessorPluginRunOracle {

    private ProcessedFilesProducer processedFilesInfoReader;

    public ProcessorPluginRunOracle(String projectBuildDirectory) {
        processedFilesInfoReader = new ProcessedFilesProducer(projectBuildDirectory);
    }

    public boolean shouldRunProcessorPlugin(List<File> currentSourceFiles) {
        List<FileInfo> processedFilesInfo = processedFilesInfoReader.readFilesInfo();
        List< FileInfo> currentFilesInfo = new CurrentFilesProducer( currentSourceFiles ).getFilesInfo();
        return  false;
    }
}
