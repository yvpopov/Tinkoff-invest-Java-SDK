# Tinkoff-invest-Java-SDK
 <h2><b>Java SDK for Tinkoff-invest API(gRPC)</b></h2><br/>

За основу в SDK взяты <a href="https://github.com/Tinkoff/investAPI/tree/main/src/docs/contracts">Protobuf-файлы сервиса из официального API Tinkoff</a>.<br/>

<b>Пример.</b><br>
<pre>TinkoffSDK sdk = new TinkoffSDK(<a href="https://www.tinkoff.ru/invest/settings/">token</a>);
GetAccountsResponse accounts = sdk.getServices().getAccounts().GetAccounts();
System.out.printf("Список счетов \n%s", accounts.toString());</pre>
 
<b>Особенности.</b><br/>

Сделан контроль лимита запросов(x-ratelimit-remaining).<br/>
При достижении лимита выдерживается пауза до сброса лимита, которая расчитывается как: "время последнего запроса" (поле date из Header) + x-ratelimit-reset пауза расчитывается относительно времени на хосте, <b>рекомендуется синхронизировать время. </b><br/>
Для выключения контроля лимита запросов: <br/>
<pre>TinkoffSDK sdk = new TinkoffSDK(<a href="https://www.tinkoff.ru/invest/settings/">token</a>).setControlLimit(false);</pre>

<b>Сервисы.</b><br/>

Список доступных(реализованных) сервисов см. interface IAllServices <br/>
Доступ к сервисам организован через 2 метода: <br/><br/>
<i><b>Базовый набор сервисов:</b></i><br/>
Реализованы методы описанные <a href="https://tinkoff.github.io/investAPI/">официальной документации</a> с минимальными изменениями в параметрах методов (<i>например вместо Quotation передаем BigDecimal и т.п.</i>)
<pre>new TinkoffSDK(token).getServices()</pre>
<i><b>Модифицированный набор сервисов:</b></i><br/>
В основе базовые классы сервисов с дополнительным(модифицированным) функционалом. Например: <ul><li>Получение инструмента по Тикеру(без указания class_code)</li> <li>Получения истории свечей за произвольный период, если интервал выходит за <a href="https://tinkoff.github.io/investAPI/load_history/">ограничения</a>, то будет выполнен в несколько запросов)</li><li>и др.</li></ul>
<pre>new TinkoffSDK(token).getServicesWithChild()</pre>

<b>Подключение через Maven.</b><br/>

Добавьте в pom.xml:
<pre>    <repositories>
        <repository>
            <id>Tinkoff-invest-Java-SDK-mvn-repo</id>
            <url>https://raw.github.com/yvpopov/Tinkoff-invest-Java-SDK/mvn-repo/</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>ru.yvpopov</groupId>
            <artifactId>Tinkoff-invest-Java-SDK</artifactId>
            <version>1.0.0-Beta</version>
        </dependency>
    </dependencies>
</pre>
