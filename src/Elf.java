package src;
import java.util.Random;

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

        if (random_num < ELF_CRIT_PERCENT) {
            base_damage *= ELF_DAMAGE_MULTIPLIER;
        }
        return base_damage;
    }

    @Override
    String getType() {
        return getClass().getSimpleName().toLowerCase(); 
    }
}
