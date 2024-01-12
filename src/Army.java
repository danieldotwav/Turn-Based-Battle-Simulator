package src;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Army implements Constants {
    private Creature[] creatures;
    private int army_size;
    private static final Random random = new Random();

    public Army() {
        setArmySize(DEFAULT_ARMY_SIZE);
        creatures = new Creature[army_size];
        creatures[0] = new Demon(); // create a default demon creature
    }

    public Army(int size) {
        setArmySize(size);
        creatures = new Creature[army_size];
        createNewCreatures(size);
    }

    void createNewCreatures(int num_creatures) {
        for (int i = 0; i < num_creatures; i++) {
            String name = getRandomName();
            int randomStrength = getRandomValue(MIN_STRENGTH, MAX_STRENGTH);
            int randomHealth = getRandomValue(MIN_HP, MAX_HP);

            creatures[i] = getRandomCreature(name, randomStrength, randomHealth);
        }
    }

    Creature getRandomCreature(String name, int strength, int health) {
        Creature random_creature;
        int creatureTypeIndex = random.nextInt(CreatureType.values().length);

        switch(CreatureType.values()[creatureTypeIndex]) {
            case DEMON:
                random_creature = new Demon(name, strength, health);
                break;
            case BALROG:
                random_creature = new Balrog(name, strength, health);
                break;
            case ELF:
                random_creature = new Elf(name, strength, health);
                break;
            case CYBERDEMON:
                random_creature = new Cyberdemon(name, strength, health);
                break;
            default:
                System.out.println("\nInvalid Creature Type. Constructing default demon creature\n");
                random_creature = new Demon();
                break;
        }
        return random_creature;
    }

    Creature getArmyCreatureAtPosition(int position) { 
        if (position >= 0 && position < army_size) {
            return creatures[position];
        }
        else {
            throw new IllegalArgumentException("Position out of bounds: " + position);
        }
    }

    int getArmySize() { 
        return army_size;
    }

    void setArmy(Creature[] new_creatures, int new_army_size) {
        if (new_creatures.length >= new_army_size) {
            creatures = new_creatures;
            army_size = new_army_size;
        }
        else {
            throw new IllegalArgumentException("Size mismatch: array length and new_army_size are not equal");
        }
    }
    void setArmySize(int n_army_size) { 
        army_size = n_army_size; 
    }
    
    static int getRandomValue(int min, int max) { 
        return random.nextInt(max - min + 1) + min; 
    }

    String getArmyStats() {
        StringBuilder army_stats = new StringBuilder();
        army_stats.append(String.format("%-30s %-12s %10s %10s%n", "Name", "Type", "HP", "STR"));

        for (int i = 0; i < army_size; i++) {
            String formattedString = String.format("%-30s %-12s %10d %10d%n",
            creatures[i].getName(),
            creatures[i].getType(),
            creatures[i].getHealth(),
            creatures[i].getStrength());
            army_stats.append(formattedString);
        }

        army_stats.append("\nArmy Size: ").append(army_size).append("\n");
        army_stats.append("Total Army HP: ").append(calculateArmyHealth());
        return army_stats.toString();
    }

    int calculateArmyHealth() {
        int HP = 0;
        for (int i = 0; i < army_size; i++) {
            HP += creatures[i].getHealth();
        }
        return HP;
    }

    public String getRandomName() {
        String[] names;
        try {
            names = readNamesFromFile("C:/Users/daniel/Documents/Visual Studio Code/SelectionRepetition/src/AvailableNames.txt");
        } catch (IOException e) {
            System.err.println("Error reading names from file: " + e.getMessage());
            return null; // Or handle this in another suitable way
        }

        String random_name;
        boolean nameUsed;

        do {
            random_name = names[random.nextInt(names.length)];
            //boolean nameUsed;
            try {
                nameUsed = isNameUsed(random_name);
            } catch (IOException e) {
                System.err.println("Error checking if name is used: " + e.getMessage());
                return null; // Or handle this in another suitable way
            }
        } while (nameUsed);

        try {
            markNameAsUsed(random_name);
        } catch (IOException e) {
            System.err.println("Error marking name as used: " + e.getMessage());
        }

        return random_name;
    }

    private String[] readNamesFromFile(String fileName) throws IOException {
        List<String> nameList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                nameList.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error while reading from " + fileName + ": " + e.getMessage());
            throw e; // Re-throwing the exception to handle it at a higher level
        }

        return nameList.toArray(new String[0]);
    }

    private boolean isNameUsed(String name) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/daniel/Documents/Visual Studio Code/SelectionRepetition/src/UsedNames.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals(name)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error while checking if name is used: " + e.getMessage());
            throw e; // Re-throwing the exception to handle it at a higher level
        }
        return false;
    }

    private void markNameAsUsed(String name) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/daniel/Documents/Visual Studio Code/SelectionRepetition/src/UsedNames.txt", true))) {
            bw.write(name);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error while marking name as used: " + e.getMessage());
            throw e; // Re-throwing the exception to handle it at a higher level
        }
    }
}
