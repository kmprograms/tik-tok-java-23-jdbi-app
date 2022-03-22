import org.jdbi.v3.core.Jdbi;

public class App {

    public static void main(String[] args) {
        var jdbi = Jdbi.create(
                "jdbc:mysql://localhost:3306/db_1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                "root",
                "root"
        );

        // Tworzenie tabeli
        var peopleTable = """
            create table people (
                id integer primary key auto_increment,
                name varchar(255) not null,
                age integer default 18
            );
        """;

        jdbi.withHandle(handle -> handle.execute(peopleTable));

        // Wstawianie nowego wiersza
        jdbi.withHandle(handle -> handle
                .createUpdate("insert into people (name, age) values (:name, :age)")
                .bind("name", "IZA")
                .bind("age", 30)
                .execute());

        // Pobieranie danych
        var people = jdbi.withHandle(handle -> handle
                .createQuery("select * from people")
                .mapToBean(Person.class)
                .list());

        people.forEach(System.out::println);
    }
}
