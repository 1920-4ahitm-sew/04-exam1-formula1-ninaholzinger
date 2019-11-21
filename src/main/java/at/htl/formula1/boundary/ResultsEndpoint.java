package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Result;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("results")
public class ResultsEndpoint {


    /**
     * @param name als QueryParam einzulesen
     * @return JsonObject
     */

    @PersistenceContext
    EntityManager em;

    @GET
    public JsonObject getPointsSumOfDriver(@QueryParam("name") String name) {

        long sumPoints = em.createNamedQuery("Result.sumPointsForAllDrivers", Long.class)
                .setParameter("NAME",name)
                .getSingleResult();
        JsonObject  jsonObject = Json.createObjectBuilder()
                .add("driver",name)
                .add("points",sumPoints)
                .build();
        return  jsonObject;
    }

    /**
     * @param id des Rennens
     * @return
     */
    @GET
    @Path("winner/{country}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findWinnerOfRace(@PathParam("country") String country) {

        Long driverId = em.createNamedQuery("Result.getWinnerOfRace",Driver.class)
                .setParameter("COUNTRY",country)
                .getSingleResult().getId();

        Driver driverName = em.find(Driver.class,driverId);
        return Response.ok(driverName).build();
    }

    @GET
    @Path("raceswon")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Race> racesWonByTeams(@QueryParam("team") String team){
        List<Race> races = em.createNamedQuery("Race.racesWonByTeams",Race.class)
                .setParameter("TEAM",team)
                .getResultList();

        return races;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public List<String[]> allRacesWonByTeam(){
        List<Driver> drivers = em.createNamedQuery("Driver.findAll",Driver.class)
                .getResultList();
        List<String[]> driverAndPoints = new LinkedList<>();

        for (Driver driver: drivers) {
            Long points = em.createNamedQuery("Result.getAllPoints",Long.class)
                    .setParameter("DRIVER",driver)
                    .getSingleResult();
            driverAndPoints.add(new String[]{driver.toString(), " " +points});
        }
        return  driverAndPoints;
    }



}
