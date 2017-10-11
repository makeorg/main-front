.PHONY: clean release reload-all start test-all test-all-with-coverage test-int test-unit

help:
	@echo "Please use 'make <target>' where <target> is one of"
	@echo "   clean                          to clean"
	@echo "   release                        to release the application"
	@echo "   reload-all                     to clean and start webpack dev server"
	@echo "   start                          to start webpack dev server"
	@echo "   test-all                       to test the application"
	@echo "   test-all-with-coverage         to test the application with code coverage"
	@echo "   test-int                       to test the application (integration tests)"
	@echo "   test-unit                      to test the application (unit tests)"

export SBT_OPTS=-Xms2G -Xmx2G

package-docker-image:
	CI_BUILD=true sbt publishLocal

clean:
	sbt clean

release:
	sbt release

reload-all: clean start

start:
	sbt fastOptJS::startWebpackDevServer ~fastOptJS

test-all: test-unit test-int

test-all-with-coverage:
	sbt clean coverage test it:test
	sbt coverageReport coverageAggregate

test-int:
	sbt it:test

test-unit:
	sbt test
