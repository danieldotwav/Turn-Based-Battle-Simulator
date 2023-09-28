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
    String name;
    CreatureType type;
    int strength;
    int health;

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
    String getNameWithType() { return name + " the " + type.name().toLowerCase(); }
    int getStrength() { return strength; }
    int getHealth() { return health; }
    String to_String() { return getNameWithType() + ", STR: " + strength + ", HP: " + health; }
    int getDamage() { return new Random().nextInt(strength + 1); }
}

class Army {
    Creature[] creatures;
    int army_size;

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

    void SetArmy(Creature[] new_creatures, int new_army_size) {
        creatures = new_creatures;
        army_size = new_army_size;
    }
    void setArmySize(int new_army_size) { 
        army_size = new_army_size; 
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
        army_stats.append("Total Army HP: ").append(calculateArmyHealth()).append("\n");
        return army_stats.toString();
    }
    int calculateArmyHealth() {
        int HP = 0;
        for(int i = 0; i < army_size; i++) {
            HP += creatures[i].getHealth();
        }
        return HP;
    }
}

public class Main {
    public static void main(String[] args) {
        String menu_options = "Menu\n1. Battle\n2. Quit\nEnter Selection: ";
        MENU menu;
        JOptionPane.showMessageDialog(null, "---Welcome to the Turn Based Battle Simulator---\n");

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
        } while(menu != MENU.QUIT);
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
        int army_size;
        do {
            army_size = Integer.parseInt(JOptionPane.showInputDialog("Enter the size of the armies: "));
            if (army_size < 1 || army_size > Constants.ARMY_SIZE_MAX) {
                JOptionPane.showMessageDialog(null, "Army size must be between 1 and 10 creatures!");
            }
        } while (army_size < 1 || army_size > Constants.ARMY_SIZE_MAX);

        // Create two armies
        Army army_one = new Army(army_size);
        Army army_two = new Army(army_size);

        // Display Army Stats
        JOptionPane.showMessageDialog(null, "--Army 1--\n\n" + army_one.getArmyStats());
        JOptionPane.showMessageDialog(null, "--Army 2--\n\n" + army_two.getArmyStats());

        for(int i = 0; i < army_size; i++) {
            // Randomly select which creature attacks first
            Random random = new Random();
            Creature attacker, defender, winner;
            StringBuilder turn_details = new StringBuilder();
            StringBuilder battle_details = new StringBuilder();
            winner = null;

            if (random.nextBoolean()) {
                attacker = army_one.creatures[i];
                defender = army_two.creatures[i];
            } 
            else {
                attacker = army_two.creatures[i];
                defender = army_one.creatures[i];
            }

            // Store the total damage dealt by each army and the total army HP
            int totalDamageArmyOne = 0;
            int totalDamageArmyTwo = 0;

            // Battle Sequence
            JOptionPane.showMessageDialog(null, "Round " + (i + 1) + "!\n" + attacker.getNameWithType() + " vs " + defender.getNameWithType() + "\n");
            JOptionPane.showMessageDialog(null, defender.getNameWithType() + " is caught off guard!\n" + attacker.getNameWithType() + " attacks first!\n");
            while (winner == null) {
                int damage = attacker.getDamage();
                int currentHP = defender.getHealth();

                // Calculate the effective damage(minimum of damage and currentHP)
                int effectiveDamage = Math.min(damage, currentHP);

                // Update defender's HP
                currentHP -= effectiveDamage;
                defender.health = currentHP;

                if (attacker == army_one.creatures[i]) {
                    totalDamageArmyTwo += effectiveDamage;
                }
                else {
                    totalDamageArmyOne += effectiveDamage;
                }
                
                turn_details.append(attacker.getNameWithType()).append(" attacks ").append(defender.getNameWithType()).append(" for ").append(damage).append(" damage!\n");
                
                if (defender.health <= 0) {
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
            JOptionPane.showMessageDialog(null, battle_details + winner.getNameWithType() + " wins this round!");
            JOptionPane.showMessageDialog(null, "Army 1 Remaining HP: " + army_one.calculateArmyHealth() + "\nArmy 2 Remaining HP: " + army_two.calculateArmyHealth());
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
        JOptionPane.showMessageDialog(null, "Battle Over!");
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
}

/* 
    String[] types = {"Alchemist", "Rogue", "Hobbit", "Dwarf", "Priest", "Wizard", "Orc", 
                      "Troll", "Balrog", "Demon", "Gollum" };
*/