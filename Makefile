init:
	cd $(dirname $0)
	mvn -B archetype:generate -DgroupId=app.onepass.organizer -DartifactId=organizer -DarchetypeArtifactId=maven-archetype-simple -DarchetypeVersion=1.4

apis:
	cd $(dirname $0)
	git clone https://github.com/hu-tao-supremacy/api.git apis
	python3 sym.py

submodule:
	cd $(dirname $0)
	rm -rf apis
	git clone https://github.com/hu-tao-supremacy/apis.git
	python3 sym.py
