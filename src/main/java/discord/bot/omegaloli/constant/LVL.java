package discord.bot.omegaloli.constant;

public enum LVL {

    LVL_1(50, 150, "1"),
    LVL_2(150, 300, "2"),
    LVL_3(300, 500, "3"),
    LVL_4(500, 750, "4"),
    LVL_5(750, 1000, "5");

    public final Integer min;
    public final Integer max;
    public final String level;

    LVL(Integer min, Integer max, String level) {
        this.min = min;
        this.max = max;
        this.level = level;
    }

    public static String getLVLsRange(Integer experience) {
        
        for (LVL lvl : LVL.values()) {
            
            if (experience >= lvl.min && experience < lvl.max) {
                return lvl.level;
            }
            else if (experience >= LVL_5.max)
                return "5";
        }
        return "0";
    }
}
