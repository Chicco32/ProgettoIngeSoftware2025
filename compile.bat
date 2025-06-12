setlocal enabledelayedexpansion
for /f "delims=" %%a in ('dir /b /s /a-d giornateFAI\src') do set "files=!files! %%a"
javac -sourcepath giornateFAI/src -classpath "junit.jar";"Librerie Esterne\mysql-connector-j-9.2.0.jar";"Librerie Esterne\UniBSFpLib.jar";"Librerie Esterne\jBCrypt.jar" -d giornateFAI/bin %files%
