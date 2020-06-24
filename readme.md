# Članovi tima
* SW-7/2016 Nikola Nemeš
* SW-21/2016 Aleksandar Nedaković

# Motivacija

Osnovna motivacija projektnog rešenja je da pomogne neiskusnim igračima igre DotA 2 (Defense of the Anicents 2) u fazi izbora heroja. Smatramo da ovo predstavlja kompleksan problem koji zavisi od mnogobrojnih faktora koji se mogu opisati produkcionim pravilima. Stoga verujemo da ekspertski sistem baziran na pravilima predstavlja dobar pristup rešavanju datog problema.

# Pregled problema

DotA je igra MOBA žanra koja se igra 5 na 5 igrača i u kojoj svaki igrač pre početka partije bira heroja kojeg će igrati. Sama faza biranja heroja je od velikog značaja na sam tok partije, pošto je svaki heroj unikatan na svoj način i ispunjava neke od uloga u timu. 
Naš sistem bi prilikom preporučivanja uzimao u obzir:

* već izabrane heroje (u prijateljskom i u protivničkom timu)
* posmatraju se sinergije sa herojima u prijateljskom timu
* posmatraju se nivoi kontriranja heroja u protivničkom timu
* posmatra se celokpuna kompozicija heroja u prijateljskom timu (npr: da li su sve uloge zadovoljene barem nekim od heroja)
* zasebno se posmatra nivo kontriranja heroja iz protivničkog tima protiv kog korisnik očekuje da će se sukobljavati na početku partije (lane matchups)
* heroje koji su zabranjeni za igru u toj partiji (banned heroes)
* korisnikove preference heroja (koje on sam unosi)

Primer sličnog rešenja se može pronaći na linku http://dotapicker.com/. Naše rešenje, za razliku od postojećeg, posmatra i igračeve preference kao i lane matchup-e.

# Input

Trenutno izabrani heroji u prijateljskom i protivničkom timu, banovani heroji i korisnikove preference.

# Output

Heroji preporučeni za izabrati eventualno sortirani po score-u za preporuku.

# Baza znanja

Produkciona pravila će biti ručno unesena na osnovu našeg iskustva kao i online literature. Pravila će imati ulogu da uzimaju u obzir gore nabrojane stavke pri pravljenju preporuke.

# Primeri rezonovanja

Missing hero with a carry role in team => Recommend hero with carry or hard carry role

User prefers Slark AND currently recommended heroes are Slark and Lifestealer => Recommend only Slark

Currently recommended heroes are Invoker and Timbersaw AND Broodmother heavly counters Invoker => Recommend only Timbersaw

# Pokretanje projekta

Da bi sve funkcionalnosti projekta radile potrebno je podesiti application.properties prema trenutnom računaru.

Config.mvn.path varijablu je potrebno postaviti na home direktorijum instaliranog maven alata na računaru, npr. "F:/Program Files/apache-maven-3.6.2/"

Takođe, u bin direktorijumu instaliranog maven alata na računaru je potrebno da se nalazi mvn.bat i mvnDebug.bat (u slučaju da se nalaze mvn.cmd i mvnDebug.cmd, potrebno ih je kopirati i preimenovati kao mvn.bat i mvnDebug.bat)

# Spisak pravila za preporuke heroja

Sva pravila su podeljena u 5 agendi, sa tim da su neka pravila u implicitnoj MAIN agendi.

Za svakog heroja u Dota 2 igrici se unosi po jedan HeroRecommendationFact objekat u radnu memoriju. Ovaj objekat u sebi sadrzi informacije o heroju i njegov score, odnosno rangiranost. Početna vrednost score-a za heroja je njegov overall winrate.

Pravila iz prve 3 agende ("hero-statistics", "team-composition" i "preferences") uticu na kranji rezultat preporuke tako što menjaju score-ove HeroRecommendationFact objekata, ili tako što ih uklanjaju iz radne memorije. 

Pravila iz poslednje 2 agende ("scale" i "sort") potom skaliraju score-ove činjenicama HeroRecommandationFact koje su preostale u memoriji, i sortiraju ih po rangiranosti.

Za neka pravila je podržana parametrizacija pomoću drools templating mehanizma. Pravila koja su obuhvaćena template mehanizmom imaju to navedeno u svom opisu. Sve parametre template-a korisnik može menjati pomoću korisničkog interfejsa. Ovoj funkcionalnosti se može pristupiti klikom na settings ikonicu u donjem desnom uglu glavnog prikaza aplikacije.

U istom prozoru gde je omogućena izmena parametara template-a je omogućeno i korisničko dodavanje pravila. 

## Agenda “hero-statistics”

Ova agenda se prva po redu izvršava i u njoj se postavljaju početne verdnost score-a za svakog heroja, koje se kasnije skaliraju. Cilj ovih pravila je da iz preporuke izbace heroje koji se ne mogu izabrati, dok vrednosti score-a prilagođava overall winrate-u heroja spojenim sa disadvantage-om trenutno izabranih protivničkih heroja.

### Pravilo "Ally hero picked"

Ovo pravilo na osnovu AllyHeroPickedFact činjenice izbaci heroja iz liste za preporuku, pošto je već uzet i ne može se preporučiti

### Pravilo "Enemy hero picked"

Ovo pravilo na osnovu EnemyHeroPickedFact činjenice izbaci heroja iz liste za preporuku, pošto je već uzet i ne može se preporučiti.

Takođe menja score za preporuku svim herojima koji jos uvek nisu odabrani. Score se skalira na osnovu toga koliko se dobro određen heroj pokazuje protiv protivničkog.

Težina koja određuje u kolikoj meri će se promeniti score heroja je određena parametrom enemyHeroDisadvantage u preferences.drt template-u.

### Pravilo "Enemy hero picked is expected to show in my lane"

Ovo pravilo se takođe okida na osnovu EnemyHeroPickedFact činjenice, ali samo ukoliko joj je atribut goingToFaceInLane postavljen na true. Ukoliko je atribut postavljen na true, i ovo i "Enemy hero picked" pravilo se okidaju za istog protivničkog heroja.

Posledica pravila je dodatno menjanje score-a za preporuku preostalim, neizabranim herojima naspram izabranog protivničkog.

Dodatna izmena score-a se radi zato što najveći uticaj na izbor našeg heroja trebaju imati heroji sa kojima ćemo se suočiti u lane-u.

Težina koja određuje u kolikoj meri će se dodatno menjati score heroja je određena parametrom enemyLaneHeroDisadvantage u preferences.drt template-u.

### Pravilo "Hero banned"

Ovo pravilo se okida na osnovu HeroBannedFact činjenice. Slično kao i "Ally hero picked" pravilo, ono izbacuje heroja iz liste za preporuku, pošto je on već odabran i ne može se preporučiti.

## Agenda "team-composition"

Druga po redu agenda koja se izvršava i čiji cilj jeste da prilagodi preporuke heroja trenutnim ulogama (roles) izabranih heroja u prijateljskom timu. Postoji pravilo za svaku od mogućih uloga koje služi da kazni heroje koji pripadaju toj ulozi ako je ona već zadovoljena u timu ili da ih istakne u slučaju da uloga nije zadovoljena u timu. Sama pravila ne menjaju score-ove heroja direktno već generišu UpdateScoresFact činjenicu, koja opisuje grupu heroja čiji score je potrebno izmeniti. Ovo znači da pravila ove agende ne menjaju score-ove direktno, već se sama promena score-ova dogodi kasnije, nakon što se izvrši forward chaining sa UpdateScoresFact činjenicom.

U ovoj agendi se nalazi 9 pravila i sva su generisana pomoću drools template mehanizma. Template za ova pravila se nalazi u roles.drt fajlu.
Svako pravilo je generisano na osnovu tri parametra:
* role - naziv uloge za koju pravilo menja score-ove
* heroesPerRole - preporučen broj heroja u timu za neku ulogu
* scoreLossPercentage - u kolikoj meri se smanjuje preporuka za neku ulogu za svakog heroja koji tu ulogu već ispunjava

Sva pravila ove agende se okidaju tačno jednom (ukoliko postoji bar jedan izabran prijateljski heroj).

Ukoliko ne postoji nijedan izabran prijateljski heroj okidanje ovih pravila nema smisla.

Parametre heroesPerRole i scoreLossPercentage se mogu menjatu u korisničkom interfejsu za svako pravilo ponaosob.

### Query roleOverflow

Da bi pravila iz agende "team-composition" pronašla koliko heroja neke uloge se nalazi u timu, koristi se roleOverflow query.

Ovaj query se nalazi zajedno sa pomenutim pravilima u roles.drt fajlu.

Ulazni parametar je $role, što je string uloge za koju se radi upit.

Izlazni parametar je $count, što je broj heroja u prijateljskom timu koji ispunjavaju ulogu $role.

## Agenda "preferences"

Treca agenda koja se izvršava i koja uvodi faktor korisnikovih preferenci. Trenutni score-ovi se skaliraju u zavisnosti od korisnikovih preferiranih heroja, uloga (role) i pozicija za igru (lane). Kao i u prethodnoj agendi, pravila ne menjaju score-ove direktno, već generišu UpdateScoresFact činjenicu, koja opisuje grupu heroja čiji score je potrebno izmeniti. Ovo znači da pravila ove agende ne menjaju score-ove direktno, već se sama promena score-ova  dogodi kasnije, nakon što se izvrši forward chaining sa UpdateScoresFact činjenicom.

Pravila ove agende se nalaze u preferences.drt fajlu. Težine ovih pravila se mogu menjati kroz korisnički interfejs.

### Pravilo "Hero preferred"

Ovo pravilo na osnovu HeroPreferredFact činjenice menja score za heroja kojeg korisnik perferira. Samu izmenu score-a ne radi direktno već pomoću UpdateScoresFact činjenice.

Težina koja određuje u kolikoj meri će se promeniti score heroja je određena template parametrom preferredHeroFactor.

### Pravilo "Role preferred"

Ovo pravilo na osnovu RolePreferredFact činjenice menja score za heroje uloge koju korisnik perferira. Samu izmenu score-a ne radi direktno već pomoću UpdateScoresFact činjenice.

Težina koja određuje u kolikoj meri će se promeniti score heroja je određena template parametrom preferredRoleFactor.

### Pravilo "Lane preferred"

Ovo pravilo na osnovu LanePreferredFact činjenice menja score za heroje koji se se mogu igrati na odabranom lane-u. Samu izmenu score-a ne radi direktno već pomoću UpdateScoresFact činjenice.

Težina koja određuje u kolikoj meri će se promeniti score heroja je određena template parametrom preferredLaneFactor.

## Agenda "scale"

Četvrta agenda po redu je "scale" agenda. U njoj se vrsi skaliranje trenutnih score-ova u memoriji na brojeve koji su smisleniji za rangiranje.

Promena scoro-ova se ne vrši direktno nad HeroRecommendationFact objekatima, već se prave prave ResultFact objekti sa skaliranim score-om, koji su pogodniji za prenos preko mreže do frontend-a.

ResultFact objekti sadrže striktno manje informacija od HeroRecommendationFact objekata. U njivove atribute spadaju: score (skaliran), heroName i heroId.

### Pravilo "Find min and max score"

Ovo pravilo pronalazi maksimalni i minimalni score za sve heroje u radnoj memoriji. Ovo radi pomoću predefinisanih accumulate funkcija.

Rezultat pravila su MinScoreFact i MaxScoreFact činjenice. Pravilo se okida tačno jednom.

### Pravilo "Scaling rule"

Ovo pravilo od svake HeroRecommendationFact činjenice napravi tačno jednu ResultFact činjenicu sa skaliranim rezultatom. 

Skaliranje se radi pomoću MinScoreFact i MaxScoreFact.




## Implicitna MAIN agenda

Ovde se nalaze pravila koja reaguju na UpdateScoresFact činjenicu i vrše potrebno skaliranje i linearno povećanje/smanjenje score-ova odgovarajućih heroja. Heroji čiji score je potrebno izmeniti se mogu birati pomoću identifikatora heroja (heroId), njihove uloge (role) ili pozicije u kojoj se najčešće igraju (lane).

Pravila ove agende se nalze u basicRules.txt fajlu.

### Pravilo "Update by heroId"

Ovo pravilo na osnovu UpdateScoresFact činjenice menja score za jednog heroja na osnovu heroId atributa UpdateScoresFact činjenice.

### Pravilo "Update score by role"

Ovo pravilo na osnovu UpdateScoresFact činjenice menja score za više heroja na osnovu role atributa UpdateScoresFact činjenice.

### Pravilo "Update score by lane"

Ovo pravilo na osnovu UpdateScoresFact činjenice menja score za više heroja na osnovu lane atribute UpdateScoresFact činjenice.

# Spisak pravila za racunanje cene item-a

Sva pravila se nalaze u implicitnoj MAIN agendi.

Računanje cene Item-a se radi tako što se uzimaju u obzir item-i koje korisnik već poseduje. Ukoliko neki item kojeg korisnik poseduje može da se iskoristi za izgradnju ciljnog item-a, cena ciljnog item-a se umanjuje za cenu posedovanog.

Prepoznavanje da li se jedan item može sagraditi na osnovu drugog se radi pomoću backward chaining mehanizma.

Ulazne činjenice za ovaj sistem su posedovani item-i i željeni item.

Pravila se nalaze u Items.drl fajlu.

### Pravilo "Subtract cost of wanted item from current balance, and set wanted item global string"

Ovo je prvo pravilo koje se izvrši, i ono se izvršava tačno jednom.

Ono ima za cilj da postavi dve globalne varijable, balance i wantedItem.

Balance je količina novca potrebna da se ciljni item kupi, koja je u početku jednaka samoj ceni ciljnog item-a.

WantedItem je string naziv ciljnog item-a.

### Query "isContainedIn"

Ovaj query omogućava backward chaining. On proverava da li se neki item nalazi u ciljnom item-u ili nekoj njegovoj podkomponenti. Ovo radi rekurzivnim pozivanjem.

Pored određivanja da li je jedan item sadržan u drugom, ovaj query takođe vraća objekat koji predstavlja vezu između njih.

### Pravilo "Remove cost of bought item from wanted item, if it is contained by wanted item"

Ovo pravilo se aktivira po jednom za svaki kupljeni item koji se može koristiti za izgradnju ciljnog.

Da bi odredilo da li se jedan item gradi pomoću drugog koristi prethodno pomenuti "isContainedIn" query.

Kada se pravilo aktivira, cena ciljnog item-a se smanji za cenu posedovanog. Takođe se izbaci taj kupljeni item iz stabla, da se ne bi mogla više puta oduzimala njegova cena od cene ciljnog.

# Spisak pravila za detekciju DDOS napada

Sva pravila se nalaze u implicitnoj MAIN agendi.

Ovaj skup pravila ima za cilj da detektuje DDOS napad. Ovo se postiže pomoću CEP mehanizma.

Posmatraju se IP adrese dolazećih zahteva. Ukoliko se u izvsenom vremenskom periodu primi previše zahteva sa iste IP adrese, prijavljuje se DDOS napad.

Dužina vremenskog perioda i broj zahteva u vremenskom periodu potrebni za detektovanje napada se mogu menjati u application.properties fajlu pomoću ddos.timeFrame i ddos.maxRequests varijabli.

Ova pravila se nalaze u DDOS.drl fajlu.

### Window "LatestIpAccessFact"

Ovaj window služi za pronalazak poslednje IpAccessFact činjenice.

### Window "TimeWindow"

Ovaj window služi za pronalazak svih IpAccessFact činjenica koje su se pojavile u vremenskom periodu definisanom u application.properties fajlu.

### Pravilo "Too many requests by a single ip address"

Ovo pravilo se aktivira kada najnovija činjenica, zajedno sa prethodnim prekoračuje dozvoljen broj zahteva sa iste IP adrese u definisanom vremenskom periodu. Da bi pronašlo najnoviju činjenicu koristi "LatestIpAccessFact" window, dok za pronalazak svih činjenica u vremenskom periodu koristi "TimeWindow".

Ukoliko se uoči DDOS napad, ispiše se odgovarajuća poruka u konzoli servera.
