package discord.bot.omegaloli.constant;

public enum LVL {

    LVL_1(30, 100, "1"),
    LVL_2(100, 200, "2"),
    LVL_3(200, 350, "3"),
    LVL_4(350, 550, "4"),
    LVL_5(550, 800, "5");

    public Integer min;
    public Integer max;
    public String level;

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
