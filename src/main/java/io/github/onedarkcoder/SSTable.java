package io.github.onedarkcoder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SSTable {

  private final DataPair[] data;

  public DataPair find(String key) {

    int i =
        Arrays.binarySearch(data, new DataPair(key, null), Comparator.comparing(DataPair::getKey));

    if (i >= 0 && i < data.length && Objects.equals(data[i].getKey(), key)) {
      return data[i];
    }
    return null;
  }
}
