# Make.org Main Front
# Copyright (C) 2018 Make.org
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

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

reload-all:
	sbt clean fastOptJS::startWebpackDevServer ~fastOptJS

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
