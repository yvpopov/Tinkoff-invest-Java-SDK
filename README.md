# Tinkoff-invest-Java-SDK
 Java SDK for Tinkoff-invest API(gRPC)
  (в разработке)

Пример.

TinkoffSDK sdk = new TinkoffSDK(token); //Инициализация
GetAccountsResponse accounts = sdk.getAccounts().GetAccounts();
System.out.printf("Список счетов \n%s", accounts.toString());

Особенности.

Сделан контроль лимита запросов(x-ratelimit-remaining).
При достижении лимита выдерживается пауза до сброса лимита,
которая расчитывается как: 
"время последнего запроса" (поле date из Header) + x-ratelimit-reset
!!! 
пауза расчитывается относительно времени на хосте, 
рекомендуется синхронизировать время.
!!!
Для выключения контроля лимита запросов
TinkoffSDK sdk = new TinkoffSDK(token).setControlLimit(false);


