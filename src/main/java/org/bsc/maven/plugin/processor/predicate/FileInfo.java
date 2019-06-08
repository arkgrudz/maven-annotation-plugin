package org.bsc.maven.plugin.processor.predicate;

import java.io.File;
import java.util.Objects;

public class FileInfo {
    private final String absolutePath;
    private final long lastModified;

    FileInfo(String absolutePath, long lastModified) {
        this.absolutePath = absolutePath;
        this.lastModified = lastModified;
    }

    public static FileInfo of(String absolutePath, long lastModified) {
        return new FileInfo(absolutePath, lastModified);
    }

    public static  FileInfo of( File aFile ){
        return new FileInfo(aFile.getAbsolutePath(), aFile.lastModified());
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getAbsolutePath() {
        return absolutePath;
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
