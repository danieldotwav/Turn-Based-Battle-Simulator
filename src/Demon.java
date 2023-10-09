package src;
import java.util.Random;

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
        if (random_num < DEMON_CRIT_PERCENT) {
            base_damage += DEMON_CRIT_BONUS;
        }
        return base_damage;
    }

    @Override
    String getType() {
        return getClass().getSimpleName().toLowerCase(); 
    }
}