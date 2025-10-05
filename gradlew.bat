@ECHO OFF
SET DIR=%~dp0
SET JAR=%DIR%gradle\wrapper\gradle-wrapper.jar
IF NOT EXIST %JAR% (
  ECHO Gradle wrapper JAR not found. Run 'gradle wrapper' locally to generate it.
  EXIT /B 1
)
java -jar %JAR% %*
