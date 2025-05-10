# A mostani verzióban a „MI” egy Deep Q-Network (DQN) alapú megoldás, vagyis:

Tapasztalatgyűjtés (Replay Buffer)

Minden játék kör után eltároljuk, hogy mi volt az előző állapot – milyen lépéseket tett a játékos és az AI –, milyen lépést választott az AI, és mi lett az eredmény (győzelem/vereség/döntetlen).

Állapotok kódolása (State History)

Az utolsó N kör lépéseit egy egydimenziós vektorrá alakítjuk (one-hot kódolással), ami bemenetként szolgál a hálónak.

Q-háló (Multi-Layer Perceptron)

Egy egyszerű, három rétegű neurális háló becsli meg, hogy az adott állapotban (az előző körök-összefoglaló vektor) mennyit érnek a lehetséges akciók (a 5 lépés).

A háló kimenete egy 5 elemű Q-érték vektor, ahol minden pozíció egy-egy lépés „értékét” mutatja.

Epsilon-greedy akcióválasztás

Eleinte véletlenszerűen is választ az AI (felfedezés), később egyre gyakrabban a háló által becsült Q-értékek alapján dönt (kihasználás), így fokozatosan javul a teljesítménye.

Célérték frissítés (DQN update)

A tárolt tapasztalatokból minibatch-eket veszünk, és a háló aktuális kimenetét “célértékekkel” (target) korrigáljuk, hogy közelebb kerüljön a hosszú távú nyerési esélyek optimális becsléséhez.

Nem fix, előre megírt szabályok szerint választ, hanem tapasztalati úton tanul egy neurális háló segítségével.

Megpróbálja maximalizálni a hosszú távú nyerési esélyét, ahogy egy tényleges megerősítéses tanuló ügynök (RL-agent) tenné.
