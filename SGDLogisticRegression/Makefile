
JC = javac
JFLAGS = -g -cp src
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
				src/LR.java \
				src/LRTrain.java \
				src/LRModel.java \
				src/LRTest.java \

default: build

build: $(CLASSES:.java=.class)

run:
	for ((i=1;i<=20;i++)); \
	do gshuf data/abstract.train; \
	done | java -cp src -Xmx128m LR 10000 0.5 0.1 20 130442 data/abstract.test

small:
	for ((i=1;i<=20;i++)); \
	do gshuf data/abstract.small.train; \
	done | java -cp src -Xmx128m LR 10000 0.5 0.1 20 11272 data/abstract.tiny.test

handin:
	cp report/report.pdf src/;cd src;tar -cvf hw4.tar *.java report.pdf;mv hw4.tar ..;cd ..;rm src/report.pdf

clean:
	rm src/*.class hw4.tar
