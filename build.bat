@REM compilation du framework
    javac -d framework/build framework/src/*.java


@REM transformation du framework en librairie jar
    cd framework\build

    jar -cf fw.jar .


@REM suppression du contenu du repertoire temp
    cd ../../temp

    for /D %d in (*) do rd /s /q "%d"

@REM structuration du repertoire temp
    mkdir WEB-INF

    cd WEB-INF
 
copy fw.jar "..\..\testFramework\WEB-INF\lib"

cd ..\..\testFramework

javac -parameters -d WEB-INF/classes src/*.java




jar -cf testFramework.war .

copy testFramework.war "C:\apache-tomcat-8\webapps"
