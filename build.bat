@REM compilation du framework
    javac -d framework/build framework/src/*.java


@REM transformation du framework en librairie jar
    cd framework\build

    jar -cf fw.jar .

@REM copie du librairie jar dans le repertoir lib/ du projet
    copy fw.jar "..\..\testFramework\WEB-INF\lib"

@REM compilation du projet de test
    cd ..\..\testFramework

    javac -d WEB-INF/classes src/*.java

@REM suppression du contenu du repertoire temp
    cd ../temp

    rmdir /S /Q "WEB-INF"

    del /F /Q "."

@REM structuration du repertoire temp
    mkdir WEB-INF

    cd ../

    robocopy testFramework\WEB-INF temp\WEB-INF /E

    copy "testFramework\*.jsp" "temp"

@REM transformation du repertoire temp en fichier war
    cd temp

    jar -cf testFramework.war .

@REM deploiement du projet
    copy testFramework.war "C:\apache-tomcat-8\webapps"
