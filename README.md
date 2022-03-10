# Tinkoff-invest-Java-SDK
 <h2><b>Java SDK for Tinkoff-invest API(gRPC)</b></h2><br/>

За основу в SDK взяты <a href="https://github.com/Tinkoff/investAPI/tree/main/src/docs/contracts">Protobuf-файлы сервиса из официального API Tinkoff</a>.<br/>

<b>Пример.</b><br>
<pre><code>TinkoffSDK sdk = new TinkoffSDK(<a href="https://www.tinkoff.ru/invest/settings/">token</a>);
GetAccountsResponse accounts = sdk.getServices().getAccounts().GetAccounts();
System.out.printf("Список счетов \n%s", accounts.toString());</code></pre>
 
<b>Особенности.</b><br/>

Сделан контроль лимита запросов(x-ratelimit-remaining).<br/>
При достижении лимита выдерживается пауза до сброса лимита, которая расчитывается как: "время последнего запроса" (поле date из Header) + x-ratelimit-reset пауза расчитывается относительно времени на хосте, <b>рекомендуется синхронизировать время. </b><br/>
Для выключения контроля лимита запросов: <br/>
<pre><code>TinkoffSDK sdk = new TinkoffSDK(<a href="https://www.tinkoff.ru/invest/settings/">token</a>).setControlLimit(false);</code></pre>

<b>Сервисы.</b><br/>

Список доступных(реализованных) сервисов см. interface IAllServices <br/>
Доступ к сервисам организован через 2 метода: <br/><br/>
<i><b>Базовый набор сервисов:</b></i><br/>
Реализованы методы описанные <a href="https://tinkoff.github.io/investAPI/">официальной документации</a> с минимальными изменениями в параметрах методов (<i>например вместо Quotation передаем BigDecimal и т.п.</i>)
<pre><code>new TinkoffSDK(<a href="https://www.tinkoff.ru/invest/settings/">token</a>).getServices()</code></pre>
<i><b>Модифицированный набор сервисов:</b></i><br/>
В основе базовые классы сервисов с дополнительным(модифицированным) функционалом. Например: <ul><li>Получение инструмента по Тикеру(без указания class_code)</li> <li>Получения истории свечей за произвольный период, если интервал выходит за <a href="https://tinkoff.github.io/investAPI/load_history/">ограничения</a>, то будет выполнен в несколько запросов)</li><li>и др.</li></ul>
<pre><code>new TinkoffSDK(<a href="https://www.tinkoff.ru/invest/settings/">token</a>).getServicesWithChild()</code></pre>

<b>Подключение через Maven.</b><br/>

Добавьте в pom.xml:
<pre><code>    &lt;repositories&gt;
        &lt;repository&gt;
            &lt;id&gt;Tinkoff-invest-Java-SDK-mvn-repo&lt;/id&gt;
            &lt;url&gt;https://raw.github.com/yvpopov/Tinkoff-invest-Java-SDK/mvn-repo/&lt;/url&gt;
            &lt;snapshots&gt;
                &lt;enabled&gt;true&lt;/enabled&gt;
                &lt;updatePolicy&gt;always&lt;/updatePolicy&gt;
            &lt;/snapshots&gt;
        &lt;/repository&gt;
    &lt;/repositories&gt;

    &lt;dependencies&gt;
        &lt;dependency&gt;
            &lt;groupId&gt;ru.yvpopov&lt;/groupId&gt;
            &lt;artifactId&gt;Tinkoff-invest-Java-SDK&lt;/artifactId&gt;
            &lt;version&gt;1.0.9.1-Beta&lt;/version&gt;
        &lt;/dependency&gt;
    &lt;/dependencies&gt;
</code></pre>
