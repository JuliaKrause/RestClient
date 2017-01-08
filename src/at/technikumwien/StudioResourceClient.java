package at.technikumwien;

import at.technikumwien.generated.Studio;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Scanner;

/**
 * Created by Julia on 10.12.2016.
 */

public class StudioResourceClient {

    private static Response createStudio(WebTarget target, Studio studio) {
        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(studio));
        return response;
    }

    private static Studio readStudio(WebTarget target, Long studioId) {
        Studio studio = target
                .path("/{studioId}")
                .resolveTemplate("studioId", studioId)
                .request(MediaType.APPLICATION_JSON)
                .get(Studio.class);
        return studio;

    }

    private static void updateStudio(WebTarget target, Studio studio, Long studioId) {
        Response updResponse = target
                .path("/{studioId}")
                .resolveTemplate("studioId", studioId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(studio));
        System.out.println(updResponse);
        updResponse.close();
    }

    private static void deleteStudio(WebTarget target, Long studioId) {
        Studio studioDeleted = target
                .path("/{studioId}")
                .resolveTemplate("studioId", studioId)
                .request(MediaType.APPLICATION_JSON)
                .delete(Studio.class);
    }



    public static void main(String[] args) throws Exception {

        WebTarget target = ClientBuilder
                .newClient()
                .register(new RequestFilter("writer", "123"))
                .target("http://localhost:8080/MovieServiceWebApp_war_exploded/resources/studio");

        Boolean beenden = false;
        Scanner user_input = new Scanner( System.in );
        System.out.println("Studio-Verwaltung");

        while(!beenden) {
            String userEingabe;
            System.out.println("Was willst du tun?");
            System.out.println("Es gibt folgende Optionen: ");
            System.out.println("Studio ansehen [1]");
            System.out.println("Studio hinzufügen [2]");
            System.out.println("Studio updaten [3]");
            System.out.println("Studio löschen [4]");
            System.out.println("Studio-Verwaltung beenden [5]");

            userEingabe = user_input.nextLine( );

            switch (userEingabe) {
                case "1":
                    System.out.println("Studio-Id: ");
                    String studioIdToRead = user_input.nextLine();
                    Studio studio = readStudio(target, new Long(studioIdToRead));
                    System.out.println(studio.getName() + " " + studio.getCountrycode() + " " + studio.getPostcode());
                    break;
                case "2":
                    Studio newStudio = new Studio();
                    System.out.println("Studio Name: ");
                    String newStudioName = user_input.nextLine();
                    newStudio.setName(newStudioName);
                    System.out.println("Studio CountryCode: ");
                    String newStudioCC = user_input.nextLine();
                    newStudio.setCountrycode(newStudioCC);
                    System.out.println("Studio PostCode: ");
                    String newStudioPC = user_input.nextLine();
                    newStudio.setPostcode(newStudioPC);
                    Response createResponse = createStudio(target, newStudio);
                    System.out.println(createResponse.getLocation());
                    createResponse.close();
                    break;
                case "3":
                    Studio updStudio = new Studio();
                    System.out.println("Studio ID: ");
                    String updStudioID = user_input.nextLine();
                    System.out.println("Studio Name: ");
                    String updStudioName = user_input.nextLine();
                    updStudio.setName(updStudioName);
                    System.out.println("Studio CountryCode: ");
                    String updStudioCC = user_input.nextLine();
                    updStudio.setCountrycode(updStudioCC);
                    System.out.println("Studio PostCode: ");
                    String updStudioPC = user_input.nextLine();
                    updStudio.setPostcode(updStudioPC);
                    updateStudio(target, updStudio, new Long(updStudioID));
                    break;
                case "4":
                    System.out.println("Studio ID: ");
                    String delStudioID = user_input.nextLine();
                    deleteStudio(target, new Long(delStudioID));
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
