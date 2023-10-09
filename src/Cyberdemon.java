package src;

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
