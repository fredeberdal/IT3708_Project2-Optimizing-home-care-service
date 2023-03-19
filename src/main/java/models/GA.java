/*
package models;

public class GA {


    public class GeneticAlgorithm {

        private int iterationsCompleted = 0;
        private final double threshold;
        private Population population;
        private double topFitness;

        private double percentageOffSolution;

        private int iterationsWithoutImprovements = 0;

        public GeneticAlgorithm(double threshold, Population population) {
            this.threshold = threshold;
            this.population = population;
        }

        public Population runAlgorithm(int maxNumberGenerations) {
//        KMeans kMeans = new KMeans(24, RequiredData.patients);
//        List<List<Integer>> clusters = kMeans.generateClusters();
//        plotData(new Individual(clusters));

            Individual topLad = population.getBestIndividual();
            this.topFitness = topLad.getFitness();

            while((this.topFitness > this.threshold) && this.iterationsCompleted < maxNumberGenerations && (System.currentTimeMillis() - Settings.START_TIME) / 1000 < 600){
                if(this.iterationsCompleted % 25 == 0) {
                    System.out.println("Runtime: " + (System.currentTimeMillis() - Settings.START_TIME) / 1000 + " seconds");
                    System.out.println("Generation: " + this.iterationsCompleted);
                    System.out.println(topLad.getNurseRoutes());
                    System.out.println("Current best fitness: " + this.topFitness);

                    this.percentageOffSolution = ((topFitness - this.threshold) / this.threshold * 100);

                    System.out.println("This is " + percentageOffSolution + "% off the threshold");
                    System.out.println("Valid top solution: " + topLad.is_valid);
                    System.out.println("Number of valids in pop: " + this.population.getValidIndividuals().size());
                    System.out.println("Number of unique valids: " + this.population.getUniqueValids());
                    System.out.println("\n");
                }

                this.iterationsCompleted++;

                if(this.iterationsWithoutImprovements >= 100) {
//                Individual bestValidIndividual = this.population.getValidIndividuals().get(0);
//                Individual bestInvalidIndividual = this.population.getInvalidIndividuals().get(0);
                    List<Individual> bestValidIndividuals = this.population.getValidIndividuals().subList(0, Settings.ELITE_VALIDS);
                    List<Individual> bestInvalidIndividuals = this.population.getInvalidIndividuals().subList(0, Settings.ELITE_INVALIDS);
                    // This is greedy generation
                    this.population.generatePopulation(1);
                    List<Individual> newPop = this.population.getIndividuals();
//                newPop.set(newPop.size() - 1, bestValidIndividual);
//                newPop.set(newPop.size() - 2, bestInvalidIndividual);
                    newPop.addAll(bestValidIndividuals);
                    newPop.addAll(bestInvalidIndividuals);
                    this.population.setPopulation(newPop);
                    this.iterationsWithoutImprovements = 0;
                }
                double mutationRate = Settings.MUTATION_PROBABILITY;
                if(this.iterationsWithoutImprovements > 75) {
                    mutationRate = Settings.MUTATION_PROBABILITY * 3;
                } else if(this.iterationsWithoutImprovements > 25) {
                    mutationRate = Settings.MUTATION_PROBABILITY * 2;
                }

                if(this.percentageOffSolution < 20) {
                    Settings.FITNESS_PENALTY = 3;
                } else if(this.percentageOffSolution < 25) {
                    Settings.FITNESS_PENALTY = 1.5;
                } else if(this.percentageOffSolution < 30) {
                    Settings.FITNESS_PENALTY = 1;
                }

                List<Individual> newChildren = new ArrayList<>();
                int invalidChildren = 0;
                while(newChildren.size() < Settings.POPULATION_SIZE){
//                IndividualPair parents = this.population.selectParents();

                    int k = 5;
                    IndividualPair parents = this.population.tournamentParentSelection(k);

                    IndividualPair children = this.population.crossover(parents, mutationRate, true);
                    int allowedInvalids = Settings.INVALIDS_ALLOWED;
                    if(this.percentageOffSolution < 30) {
                        allowedInvalids = (int) (Settings.INVALIDS_ALLOWED * 0.75);
                    }
                    if(invalidChildren >= allowedInvalids) {
                        if(children.individual1.is_valid) {
                            newChildren.add(children.individual1);
                        }
                        if(children.individual2.is_valid) {
                            newChildren.add(children.individual2);
                        }
                    } else {
                        newChildren.add(children.individual1);
                        newChildren.add(children.individual2);
                        if(!children.individual1.is_valid) {
                            invalidChildren++;
                        }
                        if(!children.individual2.is_valid) {
                            invalidChildren++;
                        }
                    }
                }
                List<Individual> newPop = this.population.getElites();
                newPop.addAll(newChildren);
                this.population.setPopulation(newPop.subList(0, Settings.POPULATION_SIZE));

                Individual bestIndividual = this.population.getValidIndividuals().get(0);

//            Individual bestIndividualLocalSearched = this.population.localSearch(bestIndividual);
//            if(bestIndividualLocalSearched.getFitness() > bestIndividual.getFitness()) {
//                bestIndividual = bestIndividualLocalSearched;
//                this.population.getIndividuals().set(0, bestIndividual);
//            }
//            this.population.setPopulation(this.population.getIndividuals());


                if(bestIndividual.getFitness() < this.topFitness) {
                    this.iterationsWithoutImprovements = 0;
                    topLad = bestIndividual;
                    this.topFitness = topLad.getFitness();
                    this.percentageOffSolution = ((this.topFitness - this.threshold) / this.threshold * 100);
                } else {
                    this.iterationsWithoutImprovements++;
                }
                if((System.currentTimeMillis() - Settings.START_TIME) / 1000 > 600 || percentageOffSolution < 5) {
                    System.out.println("Percentage off: " + this.percentageOffSolution);
                    printSolution(topLad);
                    plotData(topLad);
                    break;
                }
            }
            if((System.currentTimeMillis() - Settings.START_TIME) / 1000 > 599 || percentageOffSolution < 5) {
                System.out.println("Percentage off: " + this.percentageOffSolution);
                printSolution(topLad);
                plotData(topLad);
            }
            return population;
        }

        private void printSolution(Individual bestIndividual) {
            System.out.println("Best solution: " + bestIndividual.getNurseRoutes());
            System.out.println("Nurse capacity: " + Settings.nurseCapacity);
            System.out.println("Depot return time: " + Settings.returnTime);
            System.out.println("----------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------");
            for(int i = 0; i < bestIndividual.getNurseRoutes().size(); i++) {
                List<Integer> route = bestIndividual.getNurseRoutes().get(i);
                System.out.print("Nurse " + (i + 1) + "  ");
                System.out.print(bestIndividual.getDurationOfRoute(route) + "  ");
                System.out.print(bestIndividual.getUsedCapacity(route) + "  ");
                printNurseRoute(route);
                System.out.println("\n");
            }
            System.out.println("----------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------" +
                    "-------------------------------------------------------------------------------");
            System.out.println("Objective value (total travel time): " + bestIndividual.getFitness());
        }

        private void printNurseRoute(List<Integer> route) {
            List<List<Double>> travelTimeMatrix = RequiredData.travelTimeMatrix;
            List<Patient> patients = RequiredData.patients;

            System.out.print("D(0) -> ");
            double currentTime = 0;
            int prevStop = 0;
            if(!route.isEmpty()) {
                for(Integer patientId : route) {

                    double travelTime = travelTimeMatrix.get(prevStop).get(patientId);

                    currentTime += travelTime;
                    System.out.print(patientId);

                    Patient patientVisiting = patients.get(patientId);
                    int startTime = patientVisiting.getStart_time();
                    if(currentTime < startTime) {
                        currentTime = startTime;
                    }

                    System.out.print("(" + currentTime + "-");
                    currentTime += patientVisiting.getCare_time();
                    System.out.print(currentTime + ")");
                    System.out.print(" [" + patientVisiting.getStart_time() + "-" + patientVisiting.getEnd_time() + "] -> ");

                    prevStop = patientId;
                }
                currentTime += travelTimeMatrix.get(0).get(prevStop);
                System.out.print("D(" + currentTime + ")");
            }
        }

        private void plotData(Individual best_guy) {
            List<Patient> patients = new ArrayList<>(RequiredData.patients);

            List<List<List<Integer>>> nurseRouteCoordinates = new ArrayList<>();
            for(int i = 0; i < 25; i++) {
                nurseRouteCoordinates.add(new ArrayList<>());
            }

            for(List<Integer> routes : best_guy.getNurseRoutes()) {
                List<Integer> x = new ArrayList<>();
                List<Integer> y = new ArrayList<>();
                // Get the routes to start at depot
                x.add((int) RequiredData.depot_x);
                y.add((int) RequiredData.depot_y);
                for(Integer i : routes) {
                    Patient patient = patients.get(i);
                    x.add(patient.getX_coord());
                    y.add(patient.getY_coord());
                }
                // Get the routes to end at depot
                x.add((int) RequiredData.depot_x);
                y.add((int) RequiredData.depot_y);
                nurseRouteCoordinates.get(best_guy.getNurseRoutes().indexOf(routes)).add(x);
                nurseRouteCoordinates.get(best_guy.getNurseRoutes().indexOf(routes)).add(y);

            }

            try {
                Plot plt = Plot.create();

                for(List<List<Integer>> j : nurseRouteCoordinates) {
                    if(!j.isEmpty()) {
                        int indexOf = nurseRouteCoordinates.indexOf(j);
                        plt.plot().add(j.get(0), j.get(1), "-o").color(Colors.colorList.get(indexOf));
                    }
                }

                plt.plot().add(Collections.singletonList(RequiredData.depot_x),
                        Collections.singletonList(RequiredData.depot_y), "^");
                plt.title("Nurse routes");
                plt.show();
            } catch (PythonExecutionException | IOException exception) {
                System.out.println(exception);
            }
        }
    }
}


 */