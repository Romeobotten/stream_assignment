package se.lexicon.vxo.service;

import org.junit.jupiter.api.Test;
import se.lexicon.vxo.model.Gender;
import se.lexicon.vxo.model.Person;
import se.lexicon.vxo.model.PersonDto;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Your task is not make all tests pass (except task1 because its non testable).
 * You have to solve each task by using a java.util.Stream or any of it's variance.
 * You also need to use lambda expressions as implementation to functional interfaces.
 * (No Anonymous Inner Classes or Class implementation of functional interfaces)
 *
 */
public class StreamAssignment {

    private static List<Person> people = People.INSTANCE.getPeople();

    /**
     * Turn integers into a stream then use forEach as a terminal operation to print out the numbers
     */
    @Test
    public void task1(){
        List<Integer> integers = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

        integers.stream().forEach(System.out::println);   // -OK

    }

    /**
     * Turning people into a Stream count all members   -OK
     */
    @Test
    public void task2(){
        long amount = 0;

        amount = people.stream().count();

    assertEquals(10000, amount);
    }

    /**
     * Count all people that has Andersson as lastName.   -OK
     */
    @Test
    public void task3(){
        long amount = 0;
        int expected = 90;

        amount = people.stream()
                .map(Person::getLastName)
                .filter(p -> p.equals("Andersson"))
                .count();

        assertEquals(expected, amount);
    }

    /**
     * Extract a list of all female   -OK
     */
    @Test
    public void task4(){
        int expectedSize = 4988;
        List<Person> females = null;

        females = people.stream()
                .filter(f -> f.getGender().equals(Gender.FEMALE))
                .collect(Collectors.toCollection(ArrayList::new));


        assertNotNull(females);
        assertEquals(expectedSize, females.size());
    }

    /**
     * Extract a TreeSet with all birthDates     -OK
     */
    @Test
    public void task5(){
        int expectedSize = 8882;
        Set<LocalDate> dates = null;

        dates = people.stream().map(Person::getDateOfBirth)
                .collect(Collectors.toCollection(TreeSet::new));

        assertNotNull(dates);
        assertTrue(dates instanceof TreeSet);
        assertEquals(expectedSize, dates.size());
    }

    /**
     * Extract an array of all people named "Erik"   -OK
     */
    @Test
    public void task6(){
        int expectedLength = 3;

        Person[] result = null;

        result = people.stream() //.map(Person::getFirstName)
                .filter(p -> p.getFirstName().equals("Erik"))
                .toArray(Person[]::new);

        //Write code here

        assertNotNull(result);
        assertEquals(expectedLength, result.length);
    }

    /**
     * Find a person that has id of 5436   -OK
     */
    @Test
    public void task7(){
        Person expected = new Person(5436, "Tea", "Håkansson", LocalDate.parse("1968-01-25"), Gender.FEMALE);

        Optional<Person> optional = null;

        optional = people.stream()
                .filter(p -> p.getPersonId() == 5436)
                .findFirst();

        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    /**
     * Using min() define a comparator that extracts the oldest person i the list as an Optional
     */
    @Test
    public void task8(){  // -OK
        LocalDate expectedBirthDate = LocalDate.parse("1910-01-02");

        Optional<Person> optional = null;

        optional = people.stream()
                .min((p1,p2) -> p1.getDateOfBirth().compareTo(p2.getDateOfBirth()));

        assertNotNull(optional);
        assertEquals(expectedBirthDate, optional.get().getDateOfBirth());
    }

    /**
     * Map each person born before 1920-01-01 into a PersonDto object then extract to a List
     */
    @Test
    public void task9(){ //  -OK
        int expectedSize = 892;
        LocalDate date = LocalDate.parse("1920-01-01");

        List<PersonDto> dtoList = null;

        dtoList = people.stream()
                .filter(p -> p.getDateOfBirth().isBefore(date))
        .map(p -> {
            PersonDto d = new PersonDto(p.getPersonId(),p.getFirstName() +" " +  p.getLastName());
                    return d;
                })
        .collect(Collectors.toList());

        assertNotNull(dtoList);
        assertEquals(expectedSize, dtoList.size());
    }

    /**
     * In a Stream Filter out one person with id 5914 from people and take the birthdate and build a string from data that the date contains then
     * return the string.
     */
    @Test
    public void task10(){ // -OK
        String expected = "WEDNESDAY 19 DECEMBER 2012";
        int personId = 5914;

        Optional<String> optional = null;

        optional = people.stream()
                .filter(p -> p.getPersonId() == (personId))
                .map(p -> p.getDateOfBirth().getDayOfWeek().toString() + " " + p.getDateOfBirth().getDayOfMonth() +
                        " " + p.getDateOfBirth().getMonth().toString() + " " + p.getDateOfBirth().getYear())
                .peek(System.out::println)
                .findFirst();

        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());
    }

    /**
     * Get average age of all People by turning people into a stream and use defined ToIntFunction personToAge
     * changing type of stream to an IntStream.
     */
    @Test
    public void task11(){   //  -OK
        ToIntFunction<Person> personToAge =
                person -> Period.between(person.getDateOfBirth(), LocalDate.parse("2019-12-20")).getYears();
        double expected = 54.42;
        double averageAge = 0;

        averageAge = people.stream()
                .mapToInt(personToAge)
                .average().getAsDouble();

        assertTrue(averageAge > 0);
        assertEquals(expected, averageAge, .01);
    }

    /**
     * Extract from people a sorted string array of all firstNames that are palindromes. No duplicates
     */
    @Test
    public void task12(){   //  -OK
        String[] expected = {"Ada", "Ana", "Anna", "Ava", "Aya", "Bob", "Ebbe", "Efe", "Eje", "Elle", "Hannah", "Maram", "Natan", "Otto"};

        String[] result = null;

        result = people.stream()

                .filter(p -> p.getFirstName().equalsIgnoreCase(new StringBuilder(p.getFirstName()).reverse().toString())) // Filter persons that have palindrome names
                .map(Person::getFirstName)
                .sorted()
                .distinct()  // Distinct should be before sorted?
                .toArray(String[]::new);

        assertNotNull(result);
        assertArrayEquals(expected, result);
    }

    /**
     * Extract from people a map where each key is a last name with a value containing a list of all that has that lastName
     */
    @Test
    public void task13(){ //  -OK
        int expectedSize = 107;
        Map<String, List<Person>> personMap = null;
        List<Person> personList = null;
        String lastName = null;
        //Write code here


        personMap = people.stream()
                .collect(Collectors.groupingBy(Person::getLastName)); // Not very funny, is it?

//        people.stream()
//                .map(p -> {
//                    if(p.getLastName().equals(lastName)) {
//                        personList.add(p);
//                    } else {
//                        lastName = p.getLastName();
//                        personList = null;
//                        personList.add(p);
//                    }
//                    return personMap.put(p.getLastName(),personList);
//                });

        assertNotNull(personMap);
        assertEquals(expectedSize, personMap.size());
    }

    /**
     * Create a calendar using Stream.iterate of year 2020. Extract to a LocalDate array
     */
    @Test
    public void task14(){   //   -OK

        //Write code here
        LocalDate[] _2020_dates;

            _2020_dates = Stream.iterate(1, i -> i + 1)
                .limit(366)
                .map(y -> LocalDate.ofYearDay(2020,y.intValue()))
                .peek(System.out::println)
                .toArray(LocalDate[]::new);

        assertNotNull(_2020_dates);
        assertEquals(366, _2020_dates.length);
        assertEquals(LocalDate.parse("2020-01-01"), _2020_dates[0]);
        assertEquals(LocalDate.parse("2020-12-31"), _2020_dates[_2020_dates.length-1]);
    }
}
