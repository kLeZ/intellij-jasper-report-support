# Contributing

## Porting a version of Jasper Reports XSD

```shell
VERSION=6.20.6
FOLDER=${VERSION//\./_}
echo $FOLDER
ABS_PROJ_PATH=$HOME/projects
ABS_PATH=$ABS_PROJ_PATH/intellij-jasper-report-support/src/main/resources/me/klez/schemas/$FOLDER/xsd
mkdir -p $ABS_PATH
find $ABS_PROJ_PATH/jasperreports/jasperreports/src -name '*.xsd' -print -exec cp {} $ABS_PATH \;
```
