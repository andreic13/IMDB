// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.io.FileReader;
import java.io.IOException;
import java.time.format.*;
import java.time.*;
import java.util.*;
import java.lang.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//--------------CLASSES--------------

public class IMDB {
    private static IMDB instance = null;

    private IMDB() {
        allAccounts = new ArrayList<>();
        allActors = new ArrayList<>();
        allRequests = new ArrayList<>();
        allProductions = new ArrayList<>();
    }

    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    //Lists:
    public List<User> allAccounts;
    public List<Actor> allActors;
    public List<Request> allRequests;
    public List<Production> allProductions;

    public void parseAll() throws IOException, ParseException {
        //-----------------------ACTORS-----------------------
        JSONParser parser2 = new JSONParser();
        FileReader reader2 = new FileReader("C:\\Users\\Andrei\\Desktop\\INTELIJ_Proj\\IMDB\\" +
                "POO-TEMA-2023-CHECKER\\src\\main\\resources\\input\\actors.json");
        Object obj2 = parser2.parse(reader2);
        JSONArray jsonArray2 = (JSONArray) obj2;

        for (Object o : jsonArray2) {
            JSONObject actor = (JSONObject) o;

            String actorName = (String) actor.get("name");

            JSONArray performancesJson = (JSONArray) actor.get("performances");
            List<NameType> performances = new ArrayList();
            if (performancesJson != null) {
                for (Object p : performancesJson) {
                    JSONObject performance = (JSONObject) p;
                    String title = (String) performance.get("title");
                    String type = (String) performance.get("type");
                    performances.add(new NameType(title, type));
                }
            }

            String biography = (String) actor.get("biography");

            Actor thisActor = new Actor(actorName, performances, biography);
            this.allActors.add(thisActor);
        }

        //-----------------------PRODUCTIONS-----------------------
        JSONParser parser3 = new JSONParser();
        FileReader reader3 = new FileReader("C:\\Users\\Andrei\\Desktop\\INTELIJ_Proj\\IMDB\\" +
                "POO-TEMA-2023-CHECKER\\src\\main\\resources\\input\\production.json");
        Object obj3 = parser3.parse(reader3);
        JSONArray jsonArray3 = (JSONArray) obj3;
        for (Object o : jsonArray3) {
            JSONObject prod = (JSONObject) o;
            String title = (String) prod.get("title");
            String movieType = (String) prod.get("type");

            JSONArray directorsJson = (JSONArray) prod.get("directors");
            List<String> directors = new ArrayList();
            if (directorsJson != null) {
                directors.addAll(directorsJson);
            }

            JSONArray actorsJson = (JSONArray) prod.get("actors");
            List<String> actors = new ArrayList();
            if (actorsJson != null) {
                actors.addAll(actorsJson);
            }

            JSONArray genresJson = (JSONArray) prod.get("genres");
            List<Genre> genres = new ArrayList();
            if (genresJson != null) {
                for (Object g : genresJson) {
                    String genre = (String) g;
                    genres.add(Genre.valueOf(genre));
                }
            }

            JSONArray ratingsJson = (JSONArray) prod.get("ratings");
            List<Rating> ratings = new ArrayList();
            if (ratingsJson != null) {
                for (Object r : ratingsJson) {
                    JSONObject rating = (JSONObject) r;
                    String username = (String) rating.get("username");
                    Long ratingLong = (Long) rating.get("rating");
                    String comment = (String) rating.get("comment");
                    ratings.add(new Rating(username, ratingLong, comment));
                }
            }

            String description = (String) prod.get("plot");
            Double overallRating = (Double) prod.get("averageRating");

            if (movieType.equals("Movie")) {
                String lengthString = (String) prod.get("duration");
                // Remove non-numeric characters
                String numericPartLength = lengthString.replaceAll("[^0-9]", "");
                Long length = Long.parseLong(numericPartLength);

                Long releaseYear = (Long) prod.get("releaseYear");

                Movie movie = new Movie(title, directors, actors, genres, ratings,
                        description, length, releaseYear);
                this.allProductions.add(movie);
            } else if (movieType.equals("Series")) {
                Long releaseYear = (Long) prod.get("releaseYear");

                Long numberOfSeasons = (Long) prod.get("numSeasons");

                //Map<String, List<Episode>> seasonsMap = (Map<String, List<Episode>>) prod.get("seasons");
                Map<String, List<Episode>> seasonsMap = new LinkedHashMap<>();
                JSONObject seasonsObject = (JSONObject) prod.get("seasons");
                if (seasonsObject != null) {
                    for (Map.Entry<String, JSONArray> seasonEntry :
                            ((Set<Map.Entry<String, JSONArray>>) seasonsObject.entrySet())) {
                        String seasonName = seasonEntry.getKey();
                        JSONArray episodesJson = seasonEntry.getValue();
                        List<Episode> episodes = new ArrayList<>();

                        for (Object e : episodesJson) {
                            JSONObject episode = (JSONObject) e;
                            String episodeName = (String) episode.get("episodeName");
                            int length = Integer.parseInt
                                    (((String) episode.get("duration")).replaceAll("[^0-9]", ""));
                            episodes.add(new Episode(episodeName, length));
                        }

                        seasonsMap.put(seasonName, episodes);
                    }
                }

                Series series = new Series(title, directors, actors, genres, ratings, description,
                        releaseYear, numberOfSeasons, seasonsMap);
                this.allProductions.add(series);
            }
        }

        //-----------------------ACCOUNTS-----------------------
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader("C:\\Users\\Andrei\\Desktop\\INTELIJ_Proj\\IMDB\\" +
                "POO-TEMA-2023-CHECKER\\src\\main\\resources\\input\\accounts.json");
        Object obj = parser.parse(reader);
        JSONArray jsonArray = (JSONArray) obj;

        for (Object o : jsonArray) {
            JSONObject account = (JSONObject) o;

            String username = (String) account.get("username");

            String experienceString = (String) account.get("experience");
            if (experienceString == null) {
                experienceString = "0";
            }
            int experience = Integer.parseInt((String) experienceString);

            //information:
            JSONObject informationJson = (JSONObject) account.get("information");
            JSONObject credentialsJson = (JSONObject) informationJson.get("credentials");

            String email = (String) credentialsJson.get("email");
            String pass = (String) credentialsJson.get("password");
            String name = (String) informationJson.get("name");
            String country = (String) informationJson.get("country");

            Long ageLong = (Long) informationJson.get("age");
            int age = ageLong.intValue();

            String genderString = (String) informationJson.get("gender");
            char gender = genderString.charAt(0);

            //parsare + formatare data:
            String birthDateString = (String) informationJson.get("birthDate");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime birthDate = LocalDate.parse(birthDateString, formatter).atStartOfDay();

            String userType = (String) account.get("userType");
            //toAccountType:
            AccountType accountType = AccountType.valueOf(userType.toUpperCase());

            //arrays:
            //for contributors/admins
            JSONArray productionsContributionJson = (JSONArray) account.get("productionsContribution");
            if (productionsContributionJson == null) {
                productionsContributionJson = new JSONArray();
            }
            List<String> productionsContribution = new ArrayList(productionsContributionJson);


            JSONArray actorsContributionJson = (JSONArray) account.get("actorsContribution");
            if (actorsContributionJson == null) {
                actorsContributionJson = new JSONArray();
            }
            List<String> actorsContribution = new ArrayList(actorsContributionJson);

            //rest:
            JSONArray favoriteProductionsJson = (JSONArray) account.get("favoriteProductions");
            if (favoriteProductionsJson == null) {
                favoriteProductionsJson = new JSONArray();
            }
            List<String> favoriteProductions = new ArrayList(favoriteProductionsJson);

            JSONArray favoriteActorsJson = (JSONArray) account.get("favoriteActors");
            if (favoriteActorsJson == null) {
                favoriteActorsJson = new JSONArray();
            }
            List<String> favoriteActors = new ArrayList(favoriteActorsJson);

            JSONArray notificationsJson = (JSONArray) account.get("notifications");
            List<String> notifications = new ArrayList();

            if (notificationsJson != null) {
                notifications.addAll(notificationsJson);
            }

            //creare Information:
            InformationBuilder informationBuilder = new InformationBuilder();
            informationBuilder.setCredentials(new Credentials(email, pass));
            informationBuilder.setName(name);
            informationBuilder.setCountry(country);
            informationBuilder.setAge(age);
            informationBuilder.setGender(gender);
            informationBuilder.setBirthDate(birthDate);
            User.Information userInformation = informationBuilder.build();


            //creare preferences:
            SortedSet<Object> preferences = new TreeSet(new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    if (o1 instanceof Production && o2 instanceof Production) {
                        Production p1 = (Production) o1;
                        Production p2 = (Production) o2;
                        return p1.productionTitle.compareTo(p2.productionTitle);
                    } else if (o1 instanceof Actor && o2 instanceof Actor) {
                        Actor a1 = (Actor) o1;
                        Actor a2 = (Actor) o2;
                        return a1.getActorName().compareTo(a2.actorName);
                    } else if (o1 instanceof Production && o2 instanceof Actor) {
                        Production p1 = (Production) o1;
                        Actor a2 = (Actor) o2;
                        return p1.getProductionTitle().compareTo(a2.actorName);
                    } else if (o1 instanceof Actor && o2 instanceof Production) {
                        Actor a1 = (Actor) o1;
                        Production p2 = (Production) o2;
                        return a1.getActorName().compareTo(p2.productionTitle);
                    } else {
                        return 0;
                    }
                }
            });

            //Adaugare fav actors:
            for (String actorName : favoriteActors) {
                for (Actor actor : allActors) {
                    if (actor.getActorName().equals(actorName)) {
                        //System.out.println("Adaugat: " + actor.getActorName());
                        preferences.add(actor);
                        break;
                    }
                }
            }
            //Adaugare fav productions:
            for (String productionTitle : favoriteProductions) {
                for (Production prod : allProductions) {
                    if (prod.getProductionTitle().equals(productionTitle)) {
                        //System.out.println("Adaugat: " + prod.getProductionTitle());
                        preferences.add(prod);
                        break;
                    }
                }
            }

            //creare contribution:
            SortedSet<Object> addedByHim = new TreeSet(new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    if (o1 instanceof Production && o2 instanceof Production) {
                        Production p1 = (Production) o1;
                        Production p2 = (Production) o2;
                        return p1.productionTitle.compareTo(p2.productionTitle);
                    } else if (o1 instanceof Actor && o2 instanceof Actor) {
                        Actor a1 = (Actor) o1;
                        Actor a2 = (Actor) o2;
                        return a1.getActorName().compareTo(a2.actorName);
                    } else if (o1 instanceof Production && o2 instanceof Actor) {
                        Production p1 = (Production) o1;
                        Actor a2 = (Actor) o2;
                        return p1.getProductionTitle().compareTo(a2.actorName);
                    } else if (o1 instanceof Actor && o2 instanceof Production) {
                        Actor a1 = (Actor) o1;
                        Production p2 = (Production) o2;
                        return a1.getActorName().compareTo(p2.productionTitle);
                    } else {
                        return 0;
                    }
                }
            });

            //Adaugare productions contribution:
            for (String productionTitle : productionsContribution) {
                for (Production prod : allProductions) {
                    if (prod.getProductionTitle().equals(productionTitle)) {
                        //System.out.println("Adaugat: " + prod.getProductionTitle());
                        addedByHim.add(prod);
                        break;
                    }
                }
            }
            //Adaugare actors contribution:
            for (String actorName : actorsContribution) {
                for (Actor actor : allActors) {
                    if (actor.getActorName().equals(actorName)) {
                        //System.out.println("Adaugat: " + actor.getActorName());
                        addedByHim.add(actor);
                        break;
                    }
                }
            }

            //creare User:

            User user = UserFactory.createUser(userInformation, accountType,
                    username, experience, notifications, preferences, addedByHim);
            this.allAccounts.add(user);
        }

        //-----------------------REQUESTS-----------------------
        JSONParser parser4 = new JSONParser();
        FileReader reader4 = new FileReader("C:\\Users\\Andrei\\Desktop\\INTELIJ_Proj\\IMDB\\" +
                "POO-TEMA-2023-CHECKER\\src\\main\\resources\\input\\requests.json");
        Object obj4 = parser4.parse(reader4);
        JSONArray jsonArray4 = (JSONArray) obj4;
        for (Object o : jsonArray4) {
            JSONObject req = (JSONObject) o;

            String typeString = (String) req.get("type");
            RequestType type = RequestType.valueOf(typeString);

            String dateString = (String) req.get("createdDate");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(dateString, formatter);

            String title_name = null;
            //daca are nume actor sau film:
            if (typeString.equals("ACTOR_ISSUE")) {
                title_name = (String) req.get("actorName");
            } else if (typeString.equals("MOVIE_ISSUE")) {
                title_name = (String) req.get("movieTitle");
            }

            String description = (String) req.get("description");

            String requestUser = (String) req.get("username");

            Request request = new Request(type, title_name, description, date, requestUser);
            this.allRequests.add(request);
            if (request.getSolvingUser().equals("ADMIN")) {
                RequestHolder.addRequest(request);
                //Observer pattern, notificari initiale admini:
                for (User u : allAccounts) {
                    if (u.getAccountType() == AccountType.ADMIN) {
                        NotificationManager.getInstance().addObserver(u);
                    }
                }
                NotificationManager.getInstance().notifyObservers("User: " + requestUser +
                        " has a request for you!");
                for (User u : allAccounts) {
                    if (u.getAccountType() == AccountType.ADMIN) {
                        NotificationManager.getInstance().removeObserver(u);
                    }
                }
            } else {
                for (User u : allAccounts) {
                    if(u.getUsername().equals(request.getSolvingUser())) {
                        NotificationManager.getInstance().addObserver(u);
                        NotificationManager.getInstance().notifyObservers("User: " + requestUser +
                        " has a request for you!");
                        NotificationManager.getInstance().removeObserver(u);
                        break;
                    }
                }
            }
        }
    }

    public void run() throws IOException, ParseException {
        //Parse jsons:
        parseAll();

        //check Admin notif for requests:
//        for (User u : allAccounts) {
//            if (u.getUsername().equals("testContributor")) {
//                Contributor contributor = (Contributor) u;
//                Request req = new Request(RequestType.DELETE_ACCOUNT, "test", "testDescription",
//                        LocalDateTime.now(), "testContributor");
//                contributor.createRequest(req);
//                break;
//            }
//        }

        //chcek Contributor notif for requests:
//        for (User u : allAccounts) {
//            if (u.getUsername().equals("testRegular")) {
//                Regular regular = (Regular) u;
//                Request req = new Request(RequestType.MOVIE_ISSUE, "Test Movie",
//                        "testDescription", LocalDateTime.now(), "testRegular");
//                regular.createRequest(req);
//                break;
//            }
//        }
//        //check RequestUser notif
//        for (User u : allAccounts) {
//            if (u.getUsername().equals("testContributor")) {
//                if (u instanceof Staff) {
//                    Staff staff = (Staff) u;
//                    staff.solveRequest(allRequests.get(4));
//                    break;
//                }
//            }
//        }

        //check same production reviewed + production added by staff reviewed:
//        for(User u : allAccounts) {
//            if(u.getUsername().equals("susan_smith_123")) {
//                Rating rating = new Rating("susan_smith_123", 5L, "testComment");
//                for(Production prod : allProductions) {
//                    if(prod.productionTitle.equals("Test Series")) {
//                        Regular regular = (Regular) u;
//                        regular.addRating(prod, rating);
//                        break;
//                    }
//                }
//            }
//        }

        //Welcome:
        int running = 0;
        while (running == 0) {
            boolean found = false;
            User userCurrent = null;

            System.out.println("Welcome back! Please login!");

            while(found == false) {
                System.out.println("       Email: ");
                Scanner scanner = new Scanner(System.in);
                String email = scanner.next();
                System.out.println("       Password: ");
                String pass = scanner.next();
                for (User u : allAccounts) {
                    if (u.getUserInformation().getUserCredentials().getEmail().equals(email) &&
                            u.getUserInformation().getUserCredentials().getPassword().equals(pass)) {
                        found = true;
                        userCurrent = u;
                        break;
                    }
                }
                System.out.println();

                if (found == false) {
                    System.out.println("Wrong email or password!");
                    System.out.println("Please try again!");
                    System.out.println();
                }
            }

            //Logged in:
            System.out.println("Welcome back user " + userCurrent.getUsername() + "!");
            System.out.println("User experience: " + userCurrent.getExperience());
            System.out.println();

            //----------------------Regular user:-----------------------
            if (userCurrent.getAccountType() == AccountType.REGULAR) {
                Regular regular = (Regular) userCurrent;
                int menu = 0;
                while (menu == 0) {
                    System.out.println("Choose action: ");
                    System.out.println("1. View productions details");
                    System.out.println("2. View actors details");
                    System.out.println("3. View notifications");
                    System.out.println("4. Search for actor/movie/series");
                    System.out.println("5. Add/delete favorite actor/movie/series to/from favorites");
                    System.out.println("6. Add/delete request");
                    System.out.println("7. Add/delete rating for a production");
                    System.out.println("8. Logout");
                    Scanner scanner2 = new Scanner(System.in);
                    try {
                        int option = scanner2.nextInt();
                        switch (option) {
                            case 1:
                                checkProductions(IMDB.getInstance());

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 2:
                                checkActors(IMDB.getInstance());

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 3:
                                displayNotif(userCurrent);

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 4:
                                searchActorMovieSeries();

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 5:
                                addDeleteFavorite(userCurrent);

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 6:
                                addDeleteRequest(userCurrent);
                                System.out.println();
                                System.out.println("Current requests");
                                checkRequests(IMDB.getInstance());

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;

                            case 7:
                                addDeleteRating(userCurrent);
                                System.out.println();

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 8:
                                running = regular.logout();
                                if (running == 0) {
                                    menu = 1;
                                }
                                if (running == 1) {
                                    return;
                                }
                                break;
                            default:
                                throw new InvalidCommandException("Unexpected value: " + option);
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Invalid input!");
                        System.out.println("Press 'Enter' to continue...");
                        System.in.read();
                    }
                }
            }

            //----------------------Contributor user:-----------------------
            if (userCurrent.getAccountType() == AccountType.CONTRIBUTOR) {
                Contributor contributor = (Contributor) userCurrent;
                int menu = 0;
                while (menu == 0) {
                    System.out.println("Choose action: ");
                    System.out.println("1. View productions details");
                    System.out.println("2. View actors details");
                    System.out.println("3. View notifications");
                    System.out.println("4. Search for actor/movie/series");
                    System.out.println("5. Add/delete favorite actor/movie/series to/from favorites");
                    System.out.println("6. Add/delete request");
                    System.out.println("7. Add/delete production / actor from system");
                    System.out.println("8. View and solve your requests");
                    System.out.println("9. Update production / actors");
                    System.out.println("10. Logout");
                    Scanner scanner2 = new Scanner(System.in);
                    try {
                        int option = scanner2.nextInt();
                        switch (option) {
                            case 1:
                                checkProductions(IMDB.getInstance());

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 2:
                                checkActors(IMDB.getInstance());

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 3:
                                displayNotif(userCurrent);

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 4:
                                searchActorMovieSeries();

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 5:
                                addDeleteFavorite(userCurrent);

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 6:
                                addDeleteRequest(userCurrent);
                                System.out.println();
                                System.out.println("Current requests");
                                checkRequests(IMDB.getInstance());

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 7:
                                addDeleteProductionActor(userCurrent);
                                System.out.println();

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 8:
                                viewSolveRequestsContributor(userCurrent);
                                System.out.println();
                                System.out.println("Current requests");
                                checkRequests(IMDB.getInstance());

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 9:
                                updateProductionActor(userCurrent);
                                System.out.println();

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 10:
                                running = contributor.logout();
                                if (running == 0) {
                                    menu = 1;
                                }
                                if (running == 1) {
                                    return;
                                }
                                break;
                            default:
                                throw new InvalidCommandException("Unexpected value: " + option);
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Invalid input!");
                        System.out.println("Press 'Enter' to continue...");
                        System.in.read();
                    }
                }
            }

            //----------------------Admin user:-----------------------
            if (userCurrent.getAccountType() == AccountType.ADMIN) {
                Admin admin = (Admin) userCurrent;
                int menu = 0;
                while (menu == 0) {
                    System.out.println("Choose action: ");
                    System.out.println("1. View productions details");
                    System.out.println("2. View actors details");
                    System.out.println("3. View notifications");
                    System.out.println("4. Search for actor/movie/series");
                    System.out.println("5. Add/delete favorite actor/movie/series to/from favorites");
                    System.out.println("6. Add/delete production / actor from system");
                    System.out.println("7. View and solve your requests");
                    System.out.println("8. Update production / actors");
                    System.out.println("9. Add/delete user from system");
                    System.out.println("10. Logout");
                    Scanner scanner2 = new Scanner(System.in);
                    try {
                        int option = scanner2.nextInt();
                        switch (option) {
                            case 1:
                                checkProductions(IMDB.getInstance());

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 2:
                                checkActors(IMDB.getInstance());

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 3:
                                displayNotif(userCurrent);

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 4:
                                searchActorMovieSeries();

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 5:
                                addDeleteFavorite(userCurrent);

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 6:
                                addDeleteProductionActor(userCurrent);
                                System.out.println();

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 7:
                                viewSolveRequestsAdmin(userCurrent);
                                System.out.println();
                                System.out.println("Current requests");
                                checkRequests(IMDB.getInstance());

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 8:
                                updateProductionActor(userCurrent);
                                System.out.println();

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 9:
                                addDeleteUserSystem(userCurrent);
                                System.out.println();
                                System.out.println("Current users:");
                                checkAccounts(IMDB.getInstance());

                                System.out.println("Press 'Enter' to continue...");
                                System.in.read();
                                break;
                            case 10:
                                running = admin.logout();
                                if (running == 0) {
                                    menu = 1;
                                }
                                if (running == 1) {
                                    return;
                                }
                                break;
                            default:
                                throw new InvalidCommandException("Unexpected value: " + option);
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Invalid input!");
                        System.out.println("Press 'Enter' to continue...");
                        System.in.read();
                    }
                }
            }
        }
    }

    //---------------------Menu methods---------------------
    //---------------------REGULAR---------------------
    public void displayNotif(User user) {
        if (user.getNotifications().size() == 0) {
            System.out.println("No notifications!");
        } else {
            //daca exista notificari:
            for (Object notificationobj : user.getNotifications()) {
                String notification = (String) notificationobj;
                System.out.println(notification);
            }
        }
    }

    public void searchActorMovieSeries() {
        System.out.println("Search for: ");
        System.out.println("1. Actor");
        System.out.println("2. Movie");
        System.out.println("3. Series");
        Scanner scanner = new Scanner(System.in);
        try {
            int option = scanner.nextInt();
            scanner.nextLine(); //pentru a citi si \n
            switch (option) {
                case 1:
                    System.out.println("Actor name: ");
                    String actorName = scanner.nextLine();
                    int found1 = 0;
                    for (Actor actor : allActors) {
                        if (actor.getActorName().equals(actorName)) {
                            actor.displayInfo();
                            found1 = 1;
                            break;
                        }
                    }
                    //nu a gasit actorul
                    if (found1 == 0) {
                        System.out.println("Actor not found!");
                    }
                    break;
                case 2:
                    System.out.println("Movie title: ");
                    String movieTitle = scanner.nextLine();
                    int found2 = 0;
                    for (Production prod : allProductions) {
                        if (prod.productionTitle.equals(movieTitle)) {
                            prod.displayInfo();
                            found2 = 1;
                            break;
                        }
                    }
                    if (found2 == 0) {
                        System.out.println("Movie not found!");
                    }
                    break;
                case 3:
                    System.out.println("Series title: ");
                    String seriesTitle = scanner.nextLine();
                    int found3 = 0;
                    for (Production prod : allProductions) {
                        if (prod.productionTitle.equals(seriesTitle)) {
                            prod.displayInfo();
                            found3 = 1;
                            break;
                        }
                    }
                    if (found3 == 0) {
                        System.out.println("Series not found!");
                    }
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
        }
    }

    public void addDeleteFavorite(User currentUser) {
        System.out.println("Add or delete actor/production: ");
        System.out.println("1. Add");
        System.out.println("2. Delete");
        Scanner scanner = new Scanner(System.in);
        try {
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.println("What do you want to add: ");
                    System.out.println("1. Actor");
                    System.out.println("2. Production");
                    int option2 = scanner.nextInt();
                    scanner.nextLine(); //pentru a citi si \n
                    switch (option2) {
                        case 1: //add actor
                            System.out.println("Actor name: ");
                            String actorName = scanner.nextLine();
                            int found1 = 0;
                            for (Actor actor : allActors) {
                                if (actor.getActorName().equals(actorName)) {
                                    currentUser.addPreferences(actor);
                                    found1 = 1;
                                    break;
                                }
                            }
                            if (found1 == 0) {
                                System.out.println("Actor not found!");
                            }
                            break;
                        case 2: //add production
                            System.out.println("Production title: ");
                            String productTitle = scanner.nextLine();
                            int found2 = 0;
                            for (Production prodzz : IMDB.getInstance().allProductions) {
                                if (prodzz.getProductionTitle().equals(productTitle)) {
                                    currentUser.addPreferences((Production) prodzz);
                                    found2 = 1;
                                    break;
                                }
                            }
                            if (found2 == 0) {
                                System.out.println("Production not found!");
                                System.out.println();
                            }
                            break;
                    }
                    //Noua lista de favorite:
                    System.out.println("Current favorite list: ");
                    for (Object pref : currentUser.getPreferences()) {
                        if (pref instanceof Actor) {
                            Actor actor = (Actor) pref;
                            System.out.println("Actor name: " + actor.getActorName());
                        } else if (pref instanceof Production) {
                            Production prod = (Production) pref;
                            System.out.println("Production title: " + prod.getProductionTitle());
                        }
                    }
                    break;
                case 2:
                    System.out.println("What do you want to delete: ");
                    System.out.println("1. Actor");
                    System.out.println("2. Production");
                    int option3 = scanner.nextInt();
                    scanner.nextLine(); //pentru a citi si \n
                    switch (option3) {
                        case 1: //delete actor
                            System.out.println("Actor name: ");
                            String actorName = scanner.nextLine();
                            int found3 = 0;
                            for (Actor actor : allActors) {
                                if (actor.getActorName().equals(actorName)) {
                                    currentUser.removePreferences(actor);
                                    found3 = 1;
                                    break;
                                }
                            }
                            if (found3 == 0) {
                                System.out.println("Actor not found!");
                            }
                            break;
                        case 2: //delete production
                            System.out.println("Production title: ");
                            String productionTitle = scanner.nextLine();
                            int found4 = 0;
                            for (Production prod : allProductions) {
                                if (prod.productionTitle.equals(productionTitle)) {
                                    currentUser.removePreferences(prod);
                                    found4 = 1;
                                    break;
                                }
                            }
                            if (found4 == 0) {
                                System.out.println("Production not found!");
                                System.out.println();
                            }
                            break;
                    }
                    //Noua lista de favorite:
                    System.out.println("Current favorite list: ");
                    for (Object pref : currentUser.getPreferences()) {
                        if (pref instanceof Actor) {
                            Actor actor = (Actor) pref;
                            System.out.println("Actor name: " + actor.getActorName());
                        } else if (pref instanceof Production) {
                            Production prod = (Production) pref;
                            System.out.println("Production title: " + prod.getProductionTitle());
                        }
                    }
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
        }
    }

    public void addDeleteRequest(User userCurrent) {
        System.out.println("Add or delete request: ");
        System.out.println("1. Add");
        System.out.println("2. Delete");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        try {
            switch (option) {
                case 1:
                    System.out.println("What do you want to add: ");
                    System.out.println("1. Delete account");
                    System.out.println("2. Actor issue");
                    System.out.println("3. Movie issue");
                    System.out.println("4. Other");
                    int option2 = scanner.nextInt();
                    scanner.nextLine(); //pentru a citi si \n
                    switch (option2) {
                        case 1: //delete account
                            System.out.println("Description: ");
                            String description = scanner.nextLine();
                            Request req = new Request(RequestType.DELETE_ACCOUNT, null, description,
                                    LocalDateTime.now(), userCurrent.getUsername());
                            if (userCurrent instanceof Regular) {
                                Regular regular = (Regular) userCurrent;
                                regular.createRequest(req);
                            } else if (userCurrent instanceof Contributor) {
                                Contributor contributor = (Contributor) userCurrent;
                                contributor.createRequest(req);
                            }
                            System.out.println("Thank you for your request!");
                            break;
                        case 2: //actor issue
                            System.out.println("Actor name: ");
                            String actorName = scanner.nextLine();
                            System.out.println("Description: ");
                            String description2 = scanner.nextLine();
                            Request req2 = new Request(RequestType.ACTOR_ISSUE, actorName, description2,
                                    LocalDateTime.now(), userCurrent.getUsername());
                            if (userCurrent instanceof Regular) {
                                Regular regular = (Regular) userCurrent;
                                regular.createRequest(req2);
                            } else if (userCurrent instanceof Contributor) {
                                Contributor contributor = (Contributor) userCurrent;
                                contributor.createRequest(req2);
                            }
                            System.out.println("Thank you for your request!");
                            break;
                        case 3: //movie issue
                            System.out.println("Movie title: ");
                            String movieTitle = scanner.nextLine();
                            System.out.println("Description: ");
                            String description3 = scanner.nextLine();
                            Request req3 = new Request(RequestType.MOVIE_ISSUE, movieTitle, description3,
                                    LocalDateTime.now(), userCurrent.getUsername());
                            if (userCurrent instanceof Regular) {
                                Regular regular = (Regular) userCurrent;
                                regular.createRequest(req3);
                            } else if (userCurrent instanceof Contributor) {
                                Contributor contributor = (Contributor) userCurrent;
                                contributor.createRequest(req3);
                            }
                            System.out.println("Thank you for your request!");
                            break;
                        case 4: //other
                            System.out.println("Description: ");
                            String description4 = scanner.nextLine();
                            Request req4 = new Request(RequestType.OTHERS, null, description4,
                                    LocalDateTime.now(), userCurrent.getUsername());
                            if (userCurrent instanceof Regular) {
                                Regular regular = (Regular) userCurrent;
                                regular.createRequest(req4);
                            } else if (userCurrent instanceof Contributor) {
                                Contributor contributor = (Contributor) userCurrent;
                                contributor.createRequest(req4);
                            }
                            System.out.println("Thank you for your request!");
                            break;
                    }
                    break;
                case 2:
                    System.out.println("What do you want to delete: ");
                    System.out.println("1. Delete account");
                    System.out.println("2. Actor issue");
                    System.out.println("3. Movie issue");
                    System.out.println("4. Other");
                    try {
                        int option3 = scanner.nextInt();
                        scanner.nextLine(); //pentru a citi si \n
                        switch (option3) {
                            case 1: //delete account
                                System.out.println("Description: ");
                                String description = scanner.nextLine();
                                int found = 0;
                                for (Request req : IMDB.getInstance().allRequests) {
                                    if (description.equals(req.getDescription())) {
                                        if (userCurrent instanceof Regular) {
                                            Regular regular = (Regular) userCurrent;
                                            regular.removeRequest(req);
                                        } else if (userCurrent instanceof Contributor) {
                                            Contributor contributor = (Contributor) userCurrent;
                                            contributor.removeRequest(req);
                                        }
                                        System.out.println("Request deleted!");
                                        found = 1;
                                        break;
                                    }
                                }
                                if (found == 0) {
                                    System.out.println("Request not found!");
                                }
                                break;
                            case 2: //actor issue
                                System.out.println("Actor name: ");
                                String actorName = scanner.nextLine();
                                System.out.println("Description: ");
                                String description2 = scanner.nextLine();
                                int found2 = 0;
                                for (Request req : IMDB.getInstance().allRequests) {
                                    if (description2.equals(req.getDescription()) && actorName.equals(req.getTitle_name())) {
                                        if (userCurrent instanceof Regular) {
                                            Regular regular = (Regular) userCurrent;
                                            regular.removeRequest(req);
                                        } else if (userCurrent instanceof Contributor) {
                                            Contributor contributor = (Contributor) userCurrent;
                                            contributor.removeRequest(req);
                                        }
                                        System.out.println("Request deleted!");
                                        found2 = 1;
                                        break;
                                    }
                                }
                                if (found2 == 0) {
                                    System.out.println("Request not found!");
                                }
                                break;
                            case 3: //movie issue
                                System.out.println("Movie title: ");
                                String movieTitle = scanner.nextLine();
                                System.out.println("Description: ");
                                String description3 = scanner.nextLine();
                                int found3 = 0;
                                for (Request req : IMDB.getInstance().allRequests) {
                                    if (description3.equals(req.getDescription()) && movieTitle.equals(req.getTitle_name())) {
                                        if (userCurrent instanceof Regular) {
                                            Regular regular = (Regular) userCurrent;
                                            regular.removeRequest(req);
                                        } else if (userCurrent instanceof Contributor) {
                                            Contributor contributor = (Contributor) userCurrent;
                                            contributor.removeRequest(req);
                                        }
                                        System.out.println("Request deleted!");
                                        found3 = 1;
                                        break;
                                    }
                                }
                                if (found3 == 0) {
                                    System.out.println("Request not found!");
                                }
                                break;
                            case 4: //other
                                System.out.println("Description: ");
                                String description4 = scanner.nextLine();
                                int found4 = 0;
                                for (Request req : IMDB.getInstance().allRequests) {
                                    if (description4.equals(req.getDescription())) {
                                        if (userCurrent instanceof Regular) {
                                            Regular regular = (Regular) userCurrent;
                                            regular.removeRequest(req);
                                        } else if (userCurrent instanceof Contributor) {
                                            Contributor contributor = (Contributor) userCurrent;
                                            contributor.removeRequest(req);
                                        }
                                        System.out.println("Request deleted!");
                                        found4 = 1;
                                        break;
                                    }
                                }
                                if (found4 == 0) {
                                    System.out.println("Request not found!");
                                }
                                break;
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                    }
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
        }
    }

    public void addDeleteRating(User userCurrent) {
        System.out.println("Add or delete rating: ");
        System.out.println("1. Add");
        System.out.println("2. Delete");
        Scanner scanner = new Scanner(System.in);
        try {
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.println("What do you want to rate: ");
                    System.out.println("1. Movie");
                    System.out.println("2. Series");
                    int option2 = scanner.nextInt();
                    scanner.nextLine(); //pentru a citi si \n
                    switch (option2) {
                        case 1: //add movie rating
                            System.out.println("Movie title: ");
                            String movieTitle = scanner.nextLine();
                            int found1 = 0;
                            for (Production prod : allProductions) {
                                if (prod.getProductionTitle().equals(movieTitle)) {
                                    System.out.println("Rating: (between 1 and 10)");
                                    Long rating = 0L;
                                    try {
                                        rating = scanner.nextLong();
                                        if (rating < 1 || rating > 10) {
                                            System.out.println("Rating must be between 1 and 10!");
                                            found1 = 1;
                                            break;
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("Rating must be a number!");
                                        found1 = 1;
                                        break;
                                    }
                                    scanner.nextLine(); //pentru a citi si \n
                                    System.out.println("Comment: ");
                                    String comment = scanner.nextLine();
                                    Rating ratingObj = new Rating(userCurrent.getUsername(), rating, comment);
                                    Regular regular = (Regular) userCurrent;
                                    regular.addRating(prod, ratingObj);
                                    prod.updateOverallRating();
                                    System.out.println();
                                    System.out.println("Rating added!");
                                    found1 = 1;
                                    break;
                                }
                            }
                            if (found1 == 0) {
                                System.out.println("Movie not found!");
                            }
                            break;
                        case 2: //add series rating
                            System.out.println("Series title: ");
                            String seriesTitle = scanner.nextLine();
                            int found2 = 0;
                            for (Production prod : allProductions) {
                                if (prod.getProductionTitle().equals(seriesTitle)) {
                                    System.out.println("Rating: (between 1 and 10)");
                                    Long rating2 = 0L;
                                    try {
                                        rating2 = scanner.nextLong();
                                        if (rating2 < 1 || rating2 > 10) {
                                            System.out.println("Rating must be between 1 and 10!");
                                            found2 = 1;
                                            break;
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("Rating must be a number!");
                                        found2 = 1;
                                        break;
                                    }
                                    scanner.nextLine(); //pentru a citi si \n
                                    System.out.println("Comment: ");
                                    String comment = scanner.nextLine();
                                    Rating ratingObj = new Rating(userCurrent.getUsername(), rating2, comment);
                                    Regular regular = (Regular) userCurrent;
                                    regular.addRating(prod, ratingObj);
                                    prod.updateOverallRating();
                                    System.out.println();
                                    System.out.println("Rating added!");
                                    found2 = 1;
                                    break;
                                }
                            }
                            if (found2 == 0) {
                                System.out.println("Series not found!");
                            }
                            break;
                    }
                    break;
                case 2:
                    System.out.println("What do you want to delete: ");
                    System.out.println("1. Movie");
                    System.out.println("2. Series");
                    try {
                        int option3 = scanner.nextInt();
                        scanner.nextLine(); //pentru a citi si \n
                        switch (option3) {
                            case 1: //delete movie rating
                                System.out.println("Movie title: ");
                                String movieTitle = scanner.nextLine();
                                System.out.println("Comment: ");
                                String comment = scanner.nextLine();
                                int found3 = 0;
                                for (Production prod : allProductions) {
                                    for (Rating ratingObj : prod.getProductionRatings()) {
                                        if (ratingObj.getUsername().equals(userCurrent.getUsername()) &&
                                                ratingObj.getComment().equals(comment)) {
                                            Regular regular = (Regular) userCurrent;
                                            regular.removeRating(prod, ratingObj);
                                            System.out.println();
                                            System.out.println("Rating deleted!");
                                            found3 = 1;
                                            break;
                                        }
                                    }
                                }
                                if (found3 == 0) {
                                    System.out.println("Rating not found!");
                                }
                                break;
                            case 2: //delete series rating
                                System.out.println("Series title: ");
                                String seriesTitle = scanner.nextLine();
                                System.out.println("Comment: ");
                                String comment2 = scanner.nextLine();
                                int found4 = 0;
                                for (Production prod : allProductions) {
                                    for (Rating ratingObj2 : prod.getProductionRatings()) {
                                        if (ratingObj2.getUsername().equals(userCurrent.getUsername()) &&
                                                ratingObj2.getComment().equals(comment2)) {
                                            Regular regular = (Regular) userCurrent;
                                            regular.removeRating(prod, ratingObj2);
                                            System.out.println();
                                            System.out.println("Rating deleted!");
                                            found4 = 1;
                                            break;
                                        }
                                    }
                                }
                                if (found4 == 0) {
                                    System.out.println("Rating not found!");
                                }
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                    }
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
        }
    }

    //---------------------CONTRIBUTOR---------------------
    public void addDeleteProductionActor(User userCurrent) {
        System.out.println("Add or delete production/actor: ");
        System.out.println("1. Add");
        System.out.println("2. Delete");
        Scanner scanner = new Scanner(System.in);
        try {
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.println("What do you want to add: ");
                    System.out.println("1. Production");
                    System.out.println("2. Actor");
                    int option2 = scanner.nextInt();
                    scanner.nextLine(); //pentru a citi si \n
                    switch (option2) {
                        case 1: //add production
                            System.out.println("Production title: ");
                            String productionTitle = scanner.nextLine();
                            System.out.println("Production directors: (Type 'end' when you are done)");
                            System.out.println();
                            List<String> productionDirectors = new ArrayList<>();
                            System.out.println("Director: ");
                            String director = scanner.nextLine();
                            while (!director.equals("end")) {
                                productionDirectors.add(director);
                                System.out.println("Director: ");
                                director = scanner.nextLine();
                            }
                            List<String> productionActors = new ArrayList<>();
                            System.out.println("Production actors: (Type 'end' when you are done)");
                            System.out.println();
                            System.out.println("Actor: ");
                            String actor = scanner.nextLine();
                            while (!actor.equals("end")) {
                                productionActors.add(actor);
                                System.out.println("Actor: ");
                                actor = scanner.nextLine();
                            }
                            List<Genre> productionGenres = new ArrayList<>();
                            System.out.println("Production genres: (Type 'end' when you are done)");
                            System.out.println();
                            System.out.println("Available genres: Action, Adventure, Comedy, Drama, Horror, SF, Fantasy, Romance, Mystery, Thriller, Crime, Biography, War,\n" +
                                    "    Cooking, History, Documentary, Western, Musical, Sport");
                            System.out.println("Genre: ");
                            String genre = scanner.nextLine();
                            try {
                                while (!genre.equals("end")) {
                                    productionGenres.add(Genre.valueOf(genre));
                                    System.out.println("Genre: ");
                                    genre = scanner.nextLine();
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid genre!");
                                break;
                            }
                            List<Rating> productionRatings = new ArrayList<>();
                            System.out.println("Production description: ");
                            String productionDescription = scanner.nextLine();
                            System.out.println("Is your production a Movie or a Series?");
                            System.out.println("1. Movie");
                            System.out.println("2. Series");
                            int option3 = scanner.nextInt();
                            scanner.nextLine(); //pentru a citi si \n
                            switch (option3) {
                                case 1: //add movie
                                    System.out.println("Movie duration (minutes): ");
                                    Long movieDuration = 0L;
                                    try {
                                        movieDuration = scanner.nextLong();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Movie duration must be a number!");
                                        break;
                                    }
                                    scanner.nextLine(); //pentru a citi si \n
                                    System.out.println("Movie release year: ");
                                    Long movieReleaseYear = 0L;
                                    try {
                                        movieReleaseYear = scanner.nextLong();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Movie release year must be a number!");
                                        break;
                                    }
                                    scanner.nextLine(); //pentru a citi si \n
                                    Movie movie = new Movie(productionTitle, productionDirectors, productionActors,
                                            productionGenres, productionRatings, productionDescription,
                                            movieDuration, movieReleaseYear);
                                    Staff staff = (Staff) userCurrent;
                                    staff.addProductionSystem(movie);

                                    System.out.println("Movie added!");
                                    break;
                                case 2: //add series
                                    System.out.println("Series release year: ");
                                    Long seriesReleaseYear = 0L;
                                    try {
                                        seriesReleaseYear = scanner.nextLong();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Series release year must be a number!");
                                        break;
                                    }
                                    scanner.nextLine(); //pentru a citi si \n
                                    System.out.println("Series number of seasons: ");
                                    Long seriesNumberOfSeasons = 0L;
                                    try {
                                        seriesNumberOfSeasons = scanner.nextLong();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Series number of seasons must be a number!");
                                        break;
                                    }
                                    scanner.nextLine(); //pentru a citi si \n
                                    System.out.println("Series seasons map: (Type 'end' when you are done)");
                                    System.out.println();
                                    Map<String, List<Episode>> seriesSeasonsMap = new LinkedHashMap<>();
                                    System.out.println("Season: ");
                                    String season = scanner.nextLine();
                                    while (!season.equals("end")) {
                                        System.out.println("Season: " + season);
                                        List<Episode> seasonEpisodes = new ArrayList<>();
                                        System.out.println("Episode: ");
                                        String episodeName = scanner.nextLine();
                                        try {
                                            while (!episodeName.equals("end")) {
                                                System.out.println("Episode duration: ");
                                                int episodeLength = 0;
                                                try {
                                                    episodeLength = scanner.nextInt();
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Episode duration must be a number!");
                                                    break;
                                                }
                                                scanner.nextLine(); //pentru a citi si \n
                                                Episode episode = new Episode(episodeName, episodeLength);
                                                seasonEpisodes.add(episode);
                                                System.out.println("Episode: ");
                                                episodeName = scanner.nextLine();
                                            }
                                        } catch (InputMismatchException e) {
                                            System.out.println("Episode duration must be a number!");
                                            break;
                                        }

                                        seriesSeasonsMap.put(season, seasonEpisodes);
                                        System.out.println();
                                        System.out.println("Season: ");
                                        season = scanner.nextLine();
                                    }
                                    Series series = new Series(productionTitle, productionDirectors, productionActors,
                                            productionGenres, productionRatings, productionDescription,
                                            seriesReleaseYear, seriesNumberOfSeasons, seriesSeasonsMap);
                                    Staff staff2 = (Staff) userCurrent;
                                    staff2.addProductionSystem(series);

                                    System.out.println("Series added!");
                                    break;
                            }
                            break;
                        case 2: //add actor
                            System.out.println("Actor name: ");
                            String actorName = scanner.nextLine();
                            System.out.println();
                            System.out.println("Actor performances (They played in): (Type 'end' when you are done)");
                            List<NameType> actorPerformances = new ArrayList<>();
                            System.out.println("Title: ");
                            String performance = scanner.nextLine();
                            while (!performance.equals("end")) {
                                System.out.println("Type (Movie / Series): ");
                                String performanceType = scanner.nextLine();
                                NameType nameType = new NameType(performance, performanceType);
                                actorPerformances.add(nameType);
                                System.out.println("Title: ");
                                performance = scanner.nextLine();
                            }
                            System.out.println("Actor biography: ");
                            String actorBiography = scanner.nextLine();
                            Actor actor1 = new Actor(actorName, actorPerformances, actorBiography);
                            Staff staff = (Staff) userCurrent;
                            staff.addActorSystem(actor1);

                            System.out.println("Actor added!");
                            break;
                    }
                    break;
                case 2:
                    System.out.println("What do you want to delete: ");
                    System.out.println("1. Production");
                    System.out.println("2. Actor");
                    int option4 = scanner.nextInt();
                    scanner.nextLine(); //pentru a citi si \n
                    switch (option4) {
                        case 1: //delete production
                            System.out.println("Production title: ");
                            String productionTitle = scanner.nextLine();
                            Staff staff = (Staff) userCurrent;
                            staff.removeProductionSystem(productionTitle);
                            break;
                        case 2: //delete actor
                            System.out.println("Actor name: ");
                            String actorName = scanner.nextLine();
                            Staff staff2 = (Staff) userCurrent;
                            staff2.removeActorSystem(actorName);
                            break;
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
        }
    }

    public void viewSolveRequestsContributor(User currentUser) {
        int found = 0;
        for(Request req : IMDB.getInstance().allRequests) {
            if(req.getSolvingUser().equals(currentUser.getUsername())) {
                found = 1;
                System.out.println("-------------------------");
                System.out.println("Type: " + req.getType());
                System.out.println("Date: " + req.getDate());
                System.out.println("Request user: " + req.getRequestUser());
                if (req.getType().equals(RequestType.ACTOR_ISSUE)) {
                    System.out.println("Actor name: " + req.getTitle_name());
                } else if (req.getType().equals(RequestType.MOVIE_ISSUE)) {
                    System.out.println("Movie title: " + req.getTitle_name());
                }
                System.out.println("Solving user: " + req.getSolvingUser());
                System.out.println("Description: " + req.getDescription());
                System.out.println();
            }
        }
        //print requests:
        if (found == 0) {
            System.out.println("No requests to solve!");
            return;
        } else {
            System.out.println("Choose request to solve: ");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Write the description for your request: ");
            String description = scanner.nextLine();
            int found2 = 0;
            for(Request req : allRequests) {
                if(req.getDescription().equals(description)) {
                    found2 = 1;
                    System.out.println("Choose action: ");
                    System.out.println("1. Solve");
                    System.out.println("2. Delete");
                    try {
                        int option = scanner.nextInt();
                        scanner.nextLine(); //pentru a citi si \n
                        switch (option) {
                            case 1:
                                System.out.println("Solved!");
                                Staff staff = (Staff) currentUser;
                                staff.solveRequest(req);
                                break;
                            case 2:
                                System.out.println("Request deleted!");
                                //stergere notificare solving user:
                                for (User user : IMDB.getInstance().allAccounts) {
                                    if (user.getUsername().equals(req.getSolvingUser())) {
                                        //contributor
                                        user.getNotifications().remove("User: " + req.getRequestUser() +
                                                " has a request for you!");
                                    } else if (user instanceof Admin) {
                                        //admin
                                        user.getNotifications().remove("User: " + req.getRequestUser() +
                                                " has a request for you!");
                                    }
                                }
                                allRequests.remove(req);
                                RequestHolder.removeRequest(req);
                                break;
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                    }
                }
            }
            if (found2 == 0) {
                System.out.println("Request not found!");
            }
        }

    }

    public void viewSolveRequestsAdmin(User currentUser) {
        Admin admin = (Admin) currentUser;
        if(RequestHolder.allRequests.size() == 0) {
            System.out.println("No requests to solve!");
            return;
        } else {
            System.out.println("Admin requests are: ");
            System.out.println();
            RequestHolder.displayRequests();
            System.out.println();
            System.out.println("Choose request to solve: ");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Write the description for your request: ");
            String description = scanner.nextLine();
            int found = 0;
            for(Request req : RequestHolder.allRequests) {
                if(req.getDescription().equals(description)) {
                    found = 1;
                    System.out.println("Choose action: ");
                    System.out.println("1. Solve");
                    System.out.println("2. Delete");
                    try {
                        int option = scanner.nextInt();
                        scanner.nextLine(); //pentru a citi si \n
                        switch (option) {
                            case 1:
                                System.out.println("Solved!");
                                admin.solveRequest(req);
                                break;
                            case 2:
                                System.out.println("Request deleted!");
                                //stergere notificare solving user:
                                for (User user : IMDB.getInstance().allAccounts) {
                                    if (user.getUsername().equals(req.getSolvingUser())) {
                                        //contributor
                                        user.getNotifications().remove("User: " + req.getRequestUser() +
                                                " has a request for you!");
                                    } else if (user instanceof Admin) {
                                        //admin
                                        user.getNotifications().remove("User: " + req.getRequestUser() +
                                                " has a request for you!");
                                    }
                                }
                                RequestHolder.removeRequest(req);
                                IMDB.getInstance().allRequests.remove(req);
                                break;
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                    }
                }
            }
            if (found == 0) {
                System.out.println("Request not found!");
            }
        }
    }

    public void updateProductionActor(User currentUser) {
        System.out.println("What do you want to update: ");
        System.out.println("1. Production");
        System.out.println("2. Actor");
        Scanner scanner = new Scanner(System.in);
        try {
            int option = scanner.nextInt();
            scanner.nextLine(); //pentru a citi si \n
            switch (option) {
                case 1: //update production
                    System.out.println("Production title: ");
                    String productionTitle = scanner.nextLine();
                    int found1 = 0;
                    for (Production prod : allProductions) {
                        if (prod.getProductionTitle().equals(productionTitle)) {
                            Staff staff = (Staff) currentUser;
                            staff.updateProduction(prod);
                            found1 = 1;
                            break;
                        }
                    }
                    if (found1 == 0) {
                        System.out.println("Production not found!");
                    }
                    break;
                case 2: //update actor
                    System.out.println("Actor name: ");
                    String actorName = scanner.nextLine();
                    int found2 = 0;
                    for (Actor actor : allActors) {
                        if (actor.getActorName().equals(actorName)) {
                            Staff staff = (Staff) currentUser;
                            staff.updateActor(actor);
                            found2 = 1;
                            break;
                        }
                    }
                    if (found2 == 0) {
                        System.out.println("Actor not found!");
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
        }
    }

    public void addDeleteUserSystem(User currentUser) {
        Admin admin = (Admin) currentUser;
        System.out.println("Add or delete user: ");
        System.out.println("1. Add");
        System.out.println("2. Delete");
        Scanner scanner = new Scanner(System.in);
        try {
            int option = scanner.nextInt();
            scanner.nextLine(); //pentru a citi si \n
            switch (option) {
                case 1: //add
                    System.out.println("Fill user Information: ");
                    System.out.println();
                    System.out.println("Email: ");
                    String email = scanner.nextLine();
                    try {
                        if (email.equals("")) {
                            throw new InformationIncompleteException("Email field is empty!");
                        }
                    } catch (InformationIncompleteException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    System.out.println("Password: ");
                    String password = scanner.nextLine();
                    try {
                        if (password.equals("")) {
                            throw new InformationIncompleteException("Password field is empty!");
                        }
                    } catch (InformationIncompleteException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    System.out.println("Name: ");
                    String name = scanner.nextLine();
                    try {
                        if (name.equals("")) {
                            throw new InformationIncompleteException("Name field is empty!");
                        }
                    } catch (InformationIncompleteException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    System.out.println("Country: ");
                    String country = scanner.nextLine();
                    System.out.println("Age: ");
                    int age = 0;
                    try {
                        age = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Age must be a number!");
                        break;
                    }
                    scanner.nextLine(); //pentru a citi si \n
                    System.out.println("Gender: (F-Female/M-Male/N-Neutral)");
                    char gender = scanner.nextLine().charAt(0);
                    System.out.println("Birth date: (Type: yyyy-MM-dd HH:mm)");
                    LocalDateTime birthDate = null;
                    try {
                        String birthDateString = scanner.nextLine();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        birthDate = LocalDateTime.parse(birthDateString, formatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Wrong date format!");
                        break;
                    }
                    //Alcatuire information:
                    InformationBuilder informationBuilder = new InformationBuilder();
                    informationBuilder.setCredentials(new Credentials(email, password));
                    informationBuilder.setName(name);
                    informationBuilder.setCountry(country);
                    informationBuilder.setAge(age);
                    informationBuilder.setGender(gender);
                    informationBuilder.setBirthDate(birthDate);
                    User.Information userInformation = informationBuilder.build();

                    System.out.println("Account type: ");
                    System.out.println("1. Regular");
                    System.out.println("2. Contributor");
                    System.out.println("3. Admin");
                    int option2 = scanner.nextInt();
                    scanner.nextLine(); //pentru a citi si \n
                    AccountType accountType = null;
                    switch (option2) {
                        case 1: //add regular
                            accountType = AccountType.REGULAR;
                            break;
                        case 2: //add contributor
                            accountType = AccountType.CONTRIBUTOR;
                            break;
                        case 3: //add admin
                            accountType = AccountType.ADMIN;
                            break;
                    }

                    System.out.println("Username: ");
                    String username = scanner.nextLine();
                    System.out.println("Experience: ");
                    int experience = 0;
                    try {
                        experience = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Experience must be a number!");
                        break;
                    }
                    scanner.nextLine(); //pentru a citi si \n

                    //Notifications, preferences si contributions sunt initial goale:
                    List<String> notifications = new ArrayList<>();
                    SortedSet<Object> preferences = new TreeSet(new Comparator<Object>() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            if (o1 instanceof Production && o2 instanceof Production) {
                                Production p1 = (Production) o1;
                                Production p2 = (Production) o2;
                                return p1.productionTitle.compareTo(p2.productionTitle);
                            } else if (o1 instanceof Actor && o2 instanceof Actor) {
                                Actor a1 = (Actor) o1;
                                Actor a2 = (Actor) o2;
                                return a1.getActorName().compareTo(a2.actorName);
                            } else if (o1 instanceof Production && o2 instanceof Actor) {
                                Production p1 = (Production) o1;
                                Actor a2 = (Actor) o2;
                                return p1.getProductionTitle().compareTo(a2.actorName);
                            } else if (o1 instanceof Actor && o2 instanceof Production) {
                                Actor a1 = (Actor) o1;
                                Production p2 = (Production) o2;
                                return a1.getActorName().compareTo(p2.productionTitle);
                            } else {
                                return 0;
                            }
                        }
                    });
                    SortedSet<Object> addedByHim = new TreeSet(new Comparator<Object>() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            if (o1 instanceof Production && o2 instanceof Production) {
                                Production p1 = (Production) o1;
                                Production p2 = (Production) o2;
                                return p1.productionTitle.compareTo(p2.productionTitle);
                            } else if (o1 instanceof Actor && o2 instanceof Actor) {
                                Actor a1 = (Actor) o1;
                                Actor a2 = (Actor) o2;
                                return a1.getActorName().compareTo(a2.actorName);
                            } else if (o1 instanceof Production && o2 instanceof Actor) {
                                Production p1 = (Production) o1;
                                Actor a2 = (Actor) o2;
                                return p1.getProductionTitle().compareTo(a2.actorName);
                            } else if (o1 instanceof Actor && o2 instanceof Production) {
                                Actor a1 = (Actor) o1;
                                Production p2 = (Production) o2;
                                return a1.getActorName().compareTo(p2.productionTitle);
                            } else {
                                return 0;
                            }
                        }
                    });

                    //Alcatuire user:
                    User user = UserFactory.createUser(userInformation, accountType,
                            username, experience, notifications, preferences, addedByHim);
                    this.allAccounts.add(user);

                    System.out.println("User added!");
                    break;
                case 2: //delete
                    System.out.println("Type the username of the user you want to delete: ");
                    String username2 = scanner.nextLine();
                    int found = 0;
                    for (User thisuser : allAccounts) {
                        if (thisuser.getUsername().equals(username2)) {
                            found = 1;
                            if (thisuser instanceof Admin) {
                                System.out.println("You can't delete an admin!");
                                break;
                            }
                            allAccounts.remove(thisuser);
                            System.out.println("User deleted!");
                            break;
                        }
                    }
                    if (found == 0) {
                        System.out.println("User not found!");
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
        }
    }


    //---------------------Checkers---------------------

    public void checkAccounts(IMDB imdb) {
        for (User user : imdb.allAccounts) {
            System.out.println("Email: " + user.getUserInformation().getUserCredentials().getEmail());
            System.out.println("Password: " + user.getUserInformation().getUserCredentials().getPassword());
            System.out.println("Name: " + user.getUserInformation().getName());
            System.out.println("Country: " + user.getUserInformation().getCountry());
            System.out.println("Age: " + user.getUserInformation().getAge());
            System.out.println("Gender: " + user.getUserInformation().getGender());
            System.out.println("Birth date: " + user.getUserInformation().getBirthDate());
            System.out.println("Account type: " + user.getAccountType());
            System.out.println("username: " + user.getUsername());
            System.out.println("Experience: " + user.getExperience());
            System.out.println("Notifications: " + user.getNotifications());
            System.out.println("Preferences: ");
            System.out.println("---------------------");
            for (Object pref : user.getPreferences()) {
                if(pref instanceof Actor) {
                    Actor actor = (Actor) pref;
                    System.out.println("Actor name: " + actor.getActorName());
                } else if (pref instanceof Production) {
                    Production prod = (Production) pref;
                    System.out.println("Production title: " + prod.getProductionTitle());
                }
            }
            System.out.println("---------------------");
            if (user instanceof Staff) {
                Staff staff = (Staff) user;
                System.out.println("Contributions: ");
                System.out.println("---------------------");
                for (Object contr : staff.getAddedByHim()) {
                    if (contr instanceof Actor) {
                        Actor actor = (Actor) contr;
                        System.out.println("Actor name: " + actor.getActorName());
                    } else if (contr instanceof Production) {
                        Production prod = (Production) contr;
                        System.out.println("Production title: " + prod.getProductionTitle());
                    }
                }
            }
            System.out.println();
        }
    }

    public void checkActors(IMDB imdb) {
        for (Actor actor : imdb.allActors) {
                System.out.println("Actor name: " + actor.getActorName());
                System.out.println("Performances: ");
                for (NameType nameType : actor.getPerformances()) {
                    System.out.println("---------------------");
                    System.out.println("Title: " + nameType.getTitle());
                    System.out.println("Type: " + nameType.getType());
                    System.out.println("---------------------");
                }
                System.out.println("Biography: " + actor.getBiography());
                System.out.println();
            }
    }

    public void checkProductions(IMDB imdb) {
        for (Production prod : imdb.allProductions) {
            prod.displayInfo();
            System.out.println();
        }
    }

    public void checkRequests(IMDB imdb) {
        for (Request req : imdb.allRequests) {
            System.out.println("Type: " + req.getType());
            System.out.println("Date: " + req.getDate());
            System.out.println("Request user: " + req.getRequestUser());
            if (req.getType().equals(RequestType.ACTOR_ISSUE)) {
                System.out.println("Actor name: " + req.getTitle_name());
            } else if (req.getType().equals(RequestType.MOVIE_ISSUE)) {
                System.out.println("Movie title: " + req.getTitle_name());
            }
            System.out.println("Solving user: " + req.getSolvingUser());
            System.out.println("Description: " + req.getDescription());
            System.out.println();
        }
        System.out.println("---------------------");
        System.out.println("---------------------");
        RequestHolder.displayRequests();
    }

    public static void main(String[] args) {
        try {
            IMDB imdb = IMDB.getInstance();
            imdb.run();

            //Check accounts:
            //imdb.checkAccounts(imdb);

            //Check actors:
            //imdb.checkActors(imdb);

            //Check productions:
            //imdb.checkProductions(imdb);

            //Check requests:
            //imdb.checkRequests(imdb);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
