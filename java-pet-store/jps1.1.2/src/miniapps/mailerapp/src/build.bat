@echo off
set ANT_HOME=../../../lib/ant
set ANT_CLASSPATH=%JAVA_HOME%/lib/tools.jar
set ANT_CLASSPATH=%ANT_HOME%/lib/ant.jar;%ANT_HOME%/lib/xml.jar;%ANT_CLASSPATH%
set ANT_CLASSPATH=%J2EE_HOME%/lib/j2ee.jar;%ANT_CLASSPATH%
%JAVA_HOME%\bin\java -classpath "%ANT_CLASSPATH%" -Dant.home=%ANT_HOME% -Dj2ee.home=%J2EE_HOME% org.apache.tools.ant.Main %1 %2 %3 %4
