package ticketmachine.search;

import java.util.Stack;

import ticketmachine.search.AssistedSearch.Memento;
import ticketmachine.test.TestLists;

public class Test {

	public static void main(String[] args) {
		Stack<Memento> history = new Stack<Memento>();
		AssistedSearch search = new AssistedSearch(TestLists.inputA);
		history.push(search.saveToMemento());
		System.out.println(search);
		
		search.startNextSearch("D");
		history.push(search.saveToMemento());
		System.out.println(search);

		search.startNextSearch("DA");
		history.push(search.saveToMemento());
		System.out.println(search);
		
		System.out.println("##### #####");
		
		search.startNextSearch("T");
		history.push(search.saveToMemento());
		System.out.println(search);

		System.out.println("BS");
		search.restoreFromMemento(history.pop());
		
		System.out.println("BS");
		search.restoreFromMemento(history.pop());
		
		System.out.println("BS");
		search.restoreFromMemento(history.pop());
	}

}
