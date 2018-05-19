# <img src="/resources/icon.png" width="35"> JiveHive jututuba

Programm on lihtne grupichati tüüpi jututuba, mis koosneb eraldi serveri osast (*ServeriLiides.java, ServeriRakendus.java, Server.java*) ja kliendile mõeldud osast (*JiveHive.java*), lisaks veel erinditöötluseks mõeldud klassid (*ÜhenduseEbaõnnestumineErind.java, KorduvKasutajaErind.java, KasutajanimePikkusErind.java*) ning fail (*JiveHiveStylesheet.css*) kasutajaliidese kujundamise abistamiseks.
Serveri ja kliendi vahelise ühenduse loomiseks kasutasin **Java RMI** teeki ja toetusin varasemale Programmeerimisharjutuste aines antud ülesandele ning [Oracle'i tutorialile](https://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html). 

![JiveHive rakendus](/resources/klient.PNG)

---

## Kasutusjuhend

1. Esimese asjana käivitada käsurealt *rmiregistry* serveri rollis olevas arvutis. Näiteks Windowsiga arvutis on seda võimalik teha käsuga ```start rmiregistry```. Käivitamine peab käima käsurealt **selles kataloogis, kus asuvad .class failid**. Ehk IntelliJ puhul on see harilikult \<projekt\>/out/production/\<projekt\> kaustas.
    * Juhul kui arvuti ei tunne käsku ära, siis lisada kõigepealt CLASSPATH vastavasse Java bin kausta.
2. Käivitada Server.java ja vajutada tekkinud aknas "Ühenda". (Vajadusel täpsusta üle IP aadress.)

![JiveHive server](/resources/server.PNG)

3. Nüüd võib avada soovikohase arvu JiveHive.java rakendusi ja hakata suhtlema.

---

## Miks JiveHive?

- Kui sõber istub arvutiga kõrval, aga kummalgi ei ole soovi oma häälepaelu kurnata, siis ideaalne viis omavaheliseks suhtluseks 
- Kui oled väsinud kirjutest emojidest, siis JiveHive's sa midagi sellist ei näe!
- Kui vanaema jaoks Messengeri või Skype'i kasutamine on liiga keeruline, siis installi talle JiveHive 
- Kui oled tüdinenud maksimaalse funktsionaalsusega jututoa-tüüpi rakendustest ja soovid midagi lihtsat ja maalähedast
- Kui armastad mesilasi või tunned, et kollase-oranži kirjud toonid on väga südamelähedased, sest kujundust muuta ei ole võimalik
- Kui vihkad moderaatoreid, siis tea, et JiveHive's mingit tsensuuri ei ole
- Ei kannata, kui programm pakub igal käivitusel jälle mingeid uuendusi? Garanteerin, et mitte mingisuguseid uuendusi ei tule!
- Saadaval täiesti TASUTA!

---

## Lisamärkusi serveri kohta

Serveri käivitamiseks on olemas hästi minimalistlik kasutajaliides, mis muuseas püüab leida automaatselt serveri arvuti IP aadressi, ent seda saab kasutaja alati ka käsitsi ise määrata. Ühenduse katkestamisel (ka akna ristist kinnipanemisel juhul, kui ühendus on veel aktiivne) koristab server enda järelt korralikult ära ja teda saab kohe ilma probleemideta uuesti käivitada, aga seda ainult juhul, kui programm sulgus normaalselt. Mitte korrektse programmist väljumise näide oleks näiteks see, kui ServeriRakendus käivitada IntellliJ's ja seal programmi töö lõpetamiseks vajutada *stop* nuppu (õige oleks vajutada *exit* nuppu). Sellisel juhul server ei saa ennast lahti siduda registrist ja teistkordsel käivitamisel RMI samal asjal enam ühendust "uuesti" luua ei luba. Sellisel juhul tuleb ka *rmiregistry* kinni panna ja kõik täiesti uuesti käivitada.

---
## Lisamärkusi kliendi kohta

Kliendile mõledud JiveHive rakenduse kasutajaliides küsib kõigepealt kasutajalt serveri IP aadressi, kuhu ühendada, ja kasutajanime. Kui server asub samas arvutis, siis võib IP tühjaks jätta, sest RMI püüab sellisel juhul alati kohalikust registrist infot kätte saada. Kui sinna midagi suvalist panna, annab programm dialoogi akna abil teada, et ühendumine ebaõnnestus. Ise täheldasin, et kui proovida täiesti valel kujul infot sinna panna (nt. "12345"), siis erind visatakse koheselt, ent vale IP aadressi sisestamisel programm otsib seda võrdlemisi pikka aega ja viskab alles väikese aja pärast erindi. Kasutajanime puhul kontrollitakse, ega sellise nimega kasutajat juba liitunud ei ole ja kas kasutajanimi on mõistliku pikkusega.
Jututoa põhiaknas saab sõnumeid saata kas nupule vajutusega või *enteriga*. Vestluse uuendamine on korraldatud AnimationTimeri abil, kus rakendus püüab pidevalt pöörduda serveri poole, et uurida, kas sõnumitelogis on mingeid uuendusi vahepeal tekkinud (s.t. kas mõni kasutaja on vahepeal liitunud/lahkunud või keegi midagi kirjutanud). 

---

## Mis saab siis, kui ühendus katkeb?

Kui ühendus ajutiselt ära kaduma peaks, siis programm "jookseb kokku" ja koheselt teadet ei ole võimeline viskama. Ühenduse kadumisest õnnestub teada saada juhul, kui üritada vajutada *saada* nuppu, aga ka siis on programmi töö võrdlemisi aeglane. Ajapuuduse tõttu ei olnud selliseid probleeme võimalik sajaprotsendiliselt lahendada ega kõiki võimalikke stsenaariume testida. Seetõttu teen hetkel sellise eelduse, et ühenduse katkestus ei ole tõenäoline ja juhul kui ta tekib, siis ta on väga lühiajaline. Sellise katkestuse puhul kliendid eemaldatakse ajutiselt ja liidetakse siis kohe jälle uuesti (toimub automaatselt) ning eelnenud vestlus taastakse koos lisatud märkusega, et vahepeal toimus ühenduse katkestus.

---
## Nimest

Rakenduse nime JiveHive valiku lugu on selline, et googeldasin sünonüüme *chatile* inglise keeles ja jäin pidama sõnal *jive* (mille üks tähendusi on ka "*to talk nonsense*") ning kuna see riimus sõnaga *hive*, siis tunduski sobilik panna jututoale selline nimi, mis laias laastus võiks tähendada näiteks "plärakogumit".

---
## Kasutatud piltide allikad
[Taust](https://thehungryjpeg.com/product/76470-seamless-honeycomb-pattern/) \
[Ikoon](https://www.iconfinder.com/icons/339771/bee_food_honey_natural_sugar_sweet_icon#size=256) \
Logo: ise tehtud!

