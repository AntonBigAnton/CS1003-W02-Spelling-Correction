import java.util.StringTokenizer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.HashSet;

public class CS1003P1 {
	public static void main(String[] args) {
		CS1003P1 practical = new CS1003P1();
		//StringTokenizer st = new StringTokenizer(args[0], " "); // Read the single parameter
		//practical.run(st);
		practical.test();
	}

	public void run(StringTokenizer st) {
		Path myPath = Paths.get("words_alpha.txt"); // Access the dictionary
		try {
			List<String> dictionary = Files.readAllLines(myPath, StandardCharsets.UTF_8); // Store the dictionary in a List object
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (dictionary.contains(token.toLowerCase())) { // Check if the word is spelt correctly, a.k.a, it exists in the dictionary
					System.out.print(token + " ");
				}
				else { // Find the most similar string from the dictionary
					HashSet<String> tokenBigram = new HashSet<String>(calculateBigram(token));
					double max = 0.0;
					String closest = "";
					for (String word : dictionary) { // Cycle through every word of the dictionary
						HashSet<String> wordBigram = new HashSet<String>(calculateBigram(word));
						double similarity = calculateJaccard(wordBigram, tokenBigram);
						if (similarity > max) {
							max = similarity;
							closest = word;
						}
					}
					System.out.print(closest + " ");
				}
			}
		}
		catch (IOException e) { // Line 20 returns an IOException error - we have to ignore it
			e.printStackTrace();
		}
		System.out.println();
	}

	public HashSet<String> calculateBigram(String word) {
		word = "^" + word + "$"; // Add the top-and-tail
		HashSet<String> bigram = new HashSet<String>(); // The bigram is a HashSet object, to avoid repeating sequences
		for (int i=0; i<word.length()-1; i++) { // Cycle through every charcater of the inputted word, except the final one
			String a = "" + word.charAt(i);
			String b = "" + word.charAt(i+1);
			String sequence = a+b;
			bigram.add(sequence); // The HashSet automatically checks if the added sequence is already in the bigram
		}
		return bigram;
	}

	public double calculateJaccard(HashSet<String> set1, HashSet<String> set2) {
		HashSet<String> intersection = new HashSet<String>();
		HashSet<String> union = new HashSet<String>();
		if (set1.size() > set2.size()) { // Check which set is the largest
			intersection.addAll(set1);
			intersection.retainAll(set2); // Only keep the elements that are common to both sets
		}
		else {
			intersection.addAll(set2);
			intersection.retainAll(set1);
		}
		set1.addAll(set2); // Set1 being a HashSet, adding all elements of set2 does not create duplicates
		union.addAll(set1); // We do not have to remove the intersection to get the union (c.f. formula |A u B| = |A| + |B| - |A n B|)
		return (double) intersection.size()/union.size();
	}

	// TESTING METHODS
	// To run the tests, remove the comment symbol on line 15
	
	public double testJaccard(String word1, String word2) {
		return calculateJaccard(testBigramNoTopAndTail(word1), testBigramNoTopAndTail(word2));
	}

	public HashSet<String> testBigramNoTopAndTail(String word) { // This method is a copy-and-paste of calculateBigram, without the top-and-tail 
		HashSet<String> bigram = new HashSet<String>();
		for (int i=0; i<word.length()-1; i++) {
			String a = "" + word.charAt(i);
			String b = "" + word.charAt(i+1);
			String sequence = a+b;
			bigram.add(sequence);
		}
		return bigram;
	}

	public String testTopAndTail(String word) { // This method is a copy-and-paste of line 49
		word = "^" + word + "$";
		return word;
	}

	public void test() {
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("ADDITIONAL TESTING");
		System.out.println();
		System.out.println("	1. Testing the Jaccard similarity WITHOUT the top-and-tail:");
		System.out.println("		1. a) chips and crisps:");
		System.out.println("			  Expected outcome: 0.125");
		System.out.println("			  Actual outcome: " + testJaccard("chips", "crisps"));
		System.out.println();
		System.out.println("		1. b) cheese and cheese:");
		System.out.println("			  Expected outcome: 1.0");
		System.out.println("			  Actual outcome: " + testJaccard("cheese", "cheese"));
		System.out.println();
		System.out.println("		1. c) Ozgur Akgun and Alan Dearle:");
		System.out.println("			  Expected outcome: 0.0");
		System.out.println("			  Actual outcome: " + testJaccard("Ozgur Akgun", "Alan Dearl"));
		System.out.println();
		System.out.println("		1. d) speled and spelled:");
		System.out.println("			  Expected outcome: 0.833");
		System.out.println("			  Actual outcome: " + testJaccard("speled", "spelled"));
		System.out.println();
		System.out.println("	2. Testing the top-and-tail:");
		System.out.println();
		System.out.println("		2. a) Cocoa:");
		System.out.println("			  Expected outcome: ^Cocoa$");
		System.out.println("			  Actual outcome: " + testTopAndTail("Cocoa"));
		System.out.println();
		System.out.println("	3. Testing the character bigram WITH the top-and-tail:");
		System.out.println();
		System.out.println("		3. a) cocoa:");
		System.out.println("			  Expected outcome: [^c, co, oc, oa, a$]");
		System.out.println("			  Actual outcome: " + calculateBigram("cocoa"));
		System.out.println();
		System.out.println("		3. b) Antoine:");
		System.out.println("			  Expected outcome: [^A, An, nt, to, oi, in, ne, e$]");
		System.out.println("			  Actual outcome: " + calculateBigram("Antoine"));
		System.out.println();
		System.out.println("		3. c) banana:");
		System.out.println("			  Expected outcome: [^b, ba, an, na, a$]");
		System.out.println("			  Actual outcome: " + calculateBigram("banana"));
		System.out.println();
		System.out.println("	4. Testing the Jaccard similarity WITH the top-and-tail:");
		System.out.println();
		System.out.println("		4. a) Antoine and Anton:");
		System.out.println("			  Expected outcome: 0.4");
		System.out.println("			  Actual outcome: " + calculateJaccard(calculateBigram("Antoine"), calculateBigram("Anton")));
		System.out.println();
		System.out.println("		4. b) chips and crisps:");
		System.out.println("			  Expected outcome: 0.3");
		System.out.println("			  Actual outcome: " + calculateJaccard(calculateBigram("chips"), calculateBigram("crisps")));
		System.out.println();
		System.out.println("		4. c) cheese and cheese:");
		System.out.println("			  Expected outcome: 1.0");
		System.out.println("			  Actual outcome: " + calculateJaccard(calculateBigram("cheese"), calculateBigram("cheese")));
		System.out.println();
		System.out.println("		4. d) Ozgur Akgun and Alan Dearle:");
		System.out.println("			  Expected outcome: 0.0");
		System.out.println("			  Actual outcome: " + calculateJaccard(calculateBigram("Ozgur Akgun"), calculateBigram("Alan Dearl")));
		System.out.println();
		System.out.println("---------------------------------------------------------------------------");
	}
}