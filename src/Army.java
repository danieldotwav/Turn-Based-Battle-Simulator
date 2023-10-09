package src;
import java.util.Random;
// NOTE: ask if it's okay for the Army class to implement Constants

public class Army implements Constants{
    private Creature[] creatures;
    private int army_size;

    public Army() {
        setArmySize(DEFAULT_ARMY_SIZE);
        creatures = new Creature[army_size];
        creatures[0] = new Demon(); // creature a default demon creature
    }

    public Army(int size) {
        setArmySize(size);
        creatures = new Creature[army_size];
        createNewCreatures(size);
    }

    void createNewCreatures(int num_creatures) {
        for (int i = 0; i < num_creatures; i++) {
            String name = Game.getRandomName();
            int randomStrength = getRandomValue(MIN_STRENGTH, MAX_STRENGTH);
            int randomHealth = getRandomValue(MIN_HP, MAX_HP);

            creatures[i] = getRandomCreature(name, randomStrength, randomHealth);
        }
    }

    Creature getRandomCreature(String name, int strength, int health) {
        Creature random_creature;
        int random = new Random().nextInt(CreatureType.values().length);

        switch(CreatureType.values()[random]) {
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
        if(new_creatures.length >= new_army_size) {
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
        return new Random().nextInt(max - min + 1) + min; 
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
}
