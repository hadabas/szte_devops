# Felhő és DevOps alapok kötelező program
A Szegedi Tudományegyetem "Felhő és DevOps alapok" nevű gyakorlati kurzusra készített kötelező program. A "Programrendszerek Fejlesztése" nevű kurzusra írt kötelező programot fogja CI/CD környezetbe beletenni, különböző modulokkal kiegészítve a követelmény alapján. A jelenlegi repository-ban csak a scriptfájlokat, illetve a különböző build fájlokat lehet megtalálni.

Fontos megjegyezni, hogy ha linuxot használunk az itt leírt parancsokat bizonyos disztribúciók esetén root felhasználóként kell futtatni, ezért mindegyik elé oda kell írni ilyen esetben a 'sudo' szót (bizonyos disztribúciók esetén viszont elhagyható.)
A saját beüzemeléshez rendelkezni kell telepített dockerrel, és a következő lépéseket kell megtenni:

## 1. lépés

Klónozzuk ezt a repository-t egy tetszőleges helyre a fájlrendszerünkön a git clone paranccsal. Klónozás után, lépjünk be a klónozott mappába, és **hozzunk létre egy mappát 'jenkins_home' néven**, majd hagyjuk üresen. Ennek a létrehozott mappának a docker mappa mellett kell lennie a fájlrendszeren, és az a lényeg, hogy ugyan az a felhasználó hozza létre, aki majd a docker compose up parancsot futtatja (a jogosultságok miatt).

## 2. lépés

Nyissunk egy terminált, navigáljunk a docker mappába, és adjuk ki a következő parancsot:

```
docker compose up --build
```

Ekkor a docker elkezdi a docker-compose.yml fájl alapján felépíteni az egész rendszert. A programrendszer több konténerből áll, és minden szerkezeti elem külön konténert alkot. Jelenleg a kliens, a szerver, az adatbázis, és a jenkins konténerek léteznek, ezek jönnek létre előre konfigurált beállításokkal.

## 3. lépés

Nagyjából 2-3 perc után (internetsebességtől függően) a docker letölti az összes imaget, és létrehozza a konfigurációs fájlok alapján a konténereket. Először a jenkins-be ajánlott belépni, ahol egy kis utólagos konfigurációt kell végezni ahhoz, hogy a rendszer véglegesen működőképes maradjon. Ezt a konténerek felállása után megtehetjük, ha felkeressük a 

```
http://localhost:8080
```

linken a frissen telepített jenkinsünk webes felületét. A felhasználónév és jelszó: **admin/admin**. Lépjünk be, válasszuk ki, hogy saját magunk szeretnénk modulokat telepíteni, és felül kattintsunk a megjelenő ablakban a **None** opcióra. Nem szeretnénk csomagokat feltelepíteni, mert az automatizáció miatt a csomagok amikre szükségünk van már települtek a háttérben. Ha végeztünk, hagyjuk jóvá a döntésünket, és ezután a fő dashboardon találjuk magunkat.

## 4. lépés

Biztonsági okokból az ssh kulcsokat kézzel kell hozzáadni a rendszerhez utólag. A jobb felső sarokban kattintsunk a fogaskerékre, majd a "Credentials" menüre (ha a felület nem angol, váltsuk át a felületet angolra, a locale modul telepítve van). Kattintsunk a kék színű "System" store-ra, majd a "Global credentials (unrestricted)" kék szövegre. A jobb felső sarokban lesz egy kék gomb, "Add Credentials" néven, kattintsunk rá.

Állítsuk be a következőket:

```
Kind: SSH Username with private key
Scope: Global
Id: jenkins-deploy-key
Description: <Tetszőleges, akár kihagyható.>
Username: deploy
```

A private key résznél kattintsunk az enter directly opcióra, és jobb oldalon az 'Add' kék gombra. Másoljuk be ide a **docker/keys/jenkins_deploy_key** privát kulcs teljes tartalmát. A Passphrase-t hagyjuk üresen, majd kattintsunk a create gombra.

Ha sikeresen végrehajtottuk ezt az utólagos konfigurációt, jogot adtunk a jenkins konténerünknek arra, hogy minden más konténerre rá tudjon ssh kapcsolaton keresztül lépni, ami kulcsfontosságú a CI/CD pipeline-ok futtatásához.

A demonstráció érdekében én egy saját, védtelen, előre legenerált privát és publikus kulcskombinációval dolgozok, de ha sajátot szeretnénk generálni azt is megtehetjük, de akkor az legyen a 0. lépés az összes lépés előtt, hogy saját kulcspárt generálunk és kicseréljük a keys mappa tartalmát a saját kulcsokra (a neveknek meg kell, hogy egyezzenek). Utána a lépéseken végigmenve egészen csak ennél a lépésnél kell a saját privát kulcsunkat itt bemásolni.

**Ezzel vége is a rendszer feltelepítésének.**

## A programrendszer használata

A háttérben felállt a programrendszer pár alrendszere a docker segítségével, ezek különböző programkomponensek, amik a modularizációt kihasználva akár teljesen külön skálázhatóak. Nézzük komponensenként a hozzáférést:

## 1. Komponens: Jenkins

Elérhető a

```
http://localhost:8080
```

webcímen bármilyen böngészőben. Ezt már láthattuk az előkonfiguráció során, de ha megnyitjuk a weboldalt és bejelentkezünk, a dashboardon már található egy pár pipeline (a bal felső logóra kattintva bármikor visszadob minket a dashboardra). Először futtassuk a 'Server CICD pipeline' pipelinet. Ez feltelepíti a szerver konténerébe a jenkins pipeline instrukciói alapján a program szerver részét.

Ha ez lefutott, futtassuk le a 'Client CICD pipeline' pipelinet. Az pedig a kliens konténerébe fogja feltelepíteni a program kliens részét. Ha mind a kettő lefutott rendben, akkor elérhetővé és működőképessé válik a következő komponens, a kliens komponens.

A futtatott pipeline parancssort meg is lehet tekinteni a jenkinsen belül.

## 2. Komponens: NodeJS alkalmazás (Client + Server)

Előfeltétel, hogy a két CI/CD pipelinenak hibamentesen le kell futnia az előző lépésben. Ha ez megtörtént, a kliens elérhető a 

```
http://localhost:4200
```

webcímen. Ez a már egyszer bemutatott könyvtármenedzselő program kliens része. Ez a projekt egyébként elérhető a:

**https://github.com/hadabas/prf-konyvklub**

github repository-n. Ez a devops-os projekt ezen programkódnak a 'devops' branchen lévő verzióját használja. Az ebben a lépésben használt rész teljes egésze megtalálható az ottani 'client' mappában. Egyébként a szerverkomponens része is megtalálható az ottani 'server' mappában, de a szerverkomponens a felhasználó részére rejtve marad.

## 3. Komponens: Zabbix

Egy komplex monitorozó modul. Egy kis előkonfigurációt igényel a használata, először:

```
http://localhost:8081
```
Admin / zabbix
felhasználónév és jelszó kombinációval lépjünk be.

Majd vegyünk fel egy új host-ot a create host gombbal:
Host-name: docker-agent
Templates: Templates kateógriára kattintsunk , és keressük meg a "Docker by Zabbix agent 2" template-et.
Host Groups: Linux servers
Interfaces: Add -> Agent, válasszuk ki a DNS-t, és írjuk be: "zbx-agent2"
-- alternatívan lehet IP alapján is, az agent belső IP címe 172.32.0.11. A port minden esetben 10050.
Majd kattintsunk az Add gombra.

Ez egy speciális template, amely igényli hogy létezzen külön konténerben egy agent ami semelyik másik konténerbe sincs beépítve, viszont root jogosultsága legyen a docker socketre. Egy kis idő után elérhetővé válik az összes jelenleg futó konténer adata, és bármi érdekel minket, megnézhetjük.

## 4. Komponens: Graylog
Elérhető a
```
http://localhost:9000
```
címen.
Felhasználónév: admin, jelszó: nézzük ki a konzol ablakból (ezt csak egyszer kell).

Utána csináljuk végig az initial setup-ot a tanult módon. Egy datanode-ot csináltunk a rendszerhez. Miután az initial setup-nak vége van, újra be kell jelentkezni, ez már rögzítetten a következő adatokkal történik:

```
Username: admin
Password: verysecretpassword
```

Ismételten a tanult módon vegyünk fel egy Syslog UDP input-ot. 5140-es portra bindeljük, a 0.0.0.0 címen. A többi beállítást tetszőlegesre lehet állítani. Végül kattintsunk a Launch Input-ra, majd a sárga Set-up Input gombra. Csináljunk egy Stream-et, itt is tetszés szerint lehet beállítani, majd a végén a Launch-nál, kattintsunk a Start Input-ra.
