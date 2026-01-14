import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {

    static List<Persoon> personen = new ArrayList<>();
    static List<Trein> treinen = new ArrayList<>();
    static List<Reis> reizen = new ArrayList<>();
    static List<Ticket> tickets = new ArrayList<>();

    public static void main(String[] args) {
        treinen.add(new Trein("Class373", 12));
        treinen.add(new Trein("Class374", 14));

        Scanner scanner = new Scanner(System.in);
        boolean actief = true;

        while (actief) {
            System.out.println("\n    EUROMOON MENU    ");
            System.out.println("1. Passagier registreren");
            System.out.println("2. Reis aanmaken");
            System.out.println("3. Trein koppelen aan reis");
            System.out.println("4. Ticket verkopen");
            System.out.println("5. Boardinglijst afdrukken");
            System.out.println("6. Stoppen");
            System.out.print("Maak een keuze: ");

            int keuze = -1;
            try { keuze = scanner.nextInt(); }
            catch (InputMismatchException e) { System.out.println("Ongeldige keuze!"); }
            scanner.nextLine();

            switch (keuze) {
                case 1 -> registreerPassagier(scanner);
                case 2 -> maakReis(scanner);
                case 3 -> koppelTrein(scanner);
                case 4 -> verkoopTicket(scanner);
                case 5 -> printBoardinglijst(scanner);
                case 6 -> actief = false;
                default -> System.out.println("Terug naar menu.");
            }
        }

        System.out.println("Applicatie gestopt.");
    }


    private static void registreerPassagier(Scanner s) {
        try {
            System.out.print("Voornaam: "); String voornaam = s.nextLine();
            System.out.print("Achternaam: "); String achternaam = s.nextLine();
            System.out.print("Rijksregisternummer: "); String rijksnummer = s.nextLine();
            System.out.print("Geboortedatum (YYYY-MM-DD): ");
            LocalDate geboortedatum = LocalDate.parse(s.nextLine());

            Passagier p = new Passagier(voornaam, achternaam, rijksnummer, geboortedatum);
            personen.add(p);
            System.out.println("Passagier geregistreerd: " + voornaam + " " + achternaam);
        } catch (Exception e) { System.out.println("Ongeldige invoer!"); }
    }

    private static void maakReis(Scanner s) {
        try {
            System.out.print("Vertrekstation: "); String vertrek = s.nextLine();
            System.out.print("Bestemming: "); String bestemming = s.nextLine();
            System.out.print("Datum en tijd vertrek (YYYY-MM-DDTHH:MM): ");
            LocalDateTime vertrekTijd = LocalDateTime.parse(s.nextLine());

            reizen.add(new Reis(vertrek, bestemming, vertrekTijd));
            System.out.println("Reis aangemaakt: " + vertrek + " -> " + bestemming + " op " + vertrekTijd);
        } catch (Exception e) { System.out.println("Ongeldige invoer!"); }
    }

    private static void koppelTrein(Scanner s) {
        try {
            if (reizen.isEmpty() || treinen.isEmpty()) { System.out.println("Geen reizen of treinen beschikbaar."); return; }

            System.out.println("Kies reis (0-" + (reizen.size()-1) + "):");
            for (int i=0;i<reizen.size();i++)
                System.out.println(i + ". " + reizen.get(i).vertrek + " -> " + reizen.get(i).bestemming);

            int reisIndex = s.nextInt(); s.nextLine();

            System.out.println("Kies trein (0-" + (treinen.size()-1) + "):");
            for (int i=0;i<treinen.size();i++)
                System.out.println(i + ". " + treinen.get(i).naam);

            int treinIndex = s.nextInt(); s.nextLine();

            Reis reis = reizen.get(reisIndex);
            Trein trein = treinen.get(treinIndex);
            reis.setTrein(trein);

            System.out.println("Trein " + trein.naam + " gekoppeld aan reis " + reis.vertrek + " -> " + reis.bestemming);
        } catch (Exception e) { System.out.println("Ongeldige invoer!"); s.nextLine(); }
    }

    private static void verkoopTicket(Scanner s) {
        try {
            List<Passagier> passagiers = new ArrayList<>();
            for(Persoon p: personen) if(p instanceof Passagier) passagiers.add((Passagier)p);

            if (passagiers.isEmpty() || reizen.isEmpty()) { System.out.println("Geen passagiers of reizen."); return; }

            System.out.println("Kies passagier (0-" + (passagiers.size()-1) + "):");
            for(int i=0;i<passagiers.size();i++)
                System.out.println(i + ". " + passagiers.get(i).voornaam + " " + passagiers.get(i).achternaam);
            int pIndex = s.nextInt(); s.nextLine();

            System.out.println("Kies reis (0-" + (reizen.size()-1) + "):");
            for(int i=0;i<reizen.size();i++)
                System.out.println(i + ". " + reizen.get(i).vertrek + " -> " + reizen.get(i).bestemming +
                        " (" + reizen.get(i).vertrekTijd + ")");
            int rIndex = s.nextInt(); s.nextLine();

            Reis reis = reizen.get(rIndex);
            if (reis.trein == null) { System.out.println("Deze reis heeft nog geen trein."); return; }

            long verkochteTickets = tickets.stream().filter(ticket -> ticket.reis==reis).count();
            if (verkochteTickets >= reis.trein.capaciteit*80) {
                System.out.println("Geen plaatsen meer beschikbaar!"); return;
            }

            System.out.println("Kies klasse (1=1e klasse, 2=2e klasse): ");
            int klasse = s.nextInt(); s.nextLine();

            tickets.add(new Ticket(passagiers.get(pIndex), reis, klasse));
            System.out.println("Ticket verkocht aan " + passagiers.get(pIndex).voornaam);
        } catch (Exception e) { System.out.println("Ongeldige invoer!"); s.nextLine(); }
    }

    private static void printBoardinglijst(Scanner s) {
        try {
            if(reizen.isEmpty()) { System.out.println("Geen reizen."); return; }

            System.out.println("Kies reis (0-" + (reizen.size()-1) + "):");
            for(int i=0;i<reizen.size();i++)
                System.out.println(i + ". " + reizen.get(i).vertrek + " -> " + reizen.get(i).bestemming +
                        " (" + reizen.get(i).vertrekTijd + ")");
            int rIndex = s.nextInt(); s.nextLine();

            Reis reis = reizen.get(rIndex);
            String bestandNaam = reis.vertrek + "_" + reis.bestemming + "_" + reis.vertrekTijd + ".txt";

            try(FileWriter w = new FileWriter(bestandNaam)) {
                for(Ticket ticket : tickets) {
                    if(ticket.reis == reis) {
                        Passagier p = ticket.passagier;
                        w.write("Voornaam: " + p.voornaam + "\n");
                        w.write("Achternaam: " + p.achternaam + "\n");
                        w.write("Rijksregisternummer: " + p.rijksNummer + "\n");
                        w.write("Geboortedatum: " + p.geboortedatum + "\n");
                        w.write("Klasse: " + ticket.klasse + "\n");
                        w.write("--------------------------\n");
                    }
                }
                System.out.println("Boardinglijst afgedrukt: " + bestandNaam);
            } catch(IOException e) { System.out.println("Fout bij schrijven bestand."); }

        } catch(Exception e) { System.out.println("Ongeldige invoer!"); s.nextLine(); }
    }


    static abstract class Persoon {
        String voornaam, achternaam, rijksNummer;
        Persoon(String f,String a,String n){voornaam=f; achternaam=a; rijksNummer=n;}
    }

    static class Passagier extends Persoon {
        LocalDate geboortedatum;
        Passagier(String f,String a,String n, LocalDate gd){super(f,a,n); geboortedatum=gd;}
    }

    static abstract class Personeel extends Persoon {
        List<String> certificaten = new ArrayList<>();
        Personeel(String f,String a,String n){super(f,a,n);}
    }

    static class Bestuurder extends Personeel { Bestuurder(String f,String a,String n){super(f,a,n);} }
    static class Steward extends Personeel { Steward(String f,String a,String n){super(f,a,n);} }
    static class Conducteur extends Personeel { Conducteur(String f,String a,String n){super(f,a,n);} }

    static class Trein {
        String naam; int maxWagons; int capaciteit=80;
        Trein(String n,int w){naam=n; maxWagons=w;}
    }

    static class Reis {
        String vertrek,bestemming; LocalDateTime vertrekTijd; Trein trein;
        Reis(String v,String b,LocalDateTime dt){vertrek=v; bestemming=b; vertrekTijd=dt;}
        void setTrein(Trein t){trein=t;}
    }

    static class Ticket {
        Passagier passagier; Reis reis; int klasse;
        Ticket(Passagier p,Reis r,int k){passagier=p; reis=r; klasse=k;}
    }
}