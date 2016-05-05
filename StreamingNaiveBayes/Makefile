
JC = javac
JFLAGS = -g -cp src
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  src/TrainHandler.java \
		  src/NBTrain.java \
		  src/MergeHandler.java \
		  src/MergeCounts.java \
		  src/TestHandler.java \
		  src/NBTest.java \

default: classes

classes: $(CLASSES:.java=.class)

train:
	cat $(data) | java -cp src -Xmx128m NBTrain | \
		sort -k1,1 | java -cp src -Xmx128m MergeCounts \
		> $(data).model

test:
	cat $(model) | java -cp src -Xmx128m NBTest $(data) > $(data).txt

full:
	cat $(data).train | java -cp src -Xmx128m NBTrain | \
		sort -k1,1 | java -cp src -Xmx128m MergeCounts | \
		java -cp src -Xmx128m NBTest $(data).test > out.txt

handin:
	mkdir handin; \
	cp src/*.java handin/; \
	cp written/report.pdf handin/; \
	cd handin/; \
	tar -cvf hw1.tgz *; \
	cd ..;

clean:
	$(RM) src/*.class data/*.model data/*.txt out.txt

