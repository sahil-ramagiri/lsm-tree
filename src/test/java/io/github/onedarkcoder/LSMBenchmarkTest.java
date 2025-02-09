package io.github.onedarkcoder;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

class LSMBenchmarkTest {

  private static void logMemory() {
    System.out.println();
    System.out.println("Heap " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
    System.out.println("NonHeap " + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage());
    List<MemoryPoolMXBean> beans = ManagementFactory.getMemoryPoolMXBeans();
    for (MemoryPoolMXBean bean : beans) {
      System.out.println("Bean " + bean.getName());
      System.out.println("Usage " + bean.getUsage());
    }

    for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
      System.out.println(bean.getName());
      System.out.println(bean.getCollectionCount());
      System.out.println(bean.getCollectionTime());
    }
  }

  @RepeatedTest(10)
  void testInsert() {
    LSMTree lsmTree = new LSMTree(4, 100);
    HashMap<String, String> hashMap = new HashMap<>();
    long seed = 123;
    long iter = 100000;

    long c0 = System.nanoTime();
    new Random(seed)
        .longs(iter)
        .forEach(
            l -> {
              long l1 = l + l;
            });
    long f0 = System.nanoTime();
    long t0 = (f0 - c0) / 1000000;


    long c2 = System.nanoTime();
    new Random(seed).longs(iter).forEach(l -> lsmTree.put("k" + l, "v" + l));
    long f2 = System.nanoTime();
    long t2 = (f2 - c2) / 1000000;

    long c1 = System.nanoTime();
    new Random(seed).longs(iter).forEach(l -> hashMap.put("k" + l, "v" + l));
    long f1 = System.nanoTime();
    long t1 = (f1 - c1) / 1000000;


    System.out.println("PUT baseline: " + t0 + " hashmap: " + t1 + " lsmtree: " + t2);

    long cg0 = System.nanoTime();
    new Random(seed)
            .longs(iter)
            .forEach(
                    l -> {
                      long l1 = l + l;
                    });
    long fg0 = System.nanoTime();
    long tg0 = (fg0 - cg0) / 1000000;


    long cg2 = System.nanoTime();
    new Random(seed).longs(iter).forEach(l -> lsmTree.get("k" + l));
    long fg2 = System.nanoTime();
    long tg2 = (fg2 - cg2) / 1000000;

    long cg1 = System.nanoTime();
    new Random(seed).longs(iter).forEach(l -> hashMap.get("k" + l));
    long fg1 = System.nanoTime();
    long tg1 = (fg1 - cg1) / 1000000;

    System.out.println("GET baseline: " + tg0 + " hashmap: " + tg1 + " lsmtree: " + tg2);



    hashMap.forEach((k, v) -> Assertions.assertEquals(v, lsmTree.get(k)));
  }
}
