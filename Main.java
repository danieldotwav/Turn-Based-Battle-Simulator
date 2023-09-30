import java.util.Random;
import javax.swing.JOptionPane;

enum MENU { DUMMY, BATTLE, QUIT, ERROR }
enum CreatureType { DEMON, BALROG, ELF, CYBERDEMON }

final class Constants {
    public static final int MIN_STRENGTH = 25;
    public static final int MAX_STRENGTH = 150;
    public static final int MIN_HP = 25;
    public static final int MAX_HP = 150;
    public static final int ARMY_SIZE_MAX = 10;
}

class Creature {
    private String name;
    private CreatureType type;
    private int strength;
    private int health;

    Creature() {
        setCreature("D'FAULTO, Bane of Yor'Existense", CreatureType.DEMON, 999, 999);
    }

    Creature(String n_name, CreatureType n_type, int n_strength, int n_health) {
        setCreature(n_name, n_type, n_strength, n_health);
    }

    void setCreature(String n_name, CreatureType n_type, int n_strength, int n_health) {
        name = n_name;
        type = n_type;
        strength = n_strength;
        health = n_health;
    }

    String getName() { return name; }
    String getType() { return type.name().toLowerCase(); }
    String getNameWithType() { return getName() + " the " + getType(); }
    int getStrength() { return strength; }
    int getHealth() { return health; }

    void setName(String n_name) { setCreature(n_name, type, strength, health); }
    void setType(CreatureType n_type) { setCreature(name, n_type, strength, health); }
    void setStrength(int n_strength) { setCreature(name, type, n_strength, health); }
    void setHealth(int n_health) { setCreature(name, type, strength, n_health); }
    
    String to_String() { return getNameWithType() + ", STR: " + strength + ", HP: " + health; }
    int getDamage() { return new Random().nextInt(strength + 1); }
}

class Army {
    private Creature[] creatures;
    private int army_size;

    Army(int size) {
        setArmySize(size);
        creatures = new Creature[army_size];
        createNewCreatures(size);
    }

    void createNewCreatures(int num_creatures) {
        for (int i = 0; i < num_creatures; i++) {
            String name = Main.getRandomName();
            CreatureType randomType = getRandomCreatureType();
            int randomStrength = getRandomValue(Constants.MIN_STRENGTH, Constants.MAX_STRENGTH);
            int randomHealth = getRandomValue(Constants.MIN_HP, Constants.MAX_HP);
            creatures[i] = new Creature(name, randomType, randomStrength, randomHealth);
        }
    }

    Creature getArmyCreatureAtPosition(int position) { return creatures[position]; }
    int getArmySize() { return army_size; }

    void setArmy(Creature[] new_creatures, int new_army_size) {
        creatures = new_creatures;
        army_size = new_army_size;
    }
    void setArmySize(int n_army_size) { 
        army_size = n_army_size; 
    }
    static CreatureType getRandomCreatureType() {
        int random = new Random().nextInt(CreatureType.values().length);
        return CreatureType.values()[random];
    }
    static int getRandomValue(int min, int max) { 
        return new Random().nextInt(max - min + 1) + min; 
    }
    String getArmyStats() {
        StringBuilder army_stats = new StringBuilder();
        for (int i = 0; i < army_size; i++) {
            army_stats.append("Name: ").append(creatures[i].getName()).append("\nType: ").append(creatures[i].getType()).append("\nHP: ").append(creatures[i].getHealth()).append("\nSTR: ").append(creatures[i].getStrength()).append("\n\n");
        }
        army_stats.append("Army Size: ").append(army_size).append("\n");
        army_stats.append("Total Army HP: ").append(calculateArmyHealth()).append("\n");
        return army_stats.toString();
    }
    /* NOTE: Alternative Dialog Boxes with formatted output
    String getArmyStats() {
        StringBuilder army_stats = new StringBuilder();
        army_stats.append(String.format("%-40s %-20s %-20s %-20s%n", "Name", "Type", "HP", "STR"));

        for (int i = 0; i < army_size; i++) {
            String formattedString = String.format("%-40s %-10s %-10d %-10d%n",
            creatures[i].getName(),
            creatures[i].getType(),
            creatures[i].getHealth(),
            creatures[i].getStrength());
            army_stats.append(formattedString);
        }

        army_stats.append("Army Size: ").append(army_size).append("\n");
        army_stats.append("Total Army HP: ").append(calculateArmyHealth()).append("\n");
        return army_stats.toString();
    }
    */

    int calculateArmyHealth() {
        int HP = 0;
        for (int i = 0; i < army_size; i++) {
            HP += creatures[i].getHealth();
        }
        return HP;
    }
}

public class Main {
    public static void main(String[] args) {
        String menu_options = "Menu\n1. Battle\n2. Quit\nEnter Selection: ";
        MENU menu;
        JOptionPane.showMessageDialog(null, "--- Welcome to the Turn Based Battle Simulator ---\n");

        do {
            menu = getMenuSelection(menu_options);
            switch (menu) {
                case BATTLE:
                    TurnBasedCombat();
                    break;
                case QUIT:
                    JOptionPane.showMessageDialog(null, "\nFarwell Warrior!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "\nInvalid Menu Option!");
                    break;
            }
        } while (menu != MENU.QUIT);
    }
    static MENU getMenuSelection(String menu_options) {
        int input;
        MENU menu = MENU.ERROR;
        String selection = JOptionPane.showInputDialog(menu_options);

        try {
            input = Integer.parseInt(selection);
            if (input > 0 && input < MENU.values().length) {
                menu = MENU.values()[input];
            }
        }
        catch (NumberFormatException ex) {
            input = MENU.values().length;
        }
        return menu;
    }

    static void TurnBasedCombat() {
        JOptionPane.showMessageDialog(null, "\nBattle!\n");

        // Determine Army Size
        int army_size = getValidArmySize();

        // Create two armies
        Army army_one = new Army(army_size);
        Army army_two = new Army(army_size);

        // Display Army Stats
        JOptionPane.showMessageDialog(null, "-- Army 1 --\n\n" + army_one.getArmyStats());
        JOptionPane.showMessageDialog(null, "-- Army 2 --\n\n" + army_two.getArmyStats());

        for(int i = 0; i < army_size; i++) {
            // Randomly select which creature attacks first
            Random random = new Random();
            Creature attacker, defender, winner;
            StringBuilder turn_details = new StringBuilder();
            StringBuilder battle_details = new StringBuilder();
            winner = null;

            if (random.nextBoolean()) {
                attacker = army_one.getArmyCreatureAtPosition(i);
                defender = army_two.getArmyCreatureAtPosition(i);
            } 
            else {
                attacker = army_two.getArmyCreatureAtPosition(i);
                defender = army_one.getArmyCreatureAtPosition(i);
            }

            // Battle Sequence
            turn_details.append("Round ").append(i + 1).append("!\n").append(attacker.getNameWithType()).append(" vs ").append(defender.getNameWithType()).append("\n\n");
            turn_details.append(defender.getNameWithType()).append(" is caught off guard!\n").append(attacker.getNameWithType()).append(" attacks first!\n\n");
            while (winner == null) {
                int damage = attacker.getDamage();
                int currentHP = defender.getHealth();

                // Calculate the effective damage(minimum of damage and currentHP)
                int effectiveDamage = Math.min(damage, currentHP);

                // Update defender's HP
                currentHP -= effectiveDamage;
                defender.setHealth(currentHP);
                turn_details.append(attacker.getNameWithType()).append(" attacks ").append(defender.getNameWithType()).append(" for ").append(damage).append(" damage!\n");
                
                if (defender.getHealth() <= 0) {
                    turn_details.append(defender.getNameWithType()).append(" has 0 HP remaining!!!\n");
                    winner = attacker;
                }
                else {
                    turn_details.append(defender.getNameWithType()).append(" has ").append(defender.getHealth()).append(" HP remaining!\n");
                    Creature temp = attacker;
                    attacker = defender;
                    defender = temp;
                }

                battle_details.append(turn_details).append("\n"); // Store the entire battle sequence
                turn_details.setLength(0); // Clear the turn details
            }

            // Display the winning creature
            battle_details.append(winner.getNameWithType()).append(" wins this round!\n\n").append("Army 1 Remaining HP: ").append(army_one.calculateArmyHealth()).append("\nArmy 2 Remaining HP: ").append(army_two.calculateArmyHealth());
            JOptionPane.showMessageDialog(null, battle_details);
        }
        // Determine the winning Army
        StringBuilder WinningArmy = new StringBuilder(0);
        if (army_one.calculateArmyHealth() > army_two.calculateArmyHealth()) {
            WinningArmy.append("Army 1");
        }
        else if (army_one.calculateArmyHealth() < army_two.calculateArmyHealth()) {
            WinningArmy.append("Army 2");
        }
        else {
            WinningArmy.append("Neither Army");
        }
        JOptionPane.showMessageDialog(null, WinningArmy.append(" wins the battle!\n\n").append("Army 1 Final HP: ").append(army_one.calculateArmyHealth()).append("\nArmy 2 Final HP: ").append(army_two.calculateArmyHealth()));
    }

    static String getRandomName() {
        String[] names = { "Vaelgrim Deathstalker", "Morgaroth", "Ravengrim", "Draegon Blackthorn", 
                      "Vaelkara Doomweaver", "Malachar", "Sylvaris Grimshadow", "Zirelia Ashenheart", 
                      "Thexandra Bloodwraith", "Valerius Blackveil", "Azura Nightshade", "Thalgrim", 
                      "Zarael Darkthorn", "Moros Grimscythe", "Ravenna Soulstealer", "Draven Nightshade",
                      "Vaelorath", "Elara Shadowblade", "Astraea Stormcaller", "Aeliana Earthshaker" };
        
        Random random = new Random();
        int randomNum = random.nextInt(names.length);
        return names[randomNum];
    }

    static int getValidArmySize() {
        int size = 0;
        boolean valid_input = false;
        do {
            try {
                size = Integer.parseInt(JOptionPane.showInputDialog("Enter the size of the armies: "));
                if (size < 1 || size > Constants.ARMY_SIZE_MAX) {
                    JOptionPane.showMessageDialog(null, "Error: Army size must be between 1 and " + Constants.ARMY_SIZE_MAX + " creatures");
                }
                else {
                    valid_input = true;
                }
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: Please enter a valid number between 1 and " + Constants.ARMY_SIZE_MAX + " for army size");
            }
        } while (!valid_input);
        return size;
    }
}

/* 
    String[] types = {"Alchemist", "Rogue", "Hobbit", "Dwarf", "Priest", "Wizard", "Orc", 
                      "Troll", "Balrog", "Demon", "Gollum" };
*/