import java.util.*;

public abstract class Staff<T> extends User<T> implements StaffInterface {
    private List<Request> todoRequests;
    private SortedSet<T> addedByHim;

    public Staff(Information userInformation, AccountType type, String username, int experience,
                 List<String> notifications, SortedSet preferences, List<Request> todoRequests, SortedSet<T> addedByHim) {
        super(userInformation, type, username, experience, notifications, preferences);
        this.todoRequests = todoRequests;
        this.addedByHim = addedByHim;
    }

    //Shorts:
    List<Production> allProductions = IMDB.getInstance().allProductions;
    List<Actor> allActors = IMDB.getInstance().allActors;

    @Override
    public void addProductionSystem(Production p) {
        allProductions.add(p);
        addedByHim.add((T) p);
        //ExperienceStrategy:
        UserStrategy userStrategy = new UserStrategy(new ProductionAddedStrategy(this.getExperience()));
        this.setExperience(userStrategy.calculateExperience());
    }
    @Override
    public void addActorSystem(Actor a) {
        allActors.add(a);
        addedByHim.add((T) a);
        //ExperienceStrategy:
        UserStrategy userStrategy = new UserStrategy(new ProductionAddedStrategy(this.getExperience()));
        this.setExperience(userStrategy.calculateExperience());
    }
    @Override
    public void removeProductionSystem(String name) {
        int found = 0;
        if(this.getAccountType().equals(AccountType.CONTRIBUTOR)) {
            //can only remove productions added by him:
            for(Production prod : allProductions) {
                if(prod.productionTitle.equals(name)) {
                    found = 1;
                    if (addedByHim.contains((T) prod)) {
                        allProductions.remove(prod);
                        addedByHim.remove((T) prod);
                        System.out.println("Production removed!");
                        break;
                    } else {
                        System.out.println("You can't remove this production!");
                        break;
                    }
                }
            }
        } else if(this.getAccountType().equals(AccountType.ADMIN)) {
            //can remove any production:
            for(Production prod : allProductions) {
                if(prod.productionTitle.equals(name)) {
                    allProductions.remove(prod);
                    addedByHim.remove((T) prod);
                    found = 1;
                    System.out.println("Production removed!");
                    break;
                }
            }
        }
        if(found == 0) {
            System.out.println("Production not found!");
        }
    }
    @Override
    public void removeActorSystem(String name) {
        int found = 0;
        if(this.getAccountType().equals(AccountType.CONTRIBUTOR)) {
            //can only remove actors added by him:
            for(Actor actor : allActors) {
                if(actor.getActorName().equals(name)) {
                    found = 1;
                    if (addedByHim.contains((T) actor)) {
                        allActors.remove(actor);
                        addedByHim.remove((T) actor);
                        System.out.println("Actor removed!");
                        break;
                    } else {
                        System.out.println("You can't remove this actor!");
                        break;
                    }
                }
            }
        } else if(this.getAccountType().equals(AccountType.ADMIN)) {
            //can remove any actor:
            for(Actor actor : allActors) {
                if(actor.getActorName().equals(name)) {
                    allActors.remove(actor);
                    addedByHim.remove((T) actor);
                    found = 1;
                    System.out.println("Actor removed!");
                    break;
                }
            }
        }
        if(found == 0) {
            System.out.println("Actor not found!");
        }
    }
    @Override
    public void updateProduction(Production p) {
        System.out.println("Is it a movie or a series?");
        System.out.println("1. Movie");
        System.out.println("2. Series");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine(); //pentru a citi si \n
        switch (option) {
            case 1: //update movie
                for (Production prod : allProductions) {
                    if (prod.productionTitle.equals(p.productionTitle)) {
                        Movie movie = (Movie) p;
                        System.out.println("New Title: ");
                        String productionTitle = scanner.nextLine();
                        movie.setProductionTitle(productionTitle);
                        System.out.println("New Directors: (Type 'end' when you are done)");
                        System.out.println();
                        List<String> productionDirectors = new ArrayList<>();
                        System.out.println("Director: ");
                        String director = scanner.nextLine();
                        while (!director.equals("end")) {
                            productionDirectors.add(director);
                            System.out.println("Director: ");
                            director = scanner.nextLine();
                        }
                        movie.setProductionDirectors(productionDirectors);
                        List<String> productionActors = new ArrayList<>();
                        System.out.println("New Actors: (Type 'end' when you are done)");
                        System.out.println();
                        System.out.println("Actor: ");
                        String actor = scanner.nextLine();
                        while (!actor.equals("end")) {
                            productionActors.add(actor);
                            System.out.println("Actor: ");
                            actor = scanner.nextLine();
                        }
                        movie.setProductionActors(productionActors);
                        List<Genre> productionGenres = new ArrayList<>();
                        System.out.println("New Genres: (Type 'end' when you are done)");
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
                        movie.setProductionGenres(productionGenres);
                        System.out.println("New Description: ");
                        String productionDescription = scanner.nextLine();
                        movie.setProductionDescription(productionDescription);
                        System.out.println("New Length: ");
                        Long length = scanner.nextLong();
                        movie.setMovieLength(length);
                        System.out.println("New Release year: ");
                        Long releaseYear = scanner.nextLong();
                        movie.setMovieReleaseYear(releaseYear);
                        break;
                    }
                }
                break;
            case 2: //update series
                for (Production prod : allProductions) {
                    if (prod.productionTitle.equals(p.productionTitle)) {
                        Series series = (Series) p;
                        System.out.println("New Title: ");
                        String productionTitle = scanner.nextLine();
                        series.setProductionTitle(productionTitle);
                        System.out.println("New Directors: (Type 'end' when you are done)");
                        System.out.println();
                        List<String> productionDirectors = new ArrayList<>();
                        System.out.println("Director: ");
                        String director = scanner.nextLine();
                        while (!director.equals("end")) {
                            productionDirectors.add(director);
                            System.out.println("Director: ");
                            director = scanner.nextLine();
                        }
                        series.setProductionDirectors(productionDirectors);
                        List<String> productionActors = new ArrayList<>();
                        System.out.println("New Actors: (Type 'end' when you are done)");
                        System.out.println();
                        System.out.println("Actor: ");
                        String actor = scanner.nextLine();
                        while (!actor.equals("end")) {
                            productionActors.add(actor);
                            System.out.println("Actor: ");
                            actor = scanner.nextLine();
                        }
                        series.setProductionActors(productionActors);
                        List<Genre> productionGenres = new ArrayList<>();
                        System.out.println("New Genres: (Type 'end' when you are done)");
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
                        series.setProductionGenres(productionGenres);
                        System.out.println("New Description: ");
                        String productionDescription = scanner.nextLine();
                        series.setProductionDescription(productionDescription);
                        System.out.println("New Release year: ");
                        Long releaseYear = scanner.nextLong();
                        series.setSeriesReleaseYear(releaseYear);
                        System.out.println("New Number of seasons: ");
                        Long numberOfSeasons = scanner.nextLong();
                        scanner.nextLine(); //pentru a citi si \n
                        series.setSeriesNumberOfSeasons(numberOfSeasons);
                        System.out.println("New Series seasons map: (Type 'end' when you are done)");
                        System.out.println();
                        Map<String, List<Episode>> seriesSeasonsMap = new HashMap<>();
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
                                    int episodeLength = scanner.nextInt();
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
                        series.setSeriesSeasonsMap(seriesSeasonsMap);
                        break;
                    }
                }
                break;
        }
    }
    @Override
    public void updateActor(Actor a) {
        Scanner scanner = new Scanner(System.in);
        for (Actor actor : allActors) {
            if (actor.getActorName().equals(a.actorName)) {
                System.out.println("New Actor Name: ");
                String actorName1 = scanner.nextLine();
                a.setActorName(actorName1);
                System.out.println("New Actor performances (They played in): (Type 'end' when you are done)");
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
                a.setPerformances(actorPerformances);
                System.out.println("New Actor biography: ");
                String actorBiography = scanner.nextLine();
                a.setBiography(actorBiography);
                break;
            }
        }
    }
    @Override
    public void solveRequest(Request r) {
        //ObserverPattern:
        for(User user : IMDB.getInstance().allAccounts) {
            if(user.getUsername().equals(r.getRequestUser())) {
                if(user instanceof Regular || user instanceof Contributor) {
                    NotificationManager.getInstance().addObserver(user);
                    NotificationManager.getInstance().notifyObservers("Request solved! " +
                            "Thank you! +5 experience gained!");
                    NotificationManager.getInstance().removeObserver(user);
                }
            }
        }

        //stergere notificare solving user:
        for(User user : IMDB.getInstance().allAccounts) {
            if(user.getUsername().equals(r.getSolvingUser())) {
                //contributor
                user.getNotifications().remove("User: " + r.getRequestUser() +
                        " has a request for you!");
            } else if (user instanceof Admin) {
                //admin
                user.getNotifications().remove("User: " + r.getRequestUser() +
                        " has a request for you!");
            }
        }

        //ExperienceStrategy:
        String usernameRequester = r.getRequestUser();
        for(User user : IMDB.getInstance().allAccounts) {
            if(user.getUsername().equals(usernameRequester)) {
                Regular regular = (Regular) user;
                UserStrategy userStrategy = new UserStrategy(new RequestStrategy(regular.getExperience()));
                regular.setExperience(userStrategy.calculateExperience());
            }
        }

        System.out.println("Request solved");
        IMDB.getInstance().allRequests.remove(r);
        RequestHolder.removeRequest(r);
    }

    public SortedSet<String> getAddedByHim() {
        return (SortedSet<String>) addedByHim;
    }
}