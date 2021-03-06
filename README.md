# Weather Forecast

https://github.com/AlexeyKobyakov/WeatherForecast

## Описание

Необходимо создать Android приложение прогноза погоды.

В качестве API использовать: https://openweathermap.org/api

В качестве дизайна использовать следующие макеты: https://www.figma.com/file/10cef4F9w1lt8xtmlsbnAR/Test

## Техническое задание

Экран списка городов с минимальной информаций о погоде

Города и информация о погоде должны сохраняться в базе данных

При первом открытие экрана, необходимо обновляться информацию о погоде для существующих городов

Если сервер не доступен, выводить информацию из базы данных

С помощью Pull-to-refresh должна быть возможность обновить информацию

Через карточку города должна быть возможность удалить город с диалоговым подтверждением

Через карточку города должна быть возможность добавить город в избранное. Под избранным городами подразумеваются города, которые всего отображаются первыми в списке и отделены от остальных городов

Города должны быть отсортированы по порядку добавления их в базу

Экран детальной информации о погоде:

При открытии экрана необходимо обновить основную информацию о погоде и загрузить прогноз погоды на 3 дня

Если сервер не доступен, выводить информацию из базы данных

Прогноз на 3 дня сохранять не надо

Экран добавление города:

Содержит поле для ввода и кнопку добавления. После нажатия на кнопку “Добавить” необходимо получить информацию от сервера по введенному городу и добавить ее в базу данных.

После сохранения информации закрыть экран и обновить основной список городов

## Требования к приложению

Минимальное API: 21

Максимальное API: 31

Portrait режим

Только смартфоны

Светлая тема, но с возможностью в дальнейшем использовать темную тему с минимальными доработками

Дизайнер мог нарисовать не все возможные состояния, поэтому если чего-то не хватает, то можно добавлять дополнительные состояния и Material компоненты

## Реализация

![Screenshot 1](/1.jpg?raw=false)
![Screenshot 2](/2.jpg?raw=false)