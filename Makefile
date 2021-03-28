init:
	cd $(dirname $0)
	mvn -B archetype:generate -DgroupId=app.onepass.organizer -DartifactId=organizer -DarchetypeArtifactId=maven-archetype-simple -DarchetypeVersion=1.4

apis:
	cd $(dirname $0)
	git clone https://github.com/hu-tao-supremacy/api.git apis
	python3 sym.py

apis-update:
	cd $(dirname $0)
	rm -rf apis
	git clone https://github.com/hu-tao-supremacy/api.git apis
	python3 sym.py

apis-process:
	cd $(dirname $0)
	rm -rf apis
	git clone https://github.com/hu-tao-supremacy/api.git apis
	cd apis && git checkout jean/organizer
	cd apis && make setup && make generate
	python3 sym.py

setup:
	@cd $(dirname $0)
	ln -s proto/hts hts

generate:
	@cd $(dirname $0)
	make build-go
	make build-java
	make build-python
	make build-ts

build-go:
	@cd $(dirname $0)
	bazel build //:go
	rm -rf gen/go
	mkdir -p gen/go
	mv bazel-bin/go/gen/hts gen/go/.
	git add -f gen/go

build-python:
	@cd $(dirname $0)
	bazel build //:python
	rm -rf gen/python
	mkdir -p gen/python
	mv bazel-bin/python/gen/hts gen/python/.
	git add -f gen/python

build-java:
	@cd $(dirname $0)
	bazel build //:java
	rm -rf gen/java
	mkdir -p gen/java
	cp -LR bazel-bin/java/gen/java.srcjar gen/java/.
	cp -LR bazel-bin/java/gen/java_grpc.srcjar gen/java/.
	mv gen/java/java.srcjar gen/java/java.zip
	mv gen/java/java_grpc.srcjar gen/java/java_grpc.zip
	unzip -n gen/java/java.zip -d gen/java
	unzip -n gen/java/java_grpc.zip -d gen/java
	rm -rf gen/java/java.zip
	rm -rf gen/java/java_grpc.zip
	rm -rf gen/java/META-INF
	git add -f gen/java

build-ts:
	@cd $(dirname $0)
	bazel clean
	bazel build //:nest
	bazel build //:gql
	rm -rf gen/nest && mkdir -p gen/nest
	rm -rf gen/gql && mkdir -p gen/gql
	cp -LR bazel-bin/nest/gen/hts gen/nest/.
	cp -LR bazel-bin/gql/gen/hts gen/gql/.
	cp -LR bazel-bin/nest/gen/google gen/nest/.
	cp -LR bazel-bin/gql/gen/google gen/gql/.
	git add -f gen/nest
	git add -f gen/gql
