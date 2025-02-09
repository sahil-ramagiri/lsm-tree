package io.github.onedarkcoder;

import java.util.*;

public class LSMTree {

  private final int _maxLevel;

  private final MemTable _memTable;

  private final List<LinkedList<SSTable>> levels = new ArrayList<>();
  private final List<SummaryFile> summaryFiles = new ArrayList<>();

  public LSMTree(int maxLevel, int memTableCapacity) {
    this._maxLevel = maxLevel;
    this._memTable = new MemTable(memTableCapacity);
  }

  public void put(String key, String value) {
    if (!_memTable.tryPut(key, value)) {
      SSTable ssTable = _memTable.flush();
      addToLevel(0, ssTable);
      _memTable.tryPut(key, value);
    }
  }

  private void addToLevel(int i, SSTable ssTable) {
    if (levels.size() == i) {
      levels.add(new LinkedList<>());
      summaryFiles.add(new SummaryFile(null, null, new BloomFilter()));
    }

    LinkedList<SSTable> level = levels.get(i);
    level.push(ssTable);
    updateSummary(summaryFiles.get(i), ssTable);

    if (i != _maxLevel && level.size() == (i + 1) * 4) {

      while (!level.isEmpty()) {
        SSTable second = level.removeLast();
        SSTable first = level.removeLast();

        SSTable merged = merge(first, second);
        addToLevel(i + 1, merged);
      }
    }
  }

  public String get(String key) {
    DataPair valueFromMemTable = _memTable.get(key);
    if (valueFromMemTable != null) {
      return valueFromMemTable.getValue();
    }

    DataPair valueFromTree = findInStorage(key);
    if (valueFromTree != null) {
      return valueFromTree.getValue();
    }

    return null;
  }

  private DataPair findInStorage(String key) {

    for (int levelIndex = 0; levelIndex < levels.size(); levelIndex++) {
      if (summaryFiles.get(levelIndex).getBloomFilter().maybePresent(key)) {
        for (SSTable ssTable : levels.get(levelIndex)) {
          DataPair dataPair = ssTable.find(key);
          if (dataPair != null) {
            return dataPair;
          }
        }
      }
    }
    return null;
  }

  private void updateSummary(SummaryFile summaryFile, SSTable ssTable) {

    BloomFilter bloomFilter = summaryFile.getBloomFilter();
    Arrays.stream(ssTable.getData()).forEach((dataPair -> bloomFilter.add(dataPair.getKey())));

    DataPair firstEntry = ssTable.getData()[0];
    DataPair lastEntry = ssTable.getData()[ssTable.getData().length - 1];

    if (summaryFile.getMaxKey() == null
        || summaryFile.getMaxKey().compareTo(firstEntry.getKey()) < 0) {
      summaryFile.setMaxKey(firstEntry.getKey());
    }

    if (summaryFile.getMinKey() == null
        || summaryFile.getMinKey().compareTo(lastEntry.getKey()) > 0) {
      summaryFile.setMinKey(lastEntry.getKey());
    }
  }

  private SSTable merge(SSTable first, SSTable second) {
    DataPair[] firstData = first.getData();
    DataPair[] secondData = second.getData();

    DataPair[] data = new DataPair[firstData.length + secondData.length];

    int i = 0;
    int j = 0;
    int curr = 0;
    while (i < firstData.length && j < secondData.length) {
      DataPair firstPair = firstData[i];
      DataPair secondPair = secondData[j];

      if (firstPair.getKey().compareTo(secondPair.getKey()) < 0) {
        data[curr] = firstPair;
        i++;
      } else if (firstPair.getKey().compareTo(secondPair.getKey()) == 0) {
        data[curr] = firstPair;
        i++;
        j++;
      } else {
        data[curr] = secondPair;
        j++;
      }
      curr++;
    }

    for (; i < firstData.length; i++, curr++) {
      data[curr] = firstData[i];
    }

    for (; j < secondData.length; j++, curr++) {
      data[curr] = secondData[j];
    }

    data = Arrays.copyOfRange(data, 0, curr);

    return new SSTable(data);
  }
}
