package src;

public interface Constants {
    char PADDING_CHAR_TITLE = '~';   // Padding character for the title
    char PADDING_CHAR_MENU = '-';    // Padding character for the menu
    char PADDING_CHAR_BATTLE = '+';  // Padding character for the battle
    int TOTAL_WIDTH = 65;       // Total width of the console window
    int BATTLE_WIDTH = 85;      // Total width of the battle console window
    int DEFAULT_ARMY_SIZE = 1;  // Default army size
    int NUM_CREATURE_TYPES = 4; // Number of creature types
    int MIN_STRENGTH = 40;      // Minimum strength of a creature
    int MAX_STRENGTH = 170;     // Maximum strength of a creature
    int MIN_HP = 40;            // Minimum HP of a creature
    int MAX_HP = 170;           // Maximum HP of a creature
    int ARMY_SIZE_MAX = 10;     // Maximum army size
    int DEFAULT_HP = 999;       // Default HP
    int DEFAULT_STRENGTH = 999; // Default Strength
    int DEMON_CRIT_PERCENT = 5; // Demon crit chance
    int DEMON_CRIT_BONUS = 50;  // Demon crit bonus damage
    int ELF_CRIT_PERCENT = 10;  // Elf crit chance
    int ELF_DAMAGE_MULTIPLIER = 2; // Elf crit damage multiplier
    int TWO = 2;                   // For modulus and division
    int HEALTH_BAR_WIDTH = 25;     // Width of the health bar
}
