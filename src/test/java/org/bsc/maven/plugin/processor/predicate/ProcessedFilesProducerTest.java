package org.bsc.maven.plugin.processor.predicate;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ProcessedFilesProducerTest {
    private static final String FILE1_ABSOLUTE_PATH = "c:\\tmp\\org\\bsc\\maven\\plugin\\File1.java";
    private static final Long FILE1_LAST_MODIFICATION = 123l;
    private static final String FILE2_ABSOLUTE_PATH = "c:\\tmp\\org\\bsc\\maven\\plugin\\File2.java";
    private static final Long FILE2_LAST_MODIFICATION = 223l;
    private static final String FILE3_ABSOLUTE_PATH = "c:\\tmp\\org\\bsc\\maven\\plugin\\File3.java";
    private static final Long FILE3_LAST_MODIFICATION = 323l;

    @Test( expected = IllegalStateException.class)
    public void shouldReturnNoFilesFromNonExistingDirectory() {
        ProcessedFilesProvider producer = new ProcessedFilesProvider("non/existing/directory");
        producer.readFilesInfo();
        //then exception is thrown (java.lang.IllegalStateException: cannot read listing file content)
    }

    @Test( expected = IllegalStateException.class)
    public void shouldReturnNoFilesWhenNoListingExists() throws URISyntaxException {
        ProcessedFilesProvider producer = new ProcessedFilesProvider(getDirectoryPath("./no_listing_exists"));
        producer.readFilesInfo();
        //then exception is thrown (java.lang.IllegalStateException: cannot read listing file content)
    }

    @Test
    public void shouldReturnNoFilesFromEmptyListing() throws URISyntaxException {
        ProcessedFilesProvider producer = new ProcessedFilesProvider(getDirectoryPath("./empty_listing_exists"));
        List<FileInfo> filesInfo = producer.readFilesInfo();

        Assert.assertNotNull(filesInfo);
        assertThat(filesInfo, CoreMatchers.hasItems());
    }

    @Test
    public void shouldReturnOneFileFromListingWithOneEntry() throws URISyntaxException {
        ProcessedFilesProvider producer = new ProcessedFilesProvider(getDirectoryPath("./one_file_in_listing"));
        List<FileInfo> filesInfo = producer.readFilesInfo();

        assertThat(filesInfo, notNullValue());
        assertThat(filesInfo.size(), is(1));
        assertThat(filesInfo.get(0), is(FileInfo.of(FILE1_ABSOLUTE_PATH, 123)));
    }

    private String getDirectoryPath(String directory) throws URISyntaxException {
        return this.getClass().getResource(directory).toURI().getPath();
    }

    @Test
    public void shouldReturnFilesFromListing() throws URISyntaxException {
        ProcessedFilesProvider producer = new ProcessedFilesProvider(getDirectoryPath("./files_in_listing"));
        List<FileInfo> filesInfo = producer.readFilesInfo();

        assertThat(filesInfo, notNullValue());
        assertThat(filesInfo.size(), is(3));
        assertThat(filesInfo.get(0), is(FileInfo.of(FILE1_ABSOLUTE_PATH, FILE1_LAST_MODIFICATION)));
        assertThat(filesInfo.get(1), is(FileInfo.of(FILE2_ABSOLUTE_PATH, FILE2_LAST_MODIFICATION)));
        assertThat(filesInfo.get(2), is(FileInfo.of(FILE3_ABSOLUTE_PATH, FILE3_LAST_MODIFICATION)));
    }

    @Test( expected = IllegalStateException.class)
    public void shouldReturnNoFilesWhenListingIsCorrupted() throws URISyntaxException {
        ProcessedFilesProvider producer = new ProcessedFilesProvider(getDirectoryPath("./corrupted_listing"));
        producer.readFilesInfo();
        //then exception is thrown (java.lang.IllegalStateException: cannot split listing file into key=value map)
    }

    @Test( expected = IllegalStateException.class)
    public void shouldReturnNoFilesWhenListingHasCorruptedData() throws URISyntaxException {
        ProcessedFilesProvider producer = new ProcessedFilesProvider(getDirectoryPath("./corrupted_data_in_listing"));
        producer.readFilesInfo();
        //then exception is thrown (java.lang.IllegalStateException: cannot map key=value string into FileInfo)
    }

}
