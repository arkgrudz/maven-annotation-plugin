package org.bsc.maven.plugin.processor.predicate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CurrentFilesProducerTest {


    @Test
    public void shouldReturnNoFilesForEmptyInputFiles() {
        CurrentFilesProvider producer = new CurrentFilesProvider(ImmutableList.of());
        List<FileInfo> filesInfo = producer.getFilesInfo();

        assertThat(filesInfo, notNullValue());
        assertThat(filesInfo.size(), is(0));
    }

    @Test
    public void shouldReturnOneFileInfoForOneSourceInput() {
        List<File> currentSourceFiles = sourceFiles("File1.java");
        CurrentFilesProvider producer = new CurrentFilesProvider(currentSourceFiles);
        List<FileInfo> filesInfo = producer.getFilesInfo();

        assertThat(filesInfo, notNullValue());
        assertThat(filesInfo.size(), is(1));
        assertFileInfo(filesInfo.get(0), FileInfo.of(currentSourceFiles.get(0)));

    }

    private void assertFileInfo(FileInfo actual, FileInfo expected) {
        assertThat(actual.getAbsolutePath(), is(expected.getAbsolutePath()));
        assertThat(actual.getLastModified(), is(expected.getLastModified()));
    }

    @Test
    public void shouldReturnFileInfoListForManySourcesInput() {
        List<File> currentSourceFiles = sourceFiles("File1.java" ,"File2.java","File3.java");
        CurrentFilesProvider producer = new CurrentFilesProvider(currentSourceFiles);
        List<FileInfo> filesInfo = producer.getFilesInfo();

        assertThat(filesInfo, notNullValue());
        assertThat(filesInfo.size(), is(3));
        assertFileInfo(filesInfo.get(0), FileInfo.of(currentSourceFiles.get(0)));
        assertFileInfo(filesInfo.get(1), FileInfo.of(currentSourceFiles.get(1)));
        assertFileInfo(filesInfo.get(2), FileInfo.of(currentSourceFiles.get(2)));
    }

    @Test(expected =  IllegalStateException.class)
    public void shouldThrowExceptionWhenNonExistingFileInSourcesInput(){
        List<File> currentSourceFiles = sourceFiles("File1.java" ,"File2.java","NonExisting.java");
        CurrentFilesProvider producer = new CurrentFilesProvider(currentSourceFiles);
        producer.getFilesInfo();
        //then exception is thrown (java.lang.IllegalStateException: non existing source file: NonExisting.java)
    }

    private List<File> sourceFiles(String... aFileNames) {
        return Arrays.stream(aFileNames).map(fileName -> {
                return new File(CurrentFilesProducerTest.class.getResource("source_files").getFile() + File.separator + fileName);
        }).collect(Collectors.toList());
    }
}