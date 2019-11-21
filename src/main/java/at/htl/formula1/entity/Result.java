package at.htl.formula1.entity;

import jdk.jfr.Name;

import javax.json.JsonObject;
import javax.persistence.*;

/**
 * Formula1 - Result
 * <p>
 * The id's are assigned by the database.
 */
@Entity
@Table(name = "F1_RESULT")
@NamedQueries({
            @NamedQuery(name = "Result.sumPointsForAllDrivers",query = "select sum(r.points) from Result r where r.driver = (select d.id  from Driver d where d.name = :NAME)"),
            @NamedQuery(name = "Result.getWinnerOfRace",query = "select r.driver from Result r where r.position = 1 and r.race = (select ra.id from Race ra where ra.country = :COUNTRY)"),
            @NamedQuery(name = "Result.getAllPoints",query = "select sum(r.points) from Result r where r.driver = :DRIVER"),
            @NamedQuery(name = "Race.racesWonByTeams",query = "select re.race from Result re where re.position = 1 and re.driver in (select distinct d.id from Driver d where d.team = (select t.id from Team t where t.name like :TEAM))")
})
public class Result {

    @Transient
    public int[] pointsPerPosition = {0, 25, 18, 15, 12, 10, 8, 6, 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    private Race race;
    private int position;
    private int points;
    @ManyToOne
    private Driver driver;


    //region Constructors
    public Result() {
    }

    public Result(Race race, int position, Driver driver) {
        this.setRace(race);
        this.setPosition(position);
        this.setDriver(driver);
    }


    //endregion


    //region Getter and Setter

    public Long getId() {
        return id;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        this.points = pointsPerPosition[position];
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    //endregion


    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", race=" + race.getCountry() +
                ", position=" + position +
                ", points=" + points +
                ", driver=" + driver.getName() +
                '}';
    }
}
