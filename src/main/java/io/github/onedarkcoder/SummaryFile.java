package io.github.onedarkcoder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummaryFile {

    private String minKey;
    private String maxKey;

    private final BloomFilter bloomFilter;
}
