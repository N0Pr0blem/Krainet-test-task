# Что нужно для старта проекта
*  Клонировать репозиторий
*  Установить _Docker/Docker-compose_
*  Создать в корне проекта _.env_ файл (сделано для удобства настройки и безопасности). Вот пример такого файла:
***
```
POSTGRES_USER=root
POSTGRES_PASSWORD=root

PGADMIN_EMAIL=k.bobkov1408@gmail.com
PGADMIN_PASSWORD=admin

SPRING_MAIL_USERNAME=admin@miniuser.ru
SPRING_MAIL_PASSWORD=HjeX4cIst5mw
SPRING_MAIL_TREAD_COUNT=5

JWT_SECRET=b5f59337a612a2a7dc07328f3e7d1a04722967c7f86df20a499a7d3f91ff2a7c
JWT_PASSWORD_SECRET=F2K2DZ82odq$13e8aENggaMbb_fAkl-nJL4AEVBX43g
JWT_EXPIRATION=7200
```
***
Если создать файл с такими настройками, программа будет работать.
*  Теперь достаточно запустить от имени администратора файл по пути _bash/start_project.sh_ или в консоле 
***
# Для теста функционала
*  Есть _postman_ коллекция _test-task.postman_collection.json_
*  Пароль от _User_ и _Admin_ - __11111111__
***
# Дополнительная информация
*  Я не знаю в каком виде нужно было отправлять пароль на почту, но я посичитал, что в захешированном виде это будет безопаснее. Если все же нужно было в оригинальном виде, готов исправить в любой момент
*  Логи выводятся в консоль
*  Попробовал добавить локаль
*  Очень прошу дать оценку моего кода, чтоб я мог сделать его лучше
*  Спасибо за уделенное время