# Felhő és DevOps alapok kötelező program
A Szegedi Tudományegyetem "Felhő és DevOps alapok" nevű gyakorlati kurzusra készített kötelező program. A "Programrendszerek Fejlesztése" nevű kurzusra írt kötelező programot fogja CI/CD környezetbe beletenni, különböző modulokkal kiegészítve a követelmény alapján. A jelenlegi repository-ban csak a scriptfájlokat, illetve a különböző build fájlokat lehet megtalálni.

Fontos megjegyezni, hogy ha linuxot használunk az itt leírt parancsokat bizonyos disztribúciók esetén root felhasználóként kell futtatni, ezért mindegyik elé oda kell írni ilyen esetben a 'sudo' szót (bizonyos disztribúciók esetén viszont elhagyható.)
A saját beüzemeléshez rendelkezni kell telepített dockerrel, és a következő lépéseket kell megtenni:

## 1. lépés

Klónozzuk ezt a repository-t egy tetszőleges helyre a fájlrendszerünkön a git clone paranccsal. Klónozás után, lépjünk be a klónozott mappába, és hozzunk létre egy mappát 'jenkins_home' néven, majd hagyjuk üresen. Ennek a létrehozott mappának a docker mappa mellett kell lennie a fájlrendszeren, és az a lényeg, hogy ugyan az a felhasználó hozza létre, aki majd a docker compose up parancsot futtatja (a jogosultságok miatt).

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

Biztonsági okokból az ssh kulcsokat kézzel kell hozzáadni a rendszerhez utólag. A jobb felső sarokban kattintsunk a fogaskerékre, majd a "Credentials" menüre (ha a felület nem angol, váltsuk át a felületet angolra, a locale modul telepítve van). Kattintsunk a kék színű "System" store-ra, majd a Global credentials (unrestricted) kék szövegre. A jobb felső sarokban lesz egy kék gomb, "Add Credentials" néven, kattintsunk rá.

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
