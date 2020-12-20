## Webcrawl Data Processor

### Требования:
- OpenJdk:11 +;

### Примеры запросов:

##### Загрузка .WARC файла из s3 для бакета `s3://commoncrawl` по ключу `crawl-data/CC-MAIN-2016-18/segments/1461860122501.26/wet/CC-MAIN-20160428161522-00026-ip-10-239-7-51.ec2.internal.warc.wet.gz`:

```
curl --location --request PUT 'localhost:8094/api/v1/loader?root=commoncrawl&path=crawl-data/CC-MAIN-2016-18/segments/1461860122501.26/wet/CC-MAIN-20160428161522-00026-ip-10-239-7-51.ec2.internal.warc.wet.gz'
```

##### Список ключей для загруженных .WARC файлов:

```
curl --location --request GET 'localhost:8094/api/v1/store'
```

##### Получение статуса загружаемого .WARC файла по указанному ключу:

```
localhost:8094/api/v1/loader/status?path=crawl-data/CC-MAIN-2016-18/segments/1461860122501.26/wet/CC-MAIN-20160428161522-00026-ip-10-239-7-51.ec2.internal.warc.wet.gz
```

##### Поиск страниц .WARC файла по указанным ключу и ключевой фразе:

```
localhost:8094/api/v1/store/search?path=crawl-data/CC-MAIN-2016-18/segments/1461860122501.26/wet/CC-MAIN-20160428161522-00026-ip-10-239-7-51.ec2.internal.warc.wet.gz&search=music
```

### Соответствие приложения 12-ти факторной архитектуре:

1. Единая кодовая база - Есть (Git project);
2. Явное объявление зависимостей и их изоляция - Есть (Gradle);
3. Конфигурация в среде выполнения - Есть (Использование переменных окружения в application.yml);
4. Сторонние службы как ресурсы - Частично (См., как декларируются имплементации сервисов в application.yml);
5. Сборка, релиз, выполнение - Частично (Сборка на Gradle, CI и Docker не успел прикрутить);
6. Stateless процесс - Частично (Хранение стейта в WebcrawlFacade::loadCache; на хранение стейта в postgres не осталось времени);
7. Привязка портов - Нет (Таки не успел прикрутить докер);
8. Параллелизм/Масштабирование - Частично (Заложено в конфигурации см. AsyncConfig + реализована параллельная загрузка .WARC файлов, возможен запуск второго инстанса); 
9. Утилизируемость - Есть (Spring boot Hooks);
10. Паритет разработки/работы приложения - Нет (Таки не успел высторить CI/CD и сделать деплой на aws);
11. Журналирование - Частично (Spring Boot Logging + config);
12. Администрирование - Нет (Таки не успел прикрутить spring-boot-actuator);

#### Время выполнения - 12 ч/ч.