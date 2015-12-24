package mmartin.trie;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.commons.lang3.StringUtils;

public class TrieTest {
	public static void main(String[] args) {
		Trie<String, Person> trie = new PatriciaTrie<>();

		String myNumber = "6138163246";
		trie.put(StringUtils.reverse(myNumber), new Person("Mike", 29));
		trie.put(StringUtils.reverse("6132651616"), new Person("Kate", 31));
		trie.put(StringUtils.reverse("6136923246"), new Person("Tony", 59));
		
		System.out.println(trie);
		
		System.out.println("Get '" +myNumber+"': " + trie.get(StringUtils.reverse(myNumber)));
		System.out.println("Prefix map '" + myNumber +"': " + trie.prefixMap(StringUtils.reverse(myNumber)));
		
		String commonSuffix = "3246";
		System.out.println("Prefix map '" + commonSuffix +"': " + trie.prefixMap(StringUtils.reverse(commonSuffix)));
		
		commonSuffix = "6";
		System.out.println("Prefix map '" + commonSuffix +"': " + trie.prefixMap(StringUtils.reverse(commonSuffix)));
	}
}
