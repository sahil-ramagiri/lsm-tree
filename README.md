# lsm-tree
Java implementation of lsm-tree data structure


## Benchmark

```
In memory Performance in ms
maxlevel = 4; 
memtableCapacity = 100; 
seed = 123; iter = 100000;


PUT baseline: 9 hashmap: 130 lsmtree: 230
GET baseline: 4 hashmap: 33 lsmtree: 1355
PUT baseline: 2 hashmap: 26 lsmtree: 69
GET baseline: 2 hashmap: 20 lsmtree: 1717
PUT baseline: 2 hashmap: 40 lsmtree: 70
GET baseline: 2 hashmap: 14 lsmtree: 1286
PUT baseline: 2 hashmap: 33 lsmtree: 77
GET baseline: 2 hashmap: 15 lsmtree: 2008
PUT baseline: 2 hashmap: 27 lsmtree: 86
GET baseline: 2 hashmap: 12 lsmtree: 1605
PUT baseline: 2 hashmap: 37 lsmtree: 82
GET baseline: 2 hashmap: 15 lsmtree: 1263
PUT baseline: 2 hashmap: 33 lsmtree: 68
GET baseline: 2 hashmap: 17 lsmtree: 1471
PUT baseline: 2 hashmap: 55 lsmtree: 48
GET baseline: 1 hashmap: 12 lsmtree: 1251
PUT baseline: 2 hashmap: 72 lsmtree: 46
GET baseline: 2 hashmap: 19 lsmtree: 1266
PUT baseline: 2 hashmap: 35 lsmtree: 74
GET baseline: 2 hashmap: 16 lsmtree: 1699
```