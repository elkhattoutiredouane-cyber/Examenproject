import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class Personen {
}

abstract class Persoon {
    String voornaam;
    String achternaam;
    String rijksNummer;
    LocalDate geboortedatum;
}

class Passagier extends Persoon {}

abstract class Personeelslid extends Persoon {
    List<String> certificaties = new ArrayList<>();
}

class Conducteur extends Personeelslid {}
class Steward extends Personeelslid {}
