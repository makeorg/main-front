.PHONY: package-docker-image release run test-all test-all-with-coverage test-int test-unit

help:
	@echo "Please use 'make <target>' where <target> is one of"
	@echo "   package-docker-image           to build locally the docker image"
	@echo "   release                        to release the application"
	@echo "   run                            to run app"
	@echo "   test-all                       to test the application"
	@echo "   test-all-with-coverage         to test the application with code coverage"
	@echo "   test-int                       to test the application (integration tests)"
	@echo "   test-unit                      to test the application (unit tests)"

package-docker-image:
	sbt publishLocal

release:
	sbt release

run:
	sbt ~fastOptJS::startWebpackDevServer


test-all: test-unit test-int

test-all-with-coverage:
	sbt clean coverage test it:test
	sbt coverageReport coverageAggregate

test-int:
	sbt it:test

test-unit:
	sbt test
