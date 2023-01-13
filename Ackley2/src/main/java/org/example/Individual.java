package org.example;

/**
 * Reprezinta un individ
 */
public class Individual implements Comparable{
    private static final int mutationProb = 20;
    private static final int interval = 100;
    private double[] chromosome;
    private double fitness;

    /**
     * Contructorul clasei
     */
    public Individual() {
        this.chromosome = new double[2];
    }

    /**
     * Generează random valorile genelor din cromoszom
     */
    public void generareRandom() {
        chromosome[0] = Main.rnd.nextDouble(interval) * 2 - interval;
        chromosome[1] = Main.rnd.nextDouble(interval) * 2 - interval;
        calcFitness();
    }

    /**
     * Calculeaza fitness-ul individului
     */
    public void calcFitness(){
        double x = this.getGene(0);
        double y = this.getGene(1);
        Ackley ackley = new Ackley(x, y);
        ackley.calculateResult();
        setFitness(ackley.getResult());
    }

    /**
     *  Realizeaza mutatia asupra individului
     */
    public void doMutation(){
        if(Main.rnd.nextDouble()*100 <= mutationProb){
            chromosome[0] += Main.rnd.nextDouble()*2-1;
            chromosome[1] += Main.rnd.nextDouble()*2-1;
        }
    }

    /**
     * Returneaza intreg cromozomul
     * @return un array de tip double, fiecare element al array-ului reprezinta o gena
     */
    public double[] getChromosome(){
        return chromosome;
    }

    /**
     * Returneaza o anumita gena a cromozomului
     * @param index un numar de tip int, reprezentand pozitia genei in cromozom
     * @return un numar de tip double, reprezentand valoarea genei
     */
    public double getGene(int index){
        return chromosome[index];
    }

    /**
     * Seteaza o anumita gena a cromozomului
     * @param geneValue un numar de tip double, reprezentand valoarea genei ce urmeaza a fi setata
     * @param index un numar te tip int, reprezentand pozitia genei din cromozom
     */
    public void setGene(double geneValue, int index){
        chromosome[index] = geneValue;
    }

    /**
     * Returneaza fitness-ul individului
     * @return un numar de tip double, reprezentand fitness-ul individului
     */
    public double getFitness(){
        return fitness;
    }

    /**
     * Seteaza fitness-ul individului
     * @param value un numar de tip double, reprezentand fitness-ul individului
     */
    public void setFitness(double value){
        fitness = value;
    }

    public int compareTo(Object obj){
        Individual another = (Individual)obj;
        return Double.compare(this.fitness, another.fitness);
    }
}
