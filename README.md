# Felhő és DevOps alapok kötelező program
A Szegedi Tudományegyetem "Felhő és DevOps alapok" nevű gyakorlati kurzusra készített kötelező program. A "Programrendszerek Fejlesztése" nevű kurzusra írt kötelező programot fogja CI/CD környezetbe beletenni, különböző modulokkal kiegészítve a követelmény alapján. A jelenlegi repository-ban csak a scriptfájlokat, illetve a különböző build fájlokat lehet megtalálni.

A saját beüzemeléshez rendelkezni kell telepített dockerrel, és a következő lépéseket kell megtenni:

## 1. lépés

Klónozzuk ezt a repository-t egy tetszőleges helyre a fájlrendszerünkön a git clone paranccsal. Klónozás után, lépjünk be a klónozott mappába, és hozzunk létre egy mappát 'jenkins_home' néven, majd hagyjuk üresen. Ennek a létrehozott mappának a docker mappa mellett kell lennie a fájlrendszeren, és az a lényeg, hogy ugyan az a felhasználó hozza létre, aki majd a docker compose up parancsot futtatja (a jogosultságok miatt).

## 2. lépés

Nyissunk egy terminált, navigáljunk a docker mappába, és adjuk ki a következő parancsot:

"""
docker-compose up --build
"""

Ekkor a docker elkezdi a docker-compose.yml fájl alapján felépíteni az egész rendszert. A programrendszer több konténerből áll, és minden szerkezeti elem külön konténert alkot. Jelenleg a kliens, a szerver, az adatbázis, és a jenkins konténerek léteznek, ezek jönnek létre előre konfigurált beállításokkal.

## 3. lépés

Nagyjából 2-3 perc után (internetsebességtől függően) a docker letölti az összes imaget, és létrehozza a konfigurációs fájlok alapján a konténereket. Először a jenkins-be ajánlott belépni, ahol egy kis utólagos konfigurációt kell végezni ahhoz, hogy a rendszer véglegesen működőképes maradjon. Ezt a konténerek felállása után megtehetjük, ha felkeressük a 

"""
http://localhost:8080
"""

linken a jenkinsünk webszerveres hozzáférését.
