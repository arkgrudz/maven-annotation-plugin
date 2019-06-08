package org.bsc.maven.plugin.processor.predicate;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.composition.CycleDetectedInComponentGraphException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.context.Context;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RunPredicatesProviderTest {

    public static final String PREDICATE_1 = "predicate1";
    public static final String PREDICATE_2 = "predicate2";
    public static final String PREDICATE_3 = "predicate3";

    @Test
    public void shouldProvideEmptyPredicatesListWhenAskedWithEmptyString() throws ComponentLookupException {
        PlexusContainer plexusContainer = plexusContainerWithThreeRunPredicates();
        RunPredicatesProvider provider = new RunPredicatesProvider(plexusContainer);
        List<RunPredicate> runPredicates = provider.getRunPredicates("");

        assertThat(runPredicates, is(notNullValue()));
        assertThat(runPredicates.size(), is(0));
    }

    @Test
    public void shouldProvideEmptyPredicatesListWhenAskedWithNullString() throws ComponentLookupException {
        PlexusContainer plexusContainer = plexusContainerWithThreeRunPredicates();
        RunPredicatesProvider provider = new RunPredicatesProvider(plexusContainer);
        List<RunPredicate> runPredicates = provider.getRunPredicates(null);

        assertThat(runPredicates, is(notNullValue()));
        assertThat(runPredicates.size(), is(0));
    }

    private PlexusContainer plexusContainerWithThreeRunPredicates() throws ComponentLookupException {
        return plexusContainer(runPredicate(PREDICATE_1), runPredicate(PREDICATE_2), runPredicate(PREDICATE_3));
    }


    @Test
    public void shouldProvideOnePredicateWhenAskedForOneComponent() throws ComponentLookupException {
        Pair predicate1 = runPredicate(PREDICATE_1);
        PlexusContainer plexusContainer = plexusContainer(predicate1);
        RunPredicatesProvider provider = new RunPredicatesProvider(plexusContainer);
        List<RunPredicate> runPredicates = provider.getRunPredicates(PREDICATE_1);

        assertThat(runPredicates, is(notNullValue()));
        assertThat(runPredicates.size(), is(1));
        assertThat(runPredicates.get(0), is(predicate1.runPredicate));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowWhenAskedForNonExistingComponent() throws ComponentLookupException {
        PlexusContainer plexusContainer = plexusContainerWithThreeRunPredicates();
        RunPredicatesProvider provider = new RunPredicatesProvider(plexusContainer);
        provider.getRunPredicates("nonExistingPredicateName");
        //then exception is thrown: IllegalStateException
    }

    private Pair runPredicate(String name) {
        return Pair.of(name, mock(RunPredicate.class));
    }

    private PlexusContainer plexusContainer(Pair... pairs) throws ComponentLookupException {
        PlexusContainer mock = mock(PlexusContainer.class);
        when(mock.lookup(eq(RunPredicate.class), anyString())).thenAnswer(invocationOnMock -> {
            throw new ComponentLookupException("", invocationOnMock.getArgument(0).toString(), invocationOnMock.getArgument(1));
        });
        for (Pair pair : pairs) {
            when(mock.lookup(eq(RunPredicate.class), eq(pair.name))).thenReturn(pair.runPredicate);
        }
        return mock;
    }

}

class Pair {
    String name;
    RunPredicate runPredicate;

    public static Pair of(String name, RunPredicate runPredicate) {
        Pair pair = new Pair();
        pair.name = name;
        pair.runPredicate = runPredicate;
        return pair;
    }
}
