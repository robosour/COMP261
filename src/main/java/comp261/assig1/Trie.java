package comp261.assig1;

import java.util.*;

/**
 * This is an implementation of a trie, used for the search box.
 */

public class Trie {
	TrieNode root = new TrieNode(); // the root node of the trie

	public Trie() {
	}

	/**
	 * Adds a given stop to the Trie.
	 */
	public void add(Stop stop) {
		TrieNode node = root;
		String name = stop.getName();
		for (char c : name.toCharArray()) {
			if (!node.children.containsKey(c)) {
				TrieNode child = new TrieNode();
				node.children.put(c, child);
			}
			node = node.children.get(c);
		}
		node.data.add(stop);
	}
	/**
	 * Returns a list of stops that match the given prefix.
	 */
	public List<Stop> get(Stop stop) {
		TrieNode node = root;
		String name = stop.getName();
		for (char c : name.toCharArray()) {
			if (!node.children.keySet().contains(c)) {
				return null; // theres nothing to get
			}
			node = node.children.get(c); // or else keep going
		}
		return node.data;
	}

	/**
	 * Returns all the stops whose names start with a given prefix.
	 */
	public List<Stop> getAll(String prefix) {

		List<Stop> results = new ArrayList<>();
		TrieNode node = root;
		for (char c : prefix.toCharArray()) {
			if (!node.children.containsKey(c)) {
				return null;
			}
			node = node.children.get(c);
		}
		getAllFrom(node, results);
		return results;
	}

	//recursive helper method for getAll()
	
	private void getAllFrom(TrieNode node, List<Stop> results) {
		for (Stop st : node.data) {
			if (!results.contains(st)) {
				results.add(st);
			}
		}
		for (char child : node.children.keySet()) {
			getAllFrom(node.children.get(child), results);

		}

	}
	//get the list of stop names from root node 
	public List<Stop> getData() {
		return root.data;
	}

	/**
	 * Represents a single node in the trie. It contains a collection of the
	 * stops whose names are exactly the traversal down to this node.
	 */
	private class TrieNode {
		List<Stop> data = new ArrayList<>();
		Map<Character, TrieNode> children = new HashMap<>();

	}
}
