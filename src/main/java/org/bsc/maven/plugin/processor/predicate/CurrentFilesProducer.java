package org.bsc.maven.plugin.processor.predicate;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class CurrentFilesProducer {
    private final List<File> currentSourceFiles;

    public CurrentFilesProducer(List<File> currentSourceFiles) {
        this.currentSourceFiles = currentSourceFiles;
    }

    public List<FileInfo> getFilesInfo() {
        return currentSourceFiles.stream().map(this::createFileInfo).collect(Collectors.toList());
    }

    private FileInfo createFileInfo(File file) {
        return new FileInfo(file.getAbsolutePath(), file.lastModified());
    }
}
