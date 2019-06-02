package org.bsc.maven.plugin.processor.predicate;

import java.util.Objects;

public class FileInfo {
    private final String absolutePath;
    private final long lastModified;

    public FileInfo(String absolutePath, long lastModified) {
        this.absolutePath = absolutePath;
        this.lastModified = lastModified;
    }

    public static FileInfo of(String absolutePath, long lastModified) {
        return new FileInfo(absolutePath, lastModified);
    }

    @Override
    public String toString() {
        return absolutePath + ", " + lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileInfo fileInfo = (FileInfo) o;
        return lastModified == fileInfo.lastModified &&
                absolutePath.equals(fileInfo.absolutePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(absolutePath, lastModified);
    }
}
