package src;
import java.util.Random;

public class Game implements Constants {
    public void TurnBasedCombat() {
        Main.printCenteredTitle("BUILD YOUR ARMY", TOTAL_WIDTH, PADDING_CHAR_TITLE);
        int army_size = getValidArmySize();

        // Create two armies
        Army army_one = new Army(army_size);
        Army army_two = new Army(army_size);

        // Display Army Stats
        Main.printCenteredTitle("ARMY 1", TOTAL_WIDTH, PADDING_CHAR_MENU);
        System.out.println(army_one.getArmyStats());
        Main.printCenteredTitle("ARMY 2", TOTAL_WIDTH, PADDING_CHAR_MENU);
        System.out.println(army_two.getArmyStats());
        Main.printCenteredTitle("BATTLE START", BATTLE_WIDTH, PADDING_CHAR_TITLE);

        // Format strings for displaying HP
        String nameFormatString = "%-52s %s %n";
        String healthBarFormatString = "%-52s %s %n";

        for (int i = 0; i < army_size; i++) {
            // Randomly select which creature attacks first
            Random random = new Random();
            Creature attacker, defender, winner;
            int turn = 1;

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
            Main.printCenteredTitle(battle_title, BATTLE_WIDTH, PADDING_CHAR_MENU);
            System.out.print(String.format(nameFormatString, attacker.getName() + "'s HP (" + attacker.getHealth() + ")", defender.getName() + "'s HP (" + defender.getHealth() + ")"));
            System.out.println(String.format(healthBarFormatString, getHealthBar(attacker), getHealthBar(defender)));
            System.out.println(" >> " + defender.getNameWithType() + " is caught off guard!\n >> " + attacker.getNameWithType() + " attacks first!"); 

            while (winner == null) {
                int damage = attacker.getDamage();
                int currentHP = defender.getHealth();
                
                // Calculate the effective damage(minimum of damage and currentHP)
                int effectiveDamage = Math.min(damage, currentHP);

                // Update defender's HP
                currentHP -= effectiveDamage;
                defender.setHealth(currentHP);
                System.out.print(" >> " + attacker.getNameWithType() + " attacks " + defender.getNameWithType() + " for " + damage + " damage!\n\n");

                // Display HP of fighters after each turn
                if (turn % 2 != 0) {
                    System.out.print(String.format(nameFormatString, attacker.getName() + "'s HP (" + attacker.getHealth() + ")", defender.getName() + "'s HP (" + defender.getHealth() + ")"));
                    System.out.println(String.format(healthBarFormatString, getHealthBar(attacker), getHealthBar(defender)));
                }
                else {
                    System.out.print(String.format(nameFormatString, defender.getName() + "'s HP (" + defender.getHealth() + ")", attacker.getName() + "'s HP (" + attacker.getHealth() + ")"));
                    System.out.println(String.format(healthBarFormatString, getHealthBar(defender), getHealthBar(attacker)));
                }

                // Check if defender's HP is 0 or less
                if (defender.getHealth() <= 0) {
                    System.out.println(" >> " + defender.getNameWithType() + " has 0 HP remaining!!!\n");
                    winner = attacker;
                }
                else {
                    System.out.println(" >> " + defender.getNameWithType() + " has " + defender.getHealth() + " HP remaining!");
                    Creature temp = attacker;
                    attacker = defender;
                    defender = temp;
                }                
                turn++;
            }

            // Display the winning creature
            System.out.println(" >> Battle " + (i + 1) + " Winner: " + winner.getNameWithType() + "\n\nArmy 1 Remaining HP: " + army_one.calculateArmyHealth() + "\nArmy 2 Remaining HP: " + army_two.calculateArmyHealth());
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

        Main.printCenteredTitle("Results", BATTLE_WIDTH, PADDING_CHAR_MENU);
        System.out.println(WinningArmy + " is Victorious!\nFinal Army Stats:");
        Main.printCenteredTitle("ARMY 1", TOTAL_WIDTH, PADDING_CHAR_MENU);
        System.out.println(army_one.getArmyStats());
        Main.printCenteredTitle("ARMY 2", TOTAL_WIDTH, PADDING_CHAR_MENU);
        System.out.println(army_two.getArmyStats());
        
        Main.printCenteredTitle("BATTLE END", BATTLE_WIDTH, PADDING_CHAR_TITLE);
        
    }

    public String getHealthBar(Creature creature) {
        int healthPercentage = (int) ((double) (creature.getHealth() * 100) / creature.getMaxHP()); // calculating health percentage
        int barLength = (int) (( (double) (creature.getHealth() * HEALTH_BAR_WIDTH) / creature.getMaxHP()) ); // calculating number of "|" characters

        //TESTING
        /*
        double healthRatio = (double) creature.getHealth() / creature.getMaxHP();
        int healthPercentage = (int) (healthRatio * 100);
        int barLength = (int) (healthRatio * Constants.HEALTH_BAR_WIDTH);

        System.out.println("Name: " + creature.getName());
        System.out.println("Health: " + creature.getHealth());
        System.out.println("Max Health: " + creature.getMaxHP());
        System.out.println("Health Ratio: " + healthRatio);
        System.out.println("Health Percentage: " + healthPercentage);
        System.out.println("Bar Length: " + barLength);
        */
        
        StringBuilder bar = new StringBuilder("[");
        for(int i = 0; i < barLength; i++) {
            bar.append("|");
        }
        for(int i = barLength; i < HEALTH_BAR_WIDTH; i++) {
            bar.append(" ");
        }
        bar.append("]");

        return healthPercentage + "% " + bar.toString();
    }

    private int getValidArmySize() {
        int size = 0;
        boolean valid_input = false;
        do {
            try {
                size = Integer.parseInt(System.console().readLine("\nEnter the size of the armies: "));
                if (size < 1 || size > ARMY_SIZE_MAX) {
                    System.out.println("\nError: Army size must be between 1 and " + ARMY_SIZE_MAX + " creatures");
                }
                else {
                    valid_input = true;
                }
            }
            catch (NumberFormatException ex) {
                System.out.println("\nError: Please enter a valid number between 1 and " + ARMY_SIZE_MAX + " for army size");
            }
        } while (!valid_input);
        return size;
    }

    /*
    static String getRandomName() {
        String[] names = { "Vaelgrim", "Morgaroth", "Ravengrim", "Draegon", 
                      "Vaelkara", "Malachar", "Sylvaris", "Zirelia", 
                      "Thexandra", "Valerius", "Azura", "Thalgrim", 
                      "Zarael", "Grimscythe", "Ravenna", "Nightshade",
                      "Vaelorath", "Elara", "Astraea", "Aeliana" };
        
        String random_name;
        Random random = new Random();

        do {
            random_name = names[random.nextInt(names.length)];
        } while(used_names.contains(random_name));

        used_names.add(random_name);

        return random_name;
    }
    */

    


}