package io.github.onedarkcoder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LSMTreeTest {

  LSMTree lsmTree;

  @BeforeEach
  void setUp() {
    lsmTree = new LSMTree(4, 100);
  }

  @Test
  void testInsert() {
    for (int i = 0; i < 35; i++) {
      lsmTree.put("" + i, "sahil" + i);
    }

    for (int i = 0; i < 35; i++) {
      String name = lsmTree.get("" + i);
      Assertions.assertEquals("sahil" + i, name);
    }
  }

  @Test
  void testUpdate() {
    for (int i = 0; i < 35; i++) {
      lsmTree.put("" + i, "sahil" + i);
    }

    for (int i = 0; i < 35; i++) {
      lsmTree.put("" + i, "sahil2" + i);
    }

    for (int i = 0; i < 35; i++) {
      String name = lsmTree.get("" + i);
      Assertions.assertEquals("sahil2" + i, name);
    }
  }

  @Test
  void testDelete() {
    for (int i = 0; i < 35; i++) {
      lsmTree.put("" + i, "sahil" + i);
    }

    for (int i = 0; i < 35; i++) {
      lsmTree.put("" + i, null);
    }

    for (int i = 0; i < 35; i++) {
      String name = lsmTree.get("" + i);
      assertNull(name);
    }
  }

  @Test
  void testMerge() {
    for (int i = 0; i < 300; i++) {
      lsmTree.put("" + i, "sahil" + i);
    }

    for (int i = 0; i < 300; i++) {
      String name = lsmTree.get("" + i);
      Assertions.assertEquals("sahil" + i, name);
    }
  }

  @Test
  void testMergeMaxLevels() {
    for (int i = 0; i < 3000; i++) {
      lsmTree.put("" + i, "sahil" + i);
    }

    for (int i = 0; i < 3000; i++) {
      String name = lsmTree.get("" + i);
      Assertions.assertEquals("sahil" + i, name);
    }
  }
}
