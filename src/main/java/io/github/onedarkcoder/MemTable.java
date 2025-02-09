package io.github.onedarkcoder;

import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemTable {

  private final int capacity;

  private final TreeMap<String, String> treeMap = new TreeMap<>();

  public boolean tryPut(String key, String value) {

    if (treeMap.containsKey(key)) {
      treeMap.put(key, value);
      return true;
    }

    if (treeMap.size() < capacity) {
      treeMap.put(key, value);
      return true;
    }
    return false;
  }

  public SSTable flush() {

    DataPair[] data =
        treeMap.sequencedEntrySet().stream()
            .map(entry -> new DataPair(entry.getKey(), entry.getValue()))
            .toArray(DataPair[]::new);

    SSTable ssTable = new SSTable(data);

    treeMap.clear();
    return ssTable;
  }

  public DataPair get(String key) {

    if (treeMap.containsKey(key)) {
      String value = treeMap.get(key);
      return new DataPair(key, value);
    }

    return null;
  }
}
