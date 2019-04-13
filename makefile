all: compileclient compileserver

compileclient: jarclient

compileserver: jarserver

jarclient: classes
	jar cfm Cliente.jar manifest2.mf -C build/classes .

jarserver: classes
	jar cfm Servidor.jar manifest1.mf -C build/classes .

classes: dir
	javac -sourcepath src/ -d build/classes src/*.java

dir:
	if [ ! -d build/classes ]; then mkdir -p build/classes; fi
	if [ ! -d build/jar ]; then mkdir -p build/jar; fi
clean:
	if [ -d build ]; then rm build -R; fi

runserver:
	java -jar Servidor.jar 

runclient:
	java -jar fileout/Cliente.jar 
