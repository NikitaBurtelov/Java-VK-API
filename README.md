# VK API &middot;  [![Build Status](https://travis-ci.org/NikitaBurtelov/Java-VK-API.svg?branch=master)](https://travis-ci.org/NikitaBurtelov/Java-VK-API) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Библиотека предназаначена для получения и систематизации общедоступных данных с персональных страниц пользователей, состоящих в группах в Вконтакте. 

## How to use

Для работы с библиотекой вам нужно лишь знать `доменное имя группы` в Вконтакте и `token` (о том, как его узнать, будет рассказано дальше). Также нам понадобится СУБД `MySQL`.


### Первые шаги. Получение ключа доступа

- Заходим на сайт [VK Developers](https://vk.com/dev)
- Переходим в раздел `Мои приложения` и нажимаем `Создать приложение`
- Выбираем `Standalone-приложение` и придумываем название
- Даллее возвращаемся в раздел `Мои приложения` и нажимаем на `Редактировать`
- После переходим в пункт `Настройки`
- От туда копируем `Сервисный ключ доступа` - это и есть наш `token`, с помощью которого Вконтакте поймет, что вы реальный пользователь, и предоставит информацию.


### Подключение библиотеки в проект с помощью Maven.

Для подключение библиотеки в pom.xml добавляем репозиторий:

```xml
<repositories>
        <repository>
            <id>Java-VK-API-mvn-repo</id>
            <url>https://raw.github.com/NikitaBurtelov/Java-VK-API/mvn-repo/</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
</repositories>
```

Теперь подключаем саму зависимость:

```xml
<dependency>
            <groupId>com.vk_api_lib</groupId>
            <artifactId>com.vk_api_lib.dataUsers</artifactId>
            <version>1.0-SNAPSHOT</version>
</dependency>
```

### Время кода

- Класс **DataBase** - отвечает за создание и заполнение таблицы в вашей базе данных.
  
  Конструктор класса: `username`, `password`, `url`.

- Класс **WallParser** - отвечает за получение ID пользователей Вконтакте и данных этих пользователй, за парсинг json response и отпрвеление данных на запись в базу данных.

  Конструктор класса: `token`, `versionAPI`, `group ID`, `dataBase`.

**Пример использования**

В качестве примера приведены дефолтные данные для подключения к базе данных (username = root, password = root, порт = 3306).
Актуальная версия `VK_API` на данный момент - `5.103`.
Результатом метода `getUsers` будет таблица с нужной информацией.

```Java
public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        String token = "<your token>";
        String versionAPI = "<current version>";
        String domain = "<group id>";

        DataBase dataBase = new DataBase("root", "root", "jdbc:mysql://localhost:3306/test?useSSL=false");
        WallParser wallParser = new WallParser(token, versionAPI, domain, dataBase);

        wallParser.getUsers();
}
```
