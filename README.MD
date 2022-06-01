read schema
```shell
unzip -p file.jar migrations/schema.sql
```

run including external module
```shell
java -cp application.jar -Dloader.path=module.jar -Dloader.main=com.viloveul.app.example.management.ManagementApplication org.springframework.boot.loader.PropertiesLauncher
```