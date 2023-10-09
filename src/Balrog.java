package src;

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
