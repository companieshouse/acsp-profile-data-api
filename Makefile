artifact_name       := acsp-profile-data-api
version             := unversioned

.PHONY: clean
clean:
	mvn clean
	rm -f ./$(artifact_name).jar
	rm -f ./$(artifact_name)-*.zip
	rm -rf ./build-*
	rm -rf ./build.log-*

.PHONY: test
test: test-integration test-unit

.PHONY: test-unit
test-unit: clean
	mvn verify -Dskip.unit.tests=false -Dskip.integration.tests=false

.PHONY: test-integration
test-integration: clean
	mvn verify -Dskip.unit.tests=true -Dskip.integration.tests=false

.PHONY: coverage
coverage:
	mvn verify

.PHONY: verify
verify: test-unit test-integration

.PHONY: package
package:
ifndef version
	$(error No version given. Aborting)
endif
	mvn versions:set -DnewVersion=$(version) -DgenerateBackupPoms=false
	$(info Packaging version: $(version))
	@test -s ./$(artifact_name).jar || { echo "ERROR: Service JAR not found"; exit 1; }
	$(eval tmpdir:=$(shell mktemp -d build-XXXXXXXXXX))
	cp ./start.sh $(tmpdir)
	cp ./$(artifact_name).jar $(tmpdir)/$(artifact_name).jar
	cd $(tmpdir); zip -r ../$(artifact_name)-$(version).zip *
	rm -rf $(tmpdir)

.PHONY: build
build:
	mvn versions:set -DnewVersion=$(version) -DgenerateBackupPoms=false
	mvn package -Dmaven.test.skip=true
	cp ./target/$(artifact_name)-$(version).jar ./$(artifact_name).jar

.PHONY: build-container
build-container: build
	docker build .

.PHONY: dist
dist: clean build package coverage

.PHONY: sonar
sonar:
	mvn sonar:sonar

.PHONY: sonar-pr-analysis
sonar-pr-analysis:
	mvn verify -Dskip.unit.tests=true -Dskip.integration.tests=true
	#mvn sonar:sonar -P sonar-pr-analysis #temporary until sonar available for Java 21

.PHONY: security-check
security-check:
	mvn org.owasp:dependency-check-maven:check -DassemblyAnalyzerEnabled=false
