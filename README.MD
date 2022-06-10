read schema
```shell
unzip -p file.jar migrations/schema.sql
```

run including external module
```shell
java -cp application.jar -Dloader.path=module.jar -Dloader.main=com.viloveul.app.ExampleApplication org.springframework.boot.loader.PropertiesLauncher
```

```shell
CLASSPATH=${PWD}/package/context/target/viloveul-context.jar ~/Downloads/flyway-8.5.12/flyway -url=jdbc:postgresql://localhost:2001/viloveul?currentSchema=public -user=viloveul -password=123456 -locations=classpath:db/migration -baselineVersion=0.0.0 baseline migrate
```