package org.bsc.maven.plugin.processor.predicate;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessedFilesProvider {
    private final String projectBuildDirectory;

    public ProcessedFilesProvider(String projectBuildDirectory) {
        this.projectBuildDirectory = projectBuildDirectory;
    }

    public List<FileInfo> readFilesInfo() {
        String fileContent = readListingContent(createPath());
        Map<String, String> keyValueMap = toKeyValueMap(fileContent);
        return convertToFileInfo(keyValueMap);
    }

    private List<FileInfo> convertToFileInfo(Map<String, String> keyValueMap) {
        return keyValueMap.entrySet().stream().flatMap(toFileInfoMapper()).collect(Collectors.toList());
    }

    private Function<Map.Entry<String, String>, Stream<FileInfo>> toFileInfoMapper() {
        return entry -> {
            try {
                return Stream.of(FileInfo.of(entry.getKey(), Long.valueOf(entry.getValue())));
            } catch (NumberFormatException aEx) {
                throw new IllegalStateException("cannot map key=value string into " + FileInfo.class.getSimpleName());
            }
        };
    }

    private Map<String, String> toKeyValueMap(String fileContent) {
        if (fileContent.isEmpty()) {
            return ImmutableMap.of();
        }
        try {
            return Splitter.on(System.lineSeparator()).withKeyValueSeparator(";").split(fileContent);
        } catch (Exception exception) {
            throw new IllegalStateException("cannot split listing file into key=value map", exception);
        }
    }

    private String createPath() {
        return projectBuildDirectory + File.separator + "maven-processor-plugin/processed-files-listing.lst";
    }

    private String readListingContent(String pathString) {
        try {
            return FileUtils.fileRead(pathString);
        } catch (IOException exception) {
            throw new IllegalStateException("cannot read listing file content", exception);
        }
    }
}
