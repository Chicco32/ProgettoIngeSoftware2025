setlocal enabledelayedexpansion
for /f "delims=" %%a in ('dir /b /s /a-d giornateFAI\src') do set "files=!files! %%a"
javac -sourcepath giornateFAI/src -classpath "Librerie Esterne\UniBSFpLib.jar";"Librerie Esterne\jBCrypt.jar" -d giornateFAI/bin %files%
