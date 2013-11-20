package ticketmachine.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ticketmachine.search.AssistedSearch;
import ticketmachine.search.AssistedSearch.Memento;
import ticketmachine.test.TestLists;


/**
 * Tests for {@link AssistedSearch}.
 *
 * @author topexpto@gmail.com (Antonio Freire)
 */
@RunWith(JUnit4.class)
public class AssistedSearchTest {

	AssistedSearch testObject;
	
	@Before
	public void setupBefore() {
		testObject = new AssistedSearch(TestLists.inputA);
	}
	
    @Test(expected = IllegalArgumentException.class)
    public void constructorNullArray() {
    	String [] data = null;
		testObject = new AssistedSearch(data);
		fail("IllegalArgumentException should be raised");
    	
    }

    @Test
    public void constructorEmptyArray() {
    	String [] data = {};
    	try {
    		testObject = new AssistedSearch(data);
    		
    		assertNotNull(testObject.getLocationResults());
    		assertEquals(testObject.getLocationResults(), Arrays.asList());
    		
    		assertNotNull(testObject.getCharOptionsResults());
    		assertEquals(testObject.getCharOptionsResults(), new TreeSet<Object>());
    		
    	} catch (IllegalArgumentException e) {
    		System.out.println(e.getMessage());
    	}
    	
    }

    @Test
    public void resetBeforeSearch() {

    	assertTrue(testObject.getLocationResults().size() == 4);
    	assertTrue(testObject.getCurrentSearchText().equals(""));
    	
    	testObject.reset();
    	
    	assertTrue(testObject.getLocationResults().size() == 4);
    	assertTrue(testObject.getCurrentSearchText().equals(""));

    	testSearchLocationResults(testObject, "", Arrays.asList(TestLists.inputA));
		testSearchOptCharsResults(testObject, new TreeSet<Character>(Arrays.asList('D', 'T')));
    }
    
    @Test
    public void resetAfterSearch() {
    	String [] data = {"DARTFORD", "DARTMOUTH", "TOWER HILL", "DERBY"};
    	AssistedSearch testObject = new AssistedSearch(data);

    	testObject.startNextSearch("D");
    	testObject.reset();

    	testSearchLocationResults(testObject, "", Arrays.asList(data));
		testSearchOptCharsResults(testObject, new TreeSet<Character>(Arrays.asList('D', 'T')));
    	
    }

    @Test
    public void nonIncrementalSearch() {
    	String [] data = {"DARTFORD", "DARTMOUTH", "TOWER HILL", "DERBY", "DUNDEE", "DONCASTER"};
    	testObject = new AssistedSearch(data);
    	
    	testObject.startNextSearch("D");
		testSearchLocationResults(testObject, "D"    , Arrays.asList( "DARTMOUTH","DARTFORD", "DERBY", "DUNDEE", "DONCASTER"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('E', 'A', 'U', 'O')));
		
		testObject.startNextSearch("DO");
		testSearchLocationResults(testObject, "DO"    , Arrays.asList( "DONCASTER"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('N')));

		testObject.startNextSearch("DU");
		testSearchLocationResults(testObject, "DU"    , Arrays.asList( "DUNDEE"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('N')));

 		testObject.startNextSearch("DUN");
		testSearchLocationResults(testObject, "DUN"    , Arrays.asList( "DUNDEE"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('D')));

    }

    @Test
    public void incrementalSearchDARTM() {

		testSearchLocationResults(testObject, ""    , Arrays.asList( "DARTMOUTH","DARTFORD", "DERBY", "TOWER HILL"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('D', 'T')));

		testObject.startNextSearch("D");
		testSearchLocationResults(testObject, "D"    , Arrays.asList( "DARTMOUTH","DARTFORD", "DERBY"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('E', 'A')));
		
		testObject.startNextSearch("DA");
		testSearchLocationResults(testObject, "DA"   , Arrays.asList( "DARTMOUTH","DARTFORD"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('R')));
		
		testObject.startNextSearch("DAR");
		testSearchLocationResults(testObject, "DAR"  , Arrays.asList( "DARTMOUTH","DARTFORD"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('T')));
		
		testObject.startNextSearch("DART");
		testSearchLocationResults(testObject, "DART" , Arrays.asList( "DARTMOUTH","DARTFORD"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('M', 'F')));
		
		testObject.startNextSearch("DARTM");
		testSearchLocationResults(testObject, "DARTM", Arrays.asList( "DARTMOUTH"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('O')));
		
		testObject.startNextSearch("DARTMOUTH");
		testSearchLocationResults(testObject, "DARTMOUTH", Arrays.asList( "DARTMOUTH"));
		testSearchOptCharsResults(testObject, new HashSet<Character>());
    }

	@Test
    public void incrementalBackToBeginning() {
		Stack<Memento> history = new Stack<Memento>();
		
		history.push(testObject.saveToMemento());
		testObject.startNextSearch("D"); 
		
		history.push(testObject.saveToMemento());
		testObject.startNextSearch("DE");
		
		history.push(testObject.saveToMemento());
		testObject.startNextSearch("DER");
		
		history.push(testObject.saveToMemento());
		testObject.startNextSearch("DERB");
		
		history.push(testObject.saveToMemento());
		testObject.startNextSearch("DERBY");
		
		testSearchLocationResults(testObject, "DERBY", Arrays.asList( "DERBY"));
		testSearchOptCharsResults(testObject, new HashSet<Character>());
		
		testObject.restoreFromMemento(history.pop());
		testSearchLocationResults(testObject, "DERB", Arrays.asList( "DERBY"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('Y')));

		testObject.restoreFromMemento(history.pop());
		testSearchLocationResults(testObject, "DER", Arrays.asList( "DERBY"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('B')));

		testObject.restoreFromMemento(history.pop());
		testSearchLocationResults(testObject, "DE", Arrays.asList( "DERBY"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('R')));

		testObject.restoreFromMemento(history.pop());
		testSearchLocationResults(testObject, "D", Arrays.asList( "DERBY", "DARTFORD", "DARTMOUTH"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('E', 'A')));

		testObject.restoreFromMemento(history.pop());
		testSearchLocationResults(testObject, "", Arrays.asList( "DERBY", "DARTFORD", "DARTMOUTH", "TOWER HILL"));
		testSearchOptCharsResults(testObject, new HashSet<Character>(Arrays.asList('D', 'T')));

	}
	
	@Test
	@Ignore
    public void BackspaceOnEmpty() {
		
    }

    private <T> boolean unorderedCollectionIsEqual(Collection<T> expected, Collection<T> result) {
    	
    	if (expected.containsAll(result) && result.containsAll(expected)) {
    		return true;
    	}
    	return false;
    }

    private void testSearchLocationResults(AssistedSearch searchObj, String search, List<String> expectedResults) {
		List<String> expectedLocations = expectedResults; 
		
		assertTrue(searchObj.getCurrentSearchText().equals(search));
		
		List<String> resultLocations = searchObj.getLocationResults(); 
		assertNotNull(resultLocations);
		assertTrue(unorderedCollectionIsEqual(resultLocations, expectedLocations));

    }
    
    private void testSearchOptCharsResults(AssistedSearch searchObj, Set<Character> expectedResults) {
		Set<Character> expectedCharacters = expectedResults;
		Set<Character> actualCharacters = searchObj.getCharOptionsResults();
		assertEquals(expectedCharacters, actualCharacters);
		
		
	}
}
