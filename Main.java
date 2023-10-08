import java.util.Random;
import java.util.Set;
import java.util.HashSet;

enum MENU { DUMMY, BATTLE, QUIT, ERROR }
enum CreatureType { DEMON, BALROG, ELF, CYBERDEMON }

final class Constants {
    public static final char PADDING_CHAR_TITLE = '~';   // Padding character for the title
    public static final char PADDING_CHAR_MENU = '-';    // Padding character for the menu
    public static final char PADDING_CHAR_BATTLE = '.';  // Padding character for the battle
    public static final int TOTAL_WIDTH = 65;       // Total width of the console window
    public static final int DEFAULT_ARMY_SIZE = 1;  // Default army size
    public static final int NUM_CREATURE_TYPES = 4; // Number of creature types
    public static final int MIN_STRENGTH = 40;      // Minimum strength of a creature
    public static final int MAX_STRENGTH = 170;     // Maximum strength of a creature
    public static final int MIN_HP = 40;            // Minimum HP of a creature
    public static final int MAX_HP = 170;           // Maximum HP of a creature
    public static final int ARMY_SIZE_MAX = 10;     // Maximum army size
    public static final int DEFAULT_HP = 999;       // Default HP
    public static final int DEFAULT_STRENGTH = 999; // Default Strength
    public static final int DEMON_CRIT_PERCENT = 5; // Demon crit chance
    public static final int DEMON_CRIT_BONUS = 50;  // Demon crit bonus damage
    public static final int ELF_CRIT_PERCENT = 10;  // Elf crit chance
    public static final int ELF_DAMAGE_MULTIPLIER = 2; // Elf crit damage multiplier
    public static final int TWO = 2;                   // For modulus and division
    public static final int HEALTH_BAR_WIDTH = 25;     // Width of the health bar
}

class Game {

    public Game() {}
    
    public void TurnBasedCombat() {
        Main.printCenteredTitle("BUILD YOUR ARMY", Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_TITLE);
        int army_size = getValidArmySize();

        // Create two armies
        Army army_one = new Army(army_size);
        Army army_two = new Army(army_size);

        // Display Army Stats
        Main.printCenteredTitle("ARMY 1", Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_MENU);
        System.out.println(army_one.getArmyStats());
        Main.printCenteredTitle("ARMY 2", Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_MENU);
        System.out.println(army_two.getArmyStats());
        Main.printCenteredTitle("BATTLE START", Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_TITLE);

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
            String battle_title = "Round " + (i + 1);
            Main.printCenteredTitle(battle_title, Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_MENU);
            //turn_details.append("\n").append(attacker.getNameWithType()).append(" vs ").append(defender.getNameWithType()).append("\n\n");
            turn_details.append(defender.getNameWithType()).append(" is caught off guard!\n").append(attacker.getNameWithType()).append(" attacks first!\n\n");

            // Display initial HP of fighters at the beginning of each round
            //battle_details.append("\n").append(String.valueOf(Constants.PADDING_CHAR_BATTLE).repeat(Constants.TOTAL_WIDTH)).append("\n");
            //battle_details.append(attacker.getName()).append("'s HP: ").append(attacker.getHealth()).append(" ").append(String.format("%10s", attacker.getHealthBar())).append("\n");
            //battle_details.append(defender.getName()).append("'s HP: ").append(defender.getHealth()).append("\n");
            //battle_details.append(String.valueOf(Constants.PADDING_CHAR_BATTLE).repeat(Constants.TOTAL_WIDTH)).append("\n");

            // Format strings for displaying HP
            String nameFormatString = "%-37s %s %n";
            String healthBarFormatString = "%-37s %s %n";

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
                // Display HP of fighters after each turn
                //battle_details.append(attacker.getName()).append("'s HP\n").append(attacker.getHealthBar()).append("\n");
                //battle_details.append(defender.getName()).append("'s HP\n").append(defender.getHealthBar()).append("\n");
                battle_details.append(String.format(nameFormatString, attacker.getName() + "'s HP", defender.getName() + "'s HP"));
                battle_details.append(String.format(healthBarFormatString, attacker.getHealthBar(), defender.getHealthBar()));

                battle_details.append(turn_details).append("\n"); // Store the entire battle sequence
                turn_details.setLength(0); // Clear the turn details
            }

            // Display final HP of fighters after each round
            //battle_details.append("\n").append(String.valueOf(Constants.PADDING_CHAR_BATTLE).repeat(Constants.TOTAL_WIDTH)).append("\n");
            //battle_details.append(attacker.getName()).append("'s HP\n").append(attacker.getHealthBar()).append("\n");
            //battle_details.append(defender.getName()).append("'s HP\n").append(defender.getHealthBar()).append("\n");
            //battle_details.append(String.valueOf(Constants.PADDING_CHAR_BATTLE).repeat(Constants.TOTAL_WIDTH)).append("\n"); 

            // Display the winning creature
            battle_details.append(winner.getNameWithType()).append(" wins this round!\n\n").append("Army 1 Remaining HP: ").append(army_one.calculateArmyHealth()).append("\nArmy 2 Remaining HP: ").append(army_two.calculateArmyHealth());
            System.out.println(battle_details);
        }
        // Determine the winning Army
        StringBuilder WinningArmy = new StringBuilder(0);
        if (army_one.calculateArmyHealth() > army_two.calculateArmyHealth()) {
            WinningArmy.append("\nArmy 1");
        }
        else if (army_one.calculateArmyHealth() < army_two.calculateArmyHealth()) {
            WinningArmy.append("\nArmy 2");
        }
        else {
            WinningArmy.append("\nNeither Army");
        }
        Main.printCenteredTitle("Results", Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_MENU);
        System.out.println(WinningArmy.append(" wins the battle!\n\n").append("Army 1 Final HP: ").append(army_one.calculateArmyHealth()).append("\nArmy 2 Final HP: ").append(army_two.calculateArmyHealth()));
        Main.printCenteredTitle("BATTLE END", Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_TITLE);
    }

    private int getValidArmySize() {
        int size = 0;
        boolean valid_input = false;
        do {
            try {
                size = Integer.parseInt(System.console().readLine("\nEnter the size of the armies: "));
                if (size < 1 || size > Constants.ARMY_SIZE_MAX) {
                    System.out.println("\nError: Army size must be between 1 and " + Constants.ARMY_SIZE_MAX + " creatures");
                }
                else {
                    valid_input = true;
                }
            }
            catch (NumberFormatException ex) {
                System.out.println("\nError: Please enter a valid number between 1 and " + Constants.ARMY_SIZE_MAX + " for army size");
            }
        } while (!valid_input);
        return size;
    }

    static String getRandomName() {
        String[] names = { "Vaelgrim Deathstalker", "Morgaroth", "Ravengrim", "Draegon Blackthorn", 
                      "Vaelkara Doomweaver", "Malachar", "Sylvaris Grimshadow", "Zirelia Ashenheart", 
                      "Thexandra Bloodwraith", "Valerius Blackveil", "Azura Nightshade", "Thalgrim", 
                      "Zarael Darkthorn", "Moros Grimscythe", "Ravenna Soulstealer", "Draven Nightshade",
                      "Vaelorath", "Elara Shadowblade", "Astraea Stormcaller", "Aeliana Earthshaker" };
        
        String random_name;
        Random random = new Random();

        do {
            random_name = names[random.nextInt(names.length)];
        } while(Main.used_names.contains(random_name));

        Main.used_names.add(random_name);

        return random_name;
    }
}

class Creature {
    protected String name;
    protected int strength;
    protected int health;
    protected int max_HP;

    public Creature() {
        setCreature("D'FAULTO, Bane of Yor'Existense", Constants.DEFAULT_STRENGTH, Constants.DEFAULT_HP);
    }
    public Creature(String n_name, int n_strength, int n_health) {
        setCreature(n_name, n_strength, n_health);
    }

    void setCreature(String n_name, int n_strength, int n_health) {
        name = n_name;
        strength = n_strength;
        health = n_health;
        max_HP = n_health;
    }

    String getName() { return name; }
    String getType() { return getClass().getSimpleName().toLowerCase(); }
    String getNameWithType() { return getName() + " the " + getType(); }
    int getStrength() { return strength; }
    int getHealth() { return health; }
    int getMaxHP() { return max_HP; }

    void setName(String n_name) { setCreature(n_name, strength, health); }
    void setStrength(int n_strength) { setCreature(name, n_strength, health); }
    void setHealth(int n_health) { setCreature(name, strength, n_health); }
    
    String to_String() { return getNameWithType() + ", STR: " + strength + ", HP: " + health; }
    int getDamage() { return new Random().nextInt(strength + 1); }
    String getHealthBar() {
        int healthPercentage = (int) ((double) getHealth() / getMaxHP() * 100); // calculating health percentage
        int barLength = (int) (((double) getHealth() / getMaxHP()) * Constants.HEALTH_BAR_WIDTH); // calculating number of "|" characters

        StringBuilder bar = new StringBuilder("[");
        for(int i = 0; i < barLength; i++) {
            bar.append("|");
        }
        for(int i = barLength; i < Constants.HEALTH_BAR_WIDTH; i++) {
            bar.append(" ");
        }
        bar.append("]");

        return healthPercentage + "% " + bar.toString();
    }
}

// For classes that don't have their own damage, call the parent getdamage
class Demon extends Creature {
    public Demon() {
        super();
    }
    public Demon(String n_name, int n_strength, int n_health) {
        super.setCreature(n_name, n_strength, n_health);
    }

    // Calls parent getDamage() and adds additional damage based on the demons crit chance
    @Override
    int getDamage() { 
        // Calculate base damage using parent getDamage() method
        int base_damage = super.getDamage();

        // Then randomly determine whether or not they crit
        int random_num = new Random().nextInt(100);
        if (random_num < Constants.DEMON_CRIT_PERCENT) {
            base_damage += Constants.DEMON_CRIT_BONUS;
        }
        return base_damage;
    }

    @Override
    String getType() {
        return getClass().getSimpleName().toLowerCase(); 
    }
}

// Balrogs get to attack twice
class Balrog extends Demon {
    public Balrog() {
        super();
    }
    public Balrog(String n_name, int n_strength, int n_health) {
        super.setCreature(n_name, n_strength, n_health);
    }

    @Override
    int getDamage() {
        int damage = super.getDamage();
        damage += super.getDamage();
        return damage;
    }
    @Override
    String getType() {
        return getClass().getSimpleName().toLowerCase(); 
    }
}

// Cyberdemons have no special attack
class Cyberdemon extends Demon {
    public Cyberdemon() {
        super();
    }
    public Cyberdemon(String n_name, int n_strength, int n_health) {
        super.setCreature(n_name, n_strength, n_health);
    }

    @Override
    String getType() {
        return getClass().getSimpleName().toLowerCase(); 
    }
}

// Elves inflict double magical damage
class Elf extends Creature {
    public Elf() {
        super(); // calls default constructor of parent class
    }
    public Elf(String n_name, int n_strength, int n_health) {
        super.setCreature(n_name, n_strength, n_health);
    }

    @Override
    int getDamage() {
        int base_damage = super.getDamage();
        int random_num = new Random().nextInt(100);

        if(random_num < Constants.ELF_CRIT_PERCENT) {
            base_damage *= Constants.ELF_DAMAGE_MULTIPLIER;
        }
        return base_damage;
    }

    @Override
    String getType() {
        return getClass().getSimpleName().toLowerCase(); 
    }
}

class Army {
    private Creature[] creatures;
    private int army_size;

    public Army() {
        setArmySize(Constants.DEFAULT_ARMY_SIZE);
        creatures = new Creature[army_size];
        creatures[0] = new Creature();
    }
    public Army(int size) {
        setArmySize(size);
        creatures = new Creature[army_size];
        createNewCreatures(size);
    }

    void createNewCreatures(int num_creatures) {
        for (int i = 0; i < num_creatures; i++) {
            String name = Game.getRandomName();
            int randomStrength = getRandomValue(Constants.MIN_STRENGTH, Constants.MAX_STRENGTH);
            int randomHealth = getRandomValue(Constants.MIN_HP, Constants.MAX_HP);

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
                System.out.println("\nInvalid Creature Type. Releasing Defaulto\n");
                random_creature = new Creature();
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

public class Main {
    static final Set<String> used_names = new HashSet<>(); // the final is the reference to the set, not the set itself
    public static void main(String[] args) {
        Game game = new Game();
        String menu_options = "\nMain Menu\n1. Battle\n2. Quit";
        MENU menu;

        printCenteredTitle("Welcome to the Turn Based Battle Simulator", Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_TITLE);
        
        do {
            used_names.clear(); // Clear the used names at the start of each game
            menu = getMenuSelection(menu_options);
            switch (menu) {
                case BATTLE:
                    game.TurnBasedCombat();
                    break;
                case QUIT:
                    System.out.println("\nFarwell Warrior!\n");
                    break;
                default:
                    System.out.println("\nError: Invalid Menu Option");
                    break;
            }
        } while (menu != MENU.QUIT);
    }
    
    static MENU getMenuSelection(String menu_options) {
        int input;
        MENU menu = MENU.ERROR;

        try {
            System.out.println(menu_options);
            input = Integer.parseInt(System.console().readLine("Selection: "));

            if (input > 0 && input < MENU.values().length) {
                menu = MENU.values()[input];
            }
        }
        catch (NumberFormatException ex) {
            input = MENU.values().length;
        }
        return menu;
    }
    
    public static void printCenteredTitle(String title, int total_width, char padding_char) {
        int title_length = title.length();
        
        // If the title length is greater than the total width, truncate it to the total width if possible, otherwise set it to a space
        if (title_length > total_width) {
            title = title.substring(0, total_width);
            title_length = title.length(); // update title length after truncation
        }
        
        // Determine Padding
        int num_padding_chars = (total_width - title_length) / Constants.TWO;
        String padding = String.valueOf(padding_char).repeat(num_padding_chars);

        // Ensure symmetry by checking for odd total width and even title length
        boolean needs_extra_padding = (total_width % Constants.TWO != 0) && (title_length % Constants.TWO == 0);

        // StringBuilder for efficient string concatenation
        StringBuilder output = new StringBuilder("\n");

        // Append padding and title to output
        output.append(padding);
        if (needs_extra_padding) {
            output.append(padding_char);
        }

        // Check for non-empty title
        if (title_length > 0) {
            output.append(" ").append(title).append(" ");
            output.append(padding);
        }
        else {
            output.append(padding_char).append(padding_char).append(padding);
        }

        System.out.println(output.toString());
    }

    /*
    static String getRandomName() {
        String[] names = { "Vaelgrim Deathstalker", "Morgaroth", "Ravengrim", "Draegon Blackthorn", 
                      "Vaelkara Doomweaver", "Malachar", "Sylvaris Grimshadow", "Zirelia Ashenheart", 
                      "Thexandra Bloodwraith", "Valerius Blackveil", "Azura Nightshade", "Thalgrim", 
                      "Zarael Darkthorn", "Moros Grimscythe", "Ravenna Soulstealer", "Draven Nightshade",
                      "Vaelorath", "Elara Shadowblade", "Astraea Stormcaller", "Aeliana Earthshaker" };
        
        String random_name;
        Random random = new Random();

        do {
            random_name = names[random.nextInt(names.length)];
        } while (used_names.contains(random_name));

        used_names.add(random_name);

        return random_name;
    }
    */
    /*
    static int getValidArmySize() {
        int size = 0;
        boolean valid_input = false;
        do {
            try {
                size = Integer.parseInt(System.console().readLine("\nEnter the size of the armies: "));
                if (size < 1 || size > Constants.ARMY_SIZE_MAX) {
                    System.out.println("\nError: Army size must be between 1 and " + Constants.ARMY_SIZE_MAX + " creatures");
                }
                else {
                    valid_input = true;
                }
            }
            catch (NumberFormatException ex) {
                System.out.println("\nError: Please enter a valid number between 1 and " + Constants.ARMY_SIZE_MAX + " for army size");
            }
        } while (!valid_input);
        return size;
    }
    */
    
    // Takes a title, total width, and padding character (must be string), and prints it centered on the screen. Works for even or odd title lengths
    /* 
    public static void printCenteredTitle(String title, int total_width, char padding_char) {
        int title_length = title.length();
        
        // If the title length is greater than the total width, truncate it to the total width if possible, otherwise set it to a space
        if (title_length > total_width) {
            title = title.substring(0, total_width);
            title_length = title.length(); // update title length after truncation
        }

        // Determine Padding
        int num_padding_chars = (total_width - title_length) / 2;
        String padding = String.valueOf(padding_char).repeat(num_padding_chars);

        // Ensure symmetry by checking for odd total width and even title length
        boolean needs_extra_padding = (total_width % 2 != 0) && (title_length % 2 == 0);

        // StringBuilder for efficient string concatenation
        StringBuilder output = new StringBuilder("\n");

        // Append padding and title to output
        output.append(padding);
        if(needs_extra_padding) {
            output.append(padding_char);
        }

        // Check for non-empty title
        if (title_length > 0) {
            output.append(" ").append(title).append(" ");
            output.append(padding);
        }
        else {
            output.append(padding_char).append(padding_char).append(padding);
        }

        System.out.println(output.toString());
    }
    */
}

/*

// This Method uses a HashSet to keep track of which names have already been randomly selected and assigned to a creature
static String getRandomName() {
        String[] names = { "Vaelgrim Deathstalker", "Morgaroth", "Ravengrim", "Draegon Blackthorn", 
                      "Vaelkara Doomweaver", "Malachar", "Sylvaris Grimshadow", "Zirelia Ashenheart", 
                      "Thexandra Bloodwraith", "Valerius Blackveil", "Azura Nightshade", "Thalgrim", 
                      "Zarael Darkthorn", "Moros Grimscythe", "Ravenna Soulstealer", "Draven Nightshade",
                      "Vaelorath", "Elara Shadowblade", "Astraea Stormcaller", "Aeliana Earthshaker" };
        
        String random_name;
        Random random = new Random();

        do {
            random_name = names[random.nextInt(names.length)];
        } while(used_names.contains(random_name));

        used_names.add(random_name);

        return random_name;
    }
 
// Takes a title, total width, and padding character (must be string), and prints it centered on the screen. Works for even or odd title lengths
    public static void printCenteredTitle(String title, int total_width, String padding_char) {
        int title_length = title.length();
        
        // If the padding character is null or empty, set it to a space
        if (padding_char == null || padding_char.isEmpty()) {
            padding_char = " ";
        }
        // If the padding character is more than one character, set it to the first character
        else if (padding_char.length() > 1) {
            padding_char = padding_char.substring(0, 1);
        }
        // If the padding character is greater than the total width, truncate it to the total width if possible, otherwise set it to a space
        if (title_length > total_width) {
            title = title_length <= total_width ? title : title.substring(0, total_width);
        }

        int num_padding_chars = (Constants.TOTAL_WIDTH - title_length) / 2;
        String padding = String.join("", Collections.nCopies(num_padding_chars, padding_char));

        // Ensure symmetry by checking for odd total width and even title length
        if ((total_width % 2 != 0) && (title_length % 2 == 0)) {
            padding += padding_char;
        }

        System.out.println("\n" + padding + " " + title + " " + padding + "\n");
    }


*/
/* 
    String[] types = {"Alchemist", "Rogue", "Hobbit", "Dwarf", "Priest", "Wizard", "Orc", 
                      "Troll", "Balrog", "Demon", "Gollum" };
*/