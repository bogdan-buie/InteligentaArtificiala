package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm {
    private final int maxGeneration = 200;
    private final int maxIndividuals = 100;
    private ArrayList<Individual> population = new ArrayList();
    private ArrayList<Individual> populationTemp = new ArrayList();
    private static final Random rnd = new Random();

    public void run() {
        this.generateIntialPopulation();

        Individual globalFittest = new Individual();
        globalFittest.setFitness(Double.MAX_VALUE);
        int index = -1;
        System.out.printf("Generation      Fitness               Gena 0                Gena 1 \n");
        for (int generation = 1; generation <= maxGeneration; generation++) {
            for (int i = 0; i < maxIndividuals / 2; i++) {
                Individual p1 = this.selectParent();
                Individual p2 = this.selectParent2();
                this.doCrossover(p1, p2);
            }
            this.doSurvive();
            Collections.sort(population);
            Individual fittest = population.get(0);
            if (fittest.getFitness() < globalFittest.getFitness()) {
                globalFittest.setFitness(fittest.getFitness());
                globalFittest.setGene(fittest.getGene(0), 0);
                globalFittest.setGene(fittest.getGene(1), 1);
                index = generation;
            }
            //System.out.println(generation + "  Fittest: " + fittest.getFitness() + "         " + fittest.getGene(0) + "         " + fittest.getGene(1));
            //System.out.println(fittest.getFitness());
            System.out.printf("Generation %d:  %.15f   %.15f   %.15f \n",generation, fittest.getFitness(), fittest.getGene(0), fittest.getGene(1));
        }
        System.out.println("Best Solution: " + globalFittest.getFitness() + "    Genaration: " + index);
    }

    /**
     * Genereaza populatia initiala si atribuie indivizilor cromozomi random
     */
    private void generateIntialPopulation() {
        for (int i = 0; i < maxIndividuals; i++) {
            Individual newIndividual = new Individual();
            newIndividual.generareRandom();
            population.add(newIndividual);
        }
        Collections.sort(population);
    }

    /**
     * Selecteaza un parinte (de tip Individual) din populatie prin metoda tournament
     * @return un obiect de tip Individual
     */
    private Individual selectParent() {
        ArrayList<Individual> tournament = new ArrayList();
        for (int i = 0; i <= 4; i++) {
            int index = rnd.nextInt(maxIndividuals - 1);
            tournament.add(population.get(index));
        }
        Collections.sort(tournament);
        return tournament.get(0);
    }

    /**
     * Evalueaza populatia, insumand fitness-ul tuturor indivizilor
     * Indivizii cu un fitness mai mic au o pondere mai mare in suma totala a fitness-urilor
     */
    public double rouletteFitness() {
        double roulettePopulationFitness = 0;
        for (int i = 0; i < maxIndividuals; i++) {
            roulettePopulationFitness += 1 / population.get(i).getFitness();
        }
        return roulettePopulationFitness;
    }

    /**
     * Selecteaza un parinte (de tip Individual) din populatie prin metoda ruletei
     * @return un obiect de tip Individual
     */
    private Individual selectParent2() {
        double rouletteWheelPosition = Math.random() * rouletteFitness();
        double spinWheel = 0;
        for (int i = 0; i < maxIndividuals; i++) {
            spinWheel += 1/population.get(i).getFitness();
            if (spinWheel >= rouletteWheelPosition) {
                return population.get(i);
            }
        }
        return population.get(maxIndividuals - 1);
    }

    /**
     * Realizeaza recombinarea dintre 2 parinti
     * @param p1 un obiect de tip Individual, reprezentand primul parinte
     * @param p2 un obiect de tip Individual, reprezentand al doilea parinte
     */
    private void doCrossover(Individual p1, Individual p2) {
        Individual c1 = new Individual();   // copil 1
        Individual c2 = new Individual();   // copil 2

        double p = rnd.nextDouble();        // procentaj [0,1]

        double c1g0 = p * p1.getGene(0) + (1 - p) * p2.getGene(0);  // gena 0 a primului copil
        double c1g1 = p * p1.getGene(1) + (1 - p) * p2.getGene(1);  // gena 1 a primului copil
        double c2g0 = p * p2.getGene(0) + (1 - p) * p1.getGene(0);  // gena 0 al celui de al doilea copil
        double c2g1 = p * p2.getGene(1) + (1 - p) * p1.getGene(1);  // gena 1 al celui de al doilea copil

        c1.setGene(c1g0, 0);
        c1.setGene(c1g1, 1);
        c2.setGene(c2g0, 0);
        c2.setGene(c2g1, 1);

        c1.doMutation();
        c2.doMutation();
        c1.calcFitness();
        c2.calcFitness();
        populationTemp.add(c1);
        populationTemp.add(c2);
    }

    /**
     *  Schimba generatiile
     */
    private void doSurvive() {
        population.clear();
        population.addAll(populationTemp);
        populationTemp.clear();
    }
}
