// code by gemini
package ch.alpine.curios.usr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SimpleBPE {
  public record Pair(String s1, String s2) {
    public String merged() {
      return s1 + s2;
    }
  }

  private List<List<String>> corpus = new ArrayList<>();
  private final List<Pair> merges = new ArrayList<>();

  public void addWord(String spaceSeparatedWord, int frequency) {
    List<String> wordTokens = Arrays.asList(spaceSeparatedWord.split(" "));
    for (int i = 0; i < frequency; i++) {
      corpus.add(new ArrayList<>(wordTokens));
    }
  }

  public void train(int numMerges) {
    for (int i = 0; i < numMerges; i++) {
      Map<Pair, Integer> pairCounts = getPairCounts();
      if (pairCounts.isEmpty()) {
        break;
      }
      // Find the most frequent pair
      Pair bestPair = Collections.max(pairCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
      String mergedToken = bestPair.merged();
      merges.add(bestPair);
      mergePairInCorpus(bestPair, mergedToken);
      System.out.println("Merge " + (i + 1) + ": " + bestPair + " -> " + mergedToken);
    }
  }

  public List<String> encode(String spaceSeparatedWord) {
    if (spaceSeparatedWord == null || spaceSeparatedWord.isBlank()) {
      return List.of();
    }
    List<String> tokens = new ArrayList<>(Arrays.asList(spaceSeparatedWord.trim().split("\\s+")));
    for (Pair pair : merges) {
      tokens = mergePairInTokens(tokens, pair, pair.merged());
      if (tokens.size() < 2) {
        break;
      }
    }
    return tokens;
  }

  public List<Pair> getMerges() {
    return Collections.unmodifiableList(merges);
  }

  public void printSummary() {
    System.out.println("=== SimpleBPE Summary ===");
    System.out.println("Total corpus entries: " + corpus.size());
    System.out.println("Learned merges (" + merges.size() + "):");
    for (int i = 0; i < merges.size(); i++) {
      Pair pair = merges.get(i);
      System.out.println("  " + (i + 1) + ". (" + pair.s1() + ", " + pair.s2() + ") -> \"" + pair.merged() + "\"");
    }
    // Group identical tokenized sequences so the output stays readable
    Map<List<String>, Integer> groupedEntries = new LinkedHashMap<>();
    Set<String> activeTokens = new TreeSet<>();
    for (List<String> tokens : corpus) {
      groupedEntries.put(tokens, groupedEntries.getOrDefault(tokens, 0) + 1);
      activeTokens.addAll(tokens);
    }
    System.out.println("Current unique tokens in corpus (" + activeTokens.size() + "): " + activeTokens);
    System.out.println("Current tokenized corpus:");
    for (Map.Entry<List<String>, Integer> entry : groupedEntries.entrySet()) {
      System.out.println("  " + entry.getKey() + " x" + entry.getValue());
    }
    System.out.println("=========================");
  }

  private Map<Pair, Integer> getPairCounts() {
    Map<Pair, Integer> counts = new HashMap<>();
    for (List<String> word : corpus) {
      for (int i = 0; i < word.size() - 1; i++) {
        Pair pair = new Pair(word.get(i), word.get(i + 1));
        counts.put(pair, counts.getOrDefault(pair, 0) + 1);
      }
    }
    return counts;
  }

  private void mergePairInCorpus(Pair pair, String replacement) {
    List<List<String>> newCorpus = new ArrayList<>();
    for (List<String> word : corpus) {
      newCorpus.add(mergePairInTokens(word, pair, replacement));
    }
    corpus = newCorpus;
  }

  private static List<String> mergePairInTokens(List<String> tokens, Pair pair, String replacement) {
    List<String> merged = new ArrayList<>();
    int i = 0;
    while (i < tokens.size()) {
      if (i < tokens.size() - 1 && tokens.get(i).equals(pair.s1()) && tokens.get(i + 1).equals(pair.s2())) {
        merged.add(replacement);
        i += 2;
      } else {
        merged.add(tokens.get(i));
        i++;
      }
    }
    return merged;
  }

  public static void main(String[] args) {
    SimpleBPE bpe = new SimpleBPE();
    bpe.addWord("l o w </w>", 6);
    bpe.addWord("l o w e r </w>", 4);
    bpe.addWord("l o w e s t </w>", 3);
    bpe.addWord("n e w </w>", 4);
    bpe.addWord("n e w e r </w>", 3);
    bpe.addWord("n e w e s t </w>", 2);
    bpe.train(10);
    bpe.printSummary();
    System.out.println("Encode 'l o w e s t </w>': " + bpe.encode("l o w e s t </w>"));
    System.out.println("Encode 'n e w e s t </w>': " + bpe.encode("n e w e s t </w>"));
    System.out.println("Encode 'l o w e r e s t </w>': " + bpe.encode("l o w e r e s t </w>"));
    System.out.println("Encode 's l o w e r </w>': " + bpe.encode("s l o w e r </w>"));
  }
}
