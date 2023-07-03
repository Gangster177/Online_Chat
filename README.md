# Курсовой проект "Сетевой чат"

## [Описание проекта](https://github.com/netology-code/jd-homeworks/blob/master/diploma/networkchat.md)

Вам нужно разработать два приложения для обмена текстовыми сообщениями по сети с помощью консоли (терминала) между двумя и более пользователями.

**Первое приложение - сервер чата**, должно ожидать подключения пользователей.

**Второе приложение - клиент чата**, подключается к серверу чата и осуществляет доставку и получение новых сообщений.

# Описание предоставленного решения к проекту

### Состав проекта и необходимые настройки для запуска приложения и тестов
Проект выполнен в виде многомодульного приложения Maven. Проект состоит из трех модулей:
1. serverpack - содержит классы серверного приложения.
2. сlientpack - содержит классы клиентского приложение.

## [Сервер](https://github.com/Gangster177/courseworkChat/blob/main/src/main/java/serverpack/Server.java)
### Сервер используется для принятия подключений пользователей и получения от них сообщений
- Устанавливаем поток для принятия новых пользователей в [ServerHandlerClient](https://github.com/Gangster177/courseworkChat/blob/main/src/main/java/serverpack/Server.java#L20), откуда происходит дальнейшее чтение сообщений каждого пользователя отдельно
- ServerHandlerClient.java - класс для обработки [сообщений](https://github.com/Gangster177/courseworkChat/blob/main/src/main/java/serverpack/ServerHandlerClient.java)
- Получение и отправка [сообщений](https://github.com/Gangster177/courseworkChat/blob/main/src/main/java/serverpack/ServerHandlerClient.java#L27-L59)
- Сообщения записываются в [file.log]()

## [Client.java](https://github.com/Gangster177/courseworkChat/blob/main/src/main/java/clientpack/ClientFirst.java)
- Считываем [settings](https://github.com/Gangster177/courseworkChat/blob/main/src/main/java/clientpack/ClientFirst.java#L18-L21)
- Выбор [имени для участия в чате](https://github.com/Gangster177/courseworkChat/blob/main/src/main/java/clientpack/ClientFirst.java#L39)
- [Поток чтения сообщений](https://github.com/Gangster177/courseworkChat/blob/main/src/main/java/clientpack/ClientFirst.java#L87-L98) от сервера
- [Поток отправки сообщений](https://github.com/Gangster177/courseworkChat/blob/main/src/main/java/clientpack/ClientFirst.java#L86-L125) на сервер
