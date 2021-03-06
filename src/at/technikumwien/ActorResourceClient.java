package at.technikumwien;

import at.technikumwien.generated.Actor;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Julia on 10.12.2016.
 */

public class ActorResourceClient {

    private static Response createActor(WebTarget target, Actor actor) {
        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(actor));
        return response;
    }

    private static Actor readActor(WebTarget target, Long actorId) {
        Actor actor = target
                .path("/{actorId}")
                .resolveTemplate("actorId", actorId)
                .request(MediaType.APPLICATION_JSON)
                .get(Actor.class);

        return actor;

    }

    private static void updateActor(WebTarget target, Actor actor, Long actorId) {
        Response updResponse = target
                .path("/{actorId}")
                .resolveTemplate("actorId", actorId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actor));
        System.out.println(updResponse);
        updResponse.close();
    }

    private static void deleteActor(WebTarget target, Long actorId) {
        Actor actorDeleted = target
                .path("/{actorId}")
                .resolveTemplate("actorId", actorId)
                .request(MediaType.APPLICATION_JSON)
                .delete(Actor.class);
    }

    public static void main(String[] args) throws Exception {
        WebTarget target = ClientBuilder
                .newClient()
                .register(new RequestFilter("writer", "123"))
                .target("http://localhost:8080/MovieServiceWebApp_war_exploded/resources/actor");

        Boolean beenden = false;
        Scanner user_input = new Scanner( System.in );
        System.out.println("Schauspieler-Verwaltung");

        while(!beenden) {
            String userEingabe;
            System.out.println("Was willst du tun?");
            System.out.println("Es gibt folgende Optionen: ");
            System.out.println("Schauspieler ansehen [1]");
            System.out.println("Schauspieler hinzufügen [2]");
            System.out.println("Schauspieler updaten [3]");
            System.out.println("Schauspieler löschen [4]");
            System.out.println("Schauspieler-Verwaltung beenden [5]");

            userEingabe = user_input.nextLine( );

            switch (userEingabe) {
                case "1":
                    System.out.println("Schauspieler-Id: ");
                    String actorIdToRead = user_input.nextLine();
                    Actor actor = readActor(target, new Long(actorIdToRead));
                    System.out.println(actor.getFirstname() + " " + actor.getLastname() + " " + actor.getSex() + " " + actor.getBirthdate());
                    break;
                case "2":
                    Actor newActor = new Actor();
                    System.out.println("Schauspieler Vorname: ");
                    String newActorFirstName = user_input.nextLine();
                    newActor.setFirstname(newActorFirstName);
                    System.out.println("Schauspieler Nachname: ");
                    String newActorLastName = user_input.nextLine();
                    newActor.setLastname(newActorLastName);
                    System.out.println("Schauspieler Geschlecht: ");
                    String newActorSex = user_input.nextLine();
                    newActor.setSex(newActorSex);

                    System.out.println("Schauspieler Geburtsjahr - bitte gib eine vierstellige Zahl ein: ");
                    String newActorYear = user_input.nextLine();
                    System.out.println("Schauspieler Geburtsmonat - bitte gib eine Zahl von 01 bis 12 ein: ");
                    String newActorMonth = user_input.nextLine();
                    System.out.println("Schauspieler Geburtstag - bitte gib eine Zahl von 01 bis 31 ein: ");
                    String newActorDay = user_input.nextLine();

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String bd = newActorYear + "-" + newActorMonth + "-" + newActorDay;
                    try {
                        Date date = formatter.parse(bd);
                        //System.out.println(date);
                        //System.out.println(formatter.format(date));
                        newActor.setBirthdate(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Response createResponse = createActor(target, newActor);
                    System.out.println(createResponse.getLocation());
                    createResponse.close();
                    break;
                case "3":
                    Actor updActor = new Actor();
                    System.out.println("Schauspieler ID: ");
                    String updActorID = user_input.nextLine();
                    System.out.println("Schauspieler Vorname: ");
                    String updActorFirstName = user_input.nextLine();
                    updActor.setFirstname(updActorFirstName);
                    System.out.println("Schauspieler Nachname: ");
                    String updActorLastName = user_input.nextLine();
                    updActor.setLastname(updActorLastName);
                    System.out.println("Schauspieler Geschlecht: ");
                    String updActorSex = user_input.nextLine();
                    updActor.setSex(updActorSex);
                    System.out.println("Schauspieler Geburtsjahr - bitte gib eine vierstellige Zahl ein: ");
                    String updActorYear = user_input.nextLine();
                    System.out.println("Schauspieler Geburtsmonat - bitte gib eine Zahl von 01 bis 12 ein: ");
                    String updActorMonth = user_input.nextLine();
                    System.out.println("Schauspieler Geburtstag - bitte gib eine Zahl von 01 bis 31 ein: ");
                    String updActorDay = user_input.nextLine();

                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                    String bd2 = updActorYear + "-" + updActorMonth + "-" + updActorDay;
                    try {
                        Date date = formatter2.parse(bd2);
                        //System.out.println(date);
                        //System.out.println(formatter2.format(date));
                        updActor.setBirthdate(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    updateActor(target, updActor, new Long(updActorID));
                    break;
                case "4":
                    System.out.println("Schauspieler ID: ");
                    String delActorID = user_input.nextLine();
                    deleteActor(target, new Long(delActorID));
                    break;
                case "5":
                    beenden = true;
                    break;
                default:
                    System.out.println("Bitte gib eine Zahl von 1 - 5 ein");
                    break;
            }

        }

        System.out.println("Bis bald!");


    }

}
