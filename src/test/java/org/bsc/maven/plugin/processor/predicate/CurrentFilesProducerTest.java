package org.bsc.maven.plugin.processor.predicate;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.*;

public class CurrentFilesProducerTest {


    @Test
    public void shouldReturnNoFilesForEmptyInputFiles() {
        sourceFiles = sourceFiles("File1.java");
        CurrentFilesProducer producer = new CurrentFilesProducer(so);
        producer.getFilesInfo()
    }
}