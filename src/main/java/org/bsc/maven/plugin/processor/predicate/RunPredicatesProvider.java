package org.bsc.maven.plugin.processor.predicate;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RunPredicatesProvider {
    private final PlexusContainer plexusContainer;

    public RunPredicatesProvider(PlexusContainer plexusContainer) {
        this.plexusContainer = plexusContainer;
    }

    public List<RunPredicate> getRunPredicates(String comaSeparatedHints) {
        if (StringUtils.isEmpty(comaSeparatedHints)){
            return Lists.newArrayList();
        }

        Iterable<String> iterable = Splitter.on(',').trimResults().split(comaSeparatedHints);
        return StreamSupport.stream(iterable.spliterator(),false).map(h -> {
            try {
                return plexusContainer.lookup(RunPredicate.class, h);
            } catch (ComponentLookupException e) {
                throw new IllegalStateException(e);
            }
        }).collect(Collectors.toList());
    }
}
