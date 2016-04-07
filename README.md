## Stanford CoreNLP Shift-Reduce Parser for Japanese

_Chart-based Japanese parsers exist, why bother? Because shift-reduce is much faster and Stanford's is accurate._

#### Build
```bash
> gradlew build
> gradlew copyRuntimeLibs
```

#### Prepare
Annotate Japanese sentences with word boundaries and part-of-speeches (POS). For example, use [KyTea](http://www.phontron.com/kytea/)
```bash
> kytea -notag 2 < sentence_file.txt > tagged_sentence_file.txt
```
Note that words must be delimited by half-width whitespace and POS must attach to each word with a slash as separator.
```
すもも/名詞 も/助詞 もも/名詞 も/助詞 もも/名詞 の/助詞 うち/名詞
```

#### Parse
Be careful with classpath and model path.
```bash
> java -cp build/libs/yaraku-nlp-0.1.jar:lib/* \
    com.yaraku.nlp.parser.shiftreduce.demo.JapaneseShiftReduceParserDemo \
    -model ja.beam.rightmost.model.ser.gz \
    < tagged_sentence_file.txt > parsed_sentence_file.txt
```
For example, input
```
すもも/名詞 も/助詞 もも/名詞 も/助詞 もも/名詞 の/助詞 うち/名詞
```
and expect the outcome like
```
(ROOT (名詞P (助詞P (助詞P (名詞 すもも) (助詞 も)) (助詞P (名詞 もも) (助詞 も))) (名詞P (助詞P (名詞 もも) (助詞 の)) (名詞 うち))))
```

---

#### Train

_If you must...._

1. Get a Japanese treebank such as [Japanese Dependency Corpus (JDC)](http://plata.ar.media.kyoto-u.ac.jp/data/word-dep/ "日本語係り受けコーパス")
2. Prepare the trees in Penn Treebank S-expression.
   * For example, use https://github.com/neubig/travatar/blob/master/script/tree/ja-dep2cfg.pl
     to convert JDC's trees.
3. Build a model with training and development sets, e.g. `JDC/train/all.cfg` and `JDC/dev/all.cfg`
```bash
> java -cp build/libs/yaraku-nlp-0.1.jar:lib/* \
    edu.stanford.nlp.parser.shiftreduce.ShiftReduceParser \
    -headFinder com.yaraku.nlp.trees.RightHeadFinder \
    -trainTreebank JDC/cfg/train/all.cfg \
    -devTreebank JDC/cfg/dev/all.cfg \
    -serializedPath ja.beam.rightmost.model.ser.gz \
    -trainingThreads 8 \
    -trainingIterations 60 \
    -stalledIterationLimit 20 \
    -trainingMethod REORDER_BEAM \
    -trainBeamSize 4 \
    -randomSeed 31337
```

---

###### Acknowledgement
* Thanks to [Prof. Graham Neubig](https://github.com/neubig) for the advice and scripts.
* This work is made possible through the support of [Yaraku, Inc.](https://www.yarakuzen.com/about).
