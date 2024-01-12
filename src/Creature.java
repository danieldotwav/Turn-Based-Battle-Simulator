package src;
import java.util.Random;

public abstract class Creature implements Constants {
    protected String name;
    protected int strength;
    protected int health;
    protected int max_HP;

    public Creature() {
        setCreature("D'FAULTO, Bane of Yor'Existense", DEFAULT_STRENGTH, DEFAULT_HP);
    }
    public Creature(String n_name, int n_strength, int n_health) {
        setCreature(n_name, n_strength, n_health);
    }

    void setCreature(String n_name, int n_strength, int n_health) {
        setName(n_name);
        setStrength(n_strength);
        setHealth(n_health);
        setMaxHP(n_health);
    }

    String getName() { return name; }
    String getType() { return getClass().getSimpleName().toLowerCase(); }
    String getNameWithType() { return getName() + " the " + getType(); }
    int getStrength() { return strength; }
    int getHealth() { return health; }
    int getMaxHP() { return max_HP; }

    void setName(String n_name) { name = n_name; }
    void setStrength(int n_strength) { strength = n_strength; }
    void setHealth(int n_health) { health = n_health; }
    void setMaxHP(int n_max_HP) { max_HP = n_max_HP; }
    
    String to_String() { return getNameWithType() + ", STR: " + strength + ", HP: " + health; }
    int getDamage() { return new Random().nextInt(strength + 1); }
}
