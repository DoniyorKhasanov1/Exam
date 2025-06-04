## 1. Thread nima?

Thread - bu bir process (jarayon) ichidagi mustaqil ish bajaruvchi eng kichik birlik.
Thread'lar bir process ichida mustaqil vazifalar (tasks) bajaradi, lekin resurslardan (xotira, data) umumiy foydalanadi.
***

## 2. Multithreading va Multitasking farqi nimada?

Multitasking - bu bir kompyuter yoki odamning bir vaqtning o'zida bir nechta to'liq jarayonni bajarishi hisoblanadi.
Multitasking'ni alohida bir process deb tasavvur qilsak bo'ladi.
Masalan bir inson bir vaqtning o'zida musiqa eshitish, kod yozish yoki internetda browsing qilish kabi amallarni bir
vaqtda bajarishidir.
Kompyuter kabi mashinalarda esa bu jarayonga o'yin o'ynash, Browser oynasidagi jarayonlarni boshqarish kabi amallarni
misol qilib keltirish mumkin.
Multithreading - bu bitta protses ichida bir nechta oqimlar (threads) turli xil vazifalarni mustaqil bajarishidir.
Multithreading da thread lar alohida vazifalarni bajarsa ham, kompyuter resurslaridan umumiy foydalanadi.
***

## 3. ThreadPool nima?

Thread Pool — bu ko‘p sonli vazifalarni (tasks) samarali
boshqarish uchun oldindan yaratilgan va qayta ishlatiladigan
thread’lar to‘plami. Java'da har bir vazifa uchun yangi thread
yaratish va uni yo‘q qilish resurslarni juda ko‘p talab qiladi (CPU,
xotira va vaqt). Thread Pool bu muammoni hal qiladi, chunki u
mavjud thread’larni qayta-qayta ishlatadi.
Thread Pool nima?
Java'da Thread Pool asosan java.util.concurrent paketidagi
ExecutorService interfeysi (Java 1.5) va uning amalga
oshiruvchi class’lari (masalan, ThreadPoolExecutor) orqali
ta’minlanadi.
***

## 4. Callable vs Runnable

`Runnable` va `Callable<V>` interfeyslari orasidagi farq, biz `Runnable` interface orqali thread yaratganimizda undagi
`run()`
metodi orqali thread ni ishga tushirishimiz mumkin. `Runnable` thread lar task ni bajaradi va hech qanday natija
qaytarmasdan
threadlar `DEAD` holatiga o'tadi, yani o'chib ketadi. `Callable<V>` interface `call()` metodini taqdim etadi. Bu metod
orqali
biz thread yaratishimiz va ishga tushirishimiz mumkin, lekin `Runnable` dan farqi bu metod bizga thread dagi natijani
olish
imkonini beradi va biz bu natija ustida boshqa amallarni ham bajarishimiz mumkin.
***

## 5. `Future` nima va qachon ishlatilinadi?

`Future<V>` interface - bu threadlardan qaytgan natijani olish uchun ishlatiladigan interfeys bo'lib, thread'dan qaytgan
natijani
`get()` metodi orqali olish va keyinchalik natija ustida boshqa amallar qilish imkoniyatini beradi. Future interfeysi
`java.util.concurrent` package da joylashgan bo'lib, Java 1.5 versiyadan boshlab Multithreaded dasturlarda muhim
ahamiyatga ega bo'lib keladi va keng qo'llaniladi.
***

# 6. `Future<V>` va `CompletableFuture<V>` o‘rtasidagi farq

## 🔹 `Future<V>` — Oddiy va bloklovchi asinxronlik

`Future` interfeysi Java’da asinxron ishlov natijasini olish uchun ishlatiladi. Quyidagi imkoniyatlarni taqdim etadi:

- `get()` — natijani kutib olish (lekin bu bloklovchi metod)
- `cancel()` — ish bajarilishini to‘xtatish
- `isDone()` — ish tugaganini tekshirish

### ❗ Cheklovlar:

- Faqat `get()` orqali natijani olasiz — bu bloklaydi.
- Callback (javobdan keyingi avtomatik javob) yo‘q.
- Chain (bosqichma-bosqich) ishlovlarni amalga oshirish qiyin.

---

## 🔹 `CompletableFuture<V>` — Kuchli, zamonaviy, reaktiv asinxronlik

`CompletableFuture` — Java 8 dan kiritilgan va `Future`dan ancha qulay. Asinxron, non-blocking va reaktiv uslubda
ishlaydi.

### ✅ Imkoniyatlar:

- **Chain qilish**:
  ```java
  CompletableFuture.supplyAsync(() -> 5)
      .thenApply(x -> x * 2)
      .thenAccept(System.out::println); 

***

## 7. `volatile` non-access modifier haqida

Java'da multithreaded dasturlar yozishda, ba’zi field'lar ustida bir nechta threadlar bir vaqtda ishlashi mumkin.
Shunday holatda `volatile` keyword yordamida biror o‘zgaruvchining **main memory** bilan bevosita bog‘lanishini
ta’minlaymiz.

```java
public volatile Integer count;
```

***

## 8. Immutable classlar nima uchun kerak va immutable class yaratishimiz uchun qanday shartlar bajarilishi kerak?

Immutable class lar yaratish bizga bazi malumotlar dastur ishlash davomida o'zgarmasligi kerak bo'ladi. Immutable class
yaratishimiz uchun birinchi navbatda class ni `final` keywordi bilan belgilashimiz kerak. Bu uning boshqa class lar
tominidan
`extends` olinmasligini ta'minlaydi. Class malumotlari `private` bo'lishi kerak, chunki boshqa class lar tomonidan uning
field lariga kirish oldini olish kerak. Keyingi navbatda, biz bu class malumotlaridan foydalanmoqchi bo'lsak, getter
metodlari
yaratishimiz va bu metodlarda uning deep copy sini berib yuborishimiz kerak bo'ladi. setter lardan foydalanilmaydi,
chunki bu turdagi obyektimiz o'zgarmasligini taminlash lozim
***

## 9. Asynchronous programming nima?

Asinxron dasturlash - bu dastur bajarilishini kutmasdan uzoq davom etadigan vazifani boshlashi mumkin bo'lgan
texnikadir. Buning o'rniga, dastur boshqa vazifalarni bajarishda yoki voqealarga javob berishda davom etishi mumkin,
ayni paytda boshlang'ich vazifa fonda ishlaydi. Bu tarmoq so'rovlari yoki fayllarni yuklash kabi ko'p vaqt talab
qiladigan operatsiyalar bilan shug'ullanganda ham ilovalarga sezgir va samarali bo'lib qolishiga imkon beradi.
*** 

## 10. Atomic class lar qanday algoritm orqali race condition oldini oladi?

Atom sinflari umumiy o'zgaruvchilar ustida atom operatsiyalarini ta'minlash orqali irq sharoitlarini oldini oladi. Bu
operatsiyalar o'sish yoki taqqoslash va almashtirish kabi umumiy o'zgaruvchi bo'yicha operatsiyalar ketma-ketligi
yagona, bo'linmas bitim sifatida ko'rib chiqilishini ta'minlaydi. Bu bir nechta iplarning umumiy o'zgaruvchida
bir-birining ishlashiga xalaqit berishiga yo'l qo'ymaydi va shu bilan poyga sharoitlarini tavsiflovchi nomuvofiq
holatdan qochadi.
***

## 11.Serialization va deserialization nima?

Java-da **Serialization** ob'ekt holatini bayt oqimiga aylantirish jarayoni bo'lib, keyinchalik uni saqlash yoki uzatish
mumkin. **Deserialization** - ob'ektni bayt oqimidan qayta qurish teskari jarayon. Ushbu jarayonlar ob'ektlarni
fayllarga saqlash, ularni tarmoqlar orqali yuborish yoki ma'lumotlar bazalarida saqlash kabi vazifalar uchun juda
muhimdir.
Classni serializatsiya qilish uchun u java.io.Serializable interfeysini amalga oshirishi kerak. serialVersionUID
serializatsiya qilinadigan sinfning har bir versiyasi uchun noyob identifikator bo'lib, seriyani bekor qilish vaqtida
muvofiqlikni ta'minlash uchun ishlatiladi. Statik va vaqtinchalik maydonlar ketma-ketlashtirilmaydi.
***

## 12. Serializable vs Externalizable interfaces

`Serializable` - bu marker interface. Unda hech qanday method aniqlanmagan. Uning obyektlarni stream ga aylantirish
jarayoni JVM orqali avtomatlashtirilgan. Uni dasturchi custom behavior berib yubora olmaydi.
`Externalizable` - bu esa interface. Bu interfeysda `writeExternal()` va `readExternal()` methodlari aniqlangan va bu
metodlardan foydalanib stream ga aylantirilayotgan malumotlar ustida custom behavior belgilash imkoniyati bor
***

## 13. ReentrantLock haqida malumot

Java-da `ReentrantLock` sinxronlangan kalit so'zga qaraganda blokirovkani ko'proq boshqarishni ta'minlaydigan
mexanizmdir. "Reentrant" atamasi thread o'zini blokirovka qilmasdan bir xil qulfni bir necha marta olishi mumkinligini
anglatadi. Bu, ayniqsa, ichki o'rnatilgan sinxronlashtirilgan usullar yoki bloklarni o'z ichiga olgan stsenariylarda,
qulflarning oldini olishda foydalidir.
***

## 14. Logging haqida

Logging — bu dasturda muhim ma’lumotlarni (masalan, xabarlar, xatolar, holatlar) maxsus faylga yoki konsolga yozish
jarayoni. U quyidagi maqsadlarda ishlatiladi:

- **Xatolarni aniqlash:** Dasturda xato yuzaga kelganda uning sababini aniqlash.
- **Monitoring:** Dastur real vaqtda qanday ishlayotganini kuzatish.
- **Audit:** Foydalanuvchi harakatlarini yoki tizim o‘zgarishlarini yozib borish.
- **Debugging:** Kodni tahlil qilish va muammolarni topish.

***

## 15. Daemon thread nima?

Java-da daemon threadlar user threadlarni qo'llab-quvvatlaydigan low-priorityga ega backround process hisoblanadi. Ular
garbage collection, monitoring va boshqa fon xizmatlari kabi vazifalarni bajaradi. JVM barcha user threadlar
bajarilishini tugatgandan so'ng avtomatik ravishda demon threadlarni tugatadi. Daemon threadlari JVM ning chiqishia
to'sqinlik qilmaydi.
***

## 16. Jar file nima?

JAR (Java arxivi) fayli qulay distribution va deployment uchun Java classidagi koʻplab fayllarni, resurslarni va
metamaʼlumotlarni bitta faylga jamlaydi. ZIP fayl formatiga asoslanib, u Java ilovalari, kutubxonalari yoki modullarini
boshqarishni soddalashtiradi. JAR fayllari Java ilovalari va ularning komponentlarini bitta HTTP tranzaksiyasida yuklab
olishni osonlashtiradi, yuklab olish tezligini oshiradi va siqishni qo'llab-quvvatlaydi, bu esa fayl hajmini yanada
kamaytiradi. Ular, shuningdek, kodning kelib chiqishi va yaxlitligini tekshirish uchun raqamli imzolanishi mumkin.
***

## 17. Maven nima?

Maven - bu asosan Java ilovalari uchun foydalaniladigan qurilishni avtomatlashtirish va loyihalarni boshqarish vositasi.
U manba kodini kompilyatsiya qilish, bog'liqliklarni boshqarish, ikkilik fayllarni qadoqlash va testlarni bajarish
jarayonini soddalashtiradi. Maven ushbu vazifalarni konventsiyadan ko'ra konfiguratsiya (Convention over configuration)
yondashuvi asosida avtomatlashtiradi va qo'lda yaratish skriptlariga bo'lgan ehtiyojni kamaytiradi.
***

## 18. Behavior parameterization nima?

Java-da Behavior parameterization - bu metodlarga turli xil xatti-harakatlar yoki harakatlarni argument sifatida qabul
qilish va bajarishga imkon beradigan texnikadir. U to'g'ridan-to'g'ri kodini o'zgartirmasdan method functionality ni
sozlash imkonini beradi. Bunga odatda lambda ifodalari yoki method reference ko'rinishidagi kodni parametr sifatida o'
tkazish orqali erishiladi. Ushbu parametrlar methodda qo'llanilishi kerak bo'lgan xatti-harakatni ifodalaydi
***

### 19. Imperative va Declarative programming

## Imperative Programming

- Bu yondashuvda dasturchi kompyuterga qadam-baqadam nima qilish kerakligini aytadi. Ya'ni, "qanday" bajarilishini aniq
  yozadi. Dasturchi xotira holatini, o‘zgaruvchilarni, sikllarni va shartli operatorlarni boshqaradi.
  ## Declarative programming
    - Bu uslubda esa siz "nima qilish kerakligini" aytasiz, lekin "qanday bajarilishi" bilan shug‘ullanmaysiz. Ya’ni,
      siz maqsadni ko‘rsatasiz, tizim esa unga qanday erishishni hal qiladi.

***

## 20 .Stream va Collection

Stream — bu Java'da ma'lumotlar ustida ketma-ket amallarni bajarishga mo‘ljallangan abstraksiya. U ma'lumot manbasidan (
masalan, Collection’dan) kelgan elementlarni ketma-ket yoki parallel tarzda qayta ishlashga yordam beradi. Stream
o‘z-o‘zidan ma'lumot saqlamaydi, faqat oqim tarzida ularni qayta ishlaydi.

Stream va Collection o‘rtasidagi asosiy farq shundaki:
Collection — bu ma'lumotlarni saqlashga mo‘ljallangan tuzilma.
Stream esa — bu ma'lumotlarni oqim tarzida qayta ishlash uchun vosita.

*** 
Tranzaksiya nima?
Tranzaksiya — ma'lumotlar bazasida bir yoki bir nechta operatsiyalarni bir butun sifatida bajarish jarayoni bo'lib, ular yaxlit va muvofiq tarzda amalga oshiriladi. Tranzaksiya ma'lumotlar bazasining holatini bir muvofiq holatdan boshqa muvofiq holatga o'tkazadi va agar xatolik yuz bersa, o'zgarishlar bekor qilinadi.

Misol: Bankda bir hisobdan boshqa hisobga pul o'tkazish. Bu jarayon ikkita operatsiyani o'z ichiga oladi:
Birinchi hisobdan pulni ayirish.
Ikkinchi hisobga pulni qo'shish. Agar ikkala operatsiya ham muvaffaqiyatli bajarilmasa, tranzaksiya bekor qilinadi, aks holda ma'lumotlar bazasi notog'ri holatda qolishi mumkin.
Tranzaksiyalar ma'lumotlar bazasining ishonchliligi, muvofiqligi va xavfsizligini ta'minlash uchun ishlatiladi.

Tranzaksiyalarning asosiy xususiyatlari (ACID)
ACID xususiyatlari
Atomicity
Ta'rifi: Tranzaksiyadagi barcha operatsiyalar yaxlit tarzda bajarilishi kerak, ya'ni tranzaksiyaning barcha qismlari muvaffaqiyatli bajariladi yoki hech biri bajarilmaydi.
Misol: Bank tranzaksiyasida bir hisobdan ikkinchi hisobga pul o'tkazishda, agar pul bir hisobdan chiqarilsa, lekin boshqa hisobga tushmasa, tranzaksiya to'liq bekor qilinadi.
PostgreSQL'da: Tranzaksiyalar BEGIN, COMMIT va ROLLBACK buyruqlari orqali boshqariladi. Agar tranzaksiya muvaffaqiyatsiz bo'lsa, ROLLBACK barcha o'zgarishlarni bekor qiladi.
Consistency
Ta'rifi: Tranzaksiya ma'lumotlar bazasini bir muvofiq holatdan boshqa muvofiq holatga o'tkazadi. Bunda barcha ma'lumotlar integrallik qoidalariga (constraints), triggerlarga va boshqa qoidalarga rioya qilishi kerak.
Misol: Agar ma'lumotlar bazasida hisob balansining manfiy bo'lmasligi qoidasi bo'lsa, tranzaksiya ushbu qoidani buzmaydi.
PostgreSQL'da: Foreign key constraints, unique constraints va triggerlar orqali muvofiqlik ta'minlanadi. Qoida buzilsa, tranzaksiya ROLLBACK bilan bekor qilinadi.
Isolation
Ta'rifi: Tranzaksiyalar bir-biridan mustaqil ravishda ishlaydi. Bir tranzaksiya boshqa tranzaksiyalarning natijalariga ta'sir qilmasdan yoki ulardan ta'sirlanmasdan bajariladi.
Misol: Ikkita foydalanuvchi bir vaqtning o'zida bir hisobni o'zgartirsa, izolyatsiya ularning o'zgarishlari bir-biriga aralashmasligini ta'minlaydi.
PostgreSQL'da: MVCC (Multiversion Concurrency Control) mexanizmi ishlatiladi. Izolyatsiya darajalari (READ COMMITTED, REPEATABLE READ, SERIALIZABLE) tranzaksiyalar o'rtasidagi o'zaro ta'sirni boshqaradi.
Durability
Ta'rifi: Tranzaksiya muvaffaqiyatli yakunlanganidan so'ng (COMMIT qilinganidan keyin), o'zgarishlar doimiy ravishda saqlanadi va tizim nosozligi bo'lsa ham yo'qolmaydi.
Misol: Pul o'tkazish tranzaksiyasi yakunlansa, tizim o'chib-qayta yoqilganda ham o'zgarishlar saqlanib qoladi.
PostgreSQL'da: WAL (Write-Ahead Logging) mexanizmi orqali barqarorlik ta'minlanadi.

