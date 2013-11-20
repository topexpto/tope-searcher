/**
 * Oct 2013 - Truphone Java Development Exercise v2.2
 * 
 * Train Ticket Machine
 * 
 * @author Antonio Freire
 * 
 */
package ticketmachine.search;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;

/**
 * <p>Class AssistedSearch
 * 
 * Description:
 * <p>Search Train stations from a list of possible destinations and provides 
 * assistance to the caller with a list containing only the next possible 
 * characters for the next incremental search, i.e. a search such that the text
 * entered starts with the text entered in the previous search. 
 * 
 * Usage:
 * <p>Instantiate AssistedSearch providing the initial input.
 * Use StartNextSearch to update the text to be searched next.
 * To search before (e.g. after Backspace) use the Memento mechanism:
 * 	1. Save Object state
 *  2. Restore the Object state
 * (The doc section indicates the methods that change the Object state)
 * Use reset if you need to reset the current search.
 * 
 *  
 * 
 * Remarks:
 * <p>The intended usage of AssistedSearch is incrementing the current searchText T with a new character C.
 * <p>Given a searchText T, the only allowed searches are 
 * 		T - LC 	where LC is the last character
 * 		T + NC  where NC is the new character to concatenate to T
 * 		T + S   where S is a new String to concatenate to T
 * 
 * <p>Otherwise, the reset method should be used.
 *  
 * <p>All instance members must be in sync, i.e., the list of current destinations 
 * must be the result of a search for the searchText as well as the valid options for
 * the next input character. 
 *  
 *  @author topexpto@gmail.com (Antonio Freire)
 *  
 */
public class AssistedSearch {

	/**
	 * holds the text to search that was entered so far.
	 */
	private String searchText;
	

	/**
	 * The data source. use it read-only
	 */
	private List<String> initialDataSource;

	/**
	 * holds the list of possible destinations for the current search.
	 */
	private ArrayList<String> currentDestinations;
	
	/**
	 * holds the next possible input characters for the current search.
	 */
	private TreeSet<Character> currentOptions;
		
	public AssistedSearch() {
		this.searchText = new String();
		this.currentDestinations = new ArrayList<String>();
		this.currentOptions = new TreeSet<Character>();
		this.initialDataSource = new ArrayList<String>();
	}
	
	/**
	 * Initializes an empty search ready to use.
	 * currentDestinations holds the data source for all subsequent searches.
	 * It must be initialized, otherwise search results would always be empty. 
	 * 
	 * @param input The initial data source.
	 */
	public AssistedSearch(String[] input) {
		
		if (input == null) {
			throw new IllegalArgumentException("Data source was null");
		}
		
		this.initialDataSource		= Arrays.asList(input);
		this.searchText 			= new String();
		this.currentDestinations 	= new ArrayList<String>( Arrays.asList(input) );
		this.currentOptions 		= new TreeSet<Character>();
		
		searchPossibleChars();
	}

	/**
	 * Allows the {@code AssistedSearch} instance to be reused with different 
	 * data.
	 * Current search state will be reset.
	 *  
	 * @param data The new data source
	 *
	 * Note: The object internal state gets changed.
	 * @see {@code Memento}
	 */
	public void setInitialDataSource(String[] data) {
		this.initialDataSource = Arrays.asList(data);
		reset();
	}
	/**
	 * 
	 * @return The text {@code String} of the current search
	 */
	public String getCurrentSearchText() {
		return searchText;
	}

	/**
	 * Sets the text to be searched and starts the search. The text to be 
	 * searched must be a continuation of the previous entered texts.
	 * The search is immediately executed so that the {@code AssistedSearch}
	 * internal state remains coherent.
	 * 
	 * @param text to be searched.
	 *
	 * Note: The object internal state gets changed.
	 * @see {@code Memento}
	 */
	public void startNextSearch(String text) {
		
		// Do not allow non incremental searches
		if ( !text.startsWith(searchText) ) {
			reset();
		}
		
		searchText = text;
		searchLocations();
		searchPossibleChars();
	}

	
	/**
	 * Searches the destinations array looking for searchText.
	 * The internal list of possible destinations is updated so that all 
	 * destinations that don't match are discarded. 
	 * 
	 *  Note: The object internal state gets changed.
	 *  @see {@code Memento}
	 */
	protected void searchLocations() {
		
		List<String> discardable = new ArrayList<String>();
		for (String s: currentDestinations) {
			if ( !s.startsWith(searchText) ) {
				discardable.add(s);
			}
		}
		
		currentDestinations.removeAll(discardable);
				
	}
	
	/**
	 * Searches the destinations array looking for all possible characters to 
	 * be entered next.
	 * The resulting {@Set} is stored internally. 
	 * 
	 *  Note: The object internal state gets changed.
	 *  @see {@code Memento}
	 */
	protected void searchPossibleChars() {

		currentOptions = new TreeSet<Character>();
		for (String s: currentDestinations) {
			if (searchText.length() != s.length())
				currentOptions.add(s.charAt(searchText.length()));
		}
	}
	
	/**
	 * 
	 * @return Copy of the internal results {@List<String>}
	 */
	public List<String> getLocationResults() {
		
		List<String> result;
		try {
			result = new ArrayList<String>(currentDestinations);
		} catch (NullPointerException e) {
			result = new ArrayList<String>();
			reset();
		}
		return result;
	}
	
	/**
	 * 
	 * @return Copy of the {@Set} of possible next characters to be searched
	 */
	public Set<Character> getCharOptionsResults() {
		
		Set<Character> result;
		try {
			result = new TreeSet<Character>(currentOptions);
		} catch (NullPointerException e) {
			result = new TreeSet<Character>();
			reset();
		}
		return result;
	}

	/**
	 * Resets internal state of current search. 
	 *
	 * Note: The object internal state gets changed.
	 * @see {@code Memento}
	 */
	public void reset() {
		currentDestinations = new ArrayList<String>(initialDataSource);
		searchText = "";
		searchPossibleChars();
	}
	
    /**
     * Returns a string representation of this Search, containing the searched
     * text, the search results and the available options for the next
     * characters.
     */
	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append("TicketMachine:      " + this.searchText + "\n");
		out.append("Current Results:    " + currentDestinations + "\n");
		out.append("Current Options:    " + currentOptions + "\n");
		out.append("\n____________________________________________");
		
		return out.toString();
	}


	/**
	 * Save the object state.
	 * 
	 * @return {@code Memento} object with current {@AssistedSearch} object state
	 */
	public Memento saveToMemento() { 
		return new Memento(this); 
	}

	/**
	 * Load and update the object state.
	 * 
	 *
	 * Note: The object internal state gets changed.
	 * @see {@code Memento}
	 */
	public void restoreFromMemento(Memento m) {
		
		this.searchText          = m.state;
		this.currentDestinations = m.resultList;
		this.currentOptions      = m.resultOptions;
	}
	   
    
	/**
	 * Memento captures the {@code AssistedSearch} objects state. 
	 * To be used by a client for history purposes.(undo/redo; history; ...) 
	 * 
	 * @author topexpto@gmail.com (Antonio Freire)
	 * 
	 * @see Design Patterns: Memento
	 *
	 */
	public static class Memento {
        private final String state;
    	private ArrayList<String>  resultList;    	
    	private TreeSet<Character> resultOptions;
        
        
        private Memento(AssistedSearch stateToSave) {
            state         = stateToSave.getCurrentSearchText();
            resultList    = (ArrayList<String>) stateToSave.getLocationResults();
            resultOptions = (TreeSet<Character>) stateToSave.getCharOptionsResults();
        }
 
        @Override
        public String toString() {
        	StringBuilder s = new StringBuilder("Memento:\n");
        		
        	s.append(this.state);
        	s.append("\n");
        	s.append(this.resultList);
        	s.append("\n");
        	s.append(this.resultOptions);
        	s.append("\n");
        	
        	
        	return s.toString(); 
        }
    }
}
