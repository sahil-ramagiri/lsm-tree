package io.github.onedarkcoder;

import java.util.BitSet;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BloomFilter {
    
    private final BitSet bitSet = new BitSet(0xFF);
    
    public void add(String key) {
        int index = key.hashCode() & 0XFF;
        bitSet.set(index);
    }
    
    public boolean maybePresent(String key) {
        int index = key.hashCode() & 0XFF;
        return bitSet.get(index);
    }
    
}
