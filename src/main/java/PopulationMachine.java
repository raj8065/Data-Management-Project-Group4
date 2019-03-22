/* Class used to populate the database
 *
 */
public class PopulationMachine {
    int size;
    private static String USAGE = "java PopulationMachine population_size";
    private PopulationMachine(int populationSize){
        size = populationSize;
    }

    private void populateCustomers(){

    }

    private void populate(){

    }

    public static void main(String args[]){
        PopulationMachine pm = new PopulationMachine(0);
        String size = args[0];
        if (size.matches("\\d+")) {
            int num = Integer.parseInt(size);
            if (num < 0)
                num *= -1;
            pm = new PopulationMachine(num);
        }
        else {
            System.err.println(USAGE);
            System.err.println("population_size must be an integer");
            System.exit(1);
        }
        pm.populate();
    }
}
