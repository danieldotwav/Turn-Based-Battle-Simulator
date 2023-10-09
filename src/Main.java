package src;

enum MENU { DUMMY, BATTLE, QUIT, ERROR }
enum CreatureType { DEMON, BALROG, ELF, CYBERDEMON }

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        String menu_options = "\nMain Menu\n1. Battle\n2. Quit";
        MENU menu;

        printCenteredTitle("Welcome to the Turn Based Battle Simulator", Constants.TOTAL_WIDTH, Constants.PADDING_CHAR_TITLE);
        
        do {
            Game.used_names.clear(); // Clear the used names at the start of each game
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
}

/* DOCUMENTATION
    // Array of strings for potential expanded creature types
    String[] types = {"Alchemist", "Rogue", "Hobbit", "Dwarf", "Priest", "Wizard", "Orc", 
                      "Troll", "Balrog", "Demon", "Gollum" };
*/