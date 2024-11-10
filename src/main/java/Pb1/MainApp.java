package Pb1;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainApp
{
    public static void main(String[] args)
    {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            List<Angajat> angajati = List.of(mapper.readValue(new File("src/main/resources/angajati.json"), Angajat[].class));
            Scanner scanner = new Scanner(System.in);
            int optiune;
            int currentYear = LocalDate.now().getYear();
            do {
                System.out.println("\n------------------Meniu------------------");
                System.out.println("1. Afisarea listei de angajati");
                System.out.println("2. Afisarea angajatilor cu salariul peste 2500 RON");
                System.out.println("3. Afisarea angajatilor din aprilie anul trecut cu functii de conducere");
                System.out.println("4. Afisarea angajatilor fara functii de conducere, ordonati descrescator dupa salariu");
                System.out.println("5. Afisarea numelor angajatilor cu majuscule");
                System.out.println("6. Afisarea salariilor sub 3000 RON");
                System.out.println("7. Afisarea datelor primului angajat al firmei");
                System.out.println("8. Statistici despre salarii");
                System.out.println("9. Verificare existenta angajat 'Ion'");
                System.out.println("10. Numarul de angajati angajati în vara anului precedent");
                System.out.println("0. Ieșire");
                System.out.print("Selectati o optiune: ");
                optiune = scanner.nextInt();
                scanner.nextLine();
                switch (optiune) {
                    case 1:
                        angajati.forEach(System.out::println);
                        break;
                    case 2:
                        angajati.stream().filter(a -> a.getSalariu() > 2500).forEach(System.out::println);
                        break;
                    case 3:
                        List<Angajat> angajatiConducereAprilie = angajati.stream()
                                .filter(a -> a.getDataAngajarii().getYear() == currentYear - 1)
                                .filter(a -> a.getDataAngajarii().getMonth() == Month.APRIL)
                                .filter(a -> a.getPost().contains("sef") || a.getPost().contains("director"))
                                .collect(Collectors.toList());
                        angajatiConducereAprilie.forEach(System.out::println);
                        break;
                    case 4:
                        angajati.stream()
                                .filter(a -> !a.getPost().contains("director") && !a.getPost().contains("sef"))
                                .sorted(Comparator.comparing(Angajat::getSalariu).reversed())
                                .forEach(System.out::println);
                        break;
                    case 5:
                        angajati.stream()
                                .map(a -> a.getNume().toUpperCase())
                                .forEach(System.out::println);
                        break;
                    case 6:
                        angajati.stream()
                                .map(Angajat::getSalariu)
                                .filter(sal -> sal < 3000)
                                .forEach(System.out::println);
                        break;
                    case 7:
                        Optional<Angajat> primulAngajat = angajati.stream()
                                .min(Comparator.comparing(Angajat::getDataAngajarii));
                        primulAngajat.ifPresentOrElse(System.out::println,
                                () -> System.out.println("Nu exista angajati."));
                        break;
                    case 8:
                        var statistici = angajati.stream()
                                .collect(Collectors.summarizingDouble(Angajat::getSalariu));
                        System.out.println("Salariul mediu: " + statistici.getAverage());
                        System.out.println("Salariul minim: " + statistici.getMin());
                        System.out.println("Salariul maxim: " + statistici.getMax());
                        break;
                    case 9:
                        angajati.stream()
                                .map(Angajat::getNume)
                                .filter(nume -> nume.contains("Ion"))
                                .findAny()
                                .ifPresentOrElse(
                                        name -> System.out.println("Firma are cel putin un Ion angajat"),
                                        () -> System.out.println("Firma nu are nici un Ion angajat"));
                        break;
                    case 10:
                        long numarAngajatiVara = angajati.stream()
                                .filter(a -> a.getDataAngajarii().getYear() == currentYear - 1)
                                .filter(a -> a.getDataAngajarii().getMonthValue() >= 6 && a.getDataAngajarii().getMonthValue() <= 8)
                                .count();
                        System.out.println("Numar de angajati angajati in vara anului precedent: " + numarAngajatiVara);
                        break;
                    case 0:
                        System.out.println("Iesire din program.");
                        break;
                    default:
                        System.out.println("Optiune invalida!");
                }
            } while (optiune != 0);
            scanner.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
