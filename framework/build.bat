javac -d framework/build framework/src/*.java

cd framework\build

jar -cf fw.jar .
 
copy fw.jar "..\..\testFramework\WEB-INF\lib"

cd ..\..\testFramework

javac -parameters -d WEB-INF/classes src/*.java


jar -cf testFramework.war .

copy testFramework.war "C:\apache-tomcat-8\webapps"
